package com.leo.egotools.mixin;

import com.leo.egotools.config.ServerConfig;
import com.leo.egotools.util.LevelUtils;
import com.leo.egotools.util.NbtParUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SwordItem.class)
public abstract class SwordItemMixin{
    @Shadow public abstract boolean isCorrectToolForDrops(BlockState pBlock);

    @Inject(method = "mineBlock", at = @At(value = "RETURN"))
    public void addBlockExp(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving, CallbackInfoReturnable<Boolean> cir){
        if(!pLevel.isClientSide && isCorrectToolForDrops(pState)){
            if(NbtParUtils.hasToolProperties(pStack.getTag())){
                LevelUtils.increaseExp(pStack, ServerConfig.getExpPerBlock(), pEntityLiving.level().random);
            }
        }
    }

    @Inject(method = "hurtEnemy", at = @At(value = "RETURN"))
    public void addHitExp(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker, CallbackInfoReturnable<Boolean> cir){
        if(!pAttacker.level().isClientSide){
            if(pTarget.isDeadOrDying()){
                if(NbtParUtils.hasToolProperties(pStack.getTag())){
                    LevelUtils.increaseExp(pStack, (int) (pTarget.getMaxHealth() * ServerConfig.getKillExpMultiplier()), pAttacker.level().random);
                }
            }

        }
    }

}
