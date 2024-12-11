package com.leo.egotools.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class CompoundTagUtils {

    public static int getIntValue(CompoundTag tag, String s){
        if(tag.contains(s)){
            return tag.getInt(s);
        }

        return -1;
    }


    public static void increaseIntValue(CompoundTag tag, String s, int value){
        if(tag.contains(s)){
            tag.putInt(s, getIntValue(tag, s) + value);
        } else if (tag.contains("properties")) {
            CompoundTag prop = tag.getCompound("properties");
            if(prop.contains(s)){
                prop.putInt(s, getIntValue(prop, s) + value);
            }
        }
    }

    public static void overrideIntValue(@Nullable CompoundTag tag, String s, int value){
        if(tag == null) tag = new CompoundTag();

        if(tag.contains(s)){
            tag.putInt(s, value);
        } else if (tag.contains("properties")) {
            CompoundTag prop = tag.getCompound("properties");
            if(prop.contains(s)){
                prop.putInt(s, value);
            }
        }
    }

    public static boolean hasToolProperties(@Nullable CompoundTag tag){
        return tag != null && tag.contains("properties");
    }
}
