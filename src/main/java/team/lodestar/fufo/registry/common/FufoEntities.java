package team.lodestar.fufo.registry.common;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.fufo.FufoMod;
import team.lodestar.fufo.client.rendering.entity.magic.spell.tier0.MissileProjectileRenderer;
import team.lodestar.fufo.client.rendering.entity.wisp.WispEntityRenderer;
import team.lodestar.fufo.common.entity.magic.spell.tier1.SpellBolt;
import team.lodestar.fufo.common.entity.wisp.WispEntity;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FufoEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, FufoMod.FUFO);

    public static final RegistryObject<EntityType<WispEntity>> METEOR_FIRE_WISP = register("wisp", EntityType.Builder.<WispEntity>of((t, l) -> new WispEntity(l), MobCategory.MISC).sized(0.75f, 0.75f));

    public static final RegistryObject<EntityType<SpellBolt>> SPELL_BOLT = register("spell_bolt", EntityType.Builder.<SpellBolt>of((t, l) -> new SpellBolt(l), MobCategory.MISC).sized(0.1F, 0.1F));

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String id, EntityType.Builder<T> builder) {
        return ENTITY_TYPES.register(id, () -> builder.build(id));
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientOnly {
        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(FufoEntities.METEOR_FIRE_WISP.get(), WispEntityRenderer::new);
            event.registerEntityRenderer(FufoEntities.SPELL_BOLT.get(), MissileProjectileRenderer::new);
        }
    }
}