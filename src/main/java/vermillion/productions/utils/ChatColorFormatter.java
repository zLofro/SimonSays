package vermillion.productions.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.ChatColor;

/**
 * Utility class designed to translate both {@link String} to {@link Component} and {@link Component} to {@link String} and
 * use the {@link ChatColor} format by adding the "&" char and its respective {@link ChatColor} ID before the text you want to
 * apply it to.
 * @author <a href="https://github.com/zLofro">Lofro</a>.
 */
public class ChatColorFormatter {

    private static final String name = stringToString("");
    private static final String prefix = stringToString(name + " &r>> ");

    /**
     * Translates the given String into another String with ChatColor format.
     * @param text to translate.
     * @return String containing the ChatColor.COLOR_CHAR color code character replaced by '&'.
     */
    public static String stringToString(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Translates the given String into a Component with ChatColor format.
     * @param text to translate.
     * @return Component containing the ChatColor.COLOR_CHAR color code character replaced by '&'.
     */
    public static String componentToString(TextComponent text) {
        return ChatColor.translateAlternateColorCodes('&', text.content());
    }

    /**
     * Translates the given Component into a String with ChatColor format.
     * @param text to translate.
     * @return String containing the ChatColor.COLOR_CHAR color code character replaced by '&'.
     */
    public static Component stringToComponent(String text) {
        return Component.text(ChatColor.translateAlternateColorCodes('&', text));
    }

    /**
     * Translates the given Component into another Component with ChatColor format.
     * @param text to translate.
     * @return Component containing the ChatColor.COLOR_CHAR color code character replaced by '&'.
     */
    public static Component componentToComponent(TextComponent text) {
        return Component.text(ChatColor.translateAlternateColorCodes('&', text.content()));
    }

    /**
     * Translates the given String into another String with ChatColor format and the plugin prefix and the plugin prefix.
     * @param text to translate.
     * @return String containing the ChatColor.COLOR_CHAR color code character replaced by '&' with the plugin prefix.
     */
    public static String stringToStringWithPrefix(String text) {
        return ChatColor.translateAlternateColorCodes('&', prefix + text);
    }

    /**
     * Translates the given String into a Component with ChatColor format and the plugin prefix.
     * @param text to translate.
     * @return Component containing the ChatColor.COLOR_CHAR color code character replaced by '&' with the plugin prefix.
     */
    public static String componentToStringWithPrefix(TextComponent text) {
        return ChatColor.translateAlternateColorCodes('&', prefix + text.content());
    }

    /**
     * Translates the given Component into a String with ChatColor format and the plugin prefix.
     * @param text to translate.
     * @return String containing the ChatColor.COLOR_CHAR color code character replaced by '&' with the plugin prefix.
     */
    public static Component stringToComponentWithPrefix(String text) {
        return Component.text(ChatColor.translateAlternateColorCodes('&', prefix + text));
    }

    /**
     * Translates the given Component into another Component with ChatColor format and the plugin prefix.
     * @param text to translate.
     * @return Component containing the ChatColor.COLOR_CHAR color code character replaced by '&' with the plugin prefix.
     */
    public static Component componentToComponentWithPrefix(TextComponent text) {
        return Component.text(ChatColor.translateAlternateColorCodes('&', prefix + text.content()));
    }

}
