package com.leo.egotools.mixin;

import com.leo.egotools.client.tooltip.LevelTooltipComponent;
import com.leo.egotools.config.ServerConfig;
import com.leo.egotools.util.CompoundTagUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(TieredItem.class)
public abstract class TieredItemMixin extends Item {

    @Unique
    private static final String PROPERTIES_TAG = "properties";
    @Unique
    private static final String EXP_TAG = "exp";
    @Unique
    private static final String MAX_EXP_TAG = "maxExp";
    @Unique
    private static final String LEVEL_TAG = "level";

    public TieredItemMixin(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack pStack) {
        if(pStack.getTag() != null && pStack.getTag().contains(PROPERTIES_TAG)){
            CompoundTag prop = pStack.getTag().getCompound(PROPERTIES_TAG);

            return Optional.of(
                new LevelTooltipComponent(
                    CompoundTagUtils.getIntValue(prop, EXP_TAG),
                    CompoundTagUtils.getIntValue(prop, MAX_EXP_TAG),
                    CompoundTagUtils.getIntValue(prop, LEVEL_TAG)
                )
            );
        }
        return super.getTooltipImage(pStack);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if(!pLevel.isClientSide){
            CompoundTag tag = pStack.getOrCreateTag();

            if(!tag.contains(PROPERTIES_TAG)){
                CompoundTag prop = new CompoundTag();

                prop.putInt(EXP_TAG, 0);
                prop.putInt(MAX_EXP_TAG, ServerConfig.getStartExp());
                prop.putInt(LEVEL_TAG, 0);

                tag.put(PROPERTIES_TAG, prop);
                pStack.setTag(tag);
            }

            pStack.setTag(tag);
        }

        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }
}
