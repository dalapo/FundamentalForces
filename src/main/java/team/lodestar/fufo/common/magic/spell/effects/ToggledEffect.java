package team.lodestar.fufo.common.magic.spell.effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import team.lodestar.fufo.common.entity.magic.spell.AbstractSpellProjectile;
import team.lodestar.fufo.common.magic.spell.attributes.EffectActiveAttribute;
import team.lodestar.fufo.core.magic.MagicElementType;
import team.lodestar.fufo.core.magic.spell.SpellEffect;
import team.lodestar.fufo.core.magic.spell.SpellInstance;
import team.lodestar.fufo.registry.common.magic.FufoSpellDataKeys;

import java.awt.*;
import java.util.function.Function;

public abstract class ToggledEffect extends SpellEffect {

    public ToggledEffect(MagicElementType element) {
        super(CastLogicHandler.ALWAYS_DEFAULT_CAST, element);
    }

    @Override
    public void effect(SpellInstance spell, ServerPlayer player) {
        FufoSpellDataKeys.DataKey<EffectActiveAttribute> key = FufoSpellDataKeys.EFFECT_ACTIVE_KEY;
        boolean hasSpellAttribute = isActive(spell);
        if (hasSpellAttribute) {
            spell.attributes.removeSpellAttribute(key);
            toggleOff(spell, player);
        } else {
            spell.attributes.putSpellAttribute(key, new EffectActiveAttribute());
            toggleOn(spell, player);
        }
    }

    public boolean isActive(SpellInstance spell) {
        return spell.attributes.hasSpellAttribute(FufoSpellDataKeys.EFFECT_ACTIVE_KEY);
    }

    public abstract void toggleOn(SpellInstance spell, ServerPlayer player);

    public abstract void toggleOff(SpellInstance spell, ServerPlayer player);
}