package vermillion.productions.utils.item;

import org.bukkit.event.block.Action;

import java.util.List;

public enum Interaction {
    LEFT_CLICK_AIR(Action.LEFT_CLICK_AIR),
    RIGHT_CLICK_AIR(Action.RIGHT_CLICK_AIR),
    LEFT_CLICK_BLOCK(Action.LEFT_CLICK_BLOCK),
    RIGHT_CLICK_BLOCK(Action.RIGHT_CLICK_BLOCK),
    RIGHT_CLICK(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK),
    LEFT_CLICK(Action.LEFT_CLICK_BLOCK, Action.LEFT_CLICK_AIR),
    CLICK_ENTITY(),
    HIT_ENTITY(),
    EAT();

    private List<Action> a;
    Interaction(Action... a) {
        this.a = List.of(a);
    }

    public List<Action> getActions() {
        return a;
    }
}
