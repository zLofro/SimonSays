package me.lofro.utils.placeholders;

import me.clip.placeholderapi.PlaceholderHook;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.lofro.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class PointsPlaceHolder extends PlaceholderExpansion {

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        try {
            var gameManager = Main.getInstance().getGameManager();
            var connection = Main.getInstance().getConnection();

            switch (identifier) {
                case "playerpoints" -> {
                    return String.valueOf(gameManager.getPlayerPoints().get(p.getUniqueId()));
                }
                case "playerpoints_1_points" -> {
                    var topList = gameManager.getTop();
                    if (topList.isEmpty()) return "";
                    return String.valueOf(gameManager.getPlayerPoints().get(topList.get(0)));
                }
                case "playerpoints_1_name" -> {
                    var topList = gameManager.getTop();
                    if (topList.isEmpty()) return "";
                    var player = Bukkit.getOfflinePlayer(topList.get(0));
                    return player.getName();
                }
                case "playerpoints_2_points" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 2) return "";
                    return String.valueOf(gameManager.getPlayerPoints().get(topList.get(1)));
                }
                case "playerpoints_2_name" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 2) return "";
                    var player = Bukkit.getOfflinePlayer(topList.get(1));
                    return player.getName();
                }
                case "playerpoints_3_points" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 3) return "";
                    return String.valueOf(gameManager.getPlayerPoints().get(topList.get(2)));
                }
                case "playerpoints_3_name" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 3) return "";
                    var player = Bukkit.getOfflinePlayer(topList.get(2));
                    return player.getName();
                }
                case "playerpoints_4_points" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 4) return "";
                    return String.valueOf(gameManager.getPlayerPoints().get(topList.get(3)));
                }
                case "playerpoints_4_name" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 4) return "";
                    var player = Bukkit.getOfflinePlayer(topList.get(3));
                    return player.getName();
                }
                case "playerpoints_5_points" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 5) return "";
                    return String.valueOf(gameManager.getPlayerPoints().get(topList.get(4)));
                }
                case "playerpoints_5_name" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 5) return "";
                    var player = Bukkit.getOfflinePlayer(topList.get(4));
                    return player.getName();
                }
                case "playerpoints_6_points" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 6) return "";
                    return String.valueOf(gameManager.getPlayerPoints().get(topList.get(5)));
                }
                case "playerpoints_6_name" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 6) return "";
                    var player = Bukkit.getOfflinePlayer(topList.get(5));
                    return player.getName();
                }
                case "playerpoints_7_points" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 7) return "";
                    return String.valueOf(gameManager.getPlayerPoints().get(topList.get(6)));
                }
                case "playerpoints_7_name" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 7) return "";
                    var player = Bukkit.getOfflinePlayer(topList.get(6));
                    return player.getName();
                }
                case "playerpoints_8_points" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 8) return "";
                    return String.valueOf(gameManager.getPlayerPoints().get(topList.get(7)));
                }
                case "playerpoints_8_name" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 8) return "";
                    var player = Bukkit.getOfflinePlayer(topList.get(7));
                    return player.getName();
                }
                case "playerpoints_9_points" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 9) return "";
                    return String.valueOf(gameManager.getPlayerPoints().get(topList.get(8)));
                }
                case "playerpoints_9_name" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 9) return "";
                    var player = Bukkit.getOfflinePlayer(topList.get(8));
                    return player.getName();
                }
                case "playerpoints_10_points" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 10) return "";
                    return String.valueOf(gameManager.getPlayerPoints().get(topList.get(9)));
                }
                case "playerpoints_10_name" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 10) return "";
                    var player = Bukkit.getOfflinePlayer(topList.get(9));
                    return player.getName();
                }
                case "playerpoints_11_points" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 11) return "";
                    return String.valueOf(gameManager.getPlayerPoints().get(topList.get(10)));
                }
                case "playerpoints_11_name" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 11) return "";
                    var player = Bukkit.getOfflinePlayer(topList.get(10));
                    return player.getName();
                }
                case "playerpoints_12_points" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 12) return "";
                    return String.valueOf(gameManager.getPlayerPoints().get(topList.get(11)));
                }
                case "playerpoints_12_name" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 12) return "";
                    var player = Bukkit.getOfflinePlayer(topList.get(11));
                    return player.getName();
                }
                case "playerpoints_13_points" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 13) return "";
                    return String.valueOf(gameManager.getPlayerPoints().get(topList.get(12)));
                }
                case "playerpoints_13_name" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 13) return "";
                    var player = Bukkit.getOfflinePlayer(topList.get(12));
                    return player.getName();
                }
                case "playerpoints_14_points" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 14) return "";
                    return String.valueOf(gameManager.getPlayerPoints().get(topList.get(13)));
                }
                case "playerpoints_14_name" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 14) return "";
                    var player = Bukkit.getOfflinePlayer(topList.get(13));
                    return player.getName();
                }
                case "playerpoints_15_points" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 15) return "";
                    return String.valueOf(gameManager.getPlayerPoints().get(topList.get(14)));
                }
                case "playerpoints_15_name" -> {
                    var topList = gameManager.getTop();
                    if (topList.size() < 15) return "";
                    var player = Bukkit.getOfflinePlayer(topList.get(14));
                    return player.getName();
                }
            }
        } catch (SQLException | ExecutionException e) {
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
