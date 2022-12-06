package com.gnottero.medieval_beasts.block.custom.beast_egg;

import com.gnottero.medieval_beasts.block.custom.MedievalEggBlock;
import com.gnottero.medieval_beasts.tile_entity.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class PixieEggBlock extends MedievalEggBlock {
    public PixieEggBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.PIXIE_EGG_TILE.get().create();
    }
}
