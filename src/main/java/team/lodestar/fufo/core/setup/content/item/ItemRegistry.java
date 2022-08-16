package team.lodestar.fufo.core.setup.content.item;

import team.lodestar.fufo.FufoMod;
import team.lodestar.fufo.core.registratation.ItemRegistrate;
import team.lodestar.fufo.core.setup.content.item.tabs.ContentTab;
import team.lodestar.lodestone.setup.LodestoneScreenParticleRegistry;
import team.lodestar.lodestone.systems.rendering.particle.ParticleBuilders;
import team.lodestar.lodestone.systems.rendering.particle.ParticleRenderTypes;
import team.lodestar.lodestone.systems.rendering.particle.SimpleParticleOptions;
import team.lodestar.fufo.core.registratation.BlockRegistrate;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

import static team.lodestar.lodestone.handlers.ScreenParticleHandler.registerItemParticleEmitter;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FufoMod.FUFO);

    public static Item.Properties DEFAULT_PROPERTIES() {
        return new Item.Properties().tab(ContentTab.INSTANCE);
    }

    public static Item.Properties GEAR_PROPERTIES() {
        return new Item.Properties().tab(ContentTab.INSTANCE).stacksTo(1);
    }
    /*
    public static final RegistryObject<Item> CRACK = ITEMS.register("crack", () -> new Item(DEFAULT_PROPERTIES()));
    public static final RegistryObject<Item> BLOCK_OF_CRACK = ITEMS.register("block_of_crack", () -> new BlockItem(BlockRegistrate.BLOCK_OF_CRACK.get(), DEFAULT_PROPERTIES()));
    public static final RegistryObject<Item> WISP_BOTTLE = ITEMS.register("wisp_bottle", () -> new Item(DEFAULT_PROPERTIES()));

    public static final RegistryObject<Item> ORTUSITE = ITEMS.register("ortusite", () -> new BlockItem(BlockRegistrate.ORTUSITE.get(), DEFAULT_PROPERTIES()));
    public static final RegistryObject<Item> ANCHOR = ITEMS.register("anchor", () -> new BlockItem(BlockRegistrate.ANCHOR.get(), DEFAULT_PROPERTIES()));
    public static final RegistryObject<Item> BURNER_EXTRACTOR = ITEMS.register("burner_extractor", () -> new BlockItem(BlockRegistrate.BURNER_EXTRACTOR.get(), DEFAULT_PROPERTIES()));
    public static final RegistryObject<Item> DEPLETED_ORTUSITE = ITEMS.register("depleted_ortusite", () -> new BlockItem(BlockRegistrate.DEPLETED_ORTUSITE.get(), DEFAULT_PROPERTIES()));
    public static final RegistryObject<Item> ORTUSITE_CHUNK = ITEMS.register("ortusite_chunk", () -> new Item(DEFAULT_PROPERTIES()));
    public static final RegistryObject<Item> ASTRAL_SHARD = ITEMS.register("astral_shard", () -> new Item(DEFAULT_PROPERTIES()));
    public static final RegistryObject<Item> DEV_TOOL = ITEMS.register("dev_tool", () -> new DevTool(GEAR_PROPERTIES()));

    public static final RegistryObject<Item> CHARRED_ROCK = ITEMS.register("charred_rock", () -> new BlockItem(BlockRegistrate.CHARRED_ROCK.get(), DEFAULT_PROPERTIES()));
    public static final RegistryObject<Item> POLISHED_CHARRED_ROCK = ITEMS.register("polished_charred_rock", () -> new BlockItem(BlockRegistrate.POLISHED_CHARRED_ROCK.get(), DEFAULT_PROPERTIES()));

    public static final RegistryObject<Item> CHARRED_ROCK_SLAB = ITEMS.register("charred_rock_slab", () -> new BlockItem(BlockRegistrate.CHARRED_ROCK_SLAB.get(), DEFAULT_PROPERTIES()));
    public static final RegistryObject<Item> POLISHED_CHARRED_ROCK_SLAB = ITEMS.register("polished_charred_rock_slab", () -> new BlockItem(BlockRegistrate.POLISHED_CHARRED_ROCK_SLAB.get(), DEFAULT_PROPERTIES()));

    public static final RegistryObject<Item> CHARRED_ROCK_STAIRS = ITEMS.register("charred_rock_stairs", () -> new BlockItem(BlockRegistrate.CHARRED_ROCK_STAIRS.get(), DEFAULT_PROPERTIES()));
    public static final RegistryObject<Item> POLISHED_CHARRED_ROCK_STAIRS = ITEMS.register("polished_charred_rock_stairs", () -> new BlockItem(BlockRegistrate.POLISHED_CHARRED_ROCK_STAIRS.get(), DEFAULT_PROPERTIES()));

    public static final RegistryObject<Item> VOLCANIC_GLASS = ITEMS.register("volcanic_glass", () -> new BlockItem(BlockRegistrate.VOLCANIC_GLASS.get(), DEFAULT_PROPERTIES()));
    public static final RegistryObject<Item> SCORCHED_EARTH = ITEMS.register("scorched_earth", () -> new BlockItem(BlockRegistrate.SCORCHED_EARTH.get(), DEFAULT_PROPERTIES()));

    public static final RegistryObject<Item> UI_TEST_BLOCK = ITEMS.register("ui_test_block", () -> new BlockItem(BlockRegistrate.UI_TEST_BLOCK.get(), DEFAULT_PROPERTIES()));
    */
    public static class ClientOnly {

        public static void registerParticleEmitters(FMLClientSetupEvent event) {
            registerItemParticleEmitter((s, x, y, order) -> {
                Random random = Minecraft.getInstance().level.random;
                if (Minecraft.getInstance().level.getGameTime() % 6L == 0) {
                    ParticleBuilders.create(LodestoneScreenParticleRegistry.STAR)
                            .setLifetime(15 + random.nextInt(10))
                            .setColor(255, 142, 139, 248, 69, 106)
                            .setAlphaCoefficient(0.5f)
                            .setMotionCoefficient(0.8f)
                            .setMotionCoefficient(0.98f)
                            .setScale(0.15f + random.nextFloat() * 0.1f)
                            .setAlpha(0.8f + random.nextFloat() * 0.2f, 0.1f)
                            .randomOffset(8, 4)
                            .randomMotion(0.2f, 0.2f)
                            .overwriteRenderType(ParticleRenderTypes.TRANSPARENT)
                            .overwriteRenderOrder(order)
                            .overwriteAnimator(SimpleParticleOptions.Animator.LAST_INDEX)
                            .repeat(x, y - 0.25f, 1);
                }
            }, ItemRegistrate.CRACK.get(), BlockRegistrate.BLOCK_OF_CRACK.get().asItem());
        }
    }
}