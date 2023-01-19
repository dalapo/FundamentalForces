
package team.lodestar.fufo.unsorted.util;

import net.minecraft.world.item.context.UseOnContext;

public interface DevSpellResponder {
	default void respondToDev(UseOnContext context) {

	}

	default String speakToDev(boolean sneak) {
		return null;
	}
}
