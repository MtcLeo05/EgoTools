package com.leo.egotools.util;

import com.leo.egotools.config.ServerConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class LevelUtils {
    static final Supplier<Stream<Map.Entry<ResourceKey<Enchantment>, Enchantment>>> registryEnchant = () -> ForgeRegistries.ENCHANTMENTS.getEntries().stream();

    static final List<Enchantment> BLACKLIST = List.of(
        Enchantments.BINDING_CURSE,
        Enchantments.VANISHING_CURSE,
        Enchantments.SILK_TOUCH
    );

    public static void increaseExp(ItemStack stack, int increase, RandomSource random){
        CompoundTag tag = stack.getTag().getCompound("properties");
        NbtParUtils.increaseIntValue(tag, "exp", increase);
        if(shouldLevelUp(tag)){
            levelUp(stack, random);
        }

    }

    public static boolean shouldLevelUp(CompoundTag tag){
        int exp = NbtParUtils.getIntValue(tag, "exp");
        int maxExp = NbtParUtils.getIntValue(tag, "maxExp");

        return maxExp > -1 && exp >= maxExp;
    }

    public static void levelUp(ItemStack stack, RandomSource random){
        NbtParUtils.overrideIntValue(stack.getTag(), "Damage", 0);
        CompoundTag tag = stack.getTag().getCompound("properties");
        NbtParUtils.overrideIntValue(tag, "exp", 0);
        NbtParUtils.increaseIntValue(tag, "level", 1);
        int level = NbtParUtils.getIntValue(tag, "level");
        NbtParUtils.overrideIntValue(tag, "maxExp", (int) (ServerConfig.getStartExp() * ServerConfig.getExpMultiplier() * level));
        addNewEnchant(stack, random);
    }

    public static void addNewEnchant(ItemStack stack, RandomSource random){
        if(NbtParUtils.hasToolProperties(stack.getTag())){
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

            if(enchantment.category.canEnchant(stack.getItem()) &&
                EnchantmentHelper.isEnchantmentCompatible(appliedEnchantments, enchantment)
                && !BLACKLIST.contains(enchantment)){
                compatibleEnchantments.add(enchantment);
            }
        });

        compatibleEnchantments.addAll(appliedEnchantments);

        return compatibleEnchantments;
    }
}
