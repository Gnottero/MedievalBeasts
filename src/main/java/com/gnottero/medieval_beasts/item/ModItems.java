package com.gnottero.medieval_beasts.item;

import com.gnottero.medieval_beasts.MedievalBeasts;
import com.gnottero.medieval_beasts.block.ModBlocks;
import com.gnottero.medieval_beasts.item.custom.food.GenericCannedFood;
import com.gnottero.medieval_beasts.item.custom.MobBoxItem;
import com.gnottero.medieval_beasts.item.custom.UpgradeCrystalItem;
import com.gnottero.medieval_beasts.item.custom.food.GenericCelestialFood;
import net.minecraft.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MedievalBeasts.MOD_ID);

    public static final RegistryObject<Item> MOB_BOX = ITEMS.register("mob_box",
            () -> new MobBoxItem(new Item.Properties().group(ModItemGroup.MEDIEVAL_BEASTS_GROUP).maxStackSize(1))
    );

    public static final RegistryObject<Item> UPGRADE_CRYSTAL = ITEMS.register("upgrade_crystal",
            () -> new UpgradeCrystalItem(new Item.Properties().group(ModItemGroup.MEDIEVAL_BEASTS_GROUP))
    );

    public static final RegistryObject<Item> JAM_JAR = ITEMS.register("jam_jar",
            () -> new Item(new Item.Properties().group(ModItemGroup.MEDIEVAL_BEASTS_GROUP))
    );

    public static final RegistryObject<Item> CELESTIAL_BERRY_JAM = ITEMS.register("celestial_berry_jam",
            () -> new GenericCannedFood(
                    new Item.Properties().group(ModItemGroup.MEDIEVAL_BEASTS_GROUP).food(new Food.Builder().hunger(4).saturation(0.5f).setAlwaysEdible().build()),
                    ModItems.JAM_JAR.get(),
                    8
                )
    );

    public static final RegistryObject<Item> CELESTIAL_BERRY_SOUP = ITEMS.register("celestial_berry_soup",
            () -> new GenericCannedFood(
                    new Item.Properties().group(ModItemGroup.MEDIEVAL_BEASTS_GROUP).food(new Food.Builder().hunger(4).saturation(0.5f).setAlwaysEdible().build()),
                    Items.BOWL,
                    10
                )
    );

    public static final RegistryObject<Item> CELESTIAL_BERRY_TART = ITEMS.register("celestial_berry_tart",
            () -> new GenericCelestialFood(
                    new Item.Properties().group(ModItemGroup.MEDIEVAL_BEASTS_GROUP).food(new Food.Builder().hunger(8).saturation(0.3F).setAlwaysEdible().build()),
                    9
            )
    );

    public static final RegistryObject<Item> CELESTIAL_BERRY_COOKIE = ITEMS.register("celestial_berry_cookie",
            () -> new GenericCelestialFood(
                    new Item.Properties().group(ModItemGroup.MEDIEVAL_BEASTS_GROUP).food(new Food.Builder().hunger(2).saturation(0.1F).setAlwaysEdible().build()),
                    7
            )
    );

    public static final RegistryObject<Item> CELESTIAL_BERRY = ITEMS.register("celestial_berry",
            () -> new BlockNamedItem(ModBlocks.CELESTIAL_BERRY_BUSH.get(), (new Item.Properties()).group(ModItemGroup.MEDIEVAL_BEASTS_GROUP))
    );

//
//    public static final RegistryObject<Item> GOLDEN_DRAGON_EGG = ITEMS.register("golden_dragon_egg_item",
//            () -> new GenericEggItem(new Item.Properties().group(ItemGroup.MISC).maxStackSize(1))
//    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
