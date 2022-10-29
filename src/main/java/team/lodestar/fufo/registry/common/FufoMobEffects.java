package team.lodestar.fufo.registry.common;

import net.minecraftforge.registries.RegistryObject;
import team.lodestar.fufo.FufoMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import team.lodestar.fufo.common.effect.UltrakillMovementEffect;

public class FufoMobEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, FufoMod.FUFO);

    public static final RegistryObject<MobEffect> ULTRAKILL_MOVEMENT = EFFECTS.register("ultrakill_movement", UltrakillMovementEffect::new);

}
