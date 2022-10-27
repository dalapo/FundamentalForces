package team.lodestar.fufo.registry.common.magic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.fufo.common.magic.spell.datas.DamageAttribute;
import team.lodestar.fufo.common.magic.spell.datas.LifespanAttribute;
import team.lodestar.fufo.common.magic.spell.datas.CooldownAttribute;
import team.lodestar.fufo.common.magic.spell.datas.VelocityAttribute;
import team.lodestar.fufo.core.magic.spell.SpellAttribute;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static team.lodestar.fufo.FufoMod.fufoPath;

public class FufoSpellDataKeys {
    public static final Map<ResourceLocation, DataKey<? extends SpellAttribute>> DATA_KEYS = new HashMap<>();

    public static final DataKey<CooldownAttribute> COOLDOWN_KEY = registerDataKey(fufoPath("cooldown"), CooldownAttribute.class, CooldownAttribute::deserializeNBT);
    public static final DataKey<LifespanAttribute> LIFESPAN_KEY = registerDataKey(fufoPath("lifespan"), LifespanAttribute.class, LifespanAttribute::deserializeNBT);
    public static final DataKey<VelocityAttribute> VELOCITY_KEY = registerDataKey(fufoPath("velocity"), VelocityAttribute.class, VelocityAttribute::deserializeNBT);
    public static final DataKey<DamageAttribute> DAMAGE_KEY = registerDataKey(fufoPath("damage"), DamageAttribute.class, DamageAttribute::deserializeNBT);

    public static <T extends SpellAttribute> DataKey<T> registerDataKey(ResourceLocation location, Class<T> classType, Function<CompoundTag, T> serializer) {
        DataKey<T> value = new DataKey<>(location, classType, serializer);
        DATA_KEYS.put(location, value);
        return value;
    }

    public static class DataKey<T extends SpellAttribute> {
        public final ResourceLocation id;
        public final Class<T> classType;
        public final Function<CompoundTag, T> serializer;
        public DataKey(ResourceLocation id, Class<T> classType, Function<CompoundTag, T> serializer) {
            this.id = id;
            this.classType = classType;
            this.serializer = serializer;
        }
    }
}
