package team.lodestar.fufo.registry.common.magic;

import net.minecraft.resources.ResourceLocation;
import team.lodestar.fufo.FufoMod;
import team.lodestar.fufo.common.entity.magic.spell.tier1.SpellBolt;
import team.lodestar.fufo.common.magic.spell.attributes.DamageAttribute;
import team.lodestar.fufo.common.magic.spell.attributes.LifespanAttribute;
import team.lodestar.fufo.common.magic.spell.attributes.CooldownAttribute;
import team.lodestar.fufo.common.magic.spell.attributes.VelocityAttribute;
import team.lodestar.fufo.common.magic.spell.effects.ProjectileEffect;
import team.lodestar.fufo.common.magic.spell.effects.ToggledPotionSpellEffect;
import team.lodestar.fufo.core.magic.spell.*;
import team.lodestar.fufo.registry.common.FufoMobEffects;

import java.util.HashMap;
import java.util.function.Function;

import static team.lodestar.fufo.core.magic.spell.SpellType.createSpellType;

public class FufoSpellTypes {
    public static final HashMap<ResourceLocation, SpellType> SPELL_TYPES = new HashMap<>();

    public static final SpellType EMPTY = register(new EmptySpellType());

    public static final SpellType FORCE_BOLT = register(FufoMod.fufoPath("force_bolt"),
            r -> createSpellType(r, new ProjectileEffect(SpellBolt::new))
                    .addAttributes(new LifespanAttribute(80), new VelocityAttribute(2f), new DamageAttribute(6f), new CooldownAttribute(30)));

    public static final SpellType WINDSTEP = register(FufoMod.fufoPath("windstep"),
            r -> createSpellType(r, new ToggledPotionSpellEffect(FufoMobEffects.ULTRAKILL_MOVEMENT))
                    .setSpellElement(FufoMagicElements.AIR)
                    .addAttributes(new CooldownAttribute(10)));

    public static SpellType register(ResourceLocation id, Function<ResourceLocation, SpellType.SpellTypeBuilder> spellTypeFunction) {
        SpellType spellType = spellTypeFunction.apply(id).build();
        SPELL_TYPES.put(id, spellType);
        return spellType;
    }

    public static SpellType register(SpellType spellType) {
        SPELL_TYPES.put(spellType.id, spellType);
        return spellType;
    }
}