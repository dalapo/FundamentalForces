package team.lodestar.fufo.common.fluid;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import team.lodestar.fufo.FufoMod;
import team.lodestar.fufo.core.fluid.FluidStats;
import team.lodestar.fufo.core.fluid.FlowDir;
import team.lodestar.fufo.core.fluid.FluidPipeNetwork;
import team.lodestar.fufo.core.fluid.PipeNode;
import team.lodestar.fufo.core.fluid.PressureSource;
import team.lodestar.fufo.unsorted.util.DevSpellResponder;
import team.lodestar.lodestone.helpers.BlockHelper;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Triple;

import static team.lodestar.fufo.unsorted.ForcesThatAreActuallyFundamental.g;

@SuppressWarnings("unused")
public class PipeNodeBlockEntity extends LodestoneBlockEntity implements PipeNode, DevSpellResponder {

	private static final int RANGE = 10;

    public ArrayList<BlockPos> nearbyAnchorPositions = new ArrayList<>();
    public ArrayList<PipeNode> nearbyAnchors = new ArrayList<>();
    
    protected Fluid fluidType = Fluids.EMPTY;
    protected double fluidAmount = 0.0;
    
    private List<Triple<PressureSource, FlowDir, Double>> sources = new ArrayList<>();
    private boolean isOpen = false;
    private FluidPipeNetwork network;
    private int networkID;

    public PipeNodeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    // TODO: fix this method for the 41261816th time
    public double addFluid(Fluid f, double amount) {
    	FluidPipeNetwork.logIfManual(String.format("%s: Receiving %s of %s", this, amount, f.toString()));
    	if (fluidType == null || fluidType == Fluids.EMPTY) fluidType = f;
    	else if (f != fluidType) return amount; // No fluid mixing for now
    	double realAmount = Math.min(fluidAmount + amount, getCapacity());
    	double toReturn = Math.max(0, realAmount - getCapacity());
    	fluidAmount = realAmount;
		BlockHelper.updateAndNotifyState(level, getPos());
    	this.setChanged();
    	return toReturn;
    }
    
	public void transferFluid(double amount, PipeNode dest) {
		FluidPipeNetwork.logIfManual(String.format("Sending %s of %s from %s to %s", amount, getFluidAmount(), this, dest)); 
		double realAmount = Math.min(Math.min(amount, dest.getCapacity()-dest.getFluidAmount()), getFluidAmount());
		double returned = dest.addFluid(fluidType, realAmount);
		fluidAmount -= (realAmount - returned);
		if (fluidAmount < 0.001) {
			fluidAmount = 0;
			fluidType = Fluids.EMPTY;
		}
		BlockHelper.updateAndNotifyState(level, getPos());
		this.setChanged();
	}

    // this method is held together by duct tape and bubble gum
    private static final double DISTANCE_COEFF = 200;
    
    public double getPressure() {
    	
//    	if (getFluidAmount() / getCapacity() < 0.95) return 0; // If node is not full, it can't be pressurized (but allow a bit of wiggle room)
    	
    	double pressure = 0;
    	
    	// Pumps, etc
    	for (Triple<PressureSource, FlowDir, Double> t : sources) {
    		PressureSource source = t.getLeft();
    		FlowDir dir = t.getMiddle();
    		double distance = t.getRight();
    		int force = source.getForce(dir);
    		// Basically we want pressure to tend to zero
    		double contrib;
    		if (force > 0) contrib = Math.max(0, source.getForce(dir) - (DISTANCE_COEFF * distance));
    		else contrib = Math.min(0, source.getForce(dir) + (DISTANCE_COEFF * distance));
//    		double contrib = source.getForce(dir) - (DISTANCE_COEFF * distance);
    		pressure += contrib;
    	}
    	
    	// Height difference from neighbours
    	if(getFluidAmount() / getCapacity() < 0.95) {
	    	for (PipeNode p : nearbyAnchors) {
	    		int dy = p.getPos().getY() - getPos().getY();
	    		pressure += Math.max((p.getFluidAmount()*dy*FluidStats.getInfo(fluidType).rho*g)/1000, 0); // Divide by 1000 because 1 mB = 1/1000 m^3
	    	}
    	}
    	return pressure + (getFluidAmount() / 100); // Volume equals pressure, at least a little bit
    	// ignore the height difference if the node has no fluid
//    	return pressure + (fluid.isEmpty() ? 0 : (g * (getPos().getY()+64) * FluidStats.getInfo(fluid.getFluid()).rho));
    }
    
    public boolean isOpen() {
    	return isOpen;
    }
    
