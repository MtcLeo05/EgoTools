package com.leo.egotools.mixin;

import com.leo.egotools.config.ServerConfig;
import com.leo.egotools.util.LevelUtils;
import com.leo.egotools.util.CompoundTagUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DiggerItem.class)
public abstract class DiggerItemMixin{
    @Shadow(remap = false) public abstract boolean isCorrectToolForDrops(ItemStack stack, BlockState state);

    @Inject(method = "mineBlock", at = @At(value = "RETURN"))
    public void addExp(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving, CallbackInfoReturnable<Boolean> cir){
        if(!pLevel.isClientSide){
            if(isCorrectToolForDrops(pStack, pState)) {
                if(CompoundTagUtils.hasToolProperties(pStack.getTag())){
                    LevelUtils.increaseExp(pStack, ServerConfig.getExpPerBlock(), pEntityLiving.level().random);
                    return;
                }
            }

            if(ServerConfig.WRONG_TOOL.get()) {
                if(CompoundTagUtils.hasToolProperties(pStack.getTag())){
                    LevelUtils.increaseExp(pStack, (int) (ServerConfig.getExpPerBlock() * ServerConfig.WRONG_TOOL_MULT.get()), pEntityLiving.level().random);
                }
            }
        }
    }




}
