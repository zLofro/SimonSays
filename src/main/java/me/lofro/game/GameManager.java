package me.lofro.game;

import lombok.Getter;
import me.lofro.Main;
import me.lofro.game.listeners.GameListeners;
import me.lofro.utils.ChatColorFormatter;
import me.lofro.utils.ListenerUtils;
import me.lofro.utils.falseSpectator.FalseSpectator;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class GameManager {

    public GameManager(GameListeners gameListeners) {
        this.gameListeners = gameListeners;
    }

    private final GameListeners gameListeners;

    private @Getter Actions currentAction;

    private @Getter Player currentPlayer;

    private @Getter int taskLaterID;

    public void startAction(Actions action, Player player) {
        this.currentAction = action;
        this.currentPlayer = player;

        ListenerUtils.registerListener(gameListeners);

        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(action.getActionLocation());

        Bukkit.getOnlinePlayers().forEach(online -> {
            online.showTitle(Title.title(ChatColorFormatter.stringToComponent(action.getTitle()), ChatColorFormatter.stringToComponent(action.getSubtitle())));
            if (!online.getUniqueId().equals(player.getUniqueId())) {
                try {
                    var role = getRole(Main.getInstance().getConnection(), online.getUniqueId());
                    if (!(role != null && role.equals("MEMBER"))) return;
                } catch (SQLException e) {
                    Bukkit.getLogger().info(e.getMessage());
                    throw new RuntimeException(e);
                }

                FalseSpectator.addFalseSpectator(online);
                online.teleport(action.getActionLocation());
            }
        });

        this.taskLaterID = Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> endAction(false), action.getTimeLimit() * 20L).getTaskId();
    }

    public void endAction(boolean won) {
        this.currentAction = null;

        ListenerUtils.unregisterListener(gameListeners);

        currentPlayer.setGameMode(GameMode.ADVENTURE);
        currentPlayer.teleport(new Location(Bukkit.getWorlds().get(0), 0, 0, 0)); //TODO UBICACION DEL SPAWN
        Bukkit.getOnlinePlayers().forEach(online -> {
            if (!online.getUniqueId().equals(currentPlayer.getUniqueId())) {
                try {
                    var role = getRole(Main.getInstance().getConnection(), online.getUniqueId());
                    if (!(role != null && role.equals("MEMBER"))) return;
                } catch (SQLException e) {
                    Bukkit.getLogger().info(e.getMessage());
                    throw new RuntimeException(e);
                }

                FalseSpectator.removeFalseSpectator(online, GameMode.ADVENTURE);
                online.teleport(new Location(Bukkit.getWorlds().get(0), 0, 0, 0)); //TODO UBICACION DEL SPAWN
            }
        });

        var connection = Main.getInstance().getConnection();

        try {
            createPointsTable(connection);
        } catch (SQLException e) {
            Bukkit.getLogger().info(e.getMessage());
            throw new RuntimeException(e);
        }

        if (won) {
            try {
                var currentPlayerUUID = currentPlayer.getUniqueId();
                setPoints(connection, currentPlayerUUID, getPoints(connection, currentPlayerUUID) + 1);

                Bukkit.broadcast(ChatColorFormatter.stringToComponent("&aEl jugador " + currentPlayer.getName() + "&a ha conseguido +1 punto."));
            } catch (SQLException e) {
                Bukkit.getLogger().info(e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            Bukkit.broadcast(ChatColorFormatter.stringToComponent("&cNo se ha conseguido ningún punto."));
        }

        this.currentPlayer = null;
    }

    public void setPoints(Connection connection, UUID playerUUID, int points) throws SQLException {
        createPointsTable(connection);

        var preparedStatement = connection.prepareStatement("INSERT INTO Players(PlayerUUID, Points) VALUES(?,?)");

        preparedStatement.setString(1, playerUUID.toString());
        preparedStatement.setInt(2, points);

        preparedStatement.executeUpdate();
    }

    public int getPoints(Connection connection, UUID playerUUID) throws SQLException {
        createPointsTable(connection);

        var pointGetterStatement = connection.createStatement();
        ResultSet resultSet = pointGetterStatement.executeQuery("SELECT Points FROM Players");

        return resultSet.getInt(playerUUID.toString());
    }

    public void createPointsTable(Connection connection) throws SQLException {
        var statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS Players(" +
                "PlayerUUID VARCHAR(36) PRIMARY KEY," +
                "Points INTEGER);");
    }

    public void createRoleTable(Connection connection) throws SQLException {
        var statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS Roles(" +
                "PlayerUUID VARCHAR(36) PRIMARY KEY," +
                "PlayerRole VARCHAR(6));");
    }

    public void addStaff(Connection connection, UUID uuid) throws SQLException {
        createRoleTable(connection);

        var role = getRole(connection, uuid);

        if (role != null && role.equals("STAFF")) return;

        var preparedStatement = connection.prepareStatement("INSERT INTO Roles(PlayerUUID, PlayerRole) VALUES(?,?)");

        preparedStatement.setString(1, uuid.toString());
        preparedStatement.setString(2, "STAFF");

        preparedStatement.executeUpdate();
    }

    public void removeStaff(Connection connection, UUID uuid) throws SQLException {
        createRoleTable(connection);

        var role = getRole(connection, uuid);

        if (role != null && role.equals("STAFF")) {
            var preparedStatement = connection.prepareStatement("INSERT INTO Roles(PlayerUUID, PlayerRole) VALUES(?,?)");

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, null);

            preparedStatement.executeUpdate();
        }
    }

    public void addMember(Connection connection, UUID uuid) throws SQLException {
        createRoleTable(connection);

        var role = getRole(connection, uuid);

        if (role != null && role.equals("MEMBER")) return;

        var preparedStatement = connection.prepareStatement("INSERT INTO Roles(PlayerUUID, PlayerRole) VALUES(?,?)");

        preparedStatement.setString(1, uuid.toString());
        preparedStatement.setString(2, "MEMBER");

        preparedStatement.executeUpdate();
    }

    public void removeMember(Connection connection, UUID uuid) throws SQLException {
        createRoleTable(connection);

        var role = getRole(connection, uuid);

        if (role != null && role.equals("MEMBER")) {

            var preparedStatement = connection.prepareStatement("INSERT INTO Roles(PlayerUUID, PlayerRole) VALUES(?,?)");

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, null);

            preparedStatement.executeUpdate();
        }
    }

    public @Nullable String getRole(Connection connection, UUID uuid) throws SQLException {
        createRoleTable(connection);

        var pointGetterStatement = connection.createStatement();
        ResultSet resultSet = pointGetterStatement.executeQuery("SELECT PlayerRole FROM Roles");

        return resultSet.getString(uuid.toString());
    }

    public void setRole(Connection connection, UUID uuid, Roles role) throws SQLException {
        if (role.equals(Roles.MEMBER)) {
            removeStaff(connection, uuid);
            addMember(connection, uuid);
        } else {
            removeMember(connection, uuid);
            addStaff(connection, uuid);
        }
    }

}
