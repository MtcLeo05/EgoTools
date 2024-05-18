package com.leo.egotools;

import com.leo.egotools.config.ClientConfig;
import com.leo.egotools.config.ServerConfig;
import com.leo.egotools.network.ModMessages;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EgoTools.MODID)
public class EgoTools {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "egotools";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public EgoTools() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::commonSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SERVER_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModMessages.register();
    }

    public static int stringToColor(String color){
        if(color.startsWith("0x")){
            color = color.substring(2);
        }

        if(color.startsWith("#")){
            color = color.substring(1);
        }

        long longColor = Long.parseLong(color, 16);

        return (int) longColor;
    }
}
