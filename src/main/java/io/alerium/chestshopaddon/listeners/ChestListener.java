package io.alerium.chestshopaddon.listeners;

import com.Acrobot.ChestShop.Events.PreShopCreationEvent;
import com.Acrobot.ChestShop.Events.ShopDestroyedEvent;
import io.alerium.chestshopaddon.ChestShopAddon;
import io.alerium.chestshopaddon.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;

public class ChestListener implements Listener {
    
    private final ChestShopAddon plugin = ChestShopAddon.getInstance();
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onShopCreate(PreShopCreationEvent event) {
        Player player = event.getPlayer();
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        int amount = data.getShops();
        int maxAmount = plugin.getPlayerDataManager().getMaxChests(player);
        
        if (maxAmount != -1 && amount >= maxAmount) {
            plugin.getMessages().getMessage("maxAmountReached")
                    .addPlaceholder("maxAmount", maxAmount)
                    .format()
                    .send(player);
            event.setCancelled(true);
            
            event.getSign().getBlock().breakNaturally();
            return;
        }
        
        amount++;
        data.setShops(amount);
        
        plugin.getMessages().getMessage("shopPlaced")
                .addPlaceholder("amount", amount)
                .addPlaceholder("maxAmount", maxAmount)
                .format()
                .send(player);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true) 
    public void onShopDelete(ShopDestroyedEvent event) {
        String playerName = event.getSign().getLine(0);
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
        if (player == null)
            return;

        UUID uuid = player.getUniqueId();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            PlayerData data = player.isOnline() ? plugin.getPlayerDataManager().getPlayerData(uuid) : plugin.getPlayerDataManager().getOfflinePlayerData(uuid);
            if (data == null)
                return;

            data.setShops(data.getShops() - 1);
            if (!player.isOnline())
                plugin.getPlayerDataManager().savePlayer(data, false);
        });
    }
    
    
}
