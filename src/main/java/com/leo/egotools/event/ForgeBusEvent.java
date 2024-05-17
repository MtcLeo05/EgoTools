package com.leo.egotools.event;

import com.leo.egotools.EgoTools;
import com.leo.egotools.config.ConfigReloadListener;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EgoTools.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeBusEvent {

    @SubscribeEvent
    public static void addReloadListener(AddReloadListenerEvent event) {
        event.addListener(ConfigReloadListener.create());
    }

}
