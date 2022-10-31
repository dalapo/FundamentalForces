package team.lodestar.fufo.common.magic.spell.states;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import team.lodestar.fufo.FufoMod;

public class DashState implements FufoPlayerState {
    public static final ResourceLocation DASH = FufoMod.fufoPath("dash");

    public static final int INITIAL_STATE_LIFETIME = 5;
    public static final int DASH_DURATION = 2;

    public int stateLifetime = INITIAL_STATE_LIFETIME;
    public final Vec3 forcedMotion;
    public final boolean wasGrounded;
    public boolean discarded;

    public DashState(boolean wasGrounded, Vec3 forcedMotion) {
        this.wasGrounded = wasGrounded;
        this.forcedMotion = forcedMotion;
    }

    public void tick(Player player) {
        if (wasGrounded) {
            player.setOnGround(true);
        }
        if (isActive()) {
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
    }

    public boolean isActive() {
        return stateLifetime > INITIAL_STATE_LIFETIME - DASH_DURATION;
    }

    public void end(Player player) {
        discarded = true;
    }
}