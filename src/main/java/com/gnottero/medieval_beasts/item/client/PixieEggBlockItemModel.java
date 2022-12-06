package com.gnottero.medieval_beasts.item.client;

import com.gnottero.medieval_beasts.MedievalBeasts;
import com.gnottero.medieval_beasts.item.custom.beast_egg.PixieEggBlockItem;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PixieEggBlockItemModel extends AnimatedGeoModel<PixieEggBlockItem> {

    @Override
    public ResourceLocation getModelLocation(PixieEggBlockItem object) {
        return new ResourceLocation(MedievalBeasts.MOD_ID, "geo/medieval_egg.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(PixieEggBlockItem object) {
        return new ResourceLocation( MedievalBeasts.MOD_ID, "textures/block/pixie_egg.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(PixieEggBlockItem animatable) {
        return new ResourceLocation(MedievalBeasts.MOD_ID, "animations/medieval_egg.animation.json");
    }
}


