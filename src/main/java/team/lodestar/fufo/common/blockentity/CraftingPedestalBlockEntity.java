package team.lodestar.fufo.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import team.lodestar.lodestone.systems.blockentity.ItemHolderBlockEntity;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntityInventory;

/**
 * Items used in array crafting are offered up by these pedestals.
 * @author ProfessorLucario
 *
 */
public class CraftingPedestalBlockEntity extends ItemHolderBlockEntity {
	
	public CraftingPedestalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		inventory = new LodestoneBlockEntityInventory(1, 1);
	}
	
	public boolean hasItem() {
		return !inventory.getStackInSlot(0).isEmpty();
	}
	
	public ItemStack getItem(boolean remove) {
		return inventory.extractItem(0, 1, !remove);
	}
	
	
}