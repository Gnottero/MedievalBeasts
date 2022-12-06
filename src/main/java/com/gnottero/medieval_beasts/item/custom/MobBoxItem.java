package com.gnottero.medieval_beasts.item.custom;

import com.gnottero.medieval_beasts.entity.custom.MedievalBeastEntity;
import com.gnottero.medieval_beasts.util.MobBoxUtils;
import com.gnottero.medieval_beasts.util.StringUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

public class MobBoxItem extends Item {

    public MobBoxItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if (target instanceof MedievalBeastEntity) {
            if (!playerIn.getEntityWorld().isRemote()) {
                if (stack.getOrCreateTag().isEmpty()) {
                    if (MobBoxUtils.saveEntityToStack(target, stack)) {
                        playerIn.setHeldItem(hand, stack);
                    }
                    return ActionResultType.SUCCESS;
                }
            }
        } else {
            return ActionResultType.CONSUME;
        }
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        ItemStack stack = context.getItem();
        if(!(context.getWorld() instanceof ServerWorld)) return ActionResultType.SUCCESS;
        if(!context.getWorld().isRemote() && stack.hasTag() && stack.getTag().contains("StoredEntity")) {
            MobBoxUtils.respawnEntity(context, stack);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return stack.hasTag() && !stack.getOrCreateChildTag("StoredEntity").isEmpty();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.hasTag() && !stack.getOrCreateChildTag("StoredEntity").isEmpty()) {
            tooltip.add(new TranslationTextComponent("item.medieval_beasts.info.rank_info", stack.getOrCreateChildTag("StoredEntity").getString("Rank")).mergeStyle(TextFormatting.GRAY));
            tooltip.add(new TranslationTextComponent("item.medieval_beasts.info.name_info", StringUtils.toTitleCase(stack.getOrCreateTag().getString("Name"))).mergeStyle(TextFormatting.GRAY));
        }
    }

    public static String getRank(ItemStack stack) {
        if (stack.hasTag() && !stack.getOrCreateChildTag("StoredEntity").isEmpty()) {
            return stack.getOrCreateChildTag("StoredEntity").getString("Rank");
        } else {
            return "";
        }
    }

    public static void setRank(ItemStack stack, String rankIn) {
        if (stack.hasTag() && !stack.getOrCreateChildTag("StoredEntity").isEmpty()) {
            stack.getOrCreateChildTag("StoredEntity").putString("Rank", rankIn);
        }
    }
}
