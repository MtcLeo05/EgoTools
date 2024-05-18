package com.leo.egotools.config;

import com.leo.egotools.EgoTools;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public static final ForgeConfigSpec CLIENT_SPEC;

    public static ForgeConfigSpec.ConfigValue<String> LIGHT_BACKGROUND;
    public static ForgeConfigSpec.ConfigValue<String> DARK_BACKGROUND;
    public static ForgeConfigSpec.ConfigValue<String> LIGHT_FILL;
    public static ForgeConfigSpec.ConfigValue<String> DARK_FILL;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        CLIENT_SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Client Configs");

         LIGHT_BACKGROUND = builder
             .comment("The color to use as the light color when the bar is empty")
             .define("lightBackground", "0xFFAAAAAA");

         DARK_BACKGROUND = builder
            .comment("The color to use as the light color when the bar is empty")
            .define("darkBackground", "0xFF555555");

         LIGHT_FILL = builder
            .comment("The color to use as the light color when the bar is empty")
            .define("lightFill", "0xFF00FFFF");

         DARK_FILL = builder
            .comment("The color to use as the light color when the bar is empty")
            .define("darkFill", "0xFF00AAAA");

        builder.pop();
    }

    public static int getLightBackground() {
        return isConfigLoaded() ? EgoTools.stringToColor(LIGHT_BACKGROUND.get()): 0xFFAAAAAA;
    }

    public static int getDarkBackground() {
        return isConfigLoaded() ? EgoTools.stringToColor(DARK_BACKGROUND.get()): 0xFF555555;
    }

    public static int getLightFill() {
        return isConfigLoaded() ? EgoTools.stringToColor(LIGHT_FILL.get()): 0xFF00FFFF;
    }

    public static int getDarkFill() {
        return isConfigLoaded() ? EgoTools.stringToColor(DARK_FILL.get()): 0xFF00AAAA;
    }

    public static void setLightBackground(String lightBackground) {
        if(isConfigLoaded()){
            LIGHT_BACKGROUND.set(lightBackground);
        }

    }

    public static void setDarkBackground(String darkBackground) {
        if(isConfigLoaded()){
            DARK_BACKGROUND.set(darkBackground);
        }
    }

    public static void setLightFill(String lightFill) {
        if(isConfigLoaded()){
            LIGHT_FILL.set(lightFill);
        }
    }

    public static void setDarkFill(String darkFill) {
        if(isConfigLoaded()){
            DARK_FILL.set(darkFill);
        }
    }

    static boolean isConfigLoaded(){
        return CLIENT_SPEC.isLoaded();
    }
}
