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
import org.bukkit.event.hanging.HangingBreakEvent;
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
                                    gameManager.endAction(true, false);
                                    itemFrame.setItem(null);
                                    return;
                                }
                            }
                        }
                        if (!itemInMainHand.getType().equals(Material.AIR) && itemInMainHand.hasItemMeta() && itemInOffHand.getType().equals(Material.AIR)) {
                            switch (itemInMainHandIB.getCustomModelData()) {
                                case 1, 2, 3, 4 -> {
                                    gameManager.endAction(true, false);
                                    itemFrame.setItem(null);
                                    return;
                                }
                            }
                        }
                        if (!itemInMainHand.getType().equals(Material.AIR) && itemInMainHand.hasItemMeta() && !itemInOffHand.getType().equals(Material.AIR) && itemInOffHand.hasItemMeta()) {
                            switch (itemInMainHandIB.getCustomModelData()) {
                                case 1, 2, 3, 4 -> {
                                    gameManager.endAction(true, false);
                                    itemFrame.setItem(null);
                                    return;
                                }
                            }
                            switch (itemInOffHandIB.getCustomModelData()) {
                                case 1, 2, 3, 4 -> {
                                    gameManager.endAction(true, false);
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
                                    gameManager.endAction(true, false);
                                    itemFrame.setItem(null);
                                    return;
                                }
                            }
                        }
                        if (!itemInMainHand.getType().equals(Material.AIR) && itemInMainHand.hasItemMeta() && itemInOffHand.getType().equals(Material.AIR)) {
                            switch (itemInMainHandIB.getCustomModelData()) {
                                case 5, 6, 7 -> {
                                    gameManager.endAction(true, false);
                                    itemFrame.setItem(null);
                                    return;
                                }
                            }
                        }
                        if (!itemInMainHand.getType().equals(Material.AIR) && itemInMainHand.hasItemMeta() && !itemInOffHand.getType().equals(Material.AIR) && itemInOffHand.hasItemMeta()) {
                            switch (itemInMainHandIB.getCustomModelData()) {
                                case 5, 6, 7 -> {
                                    gameManager.endAction(true, false);
                                    itemFrame.setItem(null);
                                    return;
                                }
                            }
                            switch (itemInOffHandIB.getCustomModelData()) {
                                case 5, 6, 7 -> {
                                    gameManager.endAction(true, false);
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
                                    gameManager.endAction(true, false);
                                    itemFrame.setItem(null);
                                    return;
                                }
                            }
                        }
                        if (!itemInMainHand.getType().equals(Material.AIR) && itemInMainHand.hasItemMeta() && itemInOffHand.getType().equals(Material.AIR)) {
                            switch (itemInMainHandIB.getCustomModelData()) {
                                case 8, 9, 10, 11, 12 -> {
                                    gameManager.endAction(true, false);
                                    itemFrame.setItem(null);
                                    return;
                                }
                            }
                        }
                        if (!itemInMainHand.getType().equals(Material.AIR) && itemInMainHand.hasItemMeta() && !itemInOffHand.getType().equals(Material.AIR) && itemInOffHand.hasItemMeta()) {
                            switch (itemInMainHandIB.getCustomModelData()) {
                                case 8, 9, 10, 11, 12 -> {
                                    gameManager.endAction(true, false);
                                    itemFrame.setItem(null);
                                    return;
                                }
                            }
                            switch (itemInOffHandIB.getCustomModelData()) {
                                case 8, 9, 10, 11, 12 -> {
                                    gameManager.endAction(true, false);
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
                                            gameManager.endAction(true, false);
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
                                            gameManager.endAction(true, false);
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
                                            gameManager.endAction(true, false);
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
                                            gameManager.endAction(true, false);
                                            itemFrame.setItem(null);
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
                            gameManager.endAction(itemFrameItem.hasItemMeta() && new ItemBuilder(itemFrameItem).getCustomModelData() == 20, false);
                        }
                    });
                }
            }
        }
    }

    @EventHandler
    private void onHangingBreak(HangingBreakEvent event) {
        if (event.getEntity() instanceof ItemFrame && Main.getInstance().getGameManager().isMidRound()) event.setCancelled(true);
    }

}
