package team.lodestar.fufo.common.magic.spell.attributes;

import net.minecraft.nbt.CompoundTag;
import team.lodestar.fufo.core.magic.spell.SpellAttribute;
import team.lodestar.fufo.registry.common.magic.FufoSpellDataKeys;

public class LifespanAttribute extends SpellAttribute {

    public final int lifespan;
    public LifespanAttribute(int lifespan) {
        super(FufoSpellDataKeys.LIFESPAN_KEY);
        this.lifespan = lifespan;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("lifespan", lifespan);
        return tag;
    }

    public static LifespanAttribute deserializeNBT(CompoundTag tag) {
        return new LifespanAttribute(tag.getInt("lifespan"));
    }
}
