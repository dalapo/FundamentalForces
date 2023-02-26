package team.lodestar.fufo.unsorted.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;

public class DirectionMap<T> implements Iterable<T> {
	Map<Direction, T> map = new HashMap<>();
	
	public void put(Direction dir, T obj) {
		map.put(dir, obj);
	}
	
	public T get (Direction dir) {
		return map.get(dir);
	}
	
	public T remove(Direction dir) {
		return map.remove(dir);
	}

	@Override
	public Iterator<T> iterator() {
		return map.values().iterator();
	}
	
	public void rotate(Axis axis, boolean cw) {
		Map<Direction, T> newMap = new HashMap<>();
		for (Direction d : Direction.values()) {
			newMap.put((cw ? d.getClockWise(axis) : d.getCounterClockWise(axis)), map.get(d));
		}
		map = newMap;
	}
}