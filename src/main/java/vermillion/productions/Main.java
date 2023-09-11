package vermillion.productions;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import vermillion.productions.utils.ListenerUtils;
import vermillion.productions.utils.blocks.GhostBlockListeners;
import vermillion.productions.utils.item.InteractionManager;

public class Main extends JavaPlugin {

    private static @Getter Main instance;

    @Override
    public void onEnable() {
        instance = this;
        ListenerUtils.registerListener(new InteractionManager()); // Registra las interacciones.
        ListenerUtils.registerListener(new GhostBlockListeners()); // Registra los eventos de GhostBlock.

        // plugin enable logic.
    }

    @Override
    public void onDisable() {
        // plugin shutdown logic.
    }

}
