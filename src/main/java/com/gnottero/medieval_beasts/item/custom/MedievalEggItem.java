package com.gnottero.medieval_beasts.item.custom;

import com.gnottero.medieval_beasts.config.MedievalBeastsConfig;
import com.gnottero.medieval_beasts.item.custom.beast_egg.IMedievalEggItem;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class MedievalEggItem extends BlockItem implements IMedievalEggItem {

    public MedievalEggItem(Block blockIn, Properties properties) {
        super(blockIn, properties);
    }

    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateTag().putInt("WalkedSteps", 0);
            stack.getOrCreateTag().putInt("PrevDistanceWalked", 0);
            stack.getOrCreateTag().putInt("GrowthStage", 0);
            stack.getOrCreateTag().putString("Rank", "F");
            stack.getOrCreateTag().putString("Catalyst", this.getDefaultCatalyst());
//            stack.getOrCreateTag().putString("EntityType", this.getDefaultEntity());
            items.add(stack);
        }
    }

    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isRemote()) {
            if (entityIn.getType().equals(EntityType.PLAYER)) {
                PlayerEntity player = (PlayerEntity) entityIn;
                if (!player.isPassenger() && !player.isSwimming() && !player.areEyesInFluid(FluidTags.WATER) && !player.isInWater() && !player.isElytraFlying() && player.isOnGround()) {
                    int eggStoredDistance = stack.getOrCreateTag().getInt("PrevDistanceWalked");
                    int WalkedSteps = stack.getOrCreateTag().getInt("WalkedSteps");

                    if (stack.getOrCreateTag().getString("Rank").isEmpty()) {
                        stack.getOrCreateTag().putString("Rank", "F");
                    }

                    if (stack.getOrCreateTag().getString("Catalyst").isEmpty()) {
                        stack.getOrCreateTag().putString("Catalyst", this.getDefaultCatalyst());
                    }

//                    if (stack.getOrCreateTag().getString("EntityType").isEmpty()) {
//                        stack.getOrCreateTag().putString("EntityType", this.getDefaultCatalyst());
//                    }

                    if (WalkedSteps < MedievalBeastsConfig.REQUIRED_STEPS.get()) {
                        if (eggStoredDistance == 0 || eggStoredDistance + 15 < player.prevDistanceWalkedModified) {
                            stack.getOrCreateTag().putInt("PrevDistanceWalked", (int) player.distanceWalkedOnStepModified);
                        } else {
                            int prevDistance = stack.getOrCreateTag().getInt("PrevDistanceWalked");
                            int currentDistanceWalked = (int) player.distanceWalkedOnStepModified;
                            int distanceWalked = Math.abs(currentDistanceWalked - prevDistance);
                            stack.getOrCreateTag().putInt("PrevDistanceWalked", (int) player.distanceWalkedOnStepModified);
                            stack.getOrCreateTag().putInt("WalkedSteps", WalkedSteps + distanceWalked);

                            if (WalkedSteps + distanceWalked >= MedievalBeastsConfig.REQUIRED_STEPS.get()) {
                                player.sendMessage(new TranslationTextComponent("item.medieval_beasts.info.last_step_message", stack.getDisplayName()).mergeStyle(TextFormatting.GOLD), player.getUniqueID());
                            }
                        }
                    } else {
                        stack.getOrCreateTag().putInt("WalkedSteps", MedievalBeastsConfig.REQUIRED_STEPS.get());
                    }

                    replaceItemInSlot(player, itemSlot, stack);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.medieval_beasts.info.rank_info", stack.getOrCreateTag().getString("Rank")).mergeStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.medieval_beasts.info.step_info", stack.getOrCreateTag().getInt("WalkedSteps"), MedievalBeastsConfig.REQUIRED_STEPS.get()).mergeStyle(TextFormatting.GRAY));

        ItemStack catalyst = Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(ResourceLocation.tryCreate(stack.getOrCreateTag().getString("Catalyst")))).getDefaultInstance();


//        String catalystName = stack.getOrCreateTag().getString("Catalyst").replaceAll(".+:", "");
        tooltip.add(new TranslationTextComponent("item.medieval_beasts.info.catalyst_info", catalyst.getTextComponent()).mergeStyle(TextFormatting.GRAY));
//        tooltip.add(new TranslationTextComponent("item.medieval_beasts.info.catalyst_info", StringUtils.toTitleCase(catalystName)).mergeStyle(TextFormatting.GRAY));
    }

    public void replaceItemInSlot(PlayerEntity player, int itemSlot, ItemStack stack) {
        if (itemSlot == 0) {
            if (player.getItemStackFromSlot(EquipmentSlotType.OFFHAND).getItem() == this.getItem()) {
                player.setItemStackToSlot(EquipmentSlotType.OFFHAND, stack);
            } else {
                player.inventory.setInventorySlotContents(itemSlot, stack);
            }
        } else {
            player.inventory.setInventorySlotContents(itemSlot, stack);
        }
    }

    @Override
    public String getDefaultCatalyst() {
        return "minecraft:air";
    }
}
