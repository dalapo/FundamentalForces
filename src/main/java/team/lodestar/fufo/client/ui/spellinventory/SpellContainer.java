package team.lodestar.fufo.client.ui.spellinventory;

import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import team.lodestar.fufo.core.magic.spell.SpellInstance;

/**
 * The equivalent of a 'container' but for spells.
 * Really straightforward when you think about it.
 * @author ProfessorLucario
 */
public class SpellContainer {
	private SpellSlot[] slots;
	
	/**
	 * Simple constructor for rectangular inventories
	 * @param rows
	 * @param cols
	 * @param inventory
	 */
	public SpellContainer(int rows, int cols, int tX, int tY, SpellInventory inventory) {
		slots = new SpellSlot[inventory.getNumSpells()];
		if (inventory.getNumSpells() != (rows * cols)) throw new IllegalArgumentException("Given number of inventory slots does not match given container size!");
	}
	
	/**
	 * Generic constructor. Define your own mapping.
	 * @param mapping Function that maps slot ID to x/y coordinate.
	 * @param inventory Inventory to map
	 */
	public SpellContainer(Pair<Integer, Integer>[] mapping, SpellInventory inventory) {
		slots = new SpellSlot[inventory.getNumSpells()];
		for (int i=0; i<slots.length; i++) {
			Pair<Integer, Integer> coords = mapping[i];
			slots[i] = new SpellSlot(coords.getLeft(), coords.getRight());
		}
	}
	
	public void setSpell(int slot, SpellInstance spell) {
		
	}
	
	public SpellInstance getSpell(int slot) {
		return slots[slot].getSpell();
	}
	
	public int getSlotCount() {
		return slots.length;
	}
	
	public SpellSlot getSlot(int i) {
		return slots[i];
	}
}