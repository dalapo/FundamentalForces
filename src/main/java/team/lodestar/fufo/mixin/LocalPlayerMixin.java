package team.lodestar.fufo.mixin;

import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import team.lodestar.fufo.common.effect.UltrakillMovementEffect;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @ModifyVariable(method = "setSprinting", at = @At(value = "HEAD"), index = 1, argsOnly = true)
    private boolean fundamentalForces$setSprinting(boolean value) {
        if (UltrakillMovementEffect.forceSprint((LocalPlayer) ((Object) this))) {
            return true;
        }
        return value;
    }

}