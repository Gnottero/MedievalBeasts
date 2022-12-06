package com.gnottero.medieval_beasts.item.custom.beast_egg;

import com.gnottero.medieval_beasts.item.ModItemGroup;
import com.gnottero.medieval_beasts.item.client.PixieEggBlockItemRenderer;
import com.gnottero.medieval_beasts.item.custom.MedievalEggItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class PixieEggBlockItem extends MedievalEggItem implements IAnimatable {
    public AnimationFactory factory = new AnimationFactory(this);

    public PixieEggBlockItem(Block blockIn) {
        super(blockIn, new Item.Properties().group(ModItemGroup.MEDIEVAL_BEASTS_GROUP).maxStackSize(1).setISTER( () -> PixieEggBlockItemRenderer::new));
    }


    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller",
                0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public String getDefaultCatalyst() {
        return Items.LAPIS_BLOCK.toString();
    }
}
