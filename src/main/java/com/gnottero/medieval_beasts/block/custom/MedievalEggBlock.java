package com.gnottero.medieval_beasts.block.custom;

import com.gnottero.medieval_beasts.config.MedievalBeastsConfig;
import com.gnottero.medieval_beasts.tile_entity.custom.MedievalEggTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.PickaxeItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

import static com.gnottero.medieval_beasts.util.EntityRanking.Ranking;

public class MedievalEggBlock extends Block {

    protected static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    public MedievalEggBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (!world.isRemote()) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof MedievalEggTile) {
                CompoundNBT stackNbt = stack.getOrCreateTag();
                ((MedievalEggTile) tileEntity).setGrowthStage(stackNbt.getInt("GrowthStage"));
                ((MedievalEggTile) tileEntity).setWalkedSteps(stackNbt.getInt("WalkedSteps"));
                ((MedievalEggTile) tileEntity).setRank(Ranking.valueOf(stackNbt.getString("Rank")));
                ((MedievalEggTile) tileEntity).setCatalyst(stackNbt.getString("Catalyst"));
                ((MedievalEggTile) tileEntity).setIsActive(false);
//                ((MedievalEggTile) tileEntity).setEntityType(stackNbt.getString("EntityType"));
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {

        if (!world.isRemote()) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof MedievalEggTile) {
                ItemStack catalystStack = ForgeRegistries.ITEMS.getValue(new ResourceLocation(((MedievalEggTile) tileEntity).getCatalyst())).getDefaultInstance();
                ItemStack stack = player.getHeldItem(hand);
                if (((MedievalEggTile) tileEntity).getWalkedSteps() == MedievalBeastsConfig.REQUIRED_STEPS.get() && !catalystStack.isEmpty() && stack.getItem() == catalystStack.getItem() && !((MedievalEggTile) tileEntity).getIsActive()) {
                    ((MedievalEggTile) tileEntity).setIsActive(true);
                    ((MedievalEggTile) tileEntity).setOwner(player.getUniqueID());
                    world.notifyBlockUpdate(pos, state, state, 3);

                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                }
            }
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        if (!worldIn.isRemote()) {
            ItemStack stack = player.getHeldItemMainhand();
            if (stack.getItem() instanceof PickaxeItem) {
                PickaxeItem pickaxeItem = (PickaxeItem) stack.getItem();
                int harvestLevel = pickaxeItem.getTier().getHarvestLevel();

                if (harvestLevel < ItemTier.NETHERITE.getHarvestLevel()) {
                    player.sendMessage(new TranslationTextComponent("block.medieval_beasts.egg_breaking_error", stack.getDisplayName()).mergeStyle(TextFormatting.GRAY), player.getUniqueID());
                }
            } else {
                player.sendMessage(new TranslationTextComponent("block.medieval_beasts.egg_breaking_error", stack.getDisplayName()).mergeStyle(TextFormatting.GRAY), player.getUniqueID());
            }
        }
        super.onBlockClicked(state, worldIn, pos, player);
    }

    @Override
    public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader worldIn, BlockPos pos) {

        ItemStack stack = player.getHeldItemMainhand();
        if (stack.getItem() instanceof PickaxeItem) {
            PickaxeItem pickaxeItem = (PickaxeItem) stack.getItem();
            int harvestLevel = pickaxeItem.getTier().getHarvestLevel();

            if (harvestLevel < ItemTier.NETHERITE.getHarvestLevel()) {
                return 0f;
            }
        } else {
            return 0f;
        }

        return super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
    }
}
