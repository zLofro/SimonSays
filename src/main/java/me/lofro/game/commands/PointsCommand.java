package me.lofro.game.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.lofro.Main;
import me.lofro.game.Actions;
import me.lofro.game.GameManager;
import me.lofro.game.Roles;
import me.lofro.utils.ChatColorFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;

@CommandAlias("puntos")
@CommandPermission("admin.perm")
public class PointsCommand extends BaseCommand {

    private final GameManager gameManager;

    private final Connection connection;

    public PointsCommand(GameManager gameManager) {
        this.gameManager = gameManager;
        this.connection = Main.getInstance().getConnection();
    }

    @Subcommand("añadir")
    @CommandCompletion("nombre puntos")
    private void givePoints(CommandSender sender, String playerName, int points) {
        try {
            gameManager.createPointsTable(connection);

            var player = Bukkit.getOfflinePlayer(playerName);
            var uuid = player.getUniqueId();

            var newPoints = gameManager.getPoints(connection, uuid) + points;
            gameManager.setPoints(connection, uuid, newPoints);
            sender.sendMessage(ChatColorFormatter.stringToString("&aSe le han añadido " + points + "&a puntos al jugador " + player.getName() + "&a. Ahora tiene " + newPoints + "&a puntos."));
        } catch (SQLException e) {
            Bukkit.getLogger().info(e.getMessage());
            sender.sendMessage(ChatColorFormatter.stringToString("&cHa habido un error al ejecutar el comando. Revisa la consola."));
            throw new RuntimeException(e);
        }
    }

    @Subcommand("eliminar")
    @CommandCompletion(" puntos")
    private void removePoints(CommandSender sender, String playerName, int points) {
        try {
            var player = Bukkit.getOfflinePlayer(playerName);
            var uuid = player.getUniqueId();

            var newPoints = gameManager.getPoints(connection, uuid) - points;
            gameManager.setPoints(connection, uuid, newPoints);
            sender.sendMessage(ChatColorFormatter.stringToString("&aSe le han eliminado " + points + "&a puntos al jugador " + player.getName() + "&a. Ahora tiene " + newPoints + "&a puntos."));
        } catch (SQLException e) {
            Bukkit.getLogger().info(e.getMessage());
            sender.sendMessage(ChatColorFormatter.stringToString("&cHa habido un error al ejecutar el comando. Revisa la consola."));
            throw new RuntimeException(e);
        }
    }

}
