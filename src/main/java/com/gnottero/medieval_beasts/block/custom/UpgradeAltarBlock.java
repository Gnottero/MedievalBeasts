package com.gnottero.medieval_beasts.block.custom;

import com.gnottero.medieval_beasts.container.custom.UpgradeAltarContainer;
import com.gnottero.medieval_beasts.item.ModItems;
import com.gnottero.medieval_beasts.tile_entity.ModTileEntities;
import com.gnottero.medieval_beasts.tile_entity.custom.UpgradeAltarTile;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class UpgradeAltarBlock extends Block {

    public UpgradeAltarBlock(AbstractBlock.Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.fullCube();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {

        if (!worldIn.isRemote()) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);

            if (tileEntity instanceof UpgradeAltarTile) {
                if (!((UpgradeAltarTile) tileEntity).isActive()) {
                    if (!player.isCrouching()) {
                        INamedContainerProvider containerProvider = createContainerProvider(worldIn, pos);
                        NetworkHooks.openGui(((ServerPlayerEntity) player), containerProvider, tileEntity.getPos());
                    } else {
                        if (!player.getHeldItem(handIn).isEmpty() && player.getHeldItem(handIn).getItem().equals(ModItems.UPGRADE_CRYSTAL.get()) && ((UpgradeAltarTile) tileEntity).isUpgradeable()) {
                            player.getHeldItem(handIn).shrink(1);
                            ((UpgradeAltarTile) tileEntity).setActive(true);
                        }
                    }
                }
            } else {
                throw new IllegalStateException("UpgradeAltar container provider is missing!");
            }
        }

        return ActionResultType.SUCCESS;
    }

    private INamedContainerProvider createContainerProvider(World worldIn, BlockPos pos) {
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("screen.medieval_beasts.upgrade_altar");
            }

            @Nullable
            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return new UpgradeAltarContainer(i, worldIn, pos, playerInventory, playerEntity);
            }
        };
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.UPGRADE_ALTAR.get().create();
    }
}