    public void setOpen(boolean open) {
    	isOpen = open;
    	setChanged();
    	BlockHelper.updateAndNotifyState(level, getPos());
    }
    
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("network", networkID);
        pTag.putString("fluidType", ForgeRegistries.FLUIDS.getKey(fluidType).toString());
        pTag.putDouble("fluidAmount", fluidAmount);
        pTag.putBoolean("isOpen", isOpen);
        if (!nearbyAnchorPositions.isEmpty()) {
            CompoundTag compound = new CompoundTag();
            compound.putInt("anchorAmount", nearbyAnchorPositions.size());
            for (int i = 0; i < nearbyAnchorPositions.size(); i++) {
                BlockHelper.saveBlockPos(compound, nearbyAnchorPositions.get(i), "" + i);
            }
            pTag.put("anchorData", compound);
        }
    }

    // When this method is called, check the ID against the FluidPipeNetworkRegistry.
    // If the ID exists in registry, add it to the corresponding network.
    // Otherwise, load the network from NBT into the registry and then add this node to it
    // Probably also check to make sure that this node is supposed to be in the network
    // If the network is not in NBT, create an entry for it
    // Note: level is still null at the time of this method's calling
    // Need to move the network-setting code somewhere else
    // probably to the data capability loading
    @Override
    public void load(CompoundTag pTag) {
//    	Minecraft.getInstance().mouseHandler.releaseMouse();
        super.load(pTag);
        if (pTag.contains("fluidType")) fluidType = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(pTag.getString("fluidType")));
        if (pTag.contains("fluidAmount")) fluidAmount = pTag.getDouble("fluidAmount");
        isOpen = pTag.getBoolean("isOpen");
        nearbyAnchorPositions.clear();
        CompoundTag compound = pTag.getCompound("anchorData");
        int amount = compound.getInt("anchorAmount");
        for (int i = 0; i < amount; i++) {
            BlockPos pos = BlockHelper.loadBlockPos(compound, "" + i);
            nearbyAnchorPositions.add(pos);
        }
        networkID = pTag.getInt("network");
