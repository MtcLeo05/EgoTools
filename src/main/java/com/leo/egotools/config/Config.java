package com.leo.egotools.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Map;

public class Config {
    private final Map<String, String> colors;
    private final int startExp;
    private final float expMultiplier;
    private final int expPerBlock;
    private final float killExpMultiplier;
    private final int expPerTill;

    public static final Codec<Config> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("colors").forGetter(config -> config.colors),
        Codec.INT.fieldOf("startExp").forGetter(config -> config.startExp),
        Codec.FLOAT.fieldOf("expMultiplier").forGetter(config -> config.expMultiplier),
        Codec.INT.fieldOf("expPerBlock").forGetter(config -> config.expPerBlock),
        Codec.FLOAT.fieldOf("killExpMultiplier").forGetter(config -> config.killExpMultiplier),
        Codec.INT.fieldOf("expPerTill").forGetter(config -> config.expPerTill)
    ).apply(inst, Config::new));

    public Config(Map<String, String> colors, int startExp, float expMultiplier, int expPerBlock, float killExpMultiplier, int expPerTill){
        this.colors = colors;
        this.startExp = startExp;
        this.expMultiplier = expMultiplier;
        this.expPerBlock = expPerBlock;
        this.killExpMultiplier = killExpMultiplier;
        this.expPerTill = expPerTill;
    }

    public static Config INSTANCE;

    public static int getStartExp(){
        if(INSTANCE != null){
            return Math.abs(INSTANCE.startExp);
        } else {
            return 32;
        }
    }

    public static float getExpMultiplier(){
        if(INSTANCE != null){
            return Math.abs(INSTANCE.expMultiplier);
        } else {
            return 2;
        }
    }

    public static int getExpPerBlock(){
        if(INSTANCE != null){
            return Math.abs(INSTANCE.expPerBlock);
        } else {
            return 1;
        }
    }

    public static float getKillExpMultiplier(){
        if(INSTANCE != null){
            return Math.abs(INSTANCE.killExpMultiplier);
        } else {
            return 1;
        }
    }

    public static int getExpPerTill(){
        if(INSTANCE != null){
            return Math.abs(INSTANCE.expPerTill);
        } else {
            return 1;
        }
    }

    public static int getDarkBackground(){
        if(INSTANCE != null){
            return stringToColor(INSTANCE.colors.get("darkBackground"));
        } else {
            return 0xFF555555;
        }
    }

    public static int getLightBackground(){
        if(INSTANCE != null){
            return stringToColor(INSTANCE.colors.get("lightBackground"));
        } else {
            return 0xFFAAAAAA;
        }
    }

    public static int getDarkFill(){
        if(INSTANCE != null){
            return stringToColor(INSTANCE.colors.get("darkFill"));
        } else {
            return 0xFF00AAAA;
        }
    }

    public static int getLightFill(){
        if(INSTANCE != null){
            return stringToColor(INSTANCE.colors.get("lightFill"));
        } else {
            return 0xFF00FFFF;
        }
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
