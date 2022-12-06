package com.gnottero.medieval_beasts.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup {

    public static final ItemGroup MEDIEVAL_BEASTS_GROUP = new ItemGroup("medievalBeastsTab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.UPGRADE_CRYSTAL.get());
        }
    };

}
