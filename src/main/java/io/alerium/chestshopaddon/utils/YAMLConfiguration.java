package io.alerium.chestshopaddon.utils;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class YAMLConfiguration {

    private final Plugin plugin;
    private final File file;

    @Getter private FileConfiguration config;

    public YAMLConfiguration(Plugin plugin, String name) {
        this.plugin = plugin;
        file = new File(plugin.getDataFolder(), name + ".yml");

        if (!file.exists()) {
            plugin.saveResource(file.getName(), false);
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Reloads all the configuration from the file
     */
    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Saves all changes to the file
     */
    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "An error occurred while ");
        }
    }

    /**
     * This methods creates an instance of Message and returns it based on the config file
     * @param path The path where to check the Message data
     * @return The Message
     */
    public Message getMessage(String path) {
        List<String> msg = config.getStringList(path);
        if (msg == null || msg.isEmpty()) {
            msg = new ArrayList<>();
            msg.add(config.getString(path));
        }

        return new Message(msg);
    }
    
}