//    	int id = pTag.getInt("network");
//    	FufoMod.LOGGER.info("Loading network " + id);
//    	if (level != null) {
//	    	FluidPipeNetworkRegistry registry = FluidPipeNetworkRegistry.getRegistry(level);
//	    	setNetwork(registry.getOrLoadNetwork(pTag.getInt("network")), true);
//    	}
//    	else FufoMod.LOGGER.error("Null world, not loading");
    }
    
    @Override
    public void onPlace(LivingEntity placer, ItemStack stack) {
    	BlockPos prevPos = PipeBuilderAssistant.INSTANCE.oldPipeNodePos; //TODO: move this to a generic area ran by all implementations of PipeNode
    	if (!level.isClientSide() && prevPos != null && level.getBlockEntity(prevPos) instanceof PipeNode prev && prev.getNetwork() != null) {
    		addConnection(prevPos);
    		prev.addConnection(getPos());
    		setNetwork(prev.getNetwork(), true, true);
    	}
    	else setNetwork(new FluidPipeNetwork(getLevel()), true, true);
    	
    	if (!level.isClientSide() && this instanceof PressureSource p) {
    		getNetwork().addSource(p, true);
    	}
    }
    
    @Override
    public void onLoad() {
    	super.onLoad();
    	for (int i=nearbyAnchorPositions.size()-1; i>=0; i--) { // Have to do a manual iteration to avoid CMEs
    		BlockPos bp = nearbyAnchorPositions.get(i);
    		if (level.getBlockEntity(bp) instanceof PipeNode node) {
    			nearbyAnchors.add(node);
    		}
    		else {
    			nearbyAnchorPositions.remove(i);
    		}
    	}
//    	FufoMod.LOGGER.info("Running onLoad");
    	if (networkID != 0) { // load was run and NBT data was loaded
  //  		Minecraft.getInstance().mouseHandler.releaseMouse();
    		FluidPipeNetwork net = FluidPipeNetworkRegistry.getRegistry(level).getOrLoadNetwork(networkID);
    		if (network != null) {
    			this.setNetwork(net, true, false);
    			if (this instanceof PressureSource p) network.addSource(p, false);
    			network.loadNode();
    			FufoMod.LOGGER.info(String.format("Adding %s to network %s (%s/%s)\n", this, networkID, net.numLoadedNodes(), net.numSavedNodes()));
    			if (network.numLoadedNodes() == network.numSavedNodes()) network.finishLoading();
    		}
    	}
    }


	@Override
	public InteractionResult onUse(Player player, InteractionHand hand) {
		if (!player.mayBuild()) return InteractionResult.PASS;
		ItemStack itemInHand = player.getItemInHand(hand);
		if (itemInHand.isEmpty()) {
			if (player.isShiftKeyDown()) {
				setOpen(!isOpen());
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	// TODO: Have this use nearbyAnchors
    @Override
    public void onBreak(@Nullable Player player) {
    	super.onBreak(player);
    	List<PipeNode> nodes = new ArrayList<>();
    	for (BlockPos bp : nearbyAnchorPositions) {
    		PipeNode node = (PipeNodeBlockEntity) getLevel().getBlockEntity(bp);
    		nodes.add(node);
    		node.removeConnection(this.getBlockPos());
    		BlockHelper.updateState(getLevel(), bp);
    	}
    	if (!level.isClientSide && network != null) {
    		network.splitNetwork(nodes);
    		network.removeNode(this);
    	}
//    	nearbyAnchorPositions.stream().map(p -> this.getLevel().getBlockEntity(p)).forEach(te -> ((PipeNodeBlockEntity)te).nearbyAnchorPositions.remove(this.getBlockPos()));
    }

    public int countNeighbours() {
    	return nearbyAnchorPositions.size();
    }

	@Override
	public FluidStack getStoredFluid() {
		return new FluidStack(fluidType, (int)fluidAmount);
	}
	

	@Override
	public List<PipeNode> getConnectedNodes() {
		Level level = this.getLevel();
		return nearbyAnchorPositions.stream().map(bp -> (PipeNode)level.getBlockEntity(bp)).toList();
	}

	@Override
	public boolean addConnection(BlockPos bp) {
		if (FluidPipeNetwork.MANUAL_TICKING) FufoMod.LOGGER.info(String.format("%s adding connection to %s", getPos(), bp));
		if (level.getBlockEntity(bp) instanceof PipeNode other) {
			nearbyAnchorPositions.add(bp);
			nearbyAnchors.add(other);
			if (network == null) setNetwork(other.getNetwork(), false, true);
			else network.mergeWith(other.getNetwork());
			BlockHelper.updateAndNotifyState(level, getPos());
			return true;
		}
		return false;
	}

	@Override
	public boolean removeConnection(BlockPos bp) {
		return nearbyAnchorPositions.remove(bp) && nearbyAnchors.remove((PipeNode)level.getBlockEntity(bp));
	}

	@Override
	public BlockPos getPos() {
		return getBlockPos();
	}

	@Override
	public void setNetwork(FluidPipeNetwork network, boolean reciprocate, boolean recalc) {
		this.network = network;
		this.networkID = network.getID();
		if (reciprocate) network.addNode(this, false, recalc);
	}

	@Override
	public FluidPipeNetwork getNetwork() {
		// TODO Auto-generated method stub
		return network;
	}
	

	@Override
	public int getCapacity() {
		return 100;
	}

	@Override
	public void updateSource(PressureSource p, FlowDir dir, double dist) {
		for (Triple<PressureSource, FlowDir, Double> set : sources) {
			if (set.getLeft() == p && set.getMiddle() == dir) {
				set = Triple.of(p, dir, dist); // Will this CME?
				return;
			}
			
		}
		sources.add(Triple.of(p, dir, dist)); // If we get here there was no match for the source
	}

	@Override
	public double getDistFromSource(PressureSource p, FlowDir dir) {
		for (Triple<PressureSource, FlowDir, Double> set : sources) {
			if (set.getLeft() == p && set.getMiddle() == dir) return set.getRight();
		}
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public String speakToDev(boolean sneak) {
		if (sneak) {
			if (getNetwork() != null) return getNetwork().getInfo();
			else return "No network; either this is client-side or something has gone wrong";
		}
		else {
			StringBuilder builder = new StringBuilder();
			builder.append(String.format("Currently contains %s mb of %s\n", this.getFluidAmount(), getStoredFluid().getFluid()));
			builder.append(String.format("Network: %s Pressure: %s with %s (%s) neighbours\n", getNetwork() == null ? "none" : getNetwork().getID(), getPressure(), nearbyAnchorPositions.size(), nearbyAnchors.size()));
			builder.append("Sources:\n");
			for (Triple<PressureSource, FlowDir, Double> t : sources) {
				builder.append(String.format("%s, %s, %s\n", t.getLeft().toString(), t.getMiddle().name(), t.getRight()));
			}
			return builder.toString();
		}
	}
	
	@Override
	public void doExtraAction() {
		if (isOpen && fluidType != Fluids.EMPTY) {
			fluidAmount -= this.getPressure() * FluidPipeNetwork.PRESSURE_TRANSFER_COEFF;
		}
	}
	
	@Override
	public double getFluidAmount() {
		return fluidAmount;
	}
	
	public String toString() {
		return "NODE at " + getPos();
	}

	@Override
	public void respondToDev(UseOnContext context) {

		if (context.getPlayer().isShiftKeyDown() && FluidPipeNetwork.MANUAL_TICKING) {
    		if (getNetwork() != null) getNetwork().tick();
    	}
    	else if (context.getPlayer().isShiftKeyDown()) {
    		FufoMod.LOGGER.info("Toggling openness");
    		setOpen(!isOpen());
    	}
    	else {
        	FufoMod.LOGGER.info("Adding water");
        	addFluid(Fluids.WATER, 1000.0);
    	}
	}

	@Override
	public double getPotentialPressure() {
		return getPressure();
	}

	@Override
	public double getRealPressure() {
		return getPressure();
	}

	@Override
	public boolean shouldPropagate() {
		return true;
	}
}
