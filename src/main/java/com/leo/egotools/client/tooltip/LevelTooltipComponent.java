package com.leo.egotools.client.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

public class LevelTooltipComponent implements TooltipComponent {

    private final int exp;
    private final int maxExp;
    private final int level;

    public LevelTooltipComponent(int exp, int maxExp, int level) {
        this.exp = exp;
        this.maxExp = maxExp;
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public int getMaxExp() {
        return maxExp;
    }

    public int getLevel() {
        return level;
    }
}
