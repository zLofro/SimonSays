package me.lofro.game.listeners;

import me.lofro.Main;
import me.lofro.game.Actions;
import me.lofro.utils.DataContainer;
import me.lofro.utils.configuration.YMLConfig;
import me.lofro.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

public class GameListeners implements Listener {

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
                                    gameManager.endAction(true);
                                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> itemFrame.setItem(null), 60);
                                    return;
                                }
                            }
                        }
                        if (!itemInMainHand.getType().equals(Material.AIR) && itemInMainHand.hasItemMeta() && itemInOffHand.getType().equals(Material.AIR)) {
                            switch (itemInMainHandIB.getCustomModelData()) {
                                case 1, 2, 3, 4 -> {
                                    gameManager.endAction(true);
                                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> itemFrame.setItem(null), 60);
                                    return;
                                }
                            }
                        }
                        if (!itemInMainHand.getType().equals(Material.AIR) && itemInMainHand.hasItemMeta() && !itemInOffHand.getType().equals(Material.AIR) && itemInOffHand.hasItemMeta()) {
                            switch (itemInMainHandIB.getCustomModelData()) {
                                case 1, 2, 3, 4 -> {
                                    gameManager.endAction(true);
                                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> itemFrame.setItem(null), 60);
                                    return;
                                }
                            }
                            switch (itemInOffHandIB.getCustomModelData()) {
                                case 1, 2, 3, 4 -> {
                                    gameManager.endAction(true);
                                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> itemFrame.setItem(null), 60);
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
                                    gameManager.endAction(true);
                                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> itemFrame.setItem(null), 60);
                                    return;
                                }
                            }
                        }
                        if (!itemInMainHand.getType().equals(Material.AIR) && itemInMainHand.hasItemMeta() && itemInOffHand.getType().equals(Material.AIR)) {
                            switch (itemInMainHandIB.getCustomModelData()) {
                                case 5, 6, 7 -> {
                                    gameManager.endAction(true);
                                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> itemFrame.setItem(null), 60);
                                    return;
                                }
                            }
                        }
                        if (!itemInMainHand.getType().equals(Material.AIR) && itemInMainHand.hasItemMeta() && !itemInOffHand.getType().equals(Material.AIR) && itemInOffHand.hasItemMeta()) {
                            switch (itemInMainHandIB.getCustomModelData()) {
                                case 5, 6, 7 -> {
                                    gameManager.endAction(true);
                                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> itemFrame.setItem(null), 60);
                                    return;
                                }
                            }
                            switch (itemInOffHandIB.getCustomModelData()) {
                                case 5, 6, 7 -> {
                                    gameManager.endAction(true);
                                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> itemFrame.setItem(null), 60);
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
                                    gameManager.endAction(true);
                                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> itemFrame.setItem(null), 60);
                                    return;
                                }
                            }
                        }
                        if (!itemInMainHand.getType().equals(Material.AIR) && itemInMainHand.hasItemMeta() && itemInOffHand.getType().equals(Material.AIR)) {
                            switch (itemInMainHandIB.getCustomModelData()) {
                                case 8, 9, 10, 11, 12 -> {
                                    gameManager.endAction(true);
                                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> itemFrame.setItem(null), 60);
                                    return;
                                }
                            }
                        }
                        if (!itemInMainHand.getType().equals(Material.AIR) && itemInMainHand.hasItemMeta() && !itemInOffHand.getType().equals(Material.AIR) && itemInOffHand.hasItemMeta()) {
                            switch (itemInMainHandIB.getCustomModelData()) {
                                case 8, 9, 10, 11, 12 -> {
                                    gameManager.endAction(true);
                                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> itemFrame.setItem(null), 60);
                                    return;
                                }
                            }
                            switch (itemInOffHandIB.getCustomModelData()) {
                                case 8, 9, 10, 11, 12 -> {
                                    gameManager.endAction(true);
                                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> itemFrame.setItem(null), 60);
                                }
                            }
                        }
                    }
                } else if (gameManager.getCurrentAction().equals(Actions.THREE_ITEM_FRAMES)) {
                    if (itemFrame.getLocation().getBlockX() == YMLConfig.getInt("THREE_ITEM_FRAMES_FIRST_LOC_X")
                            && itemFrame.getLocation().getBlockY() == YMLConfig.getInt("THREE_ITEM_FRAMES_FIRST_LOC_Y")
                            && itemFrame.getLocation().getBlockZ() == YMLConfig.getInt("THREE_ITEM_FRAMES_FIRST_LOC_Z")
                            || itemFrame.getLocation().getBlockX() == YMLConfig.getInt("THREE_ITEM_FRAMES_SECOND_LOC_X")
                            && itemFrame.getLocation().getBlockY() == YMLConfig.getInt("THREE_ITEM_FRAMES_SECOND_LOC_Y")
                            && itemFrame.getLocation().getBlockZ() == YMLConfig.getInt("THREE_ITEM_FRAMES_SECOND_LOC_Z")
                            || itemFrame.getLocation().getBlockX() == YMLConfig.getInt("THREE_ITEM_FRAMES_SECOND_LOC_X")
                            && itemFrame.getLocation().getBlockY() == YMLConfig.getInt("THREE_ITEM_FRAMES_SECOND_LOC_Y")
                            && itemFrame.getLocation().getBlockZ() == YMLConfig.getInt("THREE_ITEM_FRAMES_SECOND_LOC_Z")) {
                        var itemInMainHand = player.getInventory().getItemInMainHand();
                        var itemInOffHand = player.getInventory().getItemInOffHand();

                        var itemInMainHandIB = new ItemBuilder(itemInMainHand);
                        var itemInOffHandIB = new ItemBuilder(itemInOffHand);

                        if (itemInMainHand.getType().equals(Material.AIR) && !itemInOffHand.getType().equals(Material.AIR) && itemInOffHand.hasItemMeta()) {
                            switch (itemInOffHandIB.getCustomModelData()) {
                                case 17, 18, 19 -> {
                                    if (itemFrame.getItem().getType().equals(Material.AIR)) return;
                                    var framedItems = DataContainer.get(player, "framedItems", PersistentDataType.BYTE);

                                    if (framedItems == null) {
                                        DataContainer.set(player, "framedItems", PersistentDataType.BYTE, (byte) 1);
                                    } else {
                                        DataContainer.set(player, "framedItems", PersistentDataType.BYTE, Byte.valueOf(String.valueOf(framedItems + 1)));
                                        if (framedItems + 1 >= 3) {
                                            gameManager.endAction(true);
                                            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> itemFrame.setItem(null), 60);
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
                                    if (itemFrame.getItem().getType().equals(Material.AIR)) return;
                                    var framedItems = DataContainer.get(player, "framedItems", PersistentDataType.BYTE);

                                    if (framedItems == null) {
                                        DataContainer.set(player, "framedItems", PersistentDataType.BYTE, (byte) 1);
                                    } else {
                                        DataContainer.set(player, "framedItems", PersistentDataType.BYTE, Byte.valueOf(String.valueOf(framedItems + 1)));
                                        if (framedItems + 1 >= 3) {
                                            gameManager.endAction(true);
                                            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> itemFrame.setItem(null), 60);
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
                                    if (itemFrame.getItem().getType().equals(Material.AIR)) return;
                                    var framedItems = DataContainer.get(player, "framedItems", PersistentDataType.BYTE);

                                    if (framedItems == null) {
                                        DataContainer.set(player, "framedItems", PersistentDataType.BYTE, (byte) 1);
                                    } else {
                                        DataContainer.set(player, "framedItems", PersistentDataType.BYTE, Byte.valueOf(String.valueOf(framedItems + 1)));
                                        if (framedItems + 1 >= 3) {
                                            gameManager.endAction(true);
                                            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> itemFrame.setItem(null), 60);
                                            DataContainer.set(player, "framedItems", PersistentDataType.BYTE, (byte) 0);
                                            return;
                                        }
                                    }
                                    return;
                                }
                            }
                            switch (itemInOffHandIB.getCustomModelData()) {
                                case 17, 18, 19 -> {
                                    if (itemFrame.getItem().getType().equals(Material.AIR)) return;
                                    var framedItems = DataContainer.get(player, "framedItems", PersistentDataType.BYTE);

                                    if (framedItems == null) {
                                        DataContainer.set(player, "framedItems", PersistentDataType.BYTE, (byte) 1);
                                    } else {
                                        DataContainer.set(player, "framedItems", PersistentDataType.BYTE, Byte.valueOf(String.valueOf(framedItems + 1)));
                                        if (framedItems + 1 >= 3) {
                                            gameManager.endAction(true);
                                            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> itemFrame.setItem(null), 60);
                                            DataContainer.set(player, "framedItems", PersistentDataType.BYTE, (byte) 0);
                                        }
                                    }
                                }
                            }
                        }
                    }
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
                if (clickedBlock.getLocation().getBlockX() == YMLConfig.getInt("PRESS_BUTTON_BUTTON_X")
                        && clickedBlock.getLocation().getBlockY() == YMLConfig.getInt("PRESS_BUTTON_BUTTON_Y")
                        && clickedBlock.getLocation().getBlockZ() == YMLConfig.getInt("PRESS_BUTTON_BUTTON_Z")) {
                    var itemFrameWorld = Bukkit.getWorld(YMLConfig.getString("PRESS_BUTTON_COMMON_LOC_WORLD_NAME"));

                    if (itemFrameWorld == null) throw new IllegalStateException("The itemFrameWorld cannot be null at this point.");

                    var itemFrameLoc = new Location(itemFrameWorld, YMLConfig.getInt("PRESS_BUTTON_FRAME_X"), YMLConfig.getInt("PRESS_BUTTON_FRAME_Y"), YMLConfig.getInt("PRESS_BUTTON_FRAME_Z"));
                    itemFrameWorld.getNearbyEntitiesByType(ItemFrame.class, itemFrameLoc, 1).forEach(itemFrame -> {
                        var itemFrameItem = itemFrame.getItem();
                        if (itemFrameItem.hasItemMeta() && new ItemBuilder(itemFrameItem).getCustomModelData() == 20) {
                            gameManager.endAction(true);
                            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> itemFrame.setItem(null), 60);
                        }
                    });
                }
            }
        }
    }

}
