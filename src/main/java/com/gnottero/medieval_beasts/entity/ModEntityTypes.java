package com.gnottero.medieval_beasts.entity;

import com.gnottero.medieval_beasts.MedievalBeasts;
import com.gnottero.medieval_beasts.entity.custom.PixieEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITIES, MedievalBeasts.MOD_ID);

    public static final RegistryObject<EntityType<PixieEntity>> PIXIE =
            ENTITY_TYPES.register("pixie",
                    () -> EntityType.Builder.create(PixieEntity::new, EntityClassification.CREATURE)
                            .size(0.8f, 1.3f)
                            .build(new ResourceLocation(MedievalBeasts.MOD_ID, "pixie").toString()));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
