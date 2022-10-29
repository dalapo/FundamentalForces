package team.lodestar.fufo.common.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import team.lodestar.fufo.common.capability.FufoPlayerDataCapability;
import team.lodestar.fufo.common.magic.spell.PlayerSpellHandler;
import team.lodestar.fufo.registry.client.FufoKeybinds;
import team.lodestar.fufo.registry.common.FufoMobEffects;
import team.lodestar.lodestone.helpers.ColorHelper;

import java.awt.*;

public class UltrakillMovementEffect extends MobEffect {
    public UltrakillMovementEffect() {
        super(MobEffectCategory.BENEFICIAL, ColorHelper.getColor(new Color(75, 243, 218)));
        addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "e2306a3e-4ffc-45dc-b9c6-30acb18efab3", -0.05f, AttributeModifier.Operation.MULTIPLY_TOTAL);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, "2f2de20d-5acf-482b-91c9-3b8f145e55af", 0.2f, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    public static boolean forceSprint(LivingEntity entity) {
        return entity.hasEffect(FufoMobEffects.ULTRAKILL_MOVEMENT.get());
    }

    public static void onEntityJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance effectInstance = entity.getEffect(FufoMobEffects.ULTRAKILL_MOVEMENT.get());
        if (effectInstance != null) {
            entity.setDeltaMovement(entity.getDeltaMovement().add(0, effectInstance.getAmplifier() * 0.2f, 0));
        }
    }

    public static void onEntityFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance effectInstance = entity.getEffect(FufoMobEffects.ULTRAKILL_MOVEMENT.get());
        if (effectInstance != null) {
            event.setDistance(event.getDistance() / (6 + effectInstance.getAmplifier()));
        }
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
            Player player = instance.player;
            FufoPlayerDataCapability.getCapabilityOptional(player).ifPresent(c -> {
                MobEffectInstance effectInstance = player.getEffect(FufoMobEffects.ULTRAKILL_MOVEMENT.get());
                if (effectInstance != null && instance.options.keySprint.consumeClick()) {
                    //dash!!!
                }
            });
        }
    }
}