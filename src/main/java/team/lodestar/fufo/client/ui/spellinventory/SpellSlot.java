package team.lodestar.fufo.client.ui.spellinventory;

import team.lodestar.fufo.core.magic.spell.SpellInstance;

public class SpellSlot {
	public static final int SIZE = 20;
	private SpellInstance spell;
	private int x;
	private int y;
	
	public SpellSlot(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public SpellInstance getSpell() {
		return spell;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}