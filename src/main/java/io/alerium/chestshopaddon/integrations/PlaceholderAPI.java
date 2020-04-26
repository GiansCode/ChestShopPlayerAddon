package io.alerium.chestshopaddon.integrations;

import io.alerium.chestshopaddon.ChestShopAddon;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlaceholderAPI extends PlaceholderExpansion {

    private final ChestShopAddon plugin = ChestShopAddon.getInstance();
    
    @Override
    public String getIdentifier() {
        return "chestshopplayeraddon";
    }

    @Override
    public String getAuthor() {
        return "xQuickGlare";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }


    @Override
    public String onPlaceholderRequest(Player player, String params) {
        switch (params.toUpperCase()) {
            case "AMOUNT":
                return Integer.toString(plugin.getPlayerDataManager().getPlayerData(player.getUniqueId()).getShops());
            case "MAXAMOUNT":
                return Integer.toString(plugin.getPlayerDataManager().getMaxChests(player));
            default:
                return null;
        }
    }
}
