package me.lofro.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class designed to translate both {@link String} to {@link Component} and {@link Component} to {@link String} and
 * use the {@link ChatColor} format by adding the "&" char and its respective {@link ChatColor} ID before the text you want to
 * apply it to.
 * @author <a href="https://github.com/zLofro">Lofro</a>.
 */
public class ChatColorFormatter {

    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

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
        return ChatColorFormatter.format(text.content());
    }

    /**
     * Translates the given Component into a String with ChatColor format.
     * @param text to translate.
     * @return String containing the ChatColor.COLOR_CHAR color code character replaced by '&'.
     */
    public static Component stringToComponent(String text) {
        return Component.text(ChatColorFormatter.format(text));
    }

    /**
     * Translates the given Component into another Component with ChatColor format.
     * @param text to translate.
     * @return Component containing the ChatColor.COLOR_CHAR color code character replaced by '&'.
     */
    public static Component componentToComponent(TextComponent text) {
        return Component.text(ChatColorFormatter.format(text.content()));
    }

    /**
     * Translates the given String into another String with ChatColor format and the plugin prefix and the plugin prefix.
     * @param text to translate.
     * @return String containing the ChatColor.COLOR_CHAR color code character replaced by '&' with the plugin prefix.
     */
    public static String stringToStringWithPrefix(String text) {
        return ChatColorFormatter.format(prefix + text);
    }

    /**
     * Translates the given String into a Component with ChatColor format and the plugin prefix.
     * @param text to translate.
     * @return Component containing the ChatColor.COLOR_CHAR color code character replaced by '&' with the plugin prefix.
     */
    public static String componentToStringWithPrefix(TextComponent text) {
        return ChatColorFormatter.format(prefix + text.content());
    }

    /**
     * Translates the given Component into a String with ChatColor format and the plugin prefix.
     * @param text to translate.
     * @return String containing the ChatColor.COLOR_CHAR color code character replaced by '&' with the plugin prefix.
     */
    public static Component stringToComponentWithPrefix(String text) {
        return Component.text(ChatColorFormatter.format(prefix + text));
    }

    /**
     * Translates the given Component into another Component with ChatColor format and the plugin prefix.
     * @param text to translate.
     * @return Component containing the ChatColor.COLOR_CHAR color code character replaced by '&' with the plugin prefix.
     */
    public static Component componentToComponentWithPrefix(TextComponent text) {
        return Component.text(ChatColorFormatter.format(prefix + text.content()));
    }

    public static String format(String text) {
        text = text.replace("#&", "#");
        Matcher m = pattern.matcher(text);
        while (m.find()) {
            String cl = text.substring(m.start(), m.end());
            text = text.replace(cl, "" + net.md_5.bungee.api.ChatColor.of(cl));
            m = pattern.matcher(text);
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
