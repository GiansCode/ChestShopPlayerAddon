package io.alerium.chestshopaddon;

import io.alerium.chestshopaddon.integrations.PlaceholderAPI;
import io.alerium.chestshopaddon.listeners.ChestListener;
import io.alerium.chestshopaddon.listeners.PlayerListener;
import io.alerium.chestshopaddon.playerdata.PlayerDataManager;
import io.alerium.chestshopaddon.utils.MySQL;
import io.alerium.chestshopaddon.utils.YAMLConfiguration;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestShopAddon extends JavaPlugin {

    @Getter private static ChestShopAddon instance;
    
    @Getter private YAMLConfiguration configuration;
    @Getter private YAMLConfiguration messages;
    
    @Getter private MySQL mySQL;
    @Getter private PlayerDataManager playerDataManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        registerConfigs();
        registerInstances();
        registerListeners();
        registerIntegrations();
    }

    @Override
    public void onDisable() {
        playerDataManager.saveAllPlayers();
        
        mySQL.disconnect();
    }
    
    private void registerConfigs() {
        configuration = new YAMLConfiguration(this, "config");
        messages = new YAMLConfiguration(this, "messages");
    }
    
    private void registerInstances() {
        mySQL = new MySQL();
        mySQL.connect();
    
        playerDataManager = new PlayerDataManager();
        playerDataManager.enable();
    }
    
    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        
        pm.registerEvents(new ChestListener(), this);
        pm.registerEvents(new PlayerListener(), this);
    }
    
    private void registerIntegrations() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            new PlaceholderAPI().register();
    }
    
}
