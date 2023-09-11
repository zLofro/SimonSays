package vermillion.productions.utils.item;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @author Agus5534
 */
public class InteractionManager implements Listener {

    private static Map<NamespacedKey, InteractableItem> interactableItems = new HashMap<>();

    /**
     * Agrega una key interactuable.
     * @param item
     */
    public static void registerItem(InteractableItem item) {
        interactableItems.put(item.getKey(), item);
    }

    /**
     * Remueve una key interactuable.
     * @param key La key con la que se detecta el item.
     */
    public static void unregisterItem(String key) {
        interactableItems.remove(key);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(!event.hasItem()) { return; }
        if(event.getItem() == null) { return; }

        var item = event.getItem();
        var data = item.getItemMeta().getPersistentDataContainer();
        var action = event.getAction();

        interactableItems.keySet().forEach(key -> {
            if(!data.has(key)) {
                return;
            }

            var interactable = interactableItems.get(key);
            var interaction = interactable.getInteraction();

            if(interaction.getActions().contains(action)) {
                interactable.getConsumer().accept(event);
            }
        });
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        var item = event.getItem();
        var data = item.getItemMeta().getPersistentDataContainer();

        interactableItems.keySet().forEach(key -> {
            if(!data.has(key)) {
                return;
            }

            var interactable = interactableItems.get(key);
            var interaction = interactable.getInteraction();

            if(interaction == Interaction.EAT) {
                interactable.getConsumer().accept(event);
            }
        });
    }

    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();

        if(player.getInventory().getItemInMainHand() == null) { return; }

        var item = player.getInventory().getItemInMainHand();
        var data = item.getItemMeta().getPersistentDataContainer();

        interactableItems.keySet().forEach(key -> {
            if(!data.has(key)) {
                return;
            }

            var interactable = interactableItems.get(key);
            var interaction = interactable.getInteraction();

            if(interaction == Interaction.CLICK_ENTITY) {
                interactable.getConsumer().accept(event);
            }
        });
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player)) { return; }

        Player player = (Player)event.getDamager();

        if(player.getInventory().getItemInMainHand() == null) { return; }

        var item = player.getInventory().getItemInMainHand();
        var data = item.getItemMeta().getPersistentDataContainer();

        interactableItems.keySet().forEach(key -> {
            if(!data.has(key)) {
                return;
            }

            var interactable = interactableItems.get(key);
            var interaction = interactable.getInteraction();

            if(interaction == Interaction.HIT_ENTITY) {
                interactable.getConsumer().accept(event);
            }
        });
    }
}
