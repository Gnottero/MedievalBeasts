package com.gnottero.medieval_beasts.item.client;

import com.gnottero.medieval_beasts.item.custom.beast_egg.PixieEggBlockItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class PixieEggBlockItemRenderer extends GeoItemRenderer<PixieEggBlockItem> {
    public PixieEggBlockItemRenderer() {
        super(new PixieEggBlockItemModel());
    }
}
