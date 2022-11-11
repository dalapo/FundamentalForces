package team.lodestar.fufo.unsorted.eventhandlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ContainerScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import team.lodestar.fufo.FufoMod;
import team.lodestar.fufo.client.ui.spellinventory.SpellContainer;
import team.lodestar.fufo.client.ui.spellinventory.SpellInventoryScreen;
import team.lodestar.fufo.common.capability.FufoPlayerDataCapability;
import team.lodestar.fufo.common.effect.UltrakillMovementEffect;
import team.lodestar.fufo.common.magic.spell.PlayerSpellHandler;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientRuntimeEvents {

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null) {
            if (minecraft.isPaused()) {
                return;
            }
            if (event.phase.equals(TickEvent.Phase.END)) {
                PlayerSpellHandler.ClientOnly.clientTick(event);
            }
            else {
                UltrakillMovementEffect.ClientOnly.clientTick(event);
            }
        }
    }

//    @SubscribeEvent
//    public static void interceptInventoryOpening(InputEvent.Key event) {
//    	int invKey = Minecraft.getInstance().options.keyInventory.getKey().getValue();
//    	if (event.getKey() == invKey && FufoPlayerDataCapability.getCapability(Minecraft.getInstance().player).hotbarHandler.open) {
//    		FufoMod.LOGGER.info("Intercepting");
//    	}
//    }
    
    @SubscribeEvent
    public static void interceptInventoryOpening(ScreenEvent event) {
    	FufoPlayerDataCapability cap = FufoPlayerDataCapability.getCapability(Minecraft.getInstance().player);
    	if (event.getScreen() instanceof InventoryScreen && FufoPlayerDataCapability.getCapability(Minecraft.getInstance().player).hotbarHandler.isSpellHotbarOpen) {
    		FufoMod.LOGGER.info("Intercepting");
    		LocalPlayer player = Minecraft.getInstance().player;
    		Minecraft.getInstance().setScreen(new SpellInventoryScreen(Component.translatable("Spell Inventory"), new SpellContainer(1, 9, cap.hotbarHandler.spellStorage), player));
    		event.setCanceled(true);
    	}
    }
    
    @SubscribeEvent
    public static void renderOverlay(RenderGuiOverlayEvent.Pre event) {
        PlayerSpellHandler.ClientOnly.moveOverlays(event);
    }

    @SubscribeEvent
    public static void renderOverlay(RenderGuiOverlayEvent.Post event) {
        PlayerSpellHandler.ClientOnly.renderSpellHotbar(event);
    }
}