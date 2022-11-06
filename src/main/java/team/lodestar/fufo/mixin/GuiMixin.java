package team.lodestar.fufo.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
<<<<<<< HEAD
import team.lodestar.fufo.unsorted.handlers.PlayerSpellInventoryHandler;
=======
import team.lodestar.fufo.common.magic.spell.PlayerSpellHandler;
>>>>>>> 7ddb0685b74a161452b277c2b29ae2bb39e3750f
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    //TODO: all this should be done with forges gui system. Wire knows this shit best.

    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void fundamentalForcesHotbarOffset(float partialTicks, PoseStack poseStack, CallbackInfo ci) {
<<<<<<< HEAD
        boolean cancel = PlayerSpellInventoryHandler.ClientOnly.moveVanillaUI(false, poseStack);
=======
        boolean cancel = PlayerSpellHandler.ClientOnly.moveVanillaUI(false, poseStack);
>>>>>>> 7ddb0685b74a161452b277c2b29ae2bb39e3750f
        if (cancel) {
            ci.cancel();
        }
    }

    @Inject(method = "renderHotbar", at = @At("RETURN"))
    private void fundamentalForcesHotbarOffsetPartTwo(float partialTicks, PoseStack poseStack, CallbackInfo ci) {
<<<<<<< HEAD
        PlayerSpellInventoryHandler.ClientOnly.moveVanillaUI(true, poseStack);
=======
        PlayerSpellHandler.ClientOnly.moveVanillaUI(true, poseStack);
>>>>>>> 7ddb0685b74a161452b277c2b29ae2bb39e3750f
    }

    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    private void fundamentalForcesExperienceOffset(PoseStack poseStack, int l, CallbackInfo ci) {
<<<<<<< HEAD
        boolean cancel = PlayerSpellInventoryHandler.ClientOnly.moveVanillaUI(false, poseStack);
=======
        boolean cancel = PlayerSpellHandler.ClientOnly.moveVanillaUI(false, poseStack);
>>>>>>> 7ddb0685b74a161452b277c2b29ae2bb39e3750f
        if (cancel) {
            ci.cancel();
        }
    }

    @Inject(method = "renderExperienceBar", at = @At("RETURN"))
    private void fundamentalForcesExperienceOffsetPartTwo(PoseStack poseStack, int l, CallbackInfo ci) {
<<<<<<< HEAD
        PlayerSpellInventoryHandler.ClientOnly.moveVanillaUI(true, poseStack);
=======
        PlayerSpellHandler.ClientOnly.moveVanillaUI(true, poseStack);
>>>>>>> 7ddb0685b74a161452b277c2b29ae2bb39e3750f
    }

    @Inject(method = "renderJumpMeter", at = @At("HEAD"), cancellable = true)
    private void fundamentalForcesHorsePlinkoOffset(PoseStack poseStack, int l, CallbackInfo ci) {
<<<<<<< HEAD
        boolean cancel = PlayerSpellInventoryHandler.ClientOnly.moveVanillaUI(false, poseStack);
=======
        boolean cancel = PlayerSpellHandler.ClientOnly.moveVanillaUI(false, poseStack);
>>>>>>> 7ddb0685b74a161452b277c2b29ae2bb39e3750f
        if (cancel) {
            ci.cancel();
        }
    }

    @Inject(method = "renderJumpMeter", at = @At("RETURN"))
    private void fundamentalForcesHorsePlinkoPartTwo(PoseStack poseStack, int l, CallbackInfo ci) {
<<<<<<< HEAD
        PlayerSpellInventoryHandler.ClientOnly.moveVanillaUI(true, poseStack);
=======
        PlayerSpellHandler.ClientOnly.moveVanillaUI(true, poseStack);
>>>>>>> 7ddb0685b74a161452b277c2b29ae2bb39e3750f
    }

    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderSlot(IIFLnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;I)V"), index = 1)
    private int fundamentalForcesHotbarItemOffset(int original) {
<<<<<<< HEAD
        return (int) (original + PlayerSpellInventoryHandler.ClientOnly.itemHotbarOffset());
=======
        return (int) (original + PlayerSpellHandler.ClientOnly.itemHotbarOffset());
>>>>>>> 7ddb0685b74a161452b277c2b29ae2bb39e3750f
    }
}