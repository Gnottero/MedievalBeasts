package com.gnottero.medieval_beasts.entity.client;

import com.gnottero.medieval_beasts.MedievalBeasts;
import com.gnottero.medieval_beasts.entity.custom.PixieEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class PixieEntityRenderer extends GeoEntityRenderer<PixieEntity> {

    public PixieEntityRenderer(EntityRendererManager renderManager) {
        super(renderManager, new PixieEntityModel());
        this.shadowSize = 0.5f;
    }

    @Override
    public ResourceLocation getEntityTexture(PixieEntity object) {
        return new ResourceLocation(MedievalBeasts.MOD_ID, "textures/entity/pixie.png");
    }

    @Override
    public RenderType getRenderType(PixieEntity animatable, float partialTicks, MatrixStack stack,
                                    IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(0.8F, 0.8F, 0.8F);
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
