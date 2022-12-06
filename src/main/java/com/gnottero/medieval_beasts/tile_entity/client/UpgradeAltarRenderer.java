package com.gnottero.medieval_beasts.tile_entity.client;

import com.gnottero.medieval_beasts.tile_entity.ModTileEntities;
import com.gnottero.medieval_beasts.tile_entity.custom.UpgradeAltarTile;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.registries.ForgeRegistries;

public class UpgradeAltarRenderer extends TileEntityRenderer<UpgradeAltarTile> {

    public UpgradeAltarRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(UpgradeAltarTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (tileEntityIn.hasPetInFirstSlot()) {
            String storedEntity = tileEntityIn.getItemStackHandler().getStackInSlot(0).getOrCreateChildTag("StoredEntity").getString("id");
            EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(storedEntity));
            if (entityType != null) {
                matrixStackIn.push();
                matrixStackIn.translate(0.5d, 0.85d + (entityType.getHeight()) / 4 + Math.cos(tileEntityIn.getWorld().getGameTime() * 0.05) / 20, 0.5d);
                matrixStackIn.rotate(Vector3f.YP.rotationDegrees(tileEntityIn.getWorld().getGameTime() * 0.8F));
                EntityRendererManager entityRenderer = Minecraft.getInstance().getRenderManager();
                Entity medievalBeast = entityType.create(tileEntityIn.getWorld());
                entityRenderer.renderEntityStatic(medievalBeast, 0.0d, 0.0d, 0.0d, 0.0f, 0.0f, matrixStackIn, bufferIn, combinedLightIn);
                matrixStackIn.pop();
            }
        }
    }

    public static void register() {
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.UPGRADE_ALTAR.get(), UpgradeAltarRenderer::new);
    }
}
