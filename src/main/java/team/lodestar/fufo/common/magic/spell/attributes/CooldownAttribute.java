package team.lodestar.fufo.common.magic.spell.attributes;

import net.minecraft.nbt.CompoundTag;
import team.lodestar.fufo.core.magic.spell.SpellAttribute;
import team.lodestar.fufo.registry.common.magic.FufoSpellDataKeys;

public class CooldownAttribute extends SpellAttribute {
    public final int duration;
    public CooldownAttribute(int duration) {
        super(FufoSpellDataKeys.COOLDOWN_KEY);
        this.duration = duration;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("duration", duration);
        return tag;
    }

    public static CooldownAttribute deserializeNBT(CompoundTag tag) {
        return new CooldownAttribute(tag.getInt("duration"));
    }
}
