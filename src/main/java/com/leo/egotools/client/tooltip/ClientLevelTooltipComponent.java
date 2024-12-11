package com.leo.egotools.client.tooltip;

import com.leo.egotools.config.ClientConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;

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
    public void renderText(Font pFont, int pMouseX, int pMouseY, Matrix4f pMatrix, MultiBufferSource.BufferSource pBufferSource) {
        String level = getBigNumber(component.level());
        String exp = getBigNumber(component.exp());
        String maxExp = getBigNumber(component.maxExp());

        String expValue = exp + "/" + maxExp;

        pFont.drawInBatch(level, pMouseX + 54, pMouseY - 1, 0xFFFFFF, true, pMatrix, pBufferSource, Font.DisplayMode.NORMAL, 0, 0xF000F0);
        pFont.drawInBatch(expValue, pMouseX, pMouseY + 8, 0xFFFFFF, true, pMatrix, pBufferSource, Font.DisplayMode.NORMAL, 0, 0xF000F0);
    }

    @Override
    public void renderImage(Font pFont, int pX, int pY, GuiGraphics gui) {
        PoseStack ps = gui.pose();
        int totalWidth = 50;
        float percentageFull = (float) component.exp() / component.maxExp();
        int height = 3;
        int offsetFromBox = 4;
        int y = pY + 8;

        ps.pushPose();

        int progress = (int) Math.ceil(totalWidth * percentageFull);

        gui.fill(pX - 1, y - height - offsetFromBox - 1, pX + totalWidth + 1, y - offsetFromBox, 0xFF000000);
        gui.fill(pX, y - height - offsetFromBox, pX + progress, y - offsetFromBox - 1, ClientConfig.getLightFill());
        gui.fill(pX + progress, y - height - offsetFromBox, pX + totalWidth, y - offsetFromBox - 1, ClientConfig.getDarkBackground());
        
        ps.popPose();
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
