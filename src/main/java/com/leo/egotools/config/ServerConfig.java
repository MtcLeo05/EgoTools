package com.leo.egotools.config;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ServerConfig {

    public static final ForgeConfigSpec SERVER_SPEC;

    public static ForgeConfigSpec.IntValue START_EXP;
    public static ForgeConfigSpec.BooleanValue WRONG_TOOL;
    public static ForgeConfigSpec.DoubleValue WRONG_TOOL_MULT;
    public static ForgeConfigSpec.IntValue EXP_PER_BLOCK;
    public static ForgeConfigSpec.IntValue EXP_PER_TILL;
    public static ForgeConfigSpec.DoubleValue EXP_MULTIPLIER;
    public static ForgeConfigSpec.DoubleValue KILL_EXP_MULTIPLIER;

    public static ForgeConfigSpec.BooleanValue LEVEL_MODE;
    public static ForgeConfigSpec.BooleanValue TOOL_HEAL;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ENCHANT_BLACKLIST;
    public static ForgeConfigSpec.BooleanValue ENCHANT_WHITELIST;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ATTRIBUTE_BLACKLIST;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> TOOL_ATTRIBUTE_BLACKLIST;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> WEAPON_ATTRIBUTE_BLACKLIST;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ATTRIBUTE_VALUE;
    public static ForgeConfigSpec.BooleanValue ATTRIBUTE_WHITELIST;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        SERVER_SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Server Configs");
        builder.push("Exp Configs");

        START_EXP = builder
            .comment("The exp needed for the first level")
            .comment("Also used as a base for consequent level ups")
            .defineInRange("startExp", 32, 1, Integer.MAX_VALUE);

        WRONG_TOOL = builder
            .comment("Should the wrong action (e.g. mining with a sword) still grant exp?")
            .define("wrongTool", false);

        WRONG_TOOL_MULT = builder
            .comment("The exp multipliers when using the wrong tool")
            .defineInRange("wrongToolMult", 1, 0, Double.MAX_VALUE);

        EXP_PER_BLOCK = builder
            .comment("Exp gained each block broken")
            .defineInRange("expPerBlock", 1, 1, Integer.MAX_VALUE);

        EXP_PER_TILL = builder
            .comment("Exp gained each block tilled")
            .defineInRange("expPerTill", 1, 1, Integer.MAX_VALUE);

        EXP_MULTIPLIER = builder
            .comment("Exp needed for the next level")
            .comment("Formula: startExp * this * (level + 1)")
            .defineInRange("expMultiplier", 2d, 1d, Double.MAX_VALUE);

        KILL_EXP_MULTIPLIER = builder
            .comment("Exp gained each mob kill")
            .comment("Formula: killedMobMaxHealth * this")
            .defineInRange("killExpMultiplier", 1d, 1d, Double.MAX_VALUE);

        builder.pop();
        builder.push("Level-Up Configs");
        TOOL_HEAL = builder
            .comment("Whether the tool should become brand new when levelling up")
            .define("toolHeal", true);

        LEVEL_MODE = builder
            .comment("Whether to grant enchants or stats when levelling up [true: stats, false: enchants]")
            .define("levelMode", false);

        builder.push("Enchant Configs");
        ENCHANT_BLACKLIST = builder
            .comment("Which enchantments should not be given when leveling up")
            .defineList("enchantBlacklist", List.of(
                "minecraft:silk_touch",
                "minecraft:mending",
                "minecraft:vanishing_curse",
                "minecraft:binding_curse"
            ), c -> true);

        ENCHANT_WHITELIST = builder
            .comment("Whether to consider the enchant blacklist as a whitelist")
            .define("enchantWhitelist", false);
        builder.pop();

        builder.push("Stats Configs");
        ATTRIBUTE_BLACKLIST = builder
            .comment("Which stats should not be given when leveling up")
            .defineList("statBlacklist", List.of(
                "minecraft:zombie.spawn_reinforcements",
                "minecraft:generic.movement_speed",
                "minecraft:generic.follow_range",
                "minecraft:generic.knockback_resistance",
                "minecraft:generic.flying_speed",
                "minecraft:horse.jump_strength",
                "forge:swim_speed",
                "forge:nametag_distance",
                "forge:entity_gravity",
                "forge:step_height_addition"
            ), c -> true);

        TOOL_ATTRIBUTE_BLACKLIST = builder
            .comment("Which stats should not be given to digger items (pickaxe, shovel etc.) when leveling up")
            .defineList("toolStatBlacklist", List.of(
                "minecraft:generic.attack_damage",
                "minecraft:generic.max_health",
                "minecraft:generic.attack_knockback",
                "minecraft:generic.armor",
                "minecraft:generic.armor_toughness",
                "forge:entity_reach"
            ), c -> true);

        WEAPON_ATTRIBUTE_BLACKLIST = builder
            .comment("Which stats should not be given to weapons (swords etc.) when leveling up")
            .defineList("weaponStatBlacklist", List.of(
                "minecraft:generic.attack_speed",
                "forge:block_reach"
            ), c -> true);

        ATTRIBUTE_WHITELIST = builder
            .comment("Whether to consider the stat blacklist as a whitelist")
            .define("statWhitelist", false);

        ATTRIBUTE_VALUE = builder
            .comment("Change value granted by Attribute")
            .comment("Format: attribute#value#operation")
            .comment("Operations: 0 (addition) / 1 (multiply_base) / 2 (multiply_total)")
            .defineList("attributeValue", List.of(
                "minecraft:generic.movement_speed#.25#0"
            ), c -> true);
        builder.pop();
        builder.pop();
        builder.pop();
    }

    public static int getStartExp() {
        return isConfigLoaded() ? START_EXP.get() : 32;
    }

    public static AttributeModifier getAttributeModifier(Attribute attribute) {
        String id = ForgeRegistries.ATTRIBUTES.getKey(attribute).toString();

        for (String s : ATTRIBUTE_VALUE.get()) {
            if (s.contains(id)) {
                String[] split = s.split("#");
                double value = Double.parseDouble(split[1]);
                int op = Integer.parseInt(split[2]);
                AttributeModifier.Operation o = AttributeModifier.Operation.fromValue(op);

                return new AttributeModifier(UUID.randomUUID().toString(), value, o);
            }
        }

        return new AttributeModifier(UUID.randomUUID().toString(), 1, AttributeModifier.Operation.ADDITION);
    }

    public static int getExpPerBlock() {
        return isConfigLoaded() ? EXP_PER_BLOCK.get() : 1;
    }

    public static int getExpPerTill() {
        return isConfigLoaded() ? EXP_PER_TILL.get() : 1;
    }

    public static float getExpMultiplier() {
        return isConfigLoaded() ? EXP_MULTIPLIER.get().floatValue() : 2f;
    }

    public static float getKillExpMultiplier() {
        return isConfigLoaded() ? KILL_EXP_MULTIPLIER.get().floatValue() : 1f;
    }

    public static List<? extends String> getEnchantBlackList() {
        return isConfigLoaded() ? ENCHANT_BLACKLIST.get() : new ArrayList<>();
    }

    public static boolean getEnchantWhiteList() {
        return isConfigLoaded() ? ENCHANT_WHITELIST.get() : false;
    }

    static boolean isConfigLoaded() {
        return SERVER_SPEC.isLoaded();
    }
}
