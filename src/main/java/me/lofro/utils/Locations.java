package me.lofro.utils;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class Locations {

    /**
     *
     * Boolean that checks whether a location is inside a cube with two locations as radius.
     *
     * @param pos1 first cube location.
     * @param pos2 second cube location.
     * @param point location to check.
     * @return If the given location is inside the cube.
     *
     */
    public static boolean isInCube(Location pos1, Location pos2, Location point) {

        var cX = pos1.getX() < pos2.getX();
        var cY = pos1.getY() < pos2.getY();
        var cZ = pos1.getZ() < pos2.getZ();

        var minX = cX ? pos1.getX() : pos2.getX();
        var maxX = cX ? pos2.getX() : pos1.getX();

        var minY = cY ? pos1.getY() : pos2.getY();
        var maxY = cY ? pos2.getY() : pos1.getY();

        var minZ = cZ ? pos1.getZ() : pos2.getZ();
        var maxZ = cZ ? pos2.getZ() : pos1.getZ();

        if (point.getX() < minX || point.getY() < minY || point.getZ() < minZ) return false;
        return !(point.getX() > maxX) && !(point.getY() > maxY) && !(point.getZ() > maxZ);
    }

    /**
     *
     * Function to get the center location of a cube with two locations as radius.
     *
     * @param world of the given locations.
     * @param pos1 first cube location.
     * @param pos2 second cube location.
     * @return Center location of the given cube.
     *
     */
    public static Location getCubeCenter(World world, Location pos1, Location pos2) {
        int x = ((pos1.getBlockX() + pos2.getBlockX()) / 2);
        int y = ((pos1.getBlockY() + pos2.getBlockY()) / 2);
        int z = ((pos2.getBlockZ() + pos1.getBlockZ()) / 2);

        return new Location(world, x, y, z);
    }

    /**
     *
     * Function that gets all the blocks inside a cube radius.
     *
     * @param loc1 first location of the cube.
     * @param loc2 second location of the cube.
     * @return a list of all the locations inside the cube.
     *
     */
    public static List<Location> getBlocksInsideCube(Location loc1, Location loc2) {
        List<Location> locations = new ArrayList<>();

        var xa = (int) (Math.max(loc1.getX(), loc2.getX()));
        var ya = (int) (Math.max(loc1.getY(), loc2.getY()));
        var za = (int) (Math.max(loc1.getZ(), loc2.getZ()));

        var xi = (int) (Math.min(loc1.getX(), loc2.getX()));
        var yi = (int) (Math.min(loc1.getY(), loc2.getY()));
        var zi = (int) (Math.min(loc1.getZ(), loc2.getZ()));

        for (int x = xi; x <= xa; x++) {
            for (int y = yi; y <= ya; y++) {
                for (int z = zi; z <= za; z++) {
                    locations.add(new Location(loc1.getWorld(), x, y, z));
                }
            }
        }

        return locations;
    }

}
