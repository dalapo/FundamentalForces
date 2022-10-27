package team.lodestar.fufo.common.magic.spell.datas;

import net.minecraft.nbt.CompoundTag;
import team.lodestar.fufo.core.magic.spell.SpellAttribute;
import team.lodestar.fufo.registry.common.magic.FufoSpellDataKeys;

public class VelocityAttribute extends SpellAttribute {

    public final float velocity;
    public VelocityAttribute(float velocity) {
        super(FufoSpellDataKeys.VELOCITY_KEY);
        this.velocity = velocity;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("velocity", velocity);
        return tag;
    }

    public static VelocityAttribute deserializeNBT(CompoundTag tag) {
        return new VelocityAttribute(tag.getFloat("velocity"));
    }
}
