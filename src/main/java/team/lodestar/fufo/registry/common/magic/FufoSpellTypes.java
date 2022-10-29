package team.lodestar.fufo.registry.common.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import team.lodestar.fufo.FufoMod;
import team.lodestar.fufo.common.entity.magic.spell.tier1.SpellBolt;
import team.lodestar.fufo.common.magic.spell.attributes.DamageAttribute;
import team.lodestar.fufo.common.magic.spell.attributes.LifespanAttribute;
import team.lodestar.fufo.common.magic.spell.attributes.CooldownAttribute;
import team.lodestar.fufo.common.magic.spell.attributes.VelocityAttribute;
import team.lodestar.fufo.common.magic.spell.effects.PlaceBlockSpellEffect;
import team.lodestar.fufo.common.magic.spell.effects.ProjectileEffect;
import team.lodestar.fufo.common.magic.spell.effects.ToggledPotionSpellEffect;
import team.lodestar.fufo.core.magic.spell.*;
import team.lodestar.fufo.registry.common.FufoBlocks;
import team.lodestar.fufo.registry.common.FufoMobEffects;

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
            new PlaceBlockSpellEffect(FufoBlocks.FORCE_ORB, FufoMagicElements.FORCE)));


    public static final SpellType HASTE_SPELL = registerSpellHolder(new SpellType(FufoMod.fufoPath("haste_spell"),
            defaultSpellInstance(FufoSpellCastModes.INSTANT,
                    new CooldownAttribute(10)
            ),
            new ToggledPotionSpellEffect(()-> {
                MobEffectInstance mobEffectInstance = new MobEffectInstance(MobEffects.DIG_SPEED, 100000, 1);
                mobEffectInstance.setNoCounter(true);
                return mobEffectInstance;
            }, FufoMagicElements.FIRE)));

    public static final SpellType MOVEMENT_SPELL = registerSpellHolder(new SpellType(FufoMod.fufoPath("movement_spell"),
            defaultSpellInstance(FufoSpellCastModes.INSTANT,
                    new CooldownAttribute(10)
            ),
            new ToggledPotionSpellEffect(()-> {
                MobEffectInstance mobEffectInstance = new MobEffectInstance(FufoMobEffects.ULTRAKILL_MOVEMENT.get(), 100000, 1);
                mobEffectInstance.setNoCounter(true);
                return mobEffectInstance;
            }, FufoMagicElements.AIR)));

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