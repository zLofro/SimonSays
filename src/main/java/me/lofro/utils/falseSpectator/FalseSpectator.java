package me.lofro.utils.falseSpectator;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.destroystokyo.paper.event.player.PlayerTeleportEndGatewayEvent;
import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import io.papermc.paper.event.player.PlayerNameEntityEvent;
import lombok.Getter;
import me.lofro.Main;
import me.lofro.utils.ChatColorFormatter;
import me.lofro.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;

import java.util.*;

@Getter
public class FalseSpectator implements Listener {

    private static final Map<UUID, Integer> falseSpectators = new HashMap<>();

     public static void addFalseSpectator(Player player) {
        falseSpectators.put(player.getUniqueId(), Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
            var currentPlayer = Bukkit.getPlayer(Main.getInstance().getGameManager().getCurrentPlayerUUID());
            if (currentPlayer == null) return;
            player.sendActionBar(ChatColorFormatter.stringToComponent("&f&lJugador: " + currentPlayer.getName()));
        }, 0, 10).getTaskId());
        player.getInventory().setItemInMainHand(new ItemBuilder(Material.STRUCTURE_VOID).setDisplayName(ChatColorFormatter.stringToString("&cEspera a tu turno.")).build());
         player.getInventory().setItemInOffHand(new ItemBuilder(Material.STRUCTURE_VOID).setDisplayName(ChatColorFormatter.stringToString("&cEspera a tu turno.")).build());
        player.setGameMode(GameMode.CREATIVE);
        Bukkit.getOnlinePlayers().forEach(online -> {
            if (!online.getUniqueId().equals(player.getUniqueId())) online.hidePlayer(Main.getInstance(), player);
        });
    }

    public static void removeFalseSpectator(Player player, GameMode nextGamemode) {
         var taskID = falseSpectators.get(player.getUniqueId());
         if (taskID != null) Bukkit.getScheduler().cancelTask(taskID);
        falseSpectators.remove(player.getUniqueId());
        player.getInventory().remove(Material.STRUCTURE_VOID);
        player.getEquipment().setItemInOffHand(null);
        player.setGameMode(nextGamemode);
        Bukkit.getOnlinePlayers().forEach(online -> {
            if (!online.getUniqueId().equals(player.getUniqueId())) online.showPlayer(Main.getInstance(), player);
        });
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        var player = event.getPlayer();
        if (falseSpectators.containsKey(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onDamageByEntity(EntityDamageByEntityEvent event) {
         if (event.getDamager() instanceof Player player) {
             if (falseSpectators.containsKey(player.getUniqueId())) event.setCancelled(true);
         }
    }

    @EventHandler
    private void onCreative(InventoryCreativeEvent event)  {
         if (falseSpectators.containsKey(event.getWhoClicked().getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        var player = event.getPlayer();
        if (falseSpectators.containsKey(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onInteractAtEntity(PlayerInteractAtEntityEvent event) {
        var player = event.getPlayer();
        if (falseSpectators.containsKey(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        var player = event.getWhoClicked();
        if (falseSpectators.containsKey(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onInventoryDrag(InventoryDragEvent event) {
        var player = event.getWhoClicked();
        if (falseSpectators.containsKey(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onInventoryOpen(InventoryOpenEvent event) {
        var player = event.getPlayer();
        if (falseSpectators.containsKey(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onPortal(PlayerPortalEvent event) {
        var player = event.getPlayer();
        if (falseSpectators.containsKey(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        var player = event.getPlayer();
        if (falseSpectators.containsKey(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerGateway(PlayerTeleportEndGatewayEvent event) {
        var player = event.getPlayer();
        if (falseSpectators.containsKey(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerBedEnter(PlayerBedEnterEvent event) {
        var player = event.getPlayer();
        if (falseSpectators.containsKey(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerPickup(PlayerAttemptPickupItemEvent event) {
        var player = event.getPlayer();
        if (falseSpectators.containsKey(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerNameEntity(PlayerNameEntityEvent event) {
        var player = event.getPlayer();
        if (falseSpectators.containsKey(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerDropItem(PlayerDropItemEvent event) {
        var player = event.getPlayer();
        if (falseSpectators.containsKey(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerItemFrame(PlayerItemFrameChangeEvent event) {
        var player = event.getPlayer();
        if (falseSpectators.containsKey(player.getUniqueId())) event.setCancelled(true);
    }
}
