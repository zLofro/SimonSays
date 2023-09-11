package vermillion.productions.utils.blocks;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;
import vermillion.productions.Main;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Agus5534
 * @version 1.0
 */
public class GhostBlock {
    private static Main plugin = Main.getInstance();
    private static final @Getter Map<Block, Material> map = new HashMap<>();
    private static final @Getter Map<Block, String> locationMap = new HashMap<>();

    public static void placeBlock(Material material, Location location, long seconds) {
        var originalBlock = location.getBlock();
        originalBlock.setMetadata("ghost", new FixedMetadataValue(plugin,   true));

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, ()-> {
            Bukkit.getOnlinePlayers().forEach(p -> p.sendBlockChange(location, Bukkit.createBlockData(material)));

            map.put(originalBlock, material);
            locationMap.put(originalBlock, deserializeLocation(originalBlock.getLocation()));

            Bukkit.getScheduler().runTaskLater(plugin, ()-> removeBlock(originalBlock), seconds * 20L);
        }, 2L);
    }

    public static void removeBlock(Block block) {
        Bukkit.getOnlinePlayers().forEach(p -> p.sendBlockChange(block.getLocation(), block.getBlockData()));
        block.removeMetadata("ghost", plugin);
        map.remove(block);
        locationMap.remove(block);
    }

    public static String deserializeLocation(Location location) {
        return String.format("%s,%s,%s,%s", location.getWorld().getName(), (int)location.getX(), (int)location.getY(), (int)location.getZ());
    }

    public static Location serializeLocation(String s) {
        var stringArray = s.split(",");

        return new Location(Bukkit.getWorld(stringArray[0]), Integer.parseInt(stringArray[1]), Integer.parseInt(stringArray[2]), Integer.parseInt(stringArray[3]));
    }
}
