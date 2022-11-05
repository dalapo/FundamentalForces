package team.lodestar.fufo.registry.common.magic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.fufo.common.magic.spell.attributes.*;
import team.lodestar.fufo.core.magic.spell.SpellAttribute;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static team.lodestar.fufo.FufoMod.fufoPath;

public class FufoSpellDataKeys {
    public static final Map<ResourceLocation, SpellDataKey<? extends SpellAttribute>> DATA_KEYS = new HashMap<>();

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

        @SuppressWarnings("unchecked")
        public void putAttribute(SpellAttributeMap<SpellAttribute> spellAttributeMap, SpellAttribute attribute) {
            spellAttributeMap.put((SpellDataKey<SpellAttribute>) this, attribute);
        }

        public T getAttribute(SpellAttributeMap<?> spellAttributeMap) {
            return classType.cast(spellAttributeMap.get(this));
        }

        public boolean hasAttribute(SpellAttributeMap<?> spellAttributeMap) {
            return spellAttributeMap.containsKey(this);
        }

        @SuppressWarnings("unchecked")
        public void removeAttribute(SpellAttributeMap<SpellAttribute> spellAttributeMap) {
            spellAttributeMap.remove((SpellDataKey<SpellAttribute>) this);
        }

        public Optional<T> getOptionalAttribute(SpellAttributeMap<?> spellAttributeMap) {
            return Optional.ofNullable(classType.cast(spellAttributeMap.get(this)));
        }

        public T getMandatoryAttribute(SpellAttributeMap<?> spellAttributeMap) {
            return getOptionalAttribute(spellAttributeMap).orElseThrow(() -> new RuntimeException("Spell Effect is missing mandatory attributes, this is very bad."));
        }
    }

    public static class SpellAttributeMap<T extends SpellAttribute> extends HashMap<FufoSpellDataKeys.SpellDataKey<T>, T> {
    }
}