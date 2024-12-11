package com.leo.egotools;

import com.leo.egotools.config.ClientConfig;
import com.leo.egotools.config.ServerConfig;
import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EgoTools.MODID)
public class EgoTools {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "egotools";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public EgoTools() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SERVER_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_SPEC);
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


