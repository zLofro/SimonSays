package me.lofro.game;

import lombok.Getter;
import me.lofro.Main;
import me.lofro.game.listeners.GameListeners;
import me.lofro.utils.ChatColorFormatter;
import me.lofro.utils.DataContainer;
import me.lofro.utils.ListenerUtils;
import me.lofro.utils.configuration.YMLConfig;
import me.lofro.utils.falseSpectator.FalseSpectator;
import me.lofro.utils.item.ItemBuilder;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class GameManager {

    public GameManager(GameListeners gameListeners) {
        this.gameListeners = gameListeners;
    }

    private final GameListeners gameListeners;

    private @Getter Actions currentAction;

    private @Getter UUID currentPlayerUUID;

    private @Getter int taskLaterID;

    private @Getter List<UUID> currentRoundParticipants = new ArrayList<>();

    private int totalActionsThisRound = 0;

    private int currentActionsThisRound = 0;

    private boolean isMidRound = false;

    private final Location spawnLocation = new Location(Bukkit.getWorld(YMLConfig.getString("spawnWorldName")), YMLConfig.getInt("spawnX"), YMLConfig.getInt("spawnY"), YMLConfig.getInt("spawnZ"));

    public void startRound() {
        if (isMidRound) {
            if (currentActionsThisRound >= totalActionsThisRound) {
                stopRound(false);
            } else {
                var player = Bukkit.getPlayer(currentRoundParticipants.get(currentActionsThisRound));
                if (player == null) {
                    currentActionsThisRound++;
                    startRound();
                    return;
                }
                startAction(Actions.values()[ThreadLocalRandom.current().nextInt(0, Actions.values().length)], player);
            }
        } else {
            this.currentRoundParticipants = getOnlineMembers();
            this.totalActionsThisRound = currentRoundParticipants.size();

            var player = Bukkit.getPlayer(currentRoundParticipants.get(currentActionsThisRound));
            if (player == null) {
                currentActionsThisRound++;
                startRound();
                return;
            }

            this.isMidRound = true;

            startAction(Actions.values()[ThreadLocalRandom.current().nextInt(0, Actions.values().length)], player);
        }
    }

    public void stopRound(boolean commandMade) {
        Bukkit.getScheduler().cancelTask(this.taskLaterID);
        if (commandMade) endAction(false, true);
        this.currentRoundParticipants.clear();
        this.totalActionsThisRound = 0;
        this.currentActionsThisRound = 0;
        this.isMidRound = false;
        this.taskLaterID = 0;
    }

    public void startAction(Actions action, Player player) {
        this.currentActionsThisRound++;

        this.currentAction = action;
        this.currentPlayerUUID = player.getUniqueId();

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
                            if (!(role != null && role.equals(Roles.MEMBER))) return;
                        } catch (SQLException e) {
                            Bukkit.getLogger().info(e.getMessage());
                    throw new RuntimeException(e);
                }

                FalseSpectator.addFalseSpectator(online);
                online.teleport(actionLocation);
            }
        });

        switch(action) {
            case PAPER_ITEM_FRAME -> {
                var customPaper = new ItemBuilder(Material.PAPER).setCustomModelData(ThreadLocalRandom.current().nextInt(1, 5));
                player.getInventory().addItem(customPaper.build());
            }
            case PAPER_ITEM_FRAME2 -> {
                var customPaper = new ItemBuilder(Material.PAPER).setCustomModelData(ThreadLocalRandom.current().nextInt(5, 8));
                player.getInventory().addItem(customPaper.build());
            }
            case PAPER_ITEM_FRAME_SELECTION -> {
                var customPaper = new ItemBuilder(Material.PAPER).setCustomModelData(ThreadLocalRandom.current().nextInt(8, 13));
                player.getInventory().addItem(customPaper.build());
            }
            case THREE_ITEM_FRAMES -> {
                var customPaper1 = new ItemBuilder(Material.PAPER).setCustomModelData(17);
                var customPaper2 = new ItemBuilder(Material.PAPER).setCustomModelData(18);
                var customPaper3 = new ItemBuilder(Material.PAPER).setCustomModelData(19);

                player.getInventory().addItem(customPaper1.build());
                player.getInventory().addItem(customPaper2.build());
                player.getInventory().addItem(customPaper3.build());
            }
            case PRESS_BUTTON -> Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                var itemFrameWorld = Bukkit.getWorld(YMLConfig.getString("PRESS_BUTTON_COMMON_LOC_WORLD_NAME"));

                if (itemFrameWorld == null) throw new IllegalStateException("The itemFrameWorld cannot be null at this point.");

                var itemFrameLoc = new Location(itemFrameWorld, YMLConfig.getInt("PRESS_BUTTON_FRAME_X"), YMLConfig.getInt("PRESS_BUTTON_FRAME_Y"), YMLConfig.getInt("PRESS_BUTTON_FRAME_Z"));
                var itemFrames = itemFrameLoc.getNearbyEntities(2, 2, 2);

                itemFrames.forEach(entity -> {
                    if (entity instanceof ItemFrame itemFrame) {
                        var item = new ItemBuilder(Material.PAPER).setCustomModelData(ThreadLocalRandom.current().nextInt(20, 22)).build();
                        itemFrame.setItem(item);
                    }
                });
            }, 5);
        }

        this.taskLaterID = Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> endAction(false, false), ThreadLocalRandom.current().nextInt(5, 11) * 20L).getTaskId();
    }

    public void endAction(boolean won, boolean commandMade) {
        Bukkit.getScheduler().cancelTask(this.taskLaterID);

        ListenerUtils.unregisterListener(gameListeners);

        var currentPlayer = Bukkit.getPlayer(currentPlayerUUID);

        var connection = Main.getInstance().getConnection();

        Bukkit.getOnlinePlayers().forEach(online -> {
            if (!online.getUniqueId().equals(currentPlayerUUID)) {
                try {
                    var role = getRole(Main.getInstance().getConnection(), online.getUniqueId());
                    if (!(role != null && role.equals(Roles.MEMBER))) return;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                FalseSpectator.removeFalseSpectator(online, GameMode.ADVENTURE);
                online.teleport(spawnLocation);
            }
        });

        switch(currentAction) {
            case THREE_ITEM_FRAMES -> {
                var commonWorld = Bukkit.getWorld(YMLConfig.getString("THREE_ITEM_FRAMES_COMMON_LOC_WORLD_NAME"));

                var firstItemFrameLoc = new Location(commonWorld, YMLConfig.getInt("THREE_ITEM_FRAMES_FIRST_LOC_X"), YMLConfig.getInt("THREE_ITEM_FRAMES_FIRST_LOC_Y"), YMLConfig.getInt("THREE_ITEM_FRAMES_FIRST_LOC_Z"));
                var secondItemFrameLoc = new Location(commonWorld, YMLConfig.getInt("THREE_ITEM_FRAMES_SECOND_LOC_X"), YMLConfig.getInt("THREE_ITEM_FRAMES_SECOND_LOC_Y"), YMLConfig.getInt("THREE_ITEM_FRAMES_SECOND_LOC_Z"));
                var thirdItemFrameLoc = new Location(commonWorld, YMLConfig.getInt("THREE_ITEM_FRAMES_THIRD_LOC_X"), YMLConfig.getInt("THREE_ITEM_FRAMES_THIRD_LOC_Y"), YMLConfig.getInt("THREE_ITEM_FRAMES_THIRD_LOC_Z"));

                firstItemFrameLoc.getNearbyEntitiesByType(ItemFrame.class, 1).forEach(itemFrame -> itemFrame.setItem(null));
                secondItemFrameLoc.getNearbyEntitiesByType(ItemFrame.class, 1).forEach(itemFrame -> itemFrame.setItem(null));
                thirdItemFrameLoc.getNearbyEntitiesByType(ItemFrame.class, 1).forEach(itemFrame -> itemFrame.setItem(null));

                DataContainer.set(currentPlayer, "framedItems", PersistentDataType.BYTE, (byte) 0);
            }
            case PRESS_BUTTON -> {
                var commonWorld = Bukkit.getWorld(YMLConfig.getString("PRESS_BUTTON_COMMON_LOC_WORLD_NAME"));

                var itemFrameLoc = new Location(commonWorld, YMLConfig.getInt("PRESS_BUTTON_FRAME_X"), YMLConfig.getInt("PRESS_BUTTON_FRAME_Y"), YMLConfig.getInt("PRESS_BUTTON_FRAME_Z"));

                itemFrameLoc.getNearbyEntitiesByType(ItemFrame.class, 1).forEach(itemFrame -> itemFrame.setItem(null));
            }
            case PAPER_ITEM_FRAME_SELECTION -> {
                var commonWorld = Bukkit.getWorld(YMLConfig.getString("PAPER_ITEM_FRAME_SELECTION_COMMON_LOC_WORLD_NAME"));

                var firstItemFrameLoc = new Location(commonWorld, YMLConfig.getInt("PAPER_ITEM_FRAME_SELECTION_CORRECT_LOC_X"), YMLConfig.getInt("PAPER_ITEM_FRAME_SELECTION_CORRECT_LOC_Y"), YMLConfig.getInt("PAPER_ITEM_FRAME_SELECTION_CORRECT_LOC_Z"));
                var secondItemFrameLoc = new Location(commonWorld, YMLConfig.getInt("PAPER_ITEM_FRAME_SELECTION_TRICK_LOC_X"), YMLConfig.getInt("PAPER_ITEM_FRAME_SELECTION_TRICK_LOC_Y"), YMLConfig.getInt("PAPER_ITEM_FRAME_SELECTION_TRICK_LOC_Z"));
                var thirdItemFrameLoc = new Location(commonWorld, YMLConfig.getInt("PAPER_ITEM_FRAME_SELECTION_TRICK2_LOC_X"), YMLConfig.getInt("PAPER_ITEM_FRAME_SELECTION_TRICK2_LOC_Y"), YMLConfig.getInt("PAPER_ITEM_FRAME_SELECTION_TRICK2_LOC_Z"));

                firstItemFrameLoc.getNearbyEntitiesByType(ItemFrame.class, 1).forEach(itemFrame -> itemFrame.setItem(null));
                secondItemFrameLoc.getNearbyEntitiesByType(ItemFrame.class, 1).forEach(itemFrame -> itemFrame.setItem(null));
                thirdItemFrameLoc.getNearbyEntitiesByType(ItemFrame.class, 1).forEach(itemFrame -> itemFrame.setItem(null));
            }
        }

        if (currentPlayer != null) {
            currentPlayer.setGameMode(GameMode.ADVENTURE);
            currentPlayer.teleport(spawnLocation);

            currentPlayer.getInventory().clear();

            if (won) {
                try {
                    setPoints(connection, currentPlayerUUID, getPoints(connection, currentPlayerUUID) + 1);

                    Bukkit.getOnlinePlayers().forEach(online -> online.showTitle(Title.title(ChatColorFormatter.stringToComponent("&a" + currentPlayer.getName() + "&a ha conseguido +1 punto."), ChatColorFormatter.stringToComponent(""))));
                } catch (SQLException e) {
                    Bukkit.getLogger().info(e.getMessage());
                    throw new RuntimeException(e);
                }
            } else {
                Bukkit.getOnlinePlayers().forEach(online -> online.showTitle(Title.title(ChatColorFormatter.stringToComponent("&cNo se han obtenido puntos."), ChatColorFormatter.stringToComponent(""))));
            }
        }

        this.currentAction = null;
        this.currentPlayerUUID = null;
        if (!commandMade) startRound();
    }

    public void setPoints(Connection connection, UUID playerUUID, int points) throws SQLException {
        createPointsTable(connection);

        var preparedStatement = connection.prepareStatement("INSERT OR REPLACE INTO Players(PlayerUUID, Points) VALUES(?,?)");

        preparedStatement.setString(1, playerUUID.toString());
        preparedStatement.setInt(2, points);

        preparedStatement.executeUpdate();
    }

    public int getPoints(Connection connection, UUID playerUUID) throws SQLException {
        createPointsTable(connection);

        var pointGetterStatement = connection.prepareStatement("SELECT Points FROM Players WHERE PlayerUUID LIKE ?");
        pointGetterStatement.setString(1, playerUUID.toString());
        ResultSet resultSet = pointGetterStatement.executeQuery();

        if (!resultSet.next()) return 0;

        return resultSet.getInt(1);
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
        statement.execute("CREATE TABLE IF NOT EXISTS ActionLocations (" +
                "ActionName VARCHAR(16) PRIMARY KEY," +
                "WorldName VARCHAR(16)," +
                "X INTEGER," +
                "Y INTEGER," +
                "Z INTEGER);");
    }

    public void setActionLocation(Connection connection, Actions action, Location location) throws SQLException {
        createLocationTable(connection);

        var setDataStatement = connection.prepareStatement("INSERT OR REPLACE INTO ActionLocations(ActionName, WorldName, X, Y, Z) VALUES(?,?,?,?,?)");

        setDataStatement.setString(1, action.name());
        setDataStatement.setString(2, location.getWorld().getName());
        setDataStatement.setInt(3, location.getBlockX());
        setDataStatement.setInt(4, location.getBlockY());
        setDataStatement.setInt(5, location.getBlockZ());

        setDataStatement.executeUpdate();
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

        if (role != null && role.equals(Roles.STAFF)) return;

        var preparedStatement = connection.prepareStatement("INSERT OR REPLACE INTO Roles(PlayerUUID, PlayerRole) VALUES(?,?)");

        preparedStatement.setString(1, uuid.toString());
        preparedStatement.setString(2, "STAFF");

        preparedStatement.executeUpdate();
    }

    public void removeStaff(Connection connection, UUID uuid) throws SQLException {
        createRoleTable(connection);

        var role = getRole(connection, uuid);

        if (role != null && role.equals(Roles.STAFF)) {
            var preparedStatement = connection.prepareStatement("INSERT OR REPLACE INTO Roles(PlayerUUID, PlayerRole) VALUES(?,?)");

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, null);

            preparedStatement.executeUpdate();
        }
    }

    public void addMember(Connection connection, UUID uuid) throws SQLException {
        createRoleTable(connection);

        var role = getRole(connection, uuid);

        if (role != null && role.equals(Roles.MEMBER)) return;

        var preparedStatement = connection.prepareStatement("INSERT OR REPLACE INTO Roles(PlayerUUID, PlayerRole) VALUES(?,?)");

        preparedStatement.setString(1, uuid.toString());
        preparedStatement.setString(2, "MEMBER");

        preparedStatement.executeUpdate();
    }

    public void removeMember(Connection connection, UUID uuid) throws SQLException {
        createRoleTable(connection);

        var role = getRole(connection, uuid);

        if (role != null && role.equals(Roles.MEMBER)) {

            var preparedStatement = connection.prepareStatement("INSERT OR REPLACE INTO Roles(PlayerUUID, PlayerRole) VALUES(?,?)");

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, null);

            preparedStatement.executeUpdate();
        }
    }

    public List<UUID> getOnlineMembers() {
        List<UUID> onlineMembers = new ArrayList<>();

        Bukkit.getOnlinePlayers().forEach(player -> {
            try {
                var role = getRole(Main.getInstance().getConnection(), player.getUniqueId());
                if (role != null && role.equals(Roles.MEMBER)) {
                    onlineMembers.add(player.getUniqueId());
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        return onlineMembers;
    }

    public @Nullable Roles getRole(Connection connection, UUID uuid) throws SQLException {
        createRoleTable(connection);

        PreparedStatement pointGetterStatement = connection.prepareStatement("SELECT PlayerRole FROM Roles WHERE PlayerUUID LIKE ?");

        pointGetterStatement.setString(1, uuid.toString());

        ResultSet resultSet = pointGetterStatement.executeQuery();

        var stringRole = resultSet.getString(1);

        return stringRole == null ? null : Roles.valueOf(stringRole);
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

    public boolean isMidRound() {
        return isMidRound;
    }

}
