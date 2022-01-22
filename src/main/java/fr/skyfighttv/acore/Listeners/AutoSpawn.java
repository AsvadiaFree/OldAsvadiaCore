package fr.skyfighttv.acore.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class AutoSpawn implements Listener {
    @EventHandler
    private void onDisconnect(PlayerQuitEvent event) {
    }
}
