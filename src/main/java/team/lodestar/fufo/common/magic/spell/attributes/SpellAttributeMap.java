package team.lodestar.fufo.common.magic.spell.attributes;

import net.minecraft.resources.ResourceLocation;
import team.lodestar.fufo.core.magic.spell.SpellAttribute;
import team.lodestar.fufo.registry.common.magic.FufoSpellDataKeys;

import java.util.HashMap;
import java.util.Optional;

import static team.lodestar.fufo.registry.common.magic.FufoSpellDataKeys.DATA_KEYS;

public class SpellAttributeMap extends HashMap<ResourceLocation, SpellAttribute> {

    public <T extends SpellAttribute> SpellAttribute putSpellAttribute(FufoSpellDataKeys.SpellDataKey<T> key, T attribute) {
        return put(key.id, attribute);
    }

    public boolean hasSpellAttribute(FufoSpellDataKeys.SpellDataKey<?> key) {
        return containsKey(key.id);
    }

    public boolean removeSpellAttribute(FufoSpellDataKeys.SpellDataKey<?> key) {
        SpellAttribute remove = remove(key.id);
        return remove != null;
    }

    public <T extends SpellAttribute> Optional<T> getSpellAttribute(FufoSpellDataKeys.SpellDataKey<T> key) {
        ResourceLocation id = key.id;
        if (!DATA_KEYS.containsKey(id)) {
            try {
                throw new IllegalAccessException("Specified Data Key isn't registered.");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (!containsKey(id)) {
            return Optional.empty();
        }
        return Optional.of(key.classType.cast(get(id)));
    }
    public <T extends SpellAttribute> T getMandatorySpellAttribute(FufoSpellDataKeys.SpellDataKey<T> key) {
        return getSpellAttribute(key).orElseThrow(()-> new RuntimeException("Spell Effect is missing mandatory attributes, this is very bad."));
    }
}