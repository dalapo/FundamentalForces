package team.lodestar.fufo.common.crafting;

import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.ItemStack;
import team.lodestar.fufo.unsorted.util.DirectionMap;

/**
 * A single item in a larger crafting graph.
 * Consider it like an atom in The Codex of Alchemical Engineering.
 * @author ProfessorLucario
 *
 */
public class LinkGraphComponent {
	ItemStack item;
	DirectionMap<LinkGraphComponent> connections;
	ItemLinkGraph graph;
	
	// A hacky way to prevent infinite recursion
	private boolean rotated = false;
	
	public LinkGraphComponent(ItemStack is) {
		item = is;
	}
	
	public void rotate(Axis axis, boolean cw) {
		rotated = true;
		connections.rotate(axis, cw);
		for (LinkGraphComponent comp : connections) {
			if (!comp.rotated) comp.rotate(axis, cw);
		}
		rotated = false;
	}
}