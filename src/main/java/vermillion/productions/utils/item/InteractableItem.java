package vermillion.productions.utils.item;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;

import java.util.function.Consumer;

/**
 * Clase para crear un item con interacciones.
 * @version 1.0
 * @author Agus5534
 */
public class InteractableItem {
    private final Interaction interaction;
    private final NamespacedKey key;
    private final Consumer<?> consumer;
    public InteractableItem(Interaction interaction, NamespacedKey key, Consumer<?> consumer) {
        this.interaction = interaction;
        this.key = key;
        this.consumer = consumer;
    }

    public Interaction getInteraction() {
        return interaction;
    }

    public Consumer<? super Event> getConsumer() {
        return (Consumer<? super Event>) consumer;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
