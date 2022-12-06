package com.gnottero.medieval_beasts.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;

public class MobBoxUtils {

    public static void respawnEntity(ItemUseContext context, ItemStack stack) {
        ServerWorld serverWorld = ((ServerWorld) context.getWorld());
        BlockPos pos = context.getPos().offset(context.getFace());
        ServerPlayerEntity player = ((ServerPlayerEntity) context.getPlayer());

        CompoundNBT entityTag = context.getItem().getChildTag("StoredEntity");

        Optional<Entity> entity = EntityType.loadEntityUnchecked(entityTag, serverWorld);

        if (entity.isPresent()) {
            Entity medievalBeast = entity.get();
            medievalBeast.setPositionAndRotation(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, player.rotationYaw, player.rotationPitch);
            serverWorld.addEntity(medievalBeast);
        }

        stack.removeChildTag("Name");
        stack.removeChildTag("StoredEntity");

        context.getPlayer().getHeldItem(context.getHand());
    }

    public static boolean saveEntityToStack(Entity entity, ItemStack stack) {
        CompoundNBT entityTag = new CompoundNBT();

        if(!entity.writeUnlessRemoved(entityTag)) {
            return false;
        }

        stack.getOrCreateTag().put("StoredEntity", entityTag);
        stack.getOrCreateTag().putString("Name", entity.getDisplayName().getString());
        entity.remove();

        return true;
    }

}
