package com.gnottero.medieval_beasts.tile_entity.client;

import com.gnottero.medieval_beasts.MedievalBeasts;
import com.gnottero.medieval_beasts.tile_entity.custom.MedievalEggTile;
import com.gnottero.medieval_beasts.tile_entity.custom.beast_egg.PixieEggTile;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PixieEggTileModel extends AnimatedGeoModel<PixieEggTile> {

    @Override
    public ResourceLocation getModelLocation(PixieEggTile object) {
        return new ResourceLocation(MedievalBeasts.MOD_ID, "geo/medieval_egg.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(PixieEggTile object) {
        return new ResourceLocation( MedievalBeasts.MOD_ID, "textures/block/pixie_egg.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(PixieEggTile animatable) {
        return new ResourceLocation(MedievalBeasts.MOD_ID, "animations/medieval_egg.animation.json");
    }
}


