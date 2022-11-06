package team.lodestar.fufo.registry.common.weave;

import net.minecraft.resources.ResourceLocation;
import team.lodestar.fufo.core.magic.WeaveBindingType;

import java.util.HashMap;

public class FufoBindingTypes {
    public static final HashMap<ResourceLocation, WeaveBindingType> BINDING_TYPES = new HashMap<>();

    public static WeaveBindingType registerBinding(WeaveBindingType binding) {
        BINDING_TYPES.put(binding.id, binding);
        return binding;
    }
}
