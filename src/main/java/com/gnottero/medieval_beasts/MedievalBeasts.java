package com.gnottero.medieval_beasts;

import com.gnottero.medieval_beasts.block.ModBlocks;
import com.gnottero.medieval_beasts.config.MedievalBeastsConfig;
import com.gnottero.medieval_beasts.container.ModContainers;
import com.gnottero.medieval_beasts.entity.ModEntityTypes;
import com.gnottero.medieval_beasts.tile_entity.client.PixieEggTileRenderer;
import com.gnottero.medieval_beasts.entity.client.PixieEntityRenderer;
import com.gnottero.medieval_beasts.item.ModItems;
import com.gnottero.medieval_beasts.tile_entity.client.UpgradeAltarRenderer;
import com.gnottero.medieval_beasts.screen.UpgradeAltarScreen;
import com.gnottero.medieval_beasts.tile_entity.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.util.datafix.fixes.ObjectiveRenderType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;

@Mod(MedievalBeasts.MOD_ID)
public class MedievalBeasts
{
    public static final String MOD_ID = "medieval_beasts";
    private static final Logger LOGGER = LogManager.getLogger();

    public MedievalBeasts() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.register(eventBus);
        ModItems.register(eventBus);
        ModEntityTypes.register(eventBus);
        ModTileEntities.register(eventBus);
        ModContainers.register(eventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MedievalBeastsConfig.SPEC, "medieval_beasts-common.toml");

        eventBus.addListener(this::clientSetup);
        eventBus.addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);

        GeckoLibMod.DISABLE_IN_DEV = true;
        GeckoLib.initialize();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.PIXIE.get(), PixieEntityRenderer::new);
        UpgradeAltarRenderer.register();

        // Animated Egg
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.PIXIE_EGG_TILE.get(), PixieEggTileRenderer::new);

        // Renderer for crops
        RenderTypeLookup.setRenderLayer(ModBlocks.CELESTIAL_BERRY_BUSH.get(), RenderType.getCutout());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ScreenManager.registerFactory(
                ModContainers.UPGRADE_ALTAR_CONTAINER.get(),
                UpgradeAltarScreen::new
            );
        });
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            LOGGER.info("HELLO from Register Block");
        }
    }
}
