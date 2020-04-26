package io.alerium.chestshopaddon.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.alerium.chestshopaddon.ChestShopAddon;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQL {
    
    private final ChestShopAddon plugin = ChestShopAddon.getInstance();
    
    private HikariDataSource dataSource;

    /**
     * This method creates a Connection to the Database
     */
    public void connect() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://" + plugin.getConfiguration().getConfig().getString("MySQL.hostname") + ":" + plugin.getConfiguration().getConfig().getInt("MySQL.port") + "/" + plugin.getConfiguration().getConfig().getString("MySQL.database"));
        config.setUsername(plugin.getConfiguration().getConfig().getString("MySQL.username"));
        config.setPassword(plugin.getConfiguration().getConfig().getString("MySQL.password"));
        
        dataSource = new HikariDataSource(config);
    }

    /**
     * This method executes the disconnection from the Database
     */
    public void disconnect() {
        dataSource.close();
    }

    /**
     * This method gets a Connection from the HikariDataSource pool
     * @return The Connection instance
     * @throws SQLException If there is a problem with the Connection
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
}
