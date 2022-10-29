package team.lodestar.fufo.common.magic.spell.attributes;

import net.minecraft.resources.ResourceLocation;
import team.lodestar.fufo.core.magic.spell.SpellAttribute;
import team.lodestar.fufo.registry.common.magic.FufoSpellDataKeys;

import java.util.HashMap;
import java.util.Optional;

import static team.lodestar.fufo.registry.common.magic.FufoSpellDataKeys.DATA_KEYS;

public class SpellAttributeMap extends HashMap<FufoSpellDataKeys.SpellDataKey<?>, SpellAttribute> {

    public <T extends SpellAttribute> SpellAttribute putSpellAttribute(FufoSpellDataKeys.SpellDataKey<T> key, T attribute) {
        return put(key, attribute);
    }

    public boolean hasSpellAttribute(FufoSpellDataKeys.SpellDataKey<?> key) {
        return containsKey(key);
    }

    public boolean removeSpellAttribute(FufoSpellDataKeys.SpellDataKey<?> key) {
        SpellAttribute remove = remove(key);
        return remove != null;
    }

    public <T extends SpellAttribute> Optional<T> getSpellAttribute(FufoSpellDataKeys.SpellDataKey<T> key) {
        if (!DATA_KEYS.containsKey(key.id)) {
            try {
                throw new IllegalAccessException("Specified Data Key isn't registered.");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (!containsKey(key)) {
            return Optional.empty();
        }
        return Optional.of(key.classType.cast(get(key)));
    }
    public <T extends SpellAttribute> T getMandatorySpellAttribute(FufoSpellDataKeys.SpellDataKey<T> key) {
        return getSpellAttribute(key).orElseThrow(()-> new RuntimeException("Spell Effect is missing mandatory attributes, this is very bad."));
    }
}