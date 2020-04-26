package io.alerium.chestshopaddon.listeners;

import io.alerium.chestshopaddon.ChestShopAddon;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    
    private final ChestShopAddon plugin = ChestShopAddon.getInstance();
    
    @EventHandler
    public void onAsyncPlayerLogin(AsyncPlayerPreLoginEvent event) {
        plugin.getPlayerDataManager().loadPlayer(event.getUniqueId());
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getPlayerDataManager().savePlayer(event.getPlayer().getUniqueId(), true));
    }
    
}
