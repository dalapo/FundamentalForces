package com.sammy.fufo.mixin;


import com.sammy.fufo.common.capability.FufoPlayerDataCapability;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public class InventoryMixin {
    @Shadow
    @Final
    public Player player;

    @Inject(method = "getSelected", at = @At(value = "RETURN"), cancellable = true)
    private void fundamentalForcesInventoryRemoveSelectedItemMixin(CallbackInfoReturnable<ItemStack> cir) {
        FufoPlayerDataCapability.getCapability(player).ifPresent(c -> {
            if (c.hotbarHandler.open || (c.hotbarHandler.updateCachedSlot && player.level.isClientSide)) {
                cir.setReturnValue(ItemStack.EMPTY);
            }
        });
    }
}