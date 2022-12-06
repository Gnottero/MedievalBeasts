package com.gnottero.medieval_beasts.item.custom;

import com.gnottero.medieval_beasts.block.ModBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class UpgradeCrystalItem extends Item {

    public UpgradeCrystalItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos, PlayerEntity player) {
        return world.getBlockState(pos).getBlock().equals(ModBlocks.UPGRADE_ALTAR.get());
    }
}
