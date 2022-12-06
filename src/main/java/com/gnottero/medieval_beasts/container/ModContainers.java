package com.gnottero.medieval_beasts.container;

import com.gnottero.medieval_beasts.MedievalBeasts;
import com.gnottero.medieval_beasts.container.custom.UpgradeAltarContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {

    public static DeferredRegister<ContainerType<?>> CONTAINERS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, MedievalBeasts.MOD_ID);

    public static final RegistryObject<ContainerType<UpgradeAltarContainer>> UPGRADE_ALTAR_CONTAINER =
            CONTAINERS.register("upgrade_altar_container",
                    () -> IForgeContainerType.create((windowId, inv, data) -> {
                        BlockPos pos = data.readBlockPos();
                        World world = inv.player.getEntityWorld();
                        return new UpgradeAltarContainer(windowId, world, pos, inv, inv.player);
                    })
            );


    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }

}
