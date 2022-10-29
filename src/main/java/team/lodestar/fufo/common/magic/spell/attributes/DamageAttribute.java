package team.lodestar.fufo.common.magic.spell.attributes;

import net.minecraft.nbt.CompoundTag;
import team.lodestar.fufo.core.magic.spell.SpellAttribute;
import team.lodestar.fufo.registry.common.magic.FufoSpellDataKeys;

public class DamageAttribute extends SpellAttribute {

    public final float damage;

    public DamageAttribute(float damage) {
        super(FufoSpellDataKeys.DAMAGE_KEY);
        this.damage = damage;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("damage", damage);
        return tag;
    }

    public static DamageAttribute deserializeNBT(CompoundTag tag) {
        return new DamageAttribute(tag.getFloat("damage"));
    }
}