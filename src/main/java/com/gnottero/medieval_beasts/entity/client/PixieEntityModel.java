package com.gnottero.medieval_beasts.entity.client;

import com.gnottero.medieval_beasts.MedievalBeasts;
import com.gnottero.medieval_beasts.entity.custom.PixieEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PixieEntityModel extends AnimatedGeoModel<PixieEntity> {

    @Override
    public ResourceLocation getModelLocation(PixieEntity object) {
        return new ResourceLocation(MedievalBeasts.MOD_ID, "geo/pixie.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(PixieEntity object) {
        return new ResourceLocation( MedievalBeasts.MOD_ID, "textures/entity/pixie.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(PixieEntity animatable) {
        return new ResourceLocation(MedievalBeasts.MOD_ID, "animations/pixie.animation.json");
    }
}
