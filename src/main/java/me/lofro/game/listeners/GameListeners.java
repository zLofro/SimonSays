package me.lofro.game.listeners;

import lombok.Getter;
import me.lofro.Main;
import me.lofro.game.Actions;
import me.lofro.utils.DataContainer;
import me.lofro.utils.configuration.YMLConfig;
import me.lofro.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
public class GameListeners implements Listener {

    private int taskID = 0;
    private int applyingPressureTicks = 0;

    private @Getter Material lastConcreteType = null;

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        var gameManager = Main.getInstance().getGameManager();
        if (gameManager.isMidRound()) event.setCancelled(true);
    }

    @EventHandler
    private void onDamageByEntity(EntityDamageByEntityEvent event) {
        var gameManager = Main.getInstance().getGameManager();
        if (gameManager.isMidRound()) event.setCancelled(true);
    }

    @EventHandler
    private void onInteractAtEntity(PlayerInteractAtEntityEvent event) {
        var player = event.getPlayer();
        var entity = event.getRightClicked();
        var gameManager = Main.getInstance().getGameManager();

        if (player.getUniqueId().equals(gameManager.getCurrentPlayerUUID())) {
            if (entity instanceof ItemFrame itemFrame) {
                if (gameManager.getCurrentAction().equals(Actions.PAPER_ITEM_FRAME)) {
                    if (itemFrame.getLocation().getBlockX() == YMLConfig.getInt("PAPER_ITEM_FRAME_X")
                            && itemFrame.getLocation().getBlockY() == YMLConfig.getInt("PAPER_ITEM_FRAME_Y")
                            && itemFrame.getLocation().getBlockZ() == YMLConfig.getInt("PAPER_ITEM_FRAME_Z")) {
                        var itemInMainHand = player.getInventory().getItemInMainHand();
                        var itemInOffHand = player.getInventory().getItemInOffHand();

                        var itemInMainHandIB = new ItemBuilder(itemInMainHand);
                        var itemInOffHandIB = new ItemBuilder(itemInOffHand);

                        if (itemInMainHand.getType().equals(Material.AIR) && !itemInOffHand.getType().equals(Material.AIR) && itemInOffHand.hasItemMeta()) {
                            switch (itemInOffHandIB.getCustomModelData()) {
                                case 1, 2, 3, 4 -> {
                                    gameManager.endAction(true, false, false);
                                    itemFrame.setItem(null);
                                    return;
                                }
                            }
                        }
                        if (!itemInMainHand.getType().equals(Material.AIR) && itemInMainHand.hasItemMeta() && itemInOffHand.getType().equals(Material.AIR)) {
                            switch (itemInMainHandIB.getCustomModelData()) {
                                case 1, 2, 3, 4 -> {
                                    gameManager.endAction(true, false, false);
                                    itemFrame.setItem(null);
                                    return;
                                }
                            }
                        }
                        if (!itemInMainHand.getType().equals(Material.AIR) && itemInMainHand.hasItemMeta() && !itemInOffHand.getType().equals(Material.AIR) && itemInOffHand.hasItemMeta()) {
                            switch (itemInMainHandIB.getCustomModelData()) {
                                case 1, 2, 3, 4 -> {
                                    gameManager.endAction(true, false, false);
                                    itemFrame.setItem(null);
                                    return;
                                }
                            }
                            switch (itemInOffHandIB.getCustomModelData()) {
                                case 1, 2, 3, 4 -> {
                                    gameManager.endAction(true, false, false);
                                    itemFrame.setItem(null);
                                }
                            }
                        }
                    }
                } else if (gameManager.getCurrentAction().equals(Actions.PAPER_ITEM_FRAME2)) {
                    if (itemFrame.getLocation().getBlockX() == YMLConfig.getInt("PAPER_ITEM_FRAME2_X")
                            && itemFrame.getLocation().getBlockY() == YMLConfig.getInt("PAPER_ITEM_FRAME2_Y")
                            && itemFrame.getLocation().getBlockZ() == YMLConfig.getInt("PAPER_ITEM_FRAME2_Z")) {
                        var itemInMainHand = player.getInventory().getItemInMainHand();
                        var itemInOffHand = player.getInventory().getItemInOffHand();

                        var itemInMainHandIB = new ItemBuilder(itemInMainHand);
                        var itemInOffHandIB = new ItemBuilder(itemInOffHand);

                        if (itemInMainHand.getType().equals(Material.AIR) && !itemInOffHand.getType().equals(Material.AIR) && itemInOffHand.hasItemMeta()) {
                            switch (itemInOffHandIB.getCustomModelData()) {
                                case 5, 6, 7 -> {
                                    gameManager.endAction(true, false, false);
                                    itemFrame.setItem(null);
                                    return;
                                }
                            }
                        }
                        if (!itemInMainHand.getType().equals(Material.AIR) && itemInMainHand.hasItemMeta() && itemInOffHand.getType().equals(Material.AIR)) {
                            switch (itemInMainHandIB.getCustomModelData()) {
                                case 5, 6, 7 -> {
                                    gameManager.endAction(true, false, false);
                                    itemFrame.setItem(null);
                                    return;
                                }
                            }
                        }
                        if (!itemInMainHand.getType().equals(Material.AIR) && itemInMainHand.hasItemMeta() && !itemInOffHand.getType().equals(Material.AIR) && itemInOffHand.hasItemMeta()) {
                            switch (itemInMainHandIB.getCustomModelData()) {
                                case 5, 6, 7 -> {
                                    gameManager.endAction(true, false, false);
                                    itemFrame.setItem(null);
                                    return;
                                }
                            }
                            switch (itemInOffHandIB.getCustomModelData()) {
                                case 5, 6, 7 -> {
                                    gameManager.endAction(true, false, false);
                                    itemFrame.setItem(null);
                                }
                            }
                        }
                    }
                } else if (gameManager.getCurrentAction().equals(Actions.PAPER_ITEM_FRAME_SELECTION)) {
                    if (itemFrame.getLocation().getBlockX() == YMLConfig.getInt("PAPER_ITEM_FRAME_SELECTION_CORRECT_LOC_X")
                            && itemFrame.getLocation().getBlockY() == YMLConfig.getInt("PAPER_ITEM_FRAME_SELECTION_CORRECT_LOC_Y")
                            && itemFrame.getLocation().getBlockZ() == YMLConfig.getInt("PAPER_ITEM_FRAME_SELECTION_CORRECT_LOC_Z")) {
                        var itemInMainHand = player.getInventory().getItemInMainHand();
                        var itemInOffHand = player.getInventory().getItemInOffHand();

                        var itemInMainHandIB = new ItemBuilder(itemInMainHand);
                        var itemInOffHandIB = new ItemBuilder(itemInOffHand);

                        if (itemInMainHand.getType().equals(Material.AIR) && !itemInOffHand.getType().equals(Material.AIR) && itemInOffHand.hasItemMeta()) {
                            switch (itemInOffHandIB.getCustomModelData()) {
                                case 8, 9, 10, 11, 12 -> {
                                    gameManager.endAction(true, false, false);
                                    itemFrame.setItem(null);
                                    return;
                                }
                            }
                        }
                        if (!itemInMainHand.getType().equals(Material.AIR) && itemInMainHand.hasItemMeta() && itemInOffHand.getType().equals(Material.AIR)) {
                            switch (itemInMainHandIB.getCustomModelData()) {
                                case 8, 9, 10, 11, 12 -> {
                                    gameManager.endAction(true, false, false);
                                    itemFrame.setItem(null);
                                    return;
                                }
                            }
                        }
                        if (!itemInMainHand.getType().equals(Material.AIR) && itemInMainHand.hasItemMeta() && !itemInOffHand.getType().equals(Material.AIR) && itemInOffHand.hasItemMeta()) {
                            switch (itemInMainHandIB.getCustomModelData()) {
                                case 8, 9, 10, 11, 12 -> {
                                    gameManager.endAction(true, false, false);
                                    itemFrame.setItem(null);
                                    return;
                                }
                            }
                            switch (itemInOffHandIB.getCustomModelData()) {
                                case 8, 9, 10, 11, 12 -> {
                                    gameManager.endAction(true, false, false);
                                    itemFrame.setItem(null);
                                }
                            }
                        }
                    }
                } else if (gameManager.getCurrentAction().equals(Actions.THREE_ITEM_FRAMES)) {
                    if ((itemFrame.getLocation().getBlockX() == YMLConfig.getInt("THREE_ITEM_FRAMES_FIRST_LOC_X")
                            && itemFrame.getLocation().getBlockY() == YMLConfig.getInt("THREE_ITEM_FRAMES_FIRST_LOC_Y")
                            && itemFrame.getLocation().getBlockZ() == YMLConfig.getInt("THREE_ITEM_FRAMES_FIRST_LOC_Z"))
                            || (itemFrame.getLocation().getBlockX() == YMLConfig.getInt("THREE_ITEM_FRAMES_SECOND_LOC_X")
                            && itemFrame.getLocation().getBlockY() == YMLConfig.getInt("THREE_ITEM_FRAMES_SECOND_LOC_Y")
                            && itemFrame.getLocation().getBlockZ() == YMLConfig.getInt("THREE_ITEM_FRAMES_SECOND_LOC_Z"))
                            || (itemFrame.getLocation().getBlockX() == YMLConfig.getInt("THREE_ITEM_FRAMES_THIRD_LOC_X")
                            && itemFrame.getLocation().getBlockY() == YMLConfig.getInt("THREE_ITEM_FRAMES_THIRD_LOC_Y")
                            && itemFrame.getLocation().getBlockZ() == YMLConfig.getInt("THREE_ITEM_FRAMES_THIRD_LOC_Z"))) {

                        var itemInMainHand = player.getInventory().getItemInMainHand();
                        var itemInOffHand = player.getInventory().getItemInOffHand();

                        var itemInMainHandIB = new ItemBuilder(itemInMainHand);
                        var itemInOffHandIB = new ItemBuilder(itemInOffHand);

                        if (itemInMainHand.getType().equals(Material.AIR) && !itemInOffHand.getType().equals(Material.AIR) && itemInOffHand.hasItemMeta()) {
                            switch (itemInOffHandIB.getCustomModelData()) {
                                case 17, 18, 19 -> {
                                    if (!itemFrame.getItem().getType().equals(Material.AIR)) return;
                                    var framedItems = DataContainer.get(player, "framedItems", PersistentDataType.BYTE);

                                    if (framedItems == null) {
                                        DataContainer.set(player, "framedItems", PersistentDataType.BYTE, (byte) 1);
                                    } else {
                                        var framedItemsCount = ((byte)(framedItems + 1));
                                        DataContainer.set(player, "framedItems", PersistentDataType.BYTE, framedItemsCount);
                                        if (framedItemsCount >= (byte) 3) {
                                            gameManager.endAction(true, false, false);
                                            itemFrame.setItem(null);
                                            DataContainer.set(player, "framedItems", PersistentDataType.BYTE, (byte) 0);
                                            return;
                                        }
                                    }
                                    return;
                                }
                            }
                        }
                        if (!itemInMainHand.getType().equals(Material.AIR) && itemInMainHand.hasItemMeta() && itemInOffHand.getType().equals(Material.AIR)) {
                            switch (itemInMainHandIB.getCustomModelData()) {
                                case 17, 18, 19 -> {
                                    if (!itemFrame.getItem().getType().equals(Material.AIR)) return;
                                    var framedItems = DataContainer.get(player, "framedItems", PersistentDataType.BYTE);

                                    if (framedItems == null) {
                                        DataContainer.set(player, "framedItems", PersistentDataType.BYTE, (byte) 1);
                                    } else {
                                        var framedItemsCount = ((byte)(framedItems + 1));
                                        DataContainer.set(player, "framedItems", PersistentDataType.BYTE, framedItemsCount);
                                        if (framedItemsCount >= (byte) 3) {
                                            gameManager.endAction(true, false, false);
                                            itemFrame.setItem(null);
                                            DataContainer.set(player, "framedItems", PersistentDataType.BYTE, (byte) 0);
                                            return;
                                        }
                                    }
                                    return;
                                }
                            }
                        }
                        if (!itemInMainHand.getType().equals(Material.AIR) && itemInMainHand.hasItemMeta() && !itemInOffHand.getType().equals(Material.AIR) && itemInOffHand.hasItemMeta()) {
                            switch (itemInMainHandIB.getCustomModelData()) {
                                case 17, 18, 19 -> {
                                    if (!itemFrame.getItem().getType().equals(Material.AIR)) return;
                                    var framedItems = DataContainer.get(player, "framedItems", PersistentDataType.BYTE);

                                    if (framedItems == null) {
                                        DataContainer.set(player, "framedItems", PersistentDataType.BYTE, (byte) 1);
                                    } else {
                                        var framedItemsCount = ((byte)(framedItems + 1));
                                        DataContainer.set(player, "framedItems", PersistentDataType.BYTE, framedItemsCount);
                                        if (framedItemsCount >= (byte) 3) {
                                            gameManager.endAction(true, false, false);
                                            itemFrame.setItem(null);
                                            DataContainer.set(player, "framedItems", PersistentDataType.BYTE, (byte) 0);
                                            return;
                                        }
                                    }
                                    return;
                                }
                            }
                            switch (itemInOffHandIB.getCustomModelData()) {
                                case 17, 18, 19 -> {
                                    if (!itemFrame.getItem().getType().equals(Material.AIR)) return;
                                    var framedItems = DataContainer.get(player, "framedItems", PersistentDataType.BYTE);

                                    if (framedItems == null) {
                                        DataContainer.set(player, "framedItems", PersistentDataType.BYTE, (byte) 1);
                                    } else {
                                        var framedItemsCount = ((byte)(framedItems + 1));
                                        DataContainer.set(player, "framedItems", PersistentDataType.BYTE, framedItemsCount);
                                        if (framedItemsCount >= (byte) 3) {
                                            gameManager.endAction(true, false, false);
                                            itemFrame.setItem(null);
                                            DataContainer.set(player, "framedItems", PersistentDataType.BYTE, (byte) 0);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (gameManager.isMidRound()) event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        var player = event.getPlayer();
        var gameManager = Main.getInstance().getGameManager();

        if (!player.getUniqueId().equals(gameManager.getCurrentPlayerUUID())) return;

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (gameManager.getCurrentAction().equals(Actions.PRESS_BUTTON)) {
                var clickedBlock = event.getClickedBlock();
                if (clickedBlock == null) return;
                if (!clickedBlock.getType().equals(Material.STONE_BUTTON)) return;
                if (clickedBlock.getLocation().getBlockX() == YMLConfig.getInt("PRESS_BUTTON_BUTTON_X")
                        && clickedBlock.getLocation().getBlockY() == YMLConfig.getInt("PRESS_BUTTON_BUTTON_Y")
                        && clickedBlock.getLocation().getBlockZ() == YMLConfig.getInt("PRESS_BUTTON_BUTTON_Z")) {
                    var itemFrameWorld = Bukkit.getWorld(YMLConfig.getString("PRESS_BUTTON_COMMON_LOC_WORLD_NAME"));

                    if (itemFrameWorld == null) throw new IllegalStateException("The itemFrameWorld cannot be null at this point.");

                    var itemFrameLoc = new Location(itemFrameWorld, YMLConfig.getInt("PRESS_BUTTON_FRAME_X"), YMLConfig.getInt("PRESS_BUTTON_FRAME_Y"), YMLConfig.getInt("PRESS_BUTTON_FRAME_Z"));
                    itemFrameLoc.getNearbyEntities(2, 2, 2).forEach(entity -> {
                        if (entity instanceof ItemFrame itemFrame) {
                            var itemFrameItem = itemFrame.getItem();
                            gameManager.endAction(itemFrameItem.hasItemMeta() && new ItemBuilder(itemFrameItem).getCustomModelData() == 20, false, false);
                        }
                    });
                } else if (clickedBlock.getLocation().getBlockX() == YMLConfig.getInt("PRESS_BUTTON_BUTTON2_X")
                        && clickedBlock.getLocation().getBlockY() == YMLConfig.getInt("PRESS_BUTTON_BUTTON2_Y")
                        && clickedBlock.getLocation().getBlockZ() == YMLConfig.getInt("PRESS_BUTTON_BUTTON2_Z")) {
                    var itemFrameWorld = Bukkit.getWorld(YMLConfig.getString("PRESS_BUTTON_COMMON_LOC_WORLD_NAME"));

                    if (itemFrameWorld == null) throw new IllegalStateException("The itemFrameWorld cannot be null at this point.");

                    var itemFrameLoc = new Location(itemFrameWorld, YMLConfig.getInt("PRESS_BUTTON_FRAME_X"), YMLConfig.getInt("PRESS_BUTTON_FRAME_Y"), YMLConfig.getInt("PRESS_BUTTON_FRAME_Z"));
                    itemFrameLoc.getNearbyEntities(2, 2, 2).forEach(entity -> {
                        if (entity instanceof ItemFrame itemFrame) {
                            var itemFrameItem = itemFrame.getItem();
                            gameManager.endAction(itemFrameItem.hasItemMeta() && new ItemBuilder(itemFrameItem).getCustomModelData() == 21, false, false);
                        }
                    });
                }
            } else if (gameManager.getCurrentAction().equals(Actions.BUTTON_MOVEMENT)) {
                var clickedBlock = event.getClickedBlock();
                if (clickedBlock == null) return;
                if (clickedBlock.getType().equals(Material.STONE_BUTTON)) {
                    var commonWorld = Bukkit.getWorld(YMLConfig.getString("BUTTON_MOVEMENT_COMMON_LOC_WORLD_NAME"));

                    if (commonWorld == null) throw new IllegalStateException("The common world is null. It MUSTN'T be null at this point.");

                    var currentButtonLoc = new Location(commonWorld, YMLConfig.getInt("BUTTON_MOVEMENT_" + gameManager.getCurrentButtonIndex() + "_X"), YMLConfig.getInt("BUTTON_MOVEMENT_" + gameManager.getCurrentButtonIndex() + "_Y"), YMLConfig.getInt("BUTTON_MOVEMENT_" + gameManager.getCurrentButtonIndex() + "_Z"));

                    if ((clickedBlock.getLocation().getBlockX() == currentButtonLoc.getBlockX()) && (clickedBlock.getLocation().getBlockY() == currentButtonLoc.getBlockY()) && (clickedBlock.getLocation().getBlockZ() == currentButtonLoc.getBlockZ())) {
                        gameManager.endAction(true, false, false);
                        clickedBlock.setType(Material.AIR);
                    }
                }
            } else {
                if (gameManager.isMidRound()) event.setCancelled(true);
            }
        } else {
            if (gameManager.isMidRound()) event.setCancelled(true);
        }
    }

    @EventHandler
    private void onMove(PlayerMoveEvent event) {
        var player = event.getPlayer();
        var gameManager = Main.getInstance().getGameManager();

        if (!player.getUniqueId().equals(gameManager.getCurrentPlayerUUID())) return;

        if (gameManager.getCurrentAction().equals(Actions.PRESSURE_PLATE)) {
            var commonWorldLoc = Bukkit.getWorld(YMLConfig.getString("PRESSURE_PLATE_COMMON_LOC_WORLD_NAME"));

            if (commonWorldLoc == null) return;

            var correctLocation = new Location(commonWorldLoc, YMLConfig.getInt("PRESSURE_PLATE_" + gameManager.getPressurePlateType() + "_X"),
                    YMLConfig.getInt("PRESSURE_PLATE_" + gameManager.getPressurePlateType() + "_Y"), YMLConfig.getInt("PRESSURE_PLATE_" + gameManager.getPressurePlateType() + "_Z"));
            if (player.getLocation().getBlockX() == correctLocation.getBlockX() && player.getLocation().getBlockY() == correctLocation.getBlockY() && player.getLocation().getBlockZ() == correctLocation.getBlockZ()) {

                if (this.taskID == 0) {
                    this.taskID = new BukkitRunnable() {

                        @Override
                        public void run() {
                            applyingPressureTicks++;
                            player.setExp((float) Math.max(0d, Math.min(1d, (double) applyingPressureTicks / 60)));
                            if (applyingPressureTicks >= 60) {
                                applyingPressureTicks = 0;
                                gameManager.endAction(true, false, false);
                                player.setExp(0);
                                taskID = 0;
                                this.cancel();
                            }
                        }

                    }.runTaskTimer(Main.getInstance(), 0, 1).getTaskId();
                }
            } else {
                Bukkit.getScheduler().cancelTask(taskID);
                player.setExp(0);
                this.applyingPressureTicks = 0;
                this.taskID = 0;

                var firstFakeType = gameManager.getPressureTypesShuffle().get(0);
                var secondFakeType = gameManager.getPressureTypesShuffle().get(1);

                var firstFakeLocation = new Location(commonWorldLoc, YMLConfig.getInt("PRESSURE_PLATE_" + firstFakeType + "_X"),
                        YMLConfig.getInt("PRESSURE_PLATE_" + firstFakeType + "_Y"), YMLConfig.getInt("PRESSURE_PLATE_" + firstFakeType + "_Z"));

                var secondFakeLocation = new Location(commonWorldLoc, YMLConfig.getInt("PRESSURE_PLATE_" + secondFakeType + "_X"),
                        YMLConfig.getInt("PRESSURE_PLATE_" + secondFakeType + "_Y"), YMLConfig.getInt("PRESSURE_PLATE_" + secondFakeType + "_Z"));

                if ((player.getLocation().getBlockX() == firstFakeLocation.getBlockX() && player.getLocation().getBlockY() == firstFakeLocation.getBlockY() && player.getLocation().getBlockZ() == firstFakeLocation.getBlockZ())
                        || (player.getLocation().getBlockX() == secondFakeLocation.getBlockX() && player.getLocation().getBlockY() == secondFakeLocation.getBlockY() && player.getLocation().getBlockZ() == secondFakeLocation.getBlockZ())) {
                    gameManager.endAction(false, false, false);
                }
            }
        } else if (gameManager.getCurrentAction().equals(Actions.FLOOR_IS_LAVA)) {
            var playerLoc = player.getLocation();

            this.lastConcreteType = playerLoc.getBlock().getRelative(BlockFace.DOWN).getType();
        }

    }

    @EventHandler
    private void onHangingBreak(HangingBreakEvent event) {
        if (event.getEntity() instanceof ItemFrame && Main.getInstance().getGameManager().isMidRound()) event.setCancelled(true);
    }

}
