package me.lofro.utils.falseSpectator;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.destroystokyo.paper.event.player.PlayerTeleportEndGatewayEvent;
import io.papermc.paper.event.player.PlayerNameEntityEvent;
import lombok.Getter;
import me.lofro.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class FalseSpectator implements Listener {

    private static final List<UUID> falseSpectators = new ArrayList<>();

     public static void addFalseSpectator(Player player) {
        falseSpectators.add(player.getUniqueId());
        player.setGameMode(GameMode.CREATIVE);
        Bukkit.getOnlinePlayers().forEach(online -> {
            if (!online.getUniqueId().equals(player.getUniqueId())) online.hidePlayer(Main.getInstance(), player);
        });
    }

    public static void removeFalseSpectator(Player player, GameMode nextGamemode) {
        falseSpectators.add(player.getUniqueId());
        player.setGameMode(nextGamemode);
        Bukkit.getOnlinePlayers().forEach(online -> {
            if (!online.getUniqueId().equals(player.getUniqueId())) online.showPlayer(Main.getInstance(), player);
        });
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        var player = event.getPlayer();
        if (falseSpectators.contains(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        var player = event.getPlayer();
        if (falseSpectators.contains(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        var player = event.getWhoClicked();
        if (falseSpectators.contains(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onInventoryDrag(InventoryDragEvent event) {
        var player = event.getWhoClicked();
        if (falseSpectators.contains(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onInventoryOpen(InventoryOpenEvent event) {
        var player = event.getPlayer();
        if (falseSpectators.contains(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onPortal(PlayerPortalEvent event) {
        var player = event.getPlayer();
        if (falseSpectators.contains(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        var player = event.getPlayer();
        if (falseSpectators.contains(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerGateway(PlayerTeleportEndGatewayEvent event) {
        var player = event.getPlayer();
        if (falseSpectators.contains(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerGateway(PlayerBedEnterEvent event) {
        var player = event.getPlayer();
        if (falseSpectators.contains(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerGateway(PlayerAttemptPickupItemEvent event) {
        var player = event.getPlayer();
        if (falseSpectators.contains(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerGateway(PlayerInteractAtEntityEvent event) {
        var player = event.getPlayer();
        if (falseSpectators.contains(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerGateway(PlayerNameEntityEvent event) {
        var player = event.getPlayer();
        if (falseSpectators.contains(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerGateway(PlayerDropItemEvent event) {
        var player = event.getPlayer();
        if (falseSpectators.contains(player.getUniqueId())) event.setCancelled(true);
    }

}
