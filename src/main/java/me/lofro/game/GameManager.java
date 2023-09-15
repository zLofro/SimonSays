package me.lofro.game;

import lombok.Getter;
import me.lofro.Main;
import me.lofro.game.listeners.GameListeners;
import me.lofro.utils.ChatColorFormatter;
import me.lofro.utils.ListenerUtils;
import me.lofro.utils.configuration.YMLConfig;
import me.lofro.utils.falseSpectator.FalseSpectator;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

    private final Location spawnLocation = new Location(Bukkit.getWorld(YMLConfig.getString("spawnWorldName")), YMLConfig.getInt("spawnX"), YMLConfig.getInt("spawnY"), YMLConfig.getInt("spawnZ"));

    public void startAction(Actions action, Player player) {
        this.currentAction = action;
        this.currentPlayer = player;

        ListenerUtils.registerListener(gameListeners);

        Location actionLocation;
        try {
            actionLocation = getActionLocation(Main.getInstance().getConnection(), action);
        } catch (SQLException e) {
            Bukkit.getLogger().info(e.getMessage());
            throw new RuntimeException(e);
        }

        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(actionLocation);

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
                online.teleport(actionLocation);
            }
        });

        this.taskLaterID = Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> endAction(false), action.getTimeLimit() * 20L).getTaskId();
    }

    public void endAction(boolean won) {
        ListenerUtils.unregisterListener(gameListeners);

        this.currentAction = null;

        currentPlayer.setGameMode(GameMode.ADVENTURE);
        currentPlayer.teleport(spawnLocation);
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
                online.teleport(spawnLocation);
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

                Bukkit.getOnlinePlayers().forEach(online -> online.showTitle(Title.title(ChatColorFormatter.stringToComponent("&a" + currentPlayer.getName() + "&a ha conseguido +1 punto."), ChatColorFormatter.stringToComponent(""))));
            } catch (SQLException e) {
                Bukkit.getLogger().info(e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            Bukkit.getOnlinePlayers().forEach(online -> online.showTitle(Title.title(ChatColorFormatter.stringToComponent("&cNo se han obtenido puntos."), ChatColorFormatter.stringToComponent(""))));
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

    public void createLocationTable(Connection connection) throws SQLException {
        var statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS ActionLocations(" +
                "ActionName VARCHAR(16) PRIMARY KEY," +
                "WorldName VARCHAR(16))," +
                "X INTEGER," +
                "Y INTEGER," +
                "Z INTEGER);");
    }

    public void setActionLocation(Connection connection, Actions action, Location location) throws SQLException {
        createRoleTable(connection);

        var preparedStatement = connection.prepareStatement("INSERT INTO ActionLocations(ActionName, WorldName, X, Y, Z) VALUES(?,?,?,?,?)");

        preparedStatement.setString(1, action.name());
        preparedStatement.setString(2, location.getWorld().getName());
        preparedStatement.setInt(3, location.getBlockX());
        preparedStatement.setInt(4, location.getBlockY());
        preparedStatement.setInt(5, location.getBlockZ());

        preparedStatement.executeUpdate();
    }

    public Location getActionLocation(Connection connection, Actions action) throws SQLException {
        createLocationTable(connection);

        var getActionLocationStatement = connection.prepareStatement("SELECT WorldName, X, Y, Z FROM ActionLocations WHERE ActionName LIKE ?");

        getActionLocationStatement.setString(1, action.name());

        ResultSet resultSet = getActionLocationStatement.executeQuery();

        return new Location(Bukkit.getWorld(resultSet.getString(1)), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4));
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

        PreparedStatement pointGetterStatement = connection.prepareStatement("SELECT PlayerRole FROM Roles WHERE PlayerUUID LIKE ?");

        pointGetterStatement.setString(1, uuid.toString());

        ResultSet resultSet = pointGetterStatement.executeQuery();

        return resultSet.getString(1);
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
