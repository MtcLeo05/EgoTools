package com.leo.egotools.util;

import com.leo.egotools.config.ServerConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class LevelUtils {
    static final Supplier<Stream<Map.Entry<ResourceKey<Enchantment>, Enchantment>>> registryEnchant = () -> ForgeRegistries.ENCHANTMENTS.getEntries().stream();

    public static void increaseExp(ItemStack stack, int increase, RandomSource random){
        CompoundTag tag = stack.getTag().getCompound("properties");
        CompoundTagUtils.increaseIntValue(tag, "exp", increase);
        if(shouldLevelUp(tag)){
            levelUp(stack, random);
        }

    }

    public static boolean shouldLevelUp(CompoundTag tag){
        int exp = CompoundTagUtils.getIntValue(tag, "exp");
        int maxExp = CompoundTagUtils.getIntValue(tag, "maxExp");

        return maxExp > -1 && exp >= maxExp;
    }

    public static void levelUp(ItemStack stack, RandomSource random){
        if(ServerConfig.TOOL_HEAL.get()) CompoundTagUtils.overrideIntValue(stack.getTag(), "Damage", 0);
        CompoundTag tag = stack.getTag().getCompound("properties");
        CompoundTagUtils.overrideIntValue(tag, "exp", 0);
        CompoundTagUtils.increaseIntValue(tag, "level", 1);
        int level = CompoundTagUtils.getIntValue(tag, "level");
        CompoundTagUtils.overrideIntValue(tag, "maxExp", (int) (ServerConfig.getStartExp() * ServerConfig.getExpMultiplier() * level));

        if(ServerConfig.LEVEL_MODE.get()) {
            addNewStat(stack, random);
            return;
        }

        addNewEnchant(stack, random);
    }

    public static void addNewStat(ItemStack stack, RandomSource random) {
        List<String> blacklist = new ArrayList<>(ServerConfig.ATTRIBUTE_BLACKLIST.get().stream().map(String::toString).toList());

        List<Map.Entry<ResourceKey<Attribute>, Attribute>> attributes = ForgeRegistries.ATTRIBUTES.getEntries().stream().toList();
        int r = random.nextInt(0, attributes.size());

        if(stack.getItem() instanceof SwordItem) {
            blacklist.addAll(ServerConfig.WEAPON_ATTRIBUTE_BLACKLIST.get());
        }

        if(stack.getItem() instanceof DiggerItem) {
            blacklist.addAll(ServerConfig.TOOL_ATTRIBUTE_BLACKLIST.get());
        }

        Attribute a = attributes.get(r).getValue();
        String id = attributes.get(r).getKey().location().toString();

        while(blacklist.contains(id) ^ ServerConfig.ATTRIBUTE_WHITELIST.get()) {
            r = random.nextInt(0, attributes.size());
            a = attributes.get(r).getValue();
            id = attributes.get(r).getKey().location().toString();
        }

        stack.addAttributeModifier(a,
            ServerConfig.getAttributeModifier(a),
            EquipmentSlot.MAINHAND
        );
    }

    public static void addNewEnchant(ItemStack stack, RandomSource random){
        if(CompoundTagUtils.hasToolProperties(stack.getTag())){
            List<Enchantment> compatEnc = getCompatibleEnchantmentsForStack(stack);

            Enchantment enchantment = compatEnc.get(
                random.nextIntBetweenInclusive(0, compatEnc.size() - 1)
            );

            addEnchant(stack, enchantment);
        }
    }

    public static void addEnchant(ItemStack stack, Enchantment enchantment){
        Map<Enchantment, Integer> allEnchantments = stack.getAllEnchantments();
        if(allEnchantments.containsKey(enchantment)){
            allEnchantments.put(enchantment, allEnchantments.get(enchantment) + 1);
        } else {
            allEnchantments.put(enchantment, 1);
        }

        EnchantmentHelper.setEnchantments(allEnchantments, stack);
    }

    public static List<Enchantment> getCompatibleEnchantmentsForStack(ItemStack stack){
        List<Enchantment> compatibleEnchantments = new ArrayList<>();
        List<Enchantment> appliedEnchantments = new ArrayList<>();

        stack.getAllEnchantments().forEach((enchantment, integer) -> {
            appliedEnchantments.add(enchantment);
        });

        registryEnchant.get().forEach(resourceKeyEnchantmentEntry -> {
            Enchantment enchantment = resourceKeyEnchantmentEntry.getValue();
            String enchantID = ForgeRegistries.ENCHANTMENTS.getKey(enchantment).toString();

            if(enchantment.category.canEnchant(stack.getItem()) &&
                EnchantmentHelper.isEnchantmentCompatible(appliedEnchantments, enchantment)
                && (!ServerConfig.getEnchantBlackList().contains(enchantID) ^ ServerConfig.getEnchantWhiteList())){
                compatibleEnchantments.add(enchantment);
            }
        });

        compatibleEnchantments.addAll(appliedEnchantments);

        return compatibleEnchantments;
    }
}
