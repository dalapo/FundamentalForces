package team.lodestar.fufo.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import team.lodestar.fufo.common.crafting.LinkGraphComponent;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;

public class ArrayBlockEntity extends LodestoneBlockEntity {
	LinkGraphComponent grabbedItem;
	
	Direction armDirection;
	int armLength;
	
	public ArrayBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		armDirection = Direction.UP;
		armLength = 2;
	}
	
	private void grabItem() {
		BlockEntity te = level.getBlockEntity(worldPosition.relative(armDirection, armLength));
		if (te instanceof CraftingPedestalBlockEntity cpbe && cpbe.hasItem()) {
			grabbedItem = new LinkGraphComponent(cpbe.getItem(true));
		}
	}
	
	private void pivot() {
		
	}
	
	private void twist() {
		
	}
	
}