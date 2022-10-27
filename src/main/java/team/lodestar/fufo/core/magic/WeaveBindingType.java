package team.lodestar.fufo.core.magic;

import net.minecraft.resources.ResourceLocation;

public class WeaveBindingType {
    public final ResourceLocation id;

    public WeaveBindingType(ResourceLocation id) {
        this.id = id;
    }

    public WeaveBindingType(MagicElementType element) {
        this.id = element.id;
    }
}