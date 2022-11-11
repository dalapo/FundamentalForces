package team.lodestar.fufo.client.ui.spellinventory;

import team.lodestar.fufo.core.magic.spell.SpellInstance;

public class SpellSlot {
	private SpellInstance spell;
	private int x;
	private int y;
	
	public SpellSlot(int x, int y) {
		this.x = x;
		this.y = y;
	}
}