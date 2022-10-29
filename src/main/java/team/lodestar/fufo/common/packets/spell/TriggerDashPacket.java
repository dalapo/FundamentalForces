package team.lodestar.fufo.common.packets.spell;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import team.lodestar.fufo.common.capability.FufoPlayerDataCapability;
import team.lodestar.fufo.common.effect.UltrakillMovementEffect;
import team.lodestar.fufo.core.magic.spell.SpellCooldown;
import team.lodestar.lodestone.systems.network.LodestoneClientPacket;
import team.lodestar.lodestone.systems.network.LodestoneServerPacket;

import java.util.UUID;
import java.util.function.Supplier;

public class TriggerDashPacket extends LodestoneServerPacket {
    private final Vec2 direction;

    public TriggerDashPacket(Vec2 direction) {
        this.direction = direction;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeFloat(direction.x);
        buf.writeFloat(direction.y);
    }

    @Override
    public void execute(Supplier<NetworkEvent.Context> context) {
        Player player = context.get().getSender();
        FufoPlayerDataCapability.getCapabilityOptional(player).ifPresent(c -> {
            UltrakillMovementEffect.handleDash(player, direction);
        });
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, TriggerDashPacket.class, TriggerDashPacket::encode, TriggerDashPacket::decode, TriggerDashPacket::execute);
    }

    public static TriggerDashPacket decode(FriendlyByteBuf buf) {
        return new TriggerDashPacket(new Vec2(buf.readFloat(), buf.readFloat()));
    }
}