package team.lodestar.fufo.core.magic.spell;

import team.lodestar.fufo.FufoMod;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class SpellType {
    public final ResourceLocation id;
    public final Function<SpellType, SpellInstance> defaultInstanceSupplier;
    public final SpellEffect effect;

    //TODO: we wouldn't want to have to check != null everywhere for all these parameters, at least mainly SpellEffect
    public SpellType(ResourceLocation id, Function<SpellType, SpellInstance> defaultInstanceSupplier, SpellEffect effect) {
        this.id = id;
        this.defaultInstanceSupplier = defaultInstanceSupplier;
        this.effect = effect;
    }

    public ResourceLocation getIconLocation() {
        return FufoMod.fufoPath("textures/spell/icon/" + id.getPath() + ".png");
    }

    public ResourceLocation getBackgroundLocation() {
        return FufoMod.fufoPath("textures/spell/background/" + id.getPath() + "_background.png");
    }
}