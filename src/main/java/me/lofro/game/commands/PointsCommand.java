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
    @CommandCompletion(" puntos")
    private void givePoints(CommandSender sender, @Flags("other") Player player, int points) {
        try {
            gameManager.createPointsTable(connection);

            var newPoints = gameManager.getPoints(connection, player.getUniqueId()) + points;
            gameManager.setPoints(connection, player.getUniqueId(), newPoints);
            sender.sendMessage(ChatColorFormatter.stringToString("&aSe le han añadido " + points + "&a puntos al jugador " + player.getName() + "&a. Ahora tiene " + newPoints + "&a puntos."));
        } catch (SQLException e) {
            Bukkit.getLogger().info(e.getMessage());
            sender.sendMessage(ChatColorFormatter.stringToString("&cHa habido un error al ejecutar el comando. Revisa la consola."));
            throw new RuntimeException(e);
        }
    }

    @Subcommand("eliminar")
    @CommandCompletion(" puntos")
    private void removePoints(CommandSender sender, @Flags("other") Player player, int points) {
        try {
            var newPoints = gameManager.getPoints(connection, player.getUniqueId()) - points;
            gameManager.setPoints(connection, player.getUniqueId(), newPoints);
            sender.sendMessage(ChatColorFormatter.stringToString("&aSe le han eliminado " + points + "&a puntos al jugador " + player.getName() + "&a. Ahora tiene " + newPoints + "&a puntos."));
        } catch (SQLException e) {
            Bukkit.getLogger().info(e.getMessage());
            sender.sendMessage(ChatColorFormatter.stringToString("&cHa habido un error al ejecutar el comando. Revisa la consola."));
            throw new RuntimeException(e);
        }
    }

}
