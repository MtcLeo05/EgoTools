package com.leo.egotools.event;

import com.leo.egotools.EgoTools;
import com.leo.egotools.client.gui.overlay.LevelHUDOverlay;
import com.leo.egotools.client.tooltip.LevelTooltipComponent;
import com.leo.egotools.client.tooltip.ClientLevelTooltipComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EgoTools.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModBusClientEvent {

    @SubscribeEvent
    public static void registerTooltip(RegisterClientTooltipComponentFactoriesEvent event){
        event.register(LevelTooltipComponent.class, ClientLevelTooltipComponent::new);
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("level", LevelHUDOverlay.HUD_LEVEL);
    }
}
