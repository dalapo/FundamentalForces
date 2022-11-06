package team.lodestar.fufo.registry.common.magic;

import net.minecraft.resources.ResourceLocation;
import team.lodestar.fufo.FufoMod;
import team.lodestar.fufo.core.magic.WeaveBindingType;
import team.lodestar.fufo.core.magic.MagicElementType;
import team.lodestar.fufo.registry.common.weave.FufoBindingTypes;

import java.util.HashMap;

public class FufoMagicElements {
    public static final HashMap<ResourceLocation, MagicElementType> ELEMENTS = new HashMap<>(); //TODO: create inner classes for all of these, containing registry entries for each registry
    public static final MagicElementType FORCE = registerElement(new MagicElementType(FufoMod.fufoPath("force")));
    public static final MagicElementType FIRE = registerElement(new MagicElementType(FufoMod.fufoPath("fire")));
    public static final MagicElementType WATER = registerElement(new MagicElementType(FufoMod.fufoPath("water")));
    public static final MagicElementType EARTH = registerElement(new MagicElementType(FufoMod.fufoPath("earth")));
    public static final MagicElementType AIR = registerElement(new MagicElementType(FufoMod.fufoPath("air")));

    protected static MagicElementType registerElement(MagicElementType element) {
        ELEMENTS.put(element.id, element);
        FufoBindingTypes.registerBinding(new WeaveBindingType(element));
        return element;
    }
}
