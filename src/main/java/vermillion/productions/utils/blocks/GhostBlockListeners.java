package vermillion.productions.utils.blocks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import vermillion.productions.Main;

import java.util.HashMap;

public class GhostBlockListeners implements Listener {

    private static final Main plugin = Main.getInstance();

    @EventHandler
    public void onInteractEvent(PlayerInteractEvent event) {
        if(event.getInteractionPoint() == null) { return; }
        var blockLoc = GhostBlock.deserializeLocation(event.getInteractionPoint());

        var locMap = GhostBlock.getLocationMap();
        var map = GhostBlock.getMap();
        var mapT = new HashMap<Location, Block>();

        locMap.keySet().forEach(s -> mapT.put(GhostBlock.serializeLocation(locMap.get(s)), s));

        var loc = GhostBlock.serializeLocation(blockLoc);
        loc.setY(loc.getY()-1);

        if(locMap.containsValue(blockLoc) || locMap.containsValue(GhostBlock.deserializeLocation(loc))) {
            var player = event.getPlayer();

            Bukkit.getScheduler().runTaskLater(plugin, ()-> player.sendBlockChange(GhostBlock.serializeLocation(blockLoc), Bukkit.createBlockData(map.get(mapT.get(GhostBlock.serializeLocation(blockLoc))))), 1L);
        }

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        var map = GhostBlock.getMap();

        Bukkit.getScheduler().runTaskLater(plugin, ()-> {
            for(var b : map.keySet()) {
                player.sendBlockChange(b.getLocation(), Bukkit.createBlockData(map.get(b)));
            }
        }, 5L);
    }
}
