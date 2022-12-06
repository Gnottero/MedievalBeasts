package com.gnottero.medieval_beasts.block.custom.crop;

import com.gnottero.medieval_beasts.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class CelestialBerryBush extends SweetBerryBushBlock {

    public CelestialBerryBush(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
        return new ItemStack(ModItems.CELESTIAL_BERRY.get());
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        int i = state.get(AGE);
        boolean flag = i == 3;
        if (!flag && player.getHeldItem(handIn).getItem() == Items.BONE_MEAL) {
            return ActionResultType.PASS;
        } else if (i > 1) {
            int j = 1 + worldIn.rand.nextInt(1);
            spawnAsEntity(worldIn, pos, new ItemStack(ModItems.CELESTIAL_BERRY.get(), j + (flag ? 1 : 0)));
            worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_SWEET_BERRIES_PICK_FROM_BUSH, SoundCategory.BLOCKS, 1.0F, 0.8F + worldIn.rand.nextFloat() * 0.4F);
            worldIn.setBlockState(pos, state.with(AGE, 1), 2);
            return ActionResultType.func_233537_a_(worldIn.isRemote);
        } else {
            return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
        }
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof LivingEntity && entityIn.getType() != EntityType.FOX && entityIn.getType() != EntityType.BEE) {
            entityIn.setMotionMultiplier(state, new Vector3d((double)0.8F, 0.75D, (double)0.8F));
            if (!worldIn.isRemote && state.get(AGE) > 0 && (entityIn.lastTickPosX != entityIn.getPosX() || entityIn.lastTickPosZ != entityIn.getPosZ())) {
                double d0 = Math.abs(entityIn.getPosX() - entityIn.lastTickPosX);
                double d1 = Math.abs(entityIn.getPosZ() - entityIn.lastTickPosZ);
                if (d0 >= (double)0.003F || d1 >= (double)0.003F) {
                    ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(Effects.GLOWING, 20, 0));
                }
            }

        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (rand.nextInt(5) == 0) {
            Direction direction = Direction.getRandomDirection(rand);
            if (direction != Direction.UP) {
                BlockPos blockpos = pos.offset(direction);
                BlockState blockstate = worldIn.getBlockState(blockpos);
                if (!stateIn.isSolid() || !blockstate.isSolidSide(worldIn, blockpos, direction.getOpposite())) {
                    double d0 = direction.getXOffset() == 0 ? rand.nextDouble() : 0.5D + (double)direction.getXOffset() * 0.6D;
                    double d1 = direction.getYOffset() == 0 ? rand.nextDouble() : 0.5D + (double)direction.getYOffset() * 0.6D;
                    double d2 = direction.getZOffset() == 0 ? rand.nextDouble() : 0.5D + (double)direction.getZOffset() * 0.6D;
                    worldIn.addParticle(new BlockParticleData(ParticleTypes.BLOCK, Blocks.GOLD_BLOCK.getDefaultState()), (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }
}
