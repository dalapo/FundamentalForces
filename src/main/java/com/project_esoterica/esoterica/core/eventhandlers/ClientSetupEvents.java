package com.project_esoterica.esoterica.core.eventhandlers;

import com.project_esoterica.esoterica.core.setup.client.KeyBindingRegistry;
import com.project_esoterica.esoterica.core.setup.worldevent.WorldEventRenderers;
import com.project_esoterica.esoterica.core.systems.rendering.RenderManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetupEvents {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        WorldEventRenderers.registerRenderers(event);
        RenderManager.setupDelayedRenderer(event);
        KeyBindingRegistry.registerKeyBinding(event);
    }
}