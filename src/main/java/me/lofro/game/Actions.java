package me.lofro.game;

import lombok.Getter;
import me.lofro.utils.configuration.YMLConfig;

public enum Actions {

    PAPER_ITEM_FRAME(),
    PAPER_ITEM_FRAME2(),
    PAPER_ITEM_FRAME_SELECTION(),
    BUTTON_MOVEMENT(),
    ITEM_CATCH(),
    PRESSURE_PLATE(),
    THREE_ITEM_FRAMES(),
    PRESS_BUTTON(),
    FLOOR_IS_LAVA();

    private final @Getter String title;
    private final @Getter String subtitle;

    Actions() {
        this.title = YMLConfig.getString(this.name() + "_TITLE");
        this.subtitle = YMLConfig.getString(this.name() + "_SUBTITLE");
    }

}
