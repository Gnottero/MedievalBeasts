package com.gnottero.medieval_beasts.item.custom.food;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class GenericCelestialFood extends Item {
    private final int healthAmount;

    public GenericCelestialFood(Properties properties, int healthAmount) {
        super(properties);
        this.healthAmount = healthAmount;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote() && worldIn instanceof ServerWorld) {
            if (entityLiving instanceof PlayerEntity) {
                // Regen 7 health points
                entityLiving.heal(this.healthAmount);

                // Generate some particles
                ((ServerWorld) worldIn).spawnParticle(ParticleTypes.HEART, entityLiving.getPosX(), entityLiving.getPosY() + entityLiving.getEyeHeight(), entityLiving.getPosZ(), 10, Math.random(), Math.random(), Math.random(), 0.1f);

            }
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
