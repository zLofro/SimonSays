package vermillion.productions.utils;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import vermillion.productions.Main;

public class ListenerUtils {

    private static final Plugin plugin = Main.getInstance();

    public static void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    public static void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, plugin);
        }
    }

    public static void unregisterListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            HandlerList.unregisterAll(listener);
        }
    }

    public static void unregisterListener(Listener listener) {
        HandlerList.unregisterAll(listener);
    }

}
