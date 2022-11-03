package team.lodestar.fufo.common.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.network.PacketDistributor;
import team.lodestar.fufo.common.capability.FufoPlayerDataCapability;
import team.lodestar.fufo.common.magic.spell.states.DashState;
import team.lodestar.fufo.common.packets.spell.TriggerDashJumpPacket;
import team.lodestar.fufo.common.packets.spell.TriggerDashPacket;
import team.lodestar.fufo.registry.common.FufoMobEffects;
import team.lodestar.fufo.registry.common.FufoPackets;
import team.lodestar.fufo.registry.common.FufoSounds;
import team.lodestar.lodestone.helpers.ColorHelper;

import java.awt.*;

import static team.lodestar.fufo.registry.common.magic.FufoPlayerStateKeys.DASH;

public class UltrakillMovementEffect extends MobEffect {
    public UltrakillMovementEffect() {
        super(MobEffectCategory.BENEFICIAL, ColorHelper.getColor(new Color(75, 243, 218)));
        addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "e2306a3e-4ffc-45dc-b9c6-30acb18efab3", -0.05f, AttributeModifier.Operation.MULTIPLY_TOTAL);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, "2f2de20d-5acf-482b-91c9-3b8f145e55af", 0.2f, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    public static boolean forceSprint(LivingEntity entity) {
        return entity.hasEffect(FufoMobEffects.ULTRAKILL_MOVEMENT.get());
    }

    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.END)) {
            FufoPlayerDataCapability.getCapabilityOptional(event.player).ifPresent(c -> {
                if (DASH.isInState(c.states)) {
                    DashState dashState = DASH.getState(c.states);
                    dashState.tick(event.player);
                    if (dashState.discarded) {
                        c.states.remove(DASH);
                    }
                }
            });
        }
    }

    public static void onEntityJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance effectInstance = entity.getEffect(FufoMobEffects.ULTRAKILL_MOVEMENT.get());
        if (entity instanceof Player player) {
            boolean success = handleDashJump(player);
            if (success) {
                return;
            }
        }
        if (effectInstance != null) {
            entity.setDeltaMovement(entity.getDeltaMovement().add(0, effectInstance.getAmplifier() * 0.2f, 0));
        }
    }

    public static void onEntityFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance effectInstance = entity.getEffect(FufoMobEffects.ULTRAKILL_MOVEMENT.get());
        if (effectInstance != null) {
            event.setDistance(event.getDistance() / (2 + effectInstance.getAmplifier()));
        }
    }

    public static void handleDash(Player player, Vec2 direction) {
        FufoPlayerDataCapability capability = FufoPlayerDataCapability.getCapability(player);
        float speed = 1.2f;
        float f2 = speed * direction.x;
        float f3 = speed * direction.y;
        float f4 = Mth.sin(player.getYRot() * ((float) Math.PI / 180F));
        float f5 = Mth.cos(player.getYRot() * ((float) Math.PI / 180F));
        Vec3 motion = new Vec3((f2 * f5 - f3 * f4), 0, (f3 * f5 + f2 * f4));
        player.setDeltaMovement(new Vec3(0, 0, 0));
        DASH.putState(capability.states, new DashState(player.isOnGround(), motion));
        player.playSound(FufoSounds.DASH.get());
        if (player.level.isClientSide) {
            FufoPackets.INSTANCE.send(PacketDistributor.SERVER.noArg(), new TriggerDashPacket(direction));
        }
    }

    public static boolean handleDashJump(Player player) {
        FufoPlayerDataCapability capability = FufoPlayerDataCapability.getCapability(player);
        if (DASH.isInState(capability.states) && player.isOnGround()) {
            DashState dashState = DASH.getState(capability.states);
            player.setDeltaMovement(player.getDeltaMovement().add(dashState.forcedMotion.multiply(1.25f, 0, 1.25f)));
            DASH.removeState(capability.states);
            if (player.level.isClientSide) {
                FufoPackets.INSTANCE.send(PacketDistributor.SERVER.noArg(), new TriggerDashJumpPacket());
            }
            return true;
        }
        return false;
    }

    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
    }

    @Override
    public double getAttributeModifierValue(int amplifier, AttributeModifier modifier) {
        return super.getAttributeModifierValue(Math.min(6, amplifier), modifier);
    }

    public static class ClientOnly {
        public static void clientTick(TickEvent.ClientTickEvent event) {
            Minecraft instance = Minecraft.getInstance();
            LocalPlayer player = instance.player;
            FufoPlayerDataCapability.getCapabilityOptional(player).ifPresent(c -> {
                MobEffectInstance effectInstance = player.getEffect(FufoMobEffects.ULTRAKILL_MOVEMENT.get());
                if (effectInstance != null) {
                    if (DASH.isInState(c.states) && DASH.getState(c.states).isActive()) {
                        return;
                    }
                    if (instance.options.keySprint.consumeClick()) {
                        Vec2 direction = player.input.getMoveVector();
                        if (direction.x == 0 && direction.y == 0) {
                            direction = new Vec2(0, 1);
                        }
                        handleDash(player, direction);
                    }
                }
            });
        }
    }
}