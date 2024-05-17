package com.leo.egotools.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class ConfigReloadListener extends SimpleJsonResourceReloadListener {
    private static ConfigReloadListener INSTANCE;

    public static final Logger LOGGER = LogManager.getLogger();
    public static final Gson GSON_INSTANCE = new Gson();
    private static final String folder = "config";

    private ConfigReloadListener() {
        super(GSON_INSTANCE, folder);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceList, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        for (ResourceLocation location : resourceList.keySet()) {
            JsonElement json = resourceList.get(location);
            Config.CODEC.parse(JsonOps.INSTANCE, json)
                .resultOrPartial(errorMsg -> LOGGER.warn("Could not decode file with json id {} - error: {}", location, errorMsg))
                .ifPresent(config -> Config.INSTANCE = config);
        }
    }

    public static ConfigReloadListener create() {
        INSTANCE = new ConfigReloadListener();
        return INSTANCE;
    }
}
