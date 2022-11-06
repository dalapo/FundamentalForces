package team.lodestar.fufo.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.lodestar.fufo.common.effect.UltrakillMovementEffect;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "isSprinting", at = @At(value = "HEAD"), cancellable = true)
    private void fundamentalForces$isSprinting(CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) ((Object) this);
        if (entity instanceof LivingEntity livingEntity && UltrakillMovementEffect.forceSprint(livingEntity)) {
            cir.setReturnValue(true);
        }
    }
}