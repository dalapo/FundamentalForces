package team.lodestar.fufo.common.magic.spell.states;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import team.lodestar.fufo.FufoMod;
import team.lodestar.fufo.common.effect.UltrakillMovementEffect;

public class DashState implements FufoPlayerState {
    public static final ResourceLocation DASH = FufoMod.fufoPath("dash");

    public int dashTimer = 6;
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
        if (dashTimer > 3) {
            if (player instanceof ServerPlayer) {
                player.hasImpulse = true;
                player.resetFallDistance();
            } else {
                player.move(MoverType.SELF, forcedMotion);
            }
        }
        dashTimer--;
        if (dashTimer == 0) {
            end(player);
        }
    }

    public void end(Player player) {
        player.setDeltaMovement(player.getDeltaMovement().multiply(0f, 0.95f, 0f));
        discarded = true;
    }
}
