package team.lodestar.fufo.client.ui.spellinventory;

import team.lodestar.fufo.core.magic.spell.SpellInstance;

public interface SpellInventory {
	public SpellInstance getSpell(int slot);
	public void setSpell(int slot, SpellInstance spell);
	public int getNumSpells();
}