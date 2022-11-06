package team.lodestar.fufo.core.magic.spell;

import net.minecraft.world.level.Level;
import team.lodestar.fufo.core.magic.MagicElementType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.BlockHitResult;
import team.lodestar.fufo.registry.common.magic.FufoSpellDataKeys;

import javax.annotation.Nullable;
import java.util.Arrays;

public abstract class SpellEffect {

    public enum CastLogicHandler {
        ONLY_BLOCK,
        INDEPENDENT,
        ALWAYS_DEFAULT_CAST
    }

    public final CastLogicHandler handler;

    public SpellEffect(CastLogicHandler handler) {
        this.handler = handler;
    }

    public void tick(SpellInstance spell, Level level, @Nullable ServerPlayer player) {

    }


    public void reactToDeath(SpellInstance spell, ServerPlayer player) {

    }

    public void cast(SpellInstance spell, ServerPlayer player) {
        if (canCast(spell, player)) {
            effect(spell, player);
        }
    }

    public void cast(SpellInstance spell, ServerPlayer player, BlockHitResult result) {
        if (canCast(spell, player)) {
            blockEffect(spell, player, result);
        }
    }

    public void blockEffect(SpellInstance spell, ServerPlayer player, BlockHitResult result) {
    }

    public void effect(SpellInstance spell, ServerPlayer player) {
    }

    public boolean canCast(SpellInstance spell, ServerPlayer player) {
        boolean isOnCooldown = spell.isOnCooldown();
        if (!isOnCooldown) {
            spell.setDefaultCooldown(player);
        }
        return !isOnCooldown;
    }
}