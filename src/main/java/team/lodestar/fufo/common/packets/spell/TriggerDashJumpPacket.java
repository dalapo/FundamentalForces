package team.lodestar.fufo.common.packets.spell;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import team.lodestar.fufo.common.capability.FufoPlayerDataCapability;
import team.lodestar.fufo.common.effect.UltrakillMovementEffect;
import team.lodestar.lodestone.systems.network.LodestoneServerPacket;

import java.util.function.Supplier;

public class TriggerDashJumpPacket extends LodestoneServerPacket {

    public TriggerDashJumpPacket() {}

    public void encode(FriendlyByteBuf buf) {
    }

    @Override
    public void execute(Supplier<NetworkEvent.Context> context) {
        Player player = context.get().getSender();
        FufoPlayerDataCapability.getCapabilityOptional(player).ifPresent(c -> {
            UltrakillMovementEffect.handleDashJump(player);
        });
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, TriggerDashJumpPacket.class, TriggerDashJumpPacket::encode, TriggerDashJumpPacket::decode, TriggerDashJumpPacket::execute);
    }

    public static TriggerDashJumpPacket decode(FriendlyByteBuf buf) {
        return new TriggerDashJumpPacket();
    }
}