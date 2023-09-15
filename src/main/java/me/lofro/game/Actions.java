package me.lofro.game;

import lombok.Getter;

public enum Actions {

    ;

    private final @Getter String title;
    private final @Getter String subtitle;
    private final @Getter int timeLimit;

    Actions(String title, String subtitle, int timeLimit) {
        this.title = title;
        this.subtitle = subtitle;
        this.timeLimit = timeLimit;
    }

}
