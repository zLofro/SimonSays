package me.lofro.game;

import lombok.Getter;
import org.bukkit.Location;

public enum Actions {

    ;

    private final @Getter String title;
    private final @Getter String subtitle;
    private final @Getter int timeLimit;
    private final @Getter Location actionLocation;

    Actions(String title, String subtitle, int timeLimit, Location actionLocation) {
        this.title = title;
        this.subtitle = subtitle;
        this.timeLimit = timeLimit;
        this.actionLocation = actionLocation;
    }

}
