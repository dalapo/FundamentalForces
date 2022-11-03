package team.lodestar.fufo.common.magic.spell.states;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import team.lodestar.fufo.FufoMod;
import team.lodestar.fufo.registry.common.magic.FufoPlayerStateKeys;

public class DashState implements FufoPlayerStateKeys.FufoPlayerState {

    public static final int DASH_DURATION = 3;
    public int stateLifetime = 3;
    public final Vec3 forcedMotion;
    public final boolean wasGrounded;
    public boolean discarded;

    public DashState(boolean wasGrounded, Vec3 forcedMotion) {
        this.wasGrounded = wasGrounded;
        this.forcedMotion = forcedMotion;
    }

    public void tick(Player player) {
        if (isActive()) {
            player.noJumpDelay = 0;
            if (player instanceof ServerPlayer) {
                player.hasImpulse = true;
                player.resetFallDistance();
            } else {
                player.move(MoverType.SELF, forcedMotion);
            }
            player.setDeltaMovement(player.getDeltaMovement().multiply(1f, 0f, 1f));
        }
        stateLifetime--;
        if (stateLifetime == 0) {
            end(player);
        }
        if (wasGrounded) {
            player.setOnGround(true);
        }
    }

    public boolean isActive() {
        return stateLifetime > 0;
    }

    public void end(Player player) {
        discarded = true;
    }
}