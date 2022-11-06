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
<<<<<<< HEAD
import team.lodestar.fufo.FufoMod;
import team.lodestar.fufo.client.rendering.entity.falling.FallingStarRenderer;
import team.lodestar.fufo.client.ui.spellinventory.SpellInventoryScreen;
import team.lodestar.fufo.common.capability.FufoPlayerDataCapability;
import team.lodestar.fufo.unsorted.handlers.PlayerSpellInventoryHandler;
=======
import team.lodestar.fufo.common.effect.UltrakillMovementEffect;
import team.lodestar.fufo.common.magic.spell.PlayerSpellHandler;
>>>>>>> 7ddb0685b74a161452b277c2b29ae2bb39e3750f

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientRuntimeEvents {

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
<<<<<<< HEAD
        if (event.phase.equals(TickEvent.Phase.END)) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null) {
                if (minecraft.isPaused()) {
                    return;
                }
                PlayerSpellInventoryHandler.ClientOnly.clientTick(event);
=======
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
>>>>>>> 7ddb0685b74a161452b277c2b29ae2bb39e3750f
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
    	if (event.getScreen() instanceof InventoryScreen && FufoPlayerDataCapability.getCapability(Minecraft.getInstance().player).hotbarHandler.open) {
    		FufoMod.LOGGER.info("Intercepting");
    		LocalPlayer player = Minecraft.getInstance().player;
    		Minecraft.getInstance().setScreen(new SpellInventoryScreen(Component.translatable("Spell Inventory"), player));
    		event.setCanceled(true);
    	}
    }
    
    @SubscribeEvent
<<<<<<< HEAD
    public static void renderTick(TickEvent.RenderTickEvent event) {
        FallingStarRenderer.renderTick(event);
    }

    @SubscribeEvent
    public static void renderGui(ContainerScreenEvent event) {
    	PlayerSpellInventoryHandler.ClientOnly.renderSpellInventory(event);
    }
    
    @SubscribeEvent
    public static void renderOverlay(RenderGuiOverlayEvent.Pre event) {
        PlayerSpellInventoryHandler.ClientOnly.moveOverlays(event);
=======
    public static void renderOverlay(RenderGuiOverlayEvent.Pre event) {
        PlayerSpellHandler.ClientOnly.moveOverlays(event);
>>>>>>> 7ddb0685b74a161452b277c2b29ae2bb39e3750f
    }

    @SubscribeEvent
    public static void renderOverlay(RenderGuiOverlayEvent.Post event) {
<<<<<<< HEAD
        PlayerSpellInventoryHandler.ClientOnly.renderSpellHotbar(event);
=======
        PlayerSpellHandler.ClientOnly.renderSpellHotbar(event);
>>>>>>> 7ddb0685b74a161452b277c2b29ae2bb39e3750f
    }
}