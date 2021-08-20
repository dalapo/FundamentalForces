package com.space_mod_group.space_mod;

import com.space_mod_group.space_mod.core.registry.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.space_mod_group.space_mod.SpaceMod.MOD_ID;

@Mod(MOD_ID)
public class SpaceMod
{
    public static final String MOD_ID = "space_mod";
    public static final Logger LOGGER = LogManager.getLogger();

    public SpaceMod()
    {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        EnchantmentRegistry.ENCHANTMENTS.register(modBus);
        BlockRegistry.BLOCKS.register(modBus);
        ItemRegistry.ITEMS.register(modBus);
        TileRegistry.TILE_TYPES.register(modBus);
        EntityRegistry.ENTITY_TYPES.register(modBus);
        PotionEffectRegistry.EFFECTS.register(modBus);
        SoundRegistry.SOUNDS.register(modBus);
    }
}
