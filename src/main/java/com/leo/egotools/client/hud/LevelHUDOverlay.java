package com.leo.egotools.client.hud;

import com.leo.egotools.config.Config;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class LevelHUDOverlay {
    public static final IGuiOverlay HUD_LEVEL = ((forgeGui, poseStack, partialTick, width, height) -> {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if(player == null) return;

        ItemStack handStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        CompoundTag tag = handStack.getOrCreateTag();

        if(tag.contains("properties")){

            CompoundTag prop = tag.getCompound("properties");

            int level = prop.getInt("level");
            int exp = prop.getInt("exp");
            int maxExp = prop.getInt("maxExp");

            int barWidth = 176;
            int startY = height - 24;
            int startX = (width / 2) - (barWidth / 2);

            float normalizedProgress = (float) exp / maxExp;

            int actualProgress = Math.round(barWidth * normalizedProgress);

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            GuiComponent.drawCenteredString(poseStack, forgeGui.getFont(), String.valueOf(level), startX - 8, startY - 4, 0xFFFFFF);

            renderTwoColor(poseStack, startX, startY, startX + barWidth, startY + 2, Config.getLightBackground(), Config.getDarkBackground());
            renderTwoColor(poseStack, startX, startY, startX + actualProgress, startY + 2, Config.getLightFill(), Config.getDarkFill());
        }
    });

    public static void renderTwoColor(PoseStack guiGraphics, int startX, int startY, int maxX, int maxY, int lightColor, int darkColor){
        GuiComponent.fill(guiGraphics, startX, startY, maxX, startY + 1, lightColor);
        GuiComponent.fill(guiGraphics, startX, maxY - 1, maxX, maxY, darkColor);
    }
}
