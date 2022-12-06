package com.gnottero.medieval_beasts.tile_entity;

import com.gnottero.medieval_beasts.MedievalBeasts;
import com.gnottero.medieval_beasts.block.ModBlocks;
import com.gnottero.medieval_beasts.tile_entity.custom.MedievalEggTile;
import com.gnottero.medieval_beasts.tile_entity.custom.UpgradeAltarTile;
import com.gnottero.medieval_beasts.tile_entity.custom.beast_egg.PixieEggTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MedievalBeasts.MOD_ID);

//    public static RegistryObject<TileEntityType<MedievalEggTile>> MEDIEVAL_EGG_TILE = TILE_ENTITIES.register(
//            "medieval_egg_tile",
//            () -> TileEntityType.Builder.create(
//                    MedievalEggTile::new,
//                    ModBlocks.NEW_DRAGON_EGG.get()
//            ).build(null)
//    );

    public static RegistryObject<TileEntityType<PixieEggTile>> PIXIE_EGG_TILE = TILE_ENTITIES.register(
            "pixie_egg_tile",
            () -> TileEntityType.Builder.create(
                    PixieEggTile::new,
                    ModBlocks.PIXIE_EGG.get()
            ).build(null)
    );


    public static RegistryObject<TileEntityType<UpgradeAltarTile>> UPGRADE_ALTAR = TILE_ENTITIES.register(
        "upgrade_altar",
            () -> TileEntityType.Builder.create(
                    UpgradeAltarTile::new,
                    ModBlocks.UPGRADE_ALTAR.get()
            ).build(null)
    );

    public static void register(IEventBus eventBus) {
        TILE_ENTITIES.register(eventBus);
    }

}
