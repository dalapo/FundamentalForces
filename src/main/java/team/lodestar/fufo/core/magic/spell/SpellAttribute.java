package team.lodestar.fufo.core.magic.spell;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.fufo.registry.common.magic.FufoSpellDataKeys;

public abstract class SpellAttribute {

    public final ResourceLocation id;

    public SpellAttribute(FufoSpellDataKeys.SpellDataKey<?> key) {
        this.id = key.id;
    }

    public abstract CompoundTag serializeNBT();
}
