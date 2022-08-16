package team.lodestar.fufo.core.setup.server;

import team.lodestar.fufo.common.packets.FufoEntityCapabilitySyncPacket;
import team.lodestar.fufo.common.packets.FufoPlayerCapabilitySyncPacket;
import team.lodestar.fufo.common.packets.FufoWorldCapabilitySyncPacket;
import team.lodestar.fufo.common.packets.spell.SyncSpellCooldownPacket;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import static team.lodestar.fufo.FufoMod.FUFO;
import static team.lodestar.fufo.FufoMod.fufoPath;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = FUFO, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PacketRegistry {
    public static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(fufoPath("main"), () -> PacketRegistry.PROTOCOL_VERSION, PacketRegistry.PROTOCOL_VERSION::equals, PacketRegistry.PROTOCOL_VERSION::equals);

    @SuppressWarnings("UnusedAssignment")
    @SubscribeEvent
    public static void registerPackets(FMLCommonSetupEvent event) {
        int index = 0;
        FufoPlayerCapabilitySyncPacket.register(INSTANCE, index++);
        FufoEntityCapabilitySyncPacket.register(INSTANCE, index++);
        FufoWorldCapabilitySyncPacket.register(INSTANCE, index++);
        SyncSpellCooldownPacket.register(INSTANCE, index++);
    }
}