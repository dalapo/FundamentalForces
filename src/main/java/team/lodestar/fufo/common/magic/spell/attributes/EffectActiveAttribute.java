package team.lodestar.fufo.common.magic.spell.attributes;

import net.minecraft.nbt.CompoundTag;
import team.lodestar.fufo.core.magic.spell.SpellAttribute;
import team.lodestar.fufo.registry.common.magic.FufoSpellDataKeys;

public class EffectActiveAttribute extends SpellAttribute {

    public EffectActiveAttribute() {
        super(FufoSpellDataKeys.EFFECT_ACTIVE_KEY);
    }

    @Override
    public CompoundTag serializeNBT() {
        return new CompoundTag();
    }

    public static EffectActiveAttribute deserializeNBT(CompoundTag tag) {
        return new EffectActiveAttribute();
    }
}
