package team.lodestar.fufo.unsorted.eventhandlers;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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

    @SubscribeEvent
    public static void renderOverlay(RenderGuiOverlayEvent.Pre event) {
        PlayerSpellHandler.ClientOnly.moveOverlays(event);
    }

    @SubscribeEvent
    public static void renderOverlay(RenderGuiOverlayEvent.Post event) {
        PlayerSpellHandler.ClientOnly.renderSpellHotbar(event);
    }
}