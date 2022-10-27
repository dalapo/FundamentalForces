package team.lodestar.fufo.registry.common.magic;

import net.minecraft.resources.ResourceLocation;
import team.lodestar.fufo.FufoMod;
import team.lodestar.fufo.common.entity.magic.spell.tier1.SpellBolt;
import team.lodestar.fufo.common.magic.spell.datas.DamageAttribute;
import team.lodestar.fufo.common.magic.spell.datas.LifespanAttribute;
import team.lodestar.fufo.common.magic.spell.datas.CooldownAttribute;
import team.lodestar.fufo.common.magic.spell.datas.VelocityAttribute;
import team.lodestar.fufo.common.magic.spell.effects.PlaceSpellEffect;
import team.lodestar.fufo.common.magic.spell.effects.ProjectileEffect;
import team.lodestar.fufo.core.magic.spell.*;
import team.lodestar.fufo.registry.common.FufoBlocks;

import java.util.HashMap;
import java.util.function.Function;

public class FufoSpellTypes {
    public static final HashMap<ResourceLocation, SpellType> SPELL_TYPES = new HashMap<>();

    public static final SpellType EMPTY = registerSpellHolder(new EmptySpellType());

    public static final SpellType FORCE_BOLT = registerSpellHolder(new SpellType(FufoMod.fufoPath("force_bolt"),
            defaultSpellInstance(FufoSpellCastModes.INSTANT,
                    new LifespanAttribute(80),
                    new VelocityAttribute(2f),
                    new DamageAttribute(6f),
                    new CooldownAttribute(30)
            ),
            new ProjectileEffect(SpellBolt::new, FufoMagicElements.FORCE)));

    public static final SpellType FORCE_ORB = registerSpellHolder(new SpellType(FufoMod.fufoPath("force_orb"),
            defaultSpellInstance(FufoSpellCastModes.INSTANT),
            new PlaceSpellEffect(FufoBlocks.FORCE_ORB, FufoMagicElements.FORCE)));

    public static SpellType registerSpellHolder(SpellType spellType) {
        SPELL_TYPES.put(spellType.id, spellType);
        return spellType;
    }

    public static Function<SpellType, SpellInstance> defaultSpellInstance(SpellCastMode mode) {
        return (type) -> new SpellInstance(type, mode);
    }

    public static Function<SpellType, SpellInstance> defaultSpellInstance(SpellCastMode mode, SpellAttribute... attributes) {
        return (type) -> {
            SpellInstance spellInstance = new SpellInstance(type, mode);
            for (SpellAttribute data : attributes) {
                spellInstance.attributes.put(data.id, data);
            }
            return spellInstance;
        };
    }
}