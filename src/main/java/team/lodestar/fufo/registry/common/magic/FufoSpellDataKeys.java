package team.lodestar.fufo.registry.common.magic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.fufo.common.magic.spell.attributes.*;
import team.lodestar.fufo.core.magic.spell.SpellAttribute;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static team.lodestar.fufo.FufoMod.fufoPath;

public class FufoSpellDataKeys {
    public static final Map<ResourceLocation, SpellDataKey<? extends SpellAttribute>> DATA_KEYS = new HashMap<>();

    public static final SpellDataKey<EffectActiveAttribute> EFFECT_ACTIVE_KEY = registerDataKey(fufoPath("active"), EffectActiveAttribute.class, EffectActiveAttribute::deserializeNBT);

    public static final SpellDataKey<CooldownAttribute> COOLDOWN_KEY = registerDataKey(fufoPath("cooldown"), CooldownAttribute.class, CooldownAttribute::deserializeNBT);
    public static final SpellDataKey<LifespanAttribute> LIFESPAN_KEY = registerDataKey(fufoPath("lifespan"), LifespanAttribute.class, LifespanAttribute::deserializeNBT);
    public static final SpellDataKey<VelocityAttribute> VELOCITY_KEY = registerDataKey(fufoPath("velocity"), VelocityAttribute.class, VelocityAttribute::deserializeNBT);
    public static final SpellDataKey<DamageAttribute> DAMAGE_KEY = registerDataKey(fufoPath("damage"), DamageAttribute.class, DamageAttribute::deserializeNBT);

    public static <T extends SpellAttribute> SpellDataKey<T> registerDataKey(ResourceLocation location, Class<T> classType, Function<CompoundTag, T> serializer) {
        SpellDataKey<T> value = new SpellDataKey<>(location, classType, serializer);
        DATA_KEYS.put(location, value);
        return value;
    }

    public static class SpellDataKey<T extends SpellAttribute> {
        public final ResourceLocation id;
        public final Class<T> classType;
        public final Function<CompoundTag, T> serializer;
        public SpellDataKey(ResourceLocation id, Class<T> classType, Function<CompoundTag, T> serializer) {
            this.id = id;
            this.classType = classType;
            this.serializer = serializer;
        }
    }
}
