package com.leo.egotools.mixin;

import com.leo.egotools.config.Config;
import com.leo.egotools.util.LevelUtils;
import com.leo.egotools.util.NbtParUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HoeItem.class)
public abstract class HoeItemMixin {

    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V"))
    public void addExp(UseOnContext pContext, CallbackInfoReturnable<InteractionResult> cir){
        ItemStack pStack = pContext.getItemInHand();
        if(NbtParUtils.hasToolProperties(pStack.getTag())){
            LevelUtils.increaseExp(pStack, Config.getExpPerTill(), pContext.getLevel().random);
        }
    }
}
