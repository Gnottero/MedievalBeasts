package com.gnottero.medieval_beasts.screen;

import com.gnottero.medieval_beasts.MedievalBeasts;
import com.gnottero.medieval_beasts.container.custom.UpgradeAltarContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

public class UpgradeAltarScreen extends ContainerScreen<UpgradeAltarContainer> {

    private final ResourceLocation GUI = new ResourceLocation(
            MedievalBeasts.MOD_ID,
            "textures/gui/upgrade_altar_gui.png"
    );

    public UpgradeAltarScreen(UpgradeAltarContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (this.xSize - this.font.getStringPropertyWidth(this.title)) / 2;
        this.titleY = (this.ySize / 28) - 1;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
    }
}
