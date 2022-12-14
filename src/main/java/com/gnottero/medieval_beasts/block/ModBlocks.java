package com.gnottero.medieval_beasts.block;

import com.gnottero.medieval_beasts.MedievalBeasts;
import com.gnottero.medieval_beasts.block.custom.UpgradeAltarBlock;
import com.gnottero.medieval_beasts.block.custom.beast_egg.PixieEggBlock;
import com.gnottero.medieval_beasts.block.custom.crop.CelestialBerryBush;
import com.gnottero.medieval_beasts.item.ModItemGroup;
import com.gnottero.medieval_beasts.item.ModItems;
import com.gnottero.medieval_beasts.item.custom.beast_egg.PixieEggBlockItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, MedievalBeasts.MOD_ID);

    public static final RegistryObject<Block> PIXIE_EGG = registerEggBlock(
            "pixie_egg",
            () -> new PixieEggBlock(AbstractBlock.Properties.from(Blocks.OBSIDIAN).setRequiresTool().harvestTool(ToolType.PICKAXE).harvestLevel(ItemTier.NETHERITE.getHarvestLevel()))
    );

    public static final RegistryObject<Block> UPGRADE_ALTAR = registerBlock(
            "upgrade_altar",
            () -> new UpgradeAltarBlock(AbstractBlock.Properties.from(Blocks.IRON_BLOCK).notSolid())
    );

    public static final RegistryObject<Block> CELESTIAL_BERRY_BUSH = registerBlockWithoutBlockItem(
            "celestial_berry_bush",
            () -> new CelestialBerryBush(AbstractBlock.Properties.from(Blocks.SWEET_BERRY_BUSH).notSolid())
    );

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(
                name, () -> new BlockItem(block.get(),
                new Item.Properties().group(ModItemGroup.MEDIEVAL_BEASTS_GROUP)));
    }

    private static <T extends Block> RegistryObject<T> registerEggBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerEggItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerBlockWithoutBlockItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> void registerEggItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(
                name, () -> new PixieEggBlockItem(block.get())
        );
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
