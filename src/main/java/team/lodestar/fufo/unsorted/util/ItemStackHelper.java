package team.lodestar.fufo.unsorted.util;

import java.util.function.Predicate;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ItemStackHelper {
	private ItemStackHelper() {}
	
	/**
	 * @param handler
	 * @param criterion
	 * @return The slot number of the first stack that matches the criteria, or -1 if none match.
	 */
	public static int getSlotWithCriteria(IItemHandler handler, Predicate<ItemStack> criterion) {
		for (int i=0; i<handler.getSlots(); i++) {
			ItemStack is = handler.getStackInSlot(i);
			if (criterion.test(is)) return i;
		}
		return -1;
	}
}
