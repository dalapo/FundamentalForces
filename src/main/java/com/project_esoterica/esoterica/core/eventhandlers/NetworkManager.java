package com.project_esoterica.esoterica.core.eventhandlers;

import com.project_esoterica.esoterica.EsotericHelper;
import com.project_esoterica.esoterica.common.packets.ScreenshakePacket;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

import static com.project_esoterica.esoterica.EmpiricalEsoterica.MOD_ID;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NetworkManager
{
    public static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(EsotericHelper.prefix("main"), () -> NetworkManager.PROTOCOL_VERSION, NetworkManager.PROTOCOL_VERSION::equals, NetworkManager.PROTOCOL_VERSION::equals);
    
    @SuppressWarnings("UnusedAssignment")
    @SubscribeEvent
    public static void registerNetworkStuff(FMLCommonSetupEvent event)
    {
        int index = 0;
        INSTANCE.registerMessage(index++, ScreenshakePacket.class, ScreenshakePacket::encode, ScreenshakePacket::decode, ScreenshakePacket::whenThisPacketIsReceived);
    }
}