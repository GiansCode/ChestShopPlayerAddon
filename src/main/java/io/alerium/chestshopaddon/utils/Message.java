package io.alerium.chestshopaddon.utils;

import com.destroystokyo.paper.Title;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class Message {
    
    private final List<String> message;

    public Message(String message) {
        this(Collections.singletonList(message));
    }

    public Message(List<String> message) {
        this.message = message;
    }

    /**
     * This method adds a placeholder in the message
     * @param placeholder The placeholder
     * @param value The value of the placeholder
     * @return The Message object
     */
    public Message addPlaceholder(String placeholder, Object value) {
        for (int i = 0; i < message.size(); i++) {
            String s = message.get(i);
            message.set(i, s.replaceAll("%" + placeholder, value.toString()));
        }
        return this;
    }

    /**
     * This method parse the formatting codes of the message
     * @return The Message object
     */
    public Message format() {
        for (int i = 0; i < message.size(); i++) {
            String s = message.get(i);
            message.set(i, ChatColor.translateAlternateColorCodes('&', s));
        }
        return this;
    }

    /**
     * This method sends to a CommandSender the message
     * @param sender The CommandSender
     * @return The Message object
     */
    public Message send(CommandSender sender) {
        message.forEach(sender::sendMessage);
        return this;
    }

    /**
     * This method broadcasts to all the players the message
     * @return The Message object
     */
    public Message broadcast() {
        message.forEach(Bukkit::broadcastMessage);
        return this;
    }

    /**
     * This method sends a Title to a Player
     * @param player The Player
     */
    public void sendAsTitle(Player player) {
        sendAsTitle(player, 20, 200, 20);
    }

    /**
     * This method sends a Title to a Player
     * @param player The Player
     * @param fadeIn The time that the title uses for show in the screen
     * @param stay The time that the title stays at screen
     * @param fadeOut The time that the title uses for disappear from the screen
     */
    public void sendAsTitle(Player player, int fadeIn, int stay, int fadeOut) {
        if (message.size() > 2)
            return;

        player.sendTitle(new Title(message.get(0), message.get(1), fadeIn, stay, fadeOut));
    }

    /**
     * This method returns the List containing the strings of the message
     * @return The List of strings
     */
    public List<String> toStringList() {
        return message;
    }

    /**
     * This method returns a String with all the message
     * @return The string
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String s : message)
            builder.append(s).append("\n");

        builder.setLength(builder.length()-1);
        return builder.toString();
    }
    
}
