package com.gnottero.medieval_beasts.event;

import com.gnottero.medieval_beasts.MedievalBeasts;
import com.gnottero.medieval_beasts.entity.ModEntityTypes;
import com.gnottero.medieval_beasts.entity.custom.PixieEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MedievalBeasts.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.PIXIE.get(), PixieEntity.setAttributes().create());
    }

}
