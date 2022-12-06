package com.gnottero.medieval_beasts.tile_entity.custom.beast_egg;

import com.gnottero.medieval_beasts.entity.ModEntityTypes;
import com.gnottero.medieval_beasts.tile_entity.ModTileEntities;
import com.gnottero.medieval_beasts.tile_entity.custom.MedievalEggTile;

public class PixieEggTile extends MedievalEggTile {
    public PixieEggTile() {
        super(ModTileEntities.PIXIE_EGG_TILE.get(), ModEntityTypes.PIXIE.get());
    }
}
