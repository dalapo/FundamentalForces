package team.lodestar.fufo.client.ui.spellinventory;

import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

/**
 * The equivalent of a 'container' but for spells.
 * Really straightforward when you think about it.
 * @author ProfessorLucario
 *
 */
public class SpellContainer {
	private SpellSlot[] slots;
	
	/**
	 * Simple constructor for rectangular inventories
	 * @param rows
	 * @param cols
	 * @param inventory
	 */
	public SpellContainer(int rows, int cols, SpellInventory inventory) {
		slots = new SpellSlot[inventory.getNumSpells()];
	}
	
	/**
	 * Generic constructor. Define your own mapping.
	 * @param mapping Function that maps slot ID to x/y coordinate.
	 * @param inventory Inventory to map
	 */
	public SpellContainer(Function<Integer, Pair<Integer, Integer>> mapping, SpellInventory inventory) {
		slots = new SpellSlot[inventory.getNumSpells()];
		for (int i=0; i<slots.length; i++) {
			Pair<Integer, Integer> coords = mapping.apply(i);
			slots[i] = new SpellSlot(coords.getLeft(), coords.getRight());
		}
	}
}