package com.leo.egotools.event;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.leo.egotools.EgoTools;
import com.leo.egotools.config.ClientConfig;
import com.leo.egotools.config.ServerConfig;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = EgoTools.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEvent {
    public static CommentedConfig configData;

    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent.Loading event){
        configData = event.getConfig().getConfigData();

        if(configData.contains("Server Configs")){
            int startExp = configData.getInt("Server Configs.startExp");
            int expPerBlock = configData.getInt("Server Configs.expPerBlock");
            int expPerTill = configData.getInt("Server Configs.expPerTill");
            double expMultiplier = configData.get("Server Configs.expMultiplier");
            double killExpMultiplier = configData.get("Server Configs.killExpMultiplier");
            List<? extends String> enchantBlacklist = configData.get("Server Configs.enchantBlacklist");
            boolean enchantWhitelist = configData.get("Server Configs.enchantWhitelist");

            ServerConfig.setStartExp(startExp);
            ServerConfig.setExpPerBlock(expPerBlock);
            ServerConfig.setExpPerTill(expPerTill);
            ServerConfig.setExpMultiplier(expMultiplier);
            ServerConfig.setKillExpMultiplier(killExpMultiplier);
            ServerConfig.setEnchantBlacklist(enchantBlacklist);
            ServerConfig.setEnchantWhitelist(enchantWhitelist);
        }

        if(configData.contains("Client Configs")){
            String lightBackground = configData.get("Client Configs.lightBackground");
            String darkBackground = configData.get("Client Configs.darkBackground");
            String lightFill = configData.get("Client Configs.lightFill");
            String darkFill = configData.get("Client Configs.darkFill");

            ClientConfig.setLightBackground(lightBackground);
            ClientConfig.setDarkBackground(darkBackground);
            ClientConfig.setLightFill(lightFill);
            ClientConfig.setDarkFill(darkFill);
        }
    }

    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event){
        configData = event.getConfig().getConfigData();

        if(configData.contains("Server Configs")){
            int startExp = configData.getInt("Server Configs.startExp");
            int expPerBlock = configData.getInt("Server Configs.expPerBlock");
            int expPerTill = configData.getInt("Server Configs.expPerTill");
            double expMultiplier = configData.get("Server Configs.expMultiplier");
            double killExpMultiplier = configData.get("Server Configs.killExpMultiplier");
            List<? extends String> enchantBlacklist = configData.get("Server Configs.enchantBlacklist");
            boolean enchantWhitelist = configData.get("Server Configs.enchantWhitelist");

            ServerConfig.setStartExp(startExp);
            ServerConfig.setExpPerBlock(expPerBlock);
            ServerConfig.setExpPerTill(expPerTill);
            ServerConfig.setExpMultiplier(expMultiplier);
            ServerConfig.setKillExpMultiplier(killExpMultiplier);
            ServerConfig.setEnchantBlacklist(enchantBlacklist);
            ServerConfig.setEnchantWhitelist(enchantWhitelist);
        }

        if(configData.contains("Client Configs")){
            String lightBackground = configData.get("Client Configs.lightBackground");
            String darkBackground = configData.get("Client Configs.darkBackground");
            String lightFill = configData.get("Client Configs.lightFill");
            String darkFill = configData.get("Client Configs.darkFill");

            ClientConfig.setLightBackground(lightBackground);
            ClientConfig.setDarkBackground(darkBackground);
            ClientConfig.setLightFill(lightFill);
            ClientConfig.setDarkFill(darkFill);
        }
    }
}
