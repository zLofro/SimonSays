package me.lofro.utils.placeholders;

import me.clip.placeholderapi.PlaceholderHook;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.lofro.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class PointsPlaceHolder extends PlaceholderExpansion {

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        try {
            var gameManager = Main.getInstance().getGameManager();
            var connection = Main.getInstance().getConnection();

            switch (identifier) {
                case "playerpoints" -> {
                    return String.valueOf(gameManager.getPoints(connection, p.getUniqueId()));
                }
                case "%simonsays_playerpoints_1_points%" -> {
                    var topList = gameManager.getTop();
                    if (topList.isEmpty()) return null;
                    return String.valueOf(gameManager.getPoints(connection, topList.get(0)));
                }
                case "%simonsays_playerpoints_1_name%" -> {
                    var topList = gameManager.getTop();
                    if (topList.isEmpty()) return null;
                    var player = Bukkit.getPlayer(topList.get(0));
                    return player != null ? player.getName() : null;
                }
                case "%simonsays_playerpoints_2_points%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 2) return null;
                    return String.valueOf(gameManager.getPoints(connection, topList.get(1)));
                }
                case "%simonsays_playerpoints_2_name%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 2) return null;
                    var player = Bukkit.getPlayer(topList.get(1));
                    return player != null ? player.getName() : null;
                }
                case "%simonsays_playerpoints_3_points%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 3) return null;
                    return String.valueOf(gameManager.getPoints(connection, topList.get(2)));
                }
                case "%simonsays_playerpoints_3_name%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 3) return null;
                    var player = Bukkit.getPlayer(topList.get(2));
                    return player != null ? player.getName() : null;
                }
                case "%simonsays_playerpoints_4_points%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 4) return null;
                    return String.valueOf(gameManager.getPoints(connection, topList.get(3)));
                }
                case "%simonsays_playerpoints_4_name%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 4) return null;
                    var player = Bukkit.getPlayer(topList.get(3));
                    return player != null ? player.getName() : null;
                }
                case "%simonsays_playerpoints_5_points%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 5) return null;
                    return String.valueOf(gameManager.getPoints(connection, topList.get(4)));
                }
                case "%simonsays_playerpoints_5_name%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 5) return null;
                    var player = Bukkit.getPlayer(topList.get(4));
                    return player != null ? player.getName() : null;
                }
                case "%simonsays_playerpoints_6_points%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 6) return null;
                    return String.valueOf(gameManager.getPoints(connection, topList.get(5)));
                }
                case "%simonsays_playerpoints_6_name%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 6) return null;
                    var player = Bukkit.getPlayer(topList.get(5));
                    return player != null ? player.getName() : null;
                }
                case "%simonsays_playerpoints_7_points%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 7) return null;
                    return String.valueOf(gameManager.getPoints(connection, topList.get(6)));
                }
                case "%simonsays_playerpoints_7_name%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 7) return null;
                    var player = Bukkit.getPlayer(topList.get(6));
                    return player != null ? player.getName() : null;
                }
                case "%simonsays_playerpoints_8_points%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 8) return null;
                    return String.valueOf(gameManager.getPoints(connection, topList.get(7)));
                }
                case "%simonsays_playerpoints_8_name%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 8) return null;
                    var player = Bukkit.getPlayer(topList.get(7));
                    return player != null ? player.getName() : null;
                }
                case "%simonsays_playerpoints_9_points%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 9) return null;
                    return String.valueOf(gameManager.getPoints(connection, topList.get(8)));
                }
                case "%simonsays_playerpoints_9_name%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 9) return null;
                    var player = Bukkit.getPlayer(topList.get(8));
                    return player != null ? player.getName() : null;
                }
                case "%simonsays_playerpoints_10_points%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 10) return null;
                    return String.valueOf(gameManager.getPoints(connection, topList.get(9)));
                }
                case "%simonsays_playerpoints_10_name%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 10) return null;
                    var player = Bukkit.getPlayer(topList.get(9));
                    return player != null ? player.getName() : null;
                }
                case "%simonsays_playerpoints_11_points%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 11) return null;
                    return String.valueOf(gameManager.getPoints(connection, topList.get(10)));
                }
                case "%simonsays_playerpoints_11_name%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 11) return null;
                    var player = Bukkit.getPlayer(topList.get(10));
                    return player != null ? player.getName() : null;
                }
                case "%simonsays_playerpoints_12_points%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 12) return null;
                    return String.valueOf(gameManager.getPoints(connection, topList.get(11)));
                }
                case "%simonsays_playerpoints_12_name%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 12) return null;
                    var player = Bukkit.getPlayer(topList.get(11));
                    return player != null ? player.getName() : null;
                }
                case "%simonsays_playerpoints_13_points%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 13) return null;
                    return String.valueOf(gameManager.getPoints(connection, topList.get(12)));
                }
                case "%simonsays_playerpoints_13_name%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 13) return null;
                    var player = Bukkit.getPlayer(topList.get(12));
                    return player != null ? player.getName() : null;
                }
                case "%simonsays_playerpoints_14_points%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 14) return null;
                    return String.valueOf(gameManager.getPoints(connection, topList.get(13)));
                }
                case "%simonsays_playerpoints_14_name%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 14) return null;
                    var player = Bukkit.getPlayer(topList.get(13));
                    return player != null ? player.getName() : null;
                }
                case "%simonsays_playerpoints_15_points%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 15) return null;
                    return String.valueOf(gameManager.getPoints(connection, topList.get(14)));
                }
                case "%simonsays_playerpoints_15_name%" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 15) return null;
                    var player = Bukkit.getPlayer(topList.get(14));
                    return player != null ? player.getName() : null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
