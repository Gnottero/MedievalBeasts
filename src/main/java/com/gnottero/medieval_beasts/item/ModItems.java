package com.gnottero.medieval_beasts.item;

import com.gnottero.medieval_beasts.MedievalBeasts;
import com.gnottero.medieval_beasts.block.ModBlocks;
import com.gnottero.medieval_beasts.item.custom.MobBoxItem;
import com.gnottero.medieval_beasts.item.custom.UpgradeCrystalItem;
import net.minecraft.block.Blocks;
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
