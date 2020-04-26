package io.alerium.chestshopaddon.playerdata;

import io.alerium.chestshopaddon.ChestShopAddon;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PlayerDataManager {
    
    private final ChestShopAddon plugin = ChestShopAddon.getInstance();
    
    private final List<PlayerData> players = Collections.synchronizedList(new ArrayList<>());
    private final PlayerDatabase database = new PlayerDatabase();
    
    @Getter private int maxChests;

    /**
     * This method enables the PlayerDataManager
     */
    public void enable() {
        maxChests = plugin.getConfiguration().getConfig().getInt("maxChests");
        if (!database.setup()) {
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }
        
        Bukkit.getOnlinePlayers().forEach(player -> loadPlayer(player.getUniqueId()));
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::saveAllPlayers, 20*60*5, 20*60*5);
    }

    /**
     * This method loads a PlayerData from the database
     * @param uuid The UUID of the Player
     * @return True if loaded correctly
     */
    public boolean loadPlayer(UUID uuid) {
        PlayerData playerData = getOfflinePlayerData(uuid);
        if (playerData == null)
            return false;
        
        players.add(playerData);
        return true;
    }

    /**
     * This method saves the PlayerData in the database
     * @param uuid The UUID of the Player
     * @param remove True to remove the PlayerData from the cache
     */
    public void savePlayer(UUID uuid, boolean remove) {
        PlayerData playerData = getPlayerData(uuid);
        if (playerData == null)
            return;
        
        savePlayer(playerData, remove);
    }

    /**
     * This method saves a PlayerData in the database
     * @param playerData The PlayerData
     * @param remove True to remove the PlayerData from the cache
     */
    public void savePlayer(PlayerData playerData, boolean remove) {
        database.savePlayer(playerData);
        
        if (remove)
            players.remove(playerData);
    }

    /**
     * This method gets the PlayerData of a Player
     * @param uuid The UUID of the Player
     * @return The PlayerData instance, null if not found
     */
    public PlayerData getPlayerData(UUID uuid) {
        for (PlayerData player : players) {
            if (player.getUuid().equals(uuid))
                return player;
        }
        
        return null;
    }

    /**
     * This method gets the PlayerData of an OfflinePlayer
     * @param uuid The UUID of the OfflinePlayer
     * @return The PlayerData instance
     */
    public PlayerData getOfflinePlayerData(UUID uuid) {
        return database.loadPlayerData(uuid);
    }

    /**
     * This method saves all the cached PlayerData in the Database
     */
    public void saveAllPlayers() {
        for (PlayerData player : players)
            savePlayer(player, false);
    }

    /**
     * This method gets the number of max shops that a Player can place
     * @param player The Player
     * @return The amount of shops, -1 is unlimited
     */
    public int getMaxChests(Player player) {
        if (player.hasPermission("chestshopaddon.bypass"))
            return -1;
        
        for (int i = 1; i < maxChests; i++) {
            if (player.hasPermission("chestshopaddon.amount." + i))
                return i;
        }
        
        return 0;
    }
    
}
