package me.lofro.game;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Getter;
import me.lofro.Main;
import me.lofro.game.listeners.GameListeners;
import me.lofro.utils.ChatColorFormatter;
import me.lofro.utils.DataContainer;
import me.lofro.utils.ListenerUtils;
import me.lofro.utils.Locations;
import me.lofro.utils.configuration.YMLConfig;
import me.lofro.utils.falseSpectator.FalseSpectator;
import me.lofro.utils.item.ItemBuilder;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

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

    private @Getter List<String> pressureTypesShuffle = new ArrayList<>();

    private @Getter String pressurePlateType = "";

    private @Getter List<Location> colorBlocksLocations = new ArrayList<>();

    private @Getter Material correctColorType = null;

    private @Getter int buttonTaskID = 0;

    private @Getter int currentButtonIndex = 0;

    private final CacheLoader<UUID, Integer> playerPointsLoader = new CacheLoader<>() {
        @Override
        public @NotNull Integer load(@NotNull UUID key) {
            try {
                return getPoints(Main.getInstance().getConnection(), key);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    };

    private final @Getter LoadingCache<UUID, Integer> playerPoints = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES).build(playerPointsLoader);

    private final CacheLoader<Roles, List<UUID>> membersLoader = new CacheLoader<>() {
        @Override
        public @NotNull List<UUID> load(@NotNull Roles key) {
            try {
                return getMembers(Main.getInstance().getConnection());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    };

    private final @Getter LoadingCache<Roles, List<UUID>> members = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES).build(membersLoader);

    public void startRound(Actions action) {
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
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> startAction(action == null ? Actions.values()[ThreadLocalRandom.current().nextInt(0, Actions.values().length)] : action, player), 40);
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

            startAction(action == null ? Actions.values()[ThreadLocalRandom.current().nextInt(0, Actions.values().length)] : action, player);
        }
    }

    public void startRound() {
        this.startRound(null);
    }

    public void stopRound(boolean commandMade) {
        Bukkit.getScheduler().cancelTask(this.taskLaterID);
        if (commandMade) endAction(false, true, false);
        this.currentRoundParticipants.clear();
        this.totalActionsThisRound = 0;
        this.currentActionsThisRound = 0;
        this.isMidRound = false;
        this.taskLaterID = 0;
    }

    public void startAction(@NotNull Actions action, Player player) {
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

        player.teleport(actionLocation);
        player.setGameMode(GameMode.ADVENTURE);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);

        Bukkit.getLogger().info(action.name());

        switch(action) {
            case PAPER_ITEM_FRAME -> {
                var customPaper = new ItemBuilder(Material.PAPER).setCustomModelData(ThreadLocalRandom.current().nextInt(1, 5));

                switch (customPaper.getCustomModelData()) {
                    case 1 -> customPaper.setDisplayName(ChatColorFormatter.stringToString("&f&lSUPER CHEESE QUESO"));
                    case 2 -> customPaper.setDisplayName(ChatColorFormatter.stringToString("&f&lSUPER CHEESE PEPPERONI"));
                    case 3 -> customPaper.setDisplayName(ChatColorFormatter.stringToString("&f&lSUPER CHEESE 3 MEAT"));
                    case 4 -> customPaper.setDisplayName(ChatColorFormatter.stringToString("&f&lSUPER CHEESE SUPREME"));
                }

                player.getInventory().addItem(customPaper.build());
            }
            case PAPER_ITEM_FRAME2 -> {
                var customPaper1 = new ItemBuilder(Material.PAPER).setCustomModelData(5).setDisplayName(ChatColorFormatter.stringToString("#FF6637&lCRAZY SAUCE"));
                var customPaper2 = new ItemBuilder(Material.PAPER).setCustomModelData(6).setDisplayName(ChatColorFormatter.stringToString("#FFD737&lBUFFALO DIP"));
                var customPaper3 = new ItemBuilder(Material.PAPER).setCustomModelData(7).setDisplayName(ChatColorFormatter.stringToString("#FFFFFF&lRANCH DIP"));

                switch(ThreadLocalRandom.current().nextInt(0, 3)) {
                    case 0 ->{
                        player.getInventory().setItem(0, customPaper1.build());
                        player.getInventory().setItem(1, customPaper2.build());
                        player.getInventory().setItem(2, customPaper3.build());
                    }
                    case 1 -> {
                        player.getInventory().setItem(1, customPaper1.build());
                        player.getInventory().setItem(2, customPaper2.build());
                        player.getInventory().setItem(0, customPaper3.build());
                    }
                    case 2 -> {
                        player.getInventory().setItem(2, customPaper1.build());
                        player.getInventory().setItem(1, customPaper3.build());
                        player.getInventory().setItem(0, customPaper2.build());
                    }
                }
            }
            case PAPER_ITEM_FRAME_SELECTION -> {
                var random = ThreadLocalRandom.current().nextInt(8, 13);
                var customPaper = new ItemBuilder(Material.PAPER).setCustomModelData(random);

                switch (random) {
                    case 8 -> customPaper.setDisplayName(ChatColorFormatter.stringToString("&f&lSUPER CHEESE QUESO"));
                    case 9 -> customPaper.setDisplayName(ChatColorFormatter.stringToString("&f&lSUPER CHEESE PEPPERONI"));
                    case 10 -> customPaper.setDisplayName(ChatColorFormatter.stringToString("&f&lSUPER CHEESE 3 MEAT"));
                    case 11 -> customPaper.setDisplayName(ChatColorFormatter.stringToString("&f&lSUPER CHEESE SUPREME"));
                    case 12 -> customPaper.setDisplayName(ChatColorFormatter.stringToString("&f&lULTIMATE SUPREME"));
                }

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
            case PRESSURE_PLATE -> {
                this.pressureTypesShuffle = new ArrayList<>(List.of("OAK", "JUNGLE", "BIRCH"));
                var randomNumber = ThreadLocalRandom.current().nextInt(0, 3);
                this.pressurePlateType = pressureTypesShuffle.get(randomNumber);
                pressureTypesShuffle.remove(randomNumber);
            }
            case FLOOR_IS_LAVA -> {
                var commonWorld = Bukkit.getWorld(YMLConfig.getString("FLOOR_IS_LAVA_COMMON_LOC_WORLD_NAME"));

                if (commonWorld == null) throw new IllegalStateException("The common world is null. It MUSTN'T be null at this point.");

                var firstCornerLoc = new Location(commonWorld, YMLConfig.getInt("FLOOR_IS_LAVA_FIRST_CORNER_X"), YMLConfig.getInt("FLOOR_IS_LAVA_FIRST_CORNER_Y"), YMLConfig.getInt("FLOOR_IS_LAVA_FIRST_CORNER_Z"));
                var secondCornerLoc = new Location(commonWorld, YMLConfig.getInt("FLOOR_IS_LAVA_SECOND_CORNER_X"), YMLConfig.getInt("FLOOR_IS_LAVA_SECOND_CORNER_Y"), YMLConfig.getInt("FLOOR_IS_LAVA_SECOND_CORNER_Z"));

                this.colorBlocksLocations = Locations.getBlocksInsideCube(firstCornerLoc, secondCornerLoc);

                List<Material> pickedTypes = new ArrayList<>();

                colorBlocksLocations.forEach(blockPos -> {
                    var type = this.concreteMaterials.get(ThreadLocalRandom.current().nextInt(0, concreteMaterials.size()));
                    blockPos.getBlock().setType(type);
                    if (!pickedTypes.contains(type)) pickedTypes.add(type);
                });

                this.correctColorType = pickedTypes.get(ThreadLocalRandom.current().nextInt(0, pickedTypes.size()));
            }
            case BUTTON_MOVEMENT -> {
                var commonWorld = Bukkit.getWorld(YMLConfig.getString("BUTTON_MOVEMENT_COMMON_LOC_WORLD_NAME"));

                if (commonWorld == null) throw new IllegalStateException("The common world is null. It MUSTN'T be null at this point.");

                for (int i = 0; i < 7; i++) {
                    var currentButtonLoc = new Location(commonWorld, YMLConfig.getInt("BUTTON_MOVEMENT_" + i + "_X"), YMLConfig.getInt("BUTTON_MOVEMENT_" + i + "_Y"), YMLConfig.getInt("BUTTON_MOVEMENT_" + i + "_Z"));
                    currentButtonLoc.getBlock().setType(Material.AIR);
                }

                this.buttonTaskID = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
                    var oldButtonIndex = currentButtonIndex;

                    var aux = currentButtonIndex;

                    currentButtonIndex = (aux + 1) > 7 ? 0 : currentButtonIndex + 1;

                    var oldButtonPos = new Location(commonWorld, YMLConfig.getInt("BUTTON_MOVEMENT_" + oldButtonIndex + "_X"), YMLConfig.getInt("BUTTON_MOVEMENT_" + oldButtonIndex + "_Y"), YMLConfig.getInt("BUTTON_MOVEMENT_" + oldButtonIndex + "_Z"));
                    oldButtonPos.getBlock().setType(Material.AIR);

                    var currentButtonLoc = new Location(commonWorld, YMLConfig.getInt("BUTTON_MOVEMENT_" + currentButtonIndex + "_X"), YMLConfig.getInt("BUTTON_MOVEMENT_" + currentButtonIndex + "_Y"), YMLConfig.getInt("BUTTON_MOVEMENT_" + currentButtonIndex + "_Z"));

                    var currentButton = currentButtonLoc.getBlock();
                    currentButton.setType(Material.STONE_BUTTON);

                    try {
                        var blockFace = BlockFace.valueOf(YMLConfig.getString("buttonBlockFace"));

                        currentButtonLoc.getChunk().load();
                        Directional data = (Directional)currentButton.getBlockData();
                        data.setFacing(blockFace);
                        currentButton.setBlockData(data);
                    } catch (IllegalArgumentException ex) {
                        Bukkit.getLogger().info(ChatColorFormatter.stringToString("&cDEBES DE CONFIGURAR EL BLOCKFACE DEL MOVIMIENTO DE BOTONES."));
                    }
                }, 10, 20).getTaskId();
            }
        }

        Bukkit.getOnlinePlayers().forEach(online -> {
            online.showTitle(Title.title(ChatColorFormatter.stringToComponent(action.getTitle()), ChatColorFormatter.stringToComponent(!action.equals(Actions.PRESSURE_PLATE) ? action.equals(Actions.FLOOR_IS_LAVA) ? YMLConfig.getString("correctBlockAnnounceMessage") + getBlockName(correctColorType) : action.getSubtitle() : YMLConfig.getString("correctPressurePlateMessage") + getBlockName(Material.getMaterial(pressurePlateType + "_PRESSURE_PLATE")))));
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

        this.taskLaterID = Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> endAction(false, false, action.equals(Actions.FLOOR_IS_LAVA)), !action.equals(Actions.FLOOR_IS_LAVA) ? ThreadLocalRandom.current().nextInt(YMLConfig.getInt("minSecsPerAction"), YMLConfig.getInt("maxSecsPerAction")) * 20L : ThreadLocalRandom.current().nextInt(4, 8) * 20L).getTaskId();
    }

    public void endAction(boolean won, boolean commandMade, boolean isLava) {
        var currentPlayer = Bukkit.getPlayer(currentPlayerUUID);

        var connection = Main.getInstance().getConnection();

        if (!isLava) {
            Bukkit.getScheduler().cancelTask(this.taskLaterID);

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

            switch (currentAction) {
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
                case PRESSURE_PLATE -> {
                    Bukkit.getScheduler().cancelTask(gameListeners.getTaskID());
                    if (currentPlayer != null) currentPlayer.setExp(0);
                    this.pressureTypesShuffle.clear();
                    this.pressurePlateType = "";
                }
                case BUTTON_MOVEMENT -> {
                    Bukkit.getScheduler().cancelTask(this.buttonTaskID);

                    var commonWorld = Bukkit.getWorld(YMLConfig.getString("BUTTON_MOVEMENT_COMMON_LOC_WORLD_NAME"));

                    if (commonWorld == null) throw new IllegalStateException("The common world is null. It MUSTN'T be null at this point.");

                    for (int i = 0; i < 7; i++) {
                        var currentButtonLoc = new Location(commonWorld, YMLConfig.getInt("BUTTON_MOVEMENT_" + i + "_X"), YMLConfig.getInt("BUTTON_MOVEMENT_" + i + "_Y"), YMLConfig.getInt("BUTTON_MOVEMENT_" + i + "_Z"));
                        currentButtonLoc.getBlock().setType(Material.AIR);
                    }

                    this.currentButtonIndex = 0;
                    this.buttonTaskID = 0;
                }
            }

            ListenerUtils.unregisterListener(gameListeners);

            if (currentPlayer != null) {
                currentPlayer.setGameMode(GameMode.ADVENTURE);
                currentPlayer.teleport(spawnLocation);

                currentPlayer.getInventory().clear();

                if (won) {
                    try {
                        var newPoints = playerPoints.get(currentPlayerUUID) + 1;
                        setPoints(connection, currentPlayerUUID, newPoints);
                        playerPoints.put(currentPlayerUUID, newPoints);

                        Bukkit.getOnlinePlayers().forEach(online -> online.showTitle(Title.title(ChatColorFormatter.stringToComponent(YMLConfig.getString("winTitlePointsMessage").replace("{jugador}", currentPlayer.getName())), ChatColorFormatter.stringToComponent(YMLConfig.getString("winSubtitlePointsMessage").replace("{jugador}", currentPlayer.getName())))));
                    } catch (SQLException | ExecutionException e) {
                        Bukkit.getLogger().info(e.getMessage());
                        throw new RuntimeException(e);
                    }
                } else {
                    Bukkit.getOnlinePlayers().forEach(online -> online.showTitle(Title.title(ChatColorFormatter.stringToComponent(YMLConfig.getString("looseTitleMessage").replace("{jugador}", currentPlayer.getName())), ChatColorFormatter.stringToComponent(YMLConfig.getString("looseSubtitleMessage").replace("{jugador}", currentPlayer.getName())))));
                }
            }

            this.currentAction = null;
            this.currentPlayerUUID = null;
            if (!commandMade) startRound();
        } else {
            if (currentPlayer == null) return;
            if (gameListeners.getLastConcreteType().equals(correctColorType)) {
                this.colorBlocksLocations.forEach(blockPos -> {
                    if (!blockPos.equals(currentPlayer.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation())) blockPos.getBlock().setType(Material.AIR);
                });
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> endAction(true, false, false), 30);
            } else {
                this.colorBlocksLocations.forEach(blockPos -> blockPos.getBlock().setType(Material.AIR));
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> endAction(false, false, false), 30);
            }

            this.colorBlocksLocations = new ArrayList<>();
            this.correctColorType = null;
        }
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

    public List<UUID> getTop() throws SQLException, ExecutionException {
        Map<UUID, Integer> topPoints = new HashMap<>();

        members.get(Roles.MEMBER).forEach(uuid -> {
            try {
                topPoints.put(uuid, playerPoints.get(uuid));
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        return sortByValue(topPoints).keySet().stream().toList();
    }

    public List<UUID> getMembers(Connection connection) throws SQLException {
        createRoleTable(connection);

        PreparedStatement pointGetterStatement = connection.prepareStatement("SELECT PlayerUUID FROM Roles WHERE PlayerRole LIKE ?");

        pointGetterStatement.setString(1, Roles.MEMBER.name());

        ResultSet resultSet = pointGetterStatement.executeQuery();

        ResultSetMetaData resultMetadata = resultSet.getMetaData();
        int columnCount = resultMetadata.getColumnCount();

        List<UUID> UUIDList = new ArrayList<>();
        while(resultSet.next()) {
            int i = 1;
            while (i <= columnCount) {
                UUIDList.add(UUID.fromString(resultSet.getString(i++)));
            }
        }

        return UUIDList;
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

    private final List<Material> concreteMaterials = new ArrayList<>(List.of(Material.RED_WOOL, Material.ORANGE_WOOL, Material.YELLOW_WOOL, Material.GREEN_WOOL));

    private String getBlockName(Material material) {
        var blockName = YMLConfig.getString(material.name());
        return (blockName != null) ? blockName : material.name();
    }

    private <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

}
