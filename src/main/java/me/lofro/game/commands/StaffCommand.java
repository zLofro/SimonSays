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

@CommandAlias("staffCommand")
@CommandPermission("admin.perm")
public class StaffCommand extends BaseCommand {

    private final GameManager gameManager;

    private final Connection connection;

    public StaffCommand(GameManager gameManager) {
        this.gameManager = gameManager;
        this.connection = Main.getInstance().getConnection();
    }

    @Subcommand("darRol")
    private void setRole(CommandSender sender, @Flags("other") Player player, Roles role) {
        try {
            sender.sendMessage(ChatColorFormatter.stringToString("&aSe ha cambiado el rol del jugador " + player.getName() + "&a a " + role.name() + "&a."));
            gameManager.setRole(connection, player.getUniqueId(), role);
        } catch (SQLException e) {
            Bukkit.getLogger().info(e.getMessage());
            sender.sendMessage("&cHa habido un error al ejecutar el comando. Revisa la consola.");
            throw new RuntimeException(e);
        }
    }

    @Subcommand("ubicación")
    @CommandCompletion(" @location")
    private void setActionLocation(CommandSender sender, Actions action, Location location) {
        try {
            gameManager.setActionLocation(connection, action, location);
            sender.sendMessage(ChatColorFormatter.stringToString("&aSe ha establecido la localización para la acción " + action.name() + "&a."));
        } catch (SQLException e) {
            Bukkit.getLogger().info(e.getMessage());
            sender.sendMessage("&cHa habido un error al ejecutar el comando. Revisa la consola.");
            throw new RuntimeException(e);
        }
    }

    @Subcommand("empezarRonda")
    private void startRound(CommandSender sender) {
        if (gameManager.isMidRound()) {
            sender.sendMessage(ChatColorFormatter.stringToString("&cYa hay una ronda iniciada."));
            return;
        }
        if (!gameManager.getOnlineMembers().isEmpty()) {
            gameManager.startRound();
            sender.sendMessage(ChatColorFormatter.stringToString("&a¡La ronda ha comenzado!"));
        } else {
            sender.sendMessage(ChatColorFormatter.stringToString("&cNo hay jugadores suficientes para comenzar una ronda."));
        }
    }

    @Subcommand("acabarRonda")
    private void endRound(CommandSender sender) {
        if (gameManager.isMidRound()) {
            gameManager.stopRound(true);
            sender.sendMessage(ChatColorFormatter.stringToString("&aHas acabado la ronda activa."));
        } else {
            sender.sendMessage(ChatColorFormatter.stringToString("&cNo hay ninguna ronda activa."));
        }
    }

    @Subcommand("añadirJugadorARonda")
    private void addPlayerToRound(CommandSender sender, @Flags("other") Player player) {
        if (gameManager.isMidRound()) {
            try {
                var playerRole = gameManager.getRole(connection, player.getUniqueId());
                if (playerRole != null && playerRole.equals(Roles.MEMBER)) {
                    if (gameManager.getCurrentRoundParticipants().contains(player.getUniqueId())) {
                        sender.sendMessage(ChatColorFormatter.stringToString("&c¡El jugador que quieres añadir ya está en la ronda!"));
                        return;
                    }
                    gameManager.getCurrentRoundParticipants().add(player.getUniqueId());
                    sender.sendMessage(ChatColorFormatter.stringToString("&aEl jugador se ha añadido a la ronda."));
                } else {
                    sender.sendMessage(ChatColorFormatter.stringToString("&cEl jugador no tiene el rango de miembro."));
                }
            } catch (SQLException e) {
                Bukkit.getLogger().info(e.getMessage());
                sender.sendMessage("&cHa habido un error al ejecutar el comando. Revisa la consola.");
                throw new RuntimeException(e);
            }
        } else {
            sender.sendMessage(ChatColorFormatter.stringToString("&cNo puedes añadir a alguien a la ronda si no hay ronda activa."));
        }
    }

    @Subcommand("eliminarJugadorDeRonda")
    private void removePlayerFromRound(CommandSender sender, @Flags("other") Player player) {
        if (gameManager.getCurrentPlayerUUID().equals(player.getUniqueId())) {
            sender.sendMessage(ChatColorFormatter.stringToString("&cEl jugador que tratas de eliminar de la ronda está jugando una acción ahora mismo. Espera a que termine."));
        }
        if (gameManager.isMidRound()) {
            if (!gameManager.getCurrentRoundParticipants().contains(player.getUniqueId())) {
                sender.sendMessage(ChatColorFormatter.stringToString("&c¡El jugador que quieres eliminar no está en la ronda!"));
                return;
            }
            gameManager.getCurrentRoundParticipants().remove(player.getUniqueId());
            sender.sendMessage(ChatColorFormatter.stringToString("&aEl jugador se ha eliminado de la ronda."));
        } else {
            sender.sendMessage(ChatColorFormatter.stringToString("&cNo puedes eliminar a alguien a la ronda si no hay ronda activa."));
        }
    }

}
