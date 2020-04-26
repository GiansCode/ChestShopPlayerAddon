package io.alerium.chestshopaddon.playerdata;

import io.alerium.chestshopaddon.ChestShopAddon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerDatabase {

    private final ChestShopAddon plugin = ChestShopAddon.getInstance();

    /**
     * This method setups the database
     * @return True if done
     */
    public boolean setup() {
        try (
                Connection connection = plugin.getMySQL().getConnection();
                PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS playerdata (uuid VARCHAR(36) PRIMARY KEY, shops INTEGER);");
        ) {
            statement.execute();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "An error occurred while the database setup", e);
            return false;
        }
        
        return true;
    }

    /**
     * This method loads a PlayerData from the Database
     * @param uuid The UUID of the Player
     * @return The PlayerData instance, null if there is a problem
     */
    public PlayerData loadPlayerData(UUID uuid) {
        try (
                Connection connection = plugin.getMySQL().getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT shops FROM playerdata WHERE uuid = ?;");
        ) {
            statement.setString(1, uuid.toString());

            ResultSet result = statement.executeQuery();
            if (!result.next())
                return new PlayerData(uuid, 0);
            
            return new PlayerData(uuid, result.getInt("shops"));
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "An error occurred while loading data of " + uuid, e);
        }
        
        return null;
    }

    /**
     * This method saves a PlayerData in the Database
     * @param player The PlayerData
     */
    public void savePlayer(PlayerData player) {
        try (
                Connection connection = plugin.getMySQL().getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO playerdata (uuid, shops) VALUES (?, ?) ON DUPLICATE KEY UPDATE shops = ?;");
        ) {
            statement.setString(1, player.getUuid().toString());
            statement.setInt(2, player.getShops());
            statement.setInt(3, player.getShops());
            
            statement.execute();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "An error occurred while saving data of " + player.getUuid(), e);
        }
    }
    
}
