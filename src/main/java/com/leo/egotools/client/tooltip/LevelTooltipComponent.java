package com.leo.egotools.client.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record LevelTooltipComponent(int exp, int maxExp, int level) implements TooltipComponent {

}
