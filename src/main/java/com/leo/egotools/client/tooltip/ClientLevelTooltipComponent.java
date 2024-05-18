package com.leo.egotools.client.tooltip;

import com.leo.egotools.config.ClientConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;

public class ClientLevelTooltipComponent implements ClientTooltipComponent {

    private final LevelTooltipComponent component;

    public ClientLevelTooltipComponent(LevelTooltipComponent component){
        this.component = component;
    }

    @Override
    public int getHeight() {
        return 20;
    }

    @Override
    public int getWidth(Font font) {
        return 50;
    }

    @Override
    public void renderText(Font pFont, int pX, int pY, Matrix4f pMatrix4f, MultiBufferSource.BufferSource pBufferSource) {
        String level = getBigNumber(component.getLevel());
        String exp = getBigNumber(component.getExp());
        String maxExp = getBigNumber(component.getMaxExp());

        String expValue = exp + "/" + maxExp;

        PoseStack pPoseStack = new PoseStack();

        pFont.drawInBatch(level, pX + 54, pY - 1, 0xFFFFFF, true, pMatrix4f, pBufferSource, true, 0x000000, LightTexture.FULL_BRIGHT);
        pFont.drawInBatch(expValue, pX, pY + 8, 0xFFFFFF, true, pMatrix4f, pBufferSource, true, 0x000000, LightTexture.FULL_BRIGHT);
    }

    @Override
    public void renderImage(Font pFont, int pX, int pY, PoseStack pPoseStack, ItemRenderer pItemRenderer, int pBlitOffset) {
        int totalWidth = 50;
        float percentageFull = (float) component.getExp() / component.getMaxExp();
        int height = 3;
        int offsetFromBox = 4;


        pY += 8;

        pPoseStack.pushPose();

        int progress = (int) Math.ceil(totalWidth * percentageFull);
        GuiComponent.fill(pPoseStack,pX - 1, pY - height - offsetFromBox - 1, pX + totalWidth + 1, pY - offsetFromBox, 0xFF000000);
        GuiComponent.fill(pPoseStack,pX, pY - height - offsetFromBox, pX + progress, pY - offsetFromBox, ClientConfig.getLightFill());
        GuiComponent.fill(pPoseStack,pX + progress, pY - height - offsetFromBox, pX + totalWidth, pY - offsetFromBox, ClientConfig.getLightBackground());

        pPoseStack.popPose();
    }

    public static String getBigNumber(int number){
        /*
         * Inspired by the old TeslaAPI: https://github.com/Darkhax-Minecraft/Tesla/blob/96dd08eebdec61e11ba1550e0f0edd93101a3fd0/src/main/java/net/darkhax/tesla/lib/TeslaUtils.java
         */
        String readableStacksize = String.valueOf(number);
        if(number >= 1000){
            final int exp = (int) (Math.log(number) / Math.log(1000));
            final char unitType = "KMG".charAt(exp - 1);
            final double value = number / Math.pow(1000, exp);
            final double valueForCalc = value - (int) value;
            if(valueForCalc >= 0.05D || valueForCalc <= 0.95D){
                readableStacksize = String.format("%.1f%s", value, unitType);
            } else {
                readableStacksize = String.format("%.0f%s", value, unitType);
            }
        }
        return readableStacksize;
    }
}
