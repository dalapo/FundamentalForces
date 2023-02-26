package team.lodestar.fufo.common.crafting;

import java.util.ArrayList;
import java.util.List;

/**
 * An ItemLinkGraph represents an item "molecule" held by the grabbers. It's made up of a number of LinkGraphComponents
 * all connected by links.
 * 
 * The "head" of the graph is simply whichever component is currently being grabbed and moved.
 * 
 * @author ProfessorLucario
 *
 */
public class ItemLinkGraph {
	private List<LinkGraphComponent> parts = new ArrayList<>();
	
	
}