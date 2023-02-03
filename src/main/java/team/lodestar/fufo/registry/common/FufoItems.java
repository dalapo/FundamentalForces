package team.lodestar.fufo.registry.common;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.ItemBuilder;
import team.lodestar.fufo.FufoMod;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;

import net.minecraft.world.item.Item;

public class FufoItems {
    public static final ItemEntry<Item> CRACK = setupItem("crack").register();
    public static final ItemEntry<Item> DEPLETED_ORTUSITE_CHUNK = setupItem("depleted_ortusite_chunk").register();
    public static final ItemEntry<Item> ORTUSITE_CHUNK = setupItem("ortusite_chunk").tag(FufoTags.METEOR_FLAME_CATALYST).register();
    public static final ItemEntry<Item> BOTTLED_WISP = setupItem("bottled_wisp").register();

    public static ItemBuilder<Item, Registrate> setupItem(String name) {
        return setupItem(name, Item::new);
    }

    public static <T extends Item> ItemBuilder<T, Registrate> setupItem(String name, NonNullFunction<Item.Properties, T> factory) {
        return FufoMod.registrate().item(name, factory).tab(FufoCreativeTabs.FufoContentTab::get);
    }

    public static void register() {
    }
}