package me.lofro.utils.placeholders;

import me.clip.placeholderapi.PlaceholderHook;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.lofro.Main;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class PointsPlaceHolder extends PlaceholderExpansion {

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        if (identifier.equals("playerpoints")) {
            var gameManager = Main.getInstance().getGameManager();

            try {
                return String.valueOf(gameManager.getPoints(Main.getInstance().getConnection(), p.getUniqueId()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "simonsays";
    }

    @Override
    public @NotNull String getAuthor() {
        return "lofro";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }
}
