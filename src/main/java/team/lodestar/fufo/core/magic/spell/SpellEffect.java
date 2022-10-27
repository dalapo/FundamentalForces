package team.lodestar.fufo.core.magic.spell;

import team.lodestar.fufo.core.magic.MagicElementType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.BlockHitResult;

public abstract class SpellEffect {

    public enum CastLogicHandler {
        ONLY_BLOCK,
        INDEPENDENT,
        ALWAYS_DEFAULT_CAST
    }

    public final CastLogicHandler handler;
    public final MagicElementType element;

    public SpellEffect(CastLogicHandler handler, MagicElementType element) {
        this.handler = handler;
        this.element = element;
    }

    public void cast(SpellInstance spell, ServerPlayer player) {
        if (canCast(spell, player)) {
            effect(spell, player);
        }
    }

    public void cast(SpellInstance spell, ServerPlayer player, BlockHitResult result) {
        if (canCast(spell, player)) {
            effect(spell, player, result);
        }
    }

    public abstract void effect(SpellInstance spell, ServerPlayer player, BlockHitResult result);

    public abstract void effect(SpellInstance spell, ServerPlayer player);

    public boolean canCast(SpellInstance spell, ServerPlayer player) {
        boolean isOnCooldown = spell.isOnCooldown();
        if (!isOnCooldown) {
            spell.setDefaultCooldown(player);
        }
        return !isOnCooldown;
    }
}