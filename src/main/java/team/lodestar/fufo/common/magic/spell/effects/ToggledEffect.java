package team.lodestar.fufo.common.magic.spell.effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import team.lodestar.fufo.core.magic.spell.SpellEffect;
import team.lodestar.fufo.core.magic.spell.SpellInstance;

public abstract class ToggledEffect extends SpellEffect {
    public static final String NBT = "effect_active";


    public ToggledEffect() {
        super(CastLogicHandler.ALWAYS_DEFAULT_CAST);
    }

    @Override
    public void effect(SpellInstance spell, ServerPlayer player) {
        boolean hasSpellAttribute = isActive(spell);
        if (hasSpellAttribute) {
            spell.extraData.remove(NBT);
            toggleOff(spell, player);
        } else {
            spell.extraData.putBoolean(NBT, true);
            toggleOn(spell, player);
        }
        player.swing(InteractionHand.MAIN_HAND, true);
    }

    @Override
    public void reactToDeath(SpellInstance spell, ServerPlayer player) {
        spell.extraData.remove(NBT);
        toggleOff(spell, player);
    }

    public boolean isActive(SpellInstance spell) {
        return spell.extraData.contains(NBT);
    }

    public abstract void toggleOn(SpellInstance spell, ServerPlayer player);

    public abstract void toggleOff(SpellInstance spell, ServerPlayer player);
}