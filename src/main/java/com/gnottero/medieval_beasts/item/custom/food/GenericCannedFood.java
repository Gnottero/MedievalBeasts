package com.gnottero.medieval_beasts.item.custom.food;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class GenericCannedFood extends Item {

    private final Item itemToReturn;
    private final int healthAmount;

    public GenericCannedFood(Properties properties, Item itemToReturn, int healthAmount) {
        super(properties);
        this.itemToReturn = itemToReturn;
        this.healthAmount = healthAmount;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {

        if (!worldIn.isRemote() && entityLiving instanceof ServerPlayerEntity) {
            entityLiving.heal(this.healthAmount);
            ((ServerWorld) worldIn).spawnParticle(ParticleTypes.HEART, entityLiving.getPosX(), entityLiving.getPosY() + entityLiving.getEyeHeight(), entityLiving.getPosZ(), 10, Math.random(), Math.random(), Math.random(), 0.1f);
        }

        if (stack.isEmpty()) {
            return new ItemStack(this.itemToReturn);
        } else {
            if (entityLiving instanceof PlayerEntity && !((PlayerEntity)entityLiving).abilities.isCreativeMode) {
                ItemStack itemstack = new ItemStack(this.itemToReturn);
                PlayerEntity playerentity = (PlayerEntity)entityLiving;
                if (!playerentity.inventory.addItemStackToInventory(itemstack)) {
                    playerentity.dropItem(itemstack, false);
                }
            }
        }

        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
