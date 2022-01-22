package fr.skyfighttv.acore.Listeners;

import fr.skyfighttv.acore.Manager.WebHookManager;
import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class AutoLog implements Listener {
    @EventHandler
    private void onChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage().toUpperCase();
        for (String alertwords : FileManager.getValues().get(Files.AutoLog).getStringList("AlertWords")) {
            if (message.contains(alertwords.toUpperCase())) {
                WebHookManager.logFaction.send("@everyone | Alert Word >> [Chat] " +  event.getPlayer().getName() + " » " + event.getMessage().replaceAll("@here", "").replaceAll("@everyone", ""));
                break;
            } else {
                WebHookManager.logFaction.send("[Chat] " + event.getPlayer().getName() + " » " + event.getMessage().replaceAll("@here", "").replaceAll("@everyone", ""));
                break;
            }
        }
    }

    @EventHandler
    private void onCommand(PlayerCommandPreprocessEvent event) {
        String privateMessage = event.getMessage().toUpperCase();
        if (event.getMessage().startsWith("/msg ") || event.getMessage().startsWith("/r ")) {
            for (String alertwords : FileManager.getValues().get(Files.AutoLog).getStringList("AlertWords")) {
                if (privateMessage.contains(alertwords.toUpperCase())) {
                    WebHookManager.logFaction.send("@everyone | Alert Word >> [Private message] " +  event.getPlayer().getName() + " » " + event.getMessage().replaceAll("@here", "").replaceAll("@everyone", ""));
                    break;
                } else {
                    WebHookManager.logFaction.send("[Private message] " + event.getPlayer().getName() + " » " + event.getMessage());
                    break;
                }
            }
        } else {
            for (String alertwords : FileManager.getValues().get(Files.AutoLog).getStringList("AlertWords")) {
                if (privateMessage.contains(alertwords.toUpperCase())) {
                    WebHookManager.logFaction.send("@everyone | Alert Word >> [Command] " +  event.getPlayer().getName() + " » " + event.getMessage());
                    break;
                } else {
                    WebHookManager.logFaction.send("[Command] " + event.getPlayer().getName() + " » " + event.getMessage().replaceAll("@here", "").replaceAll("@everyone", ""));
                    break;
                }
            }
        }
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        WebHookManager.logFaction.send("[+] " + event.getPlayer().getName());
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        WebHookManager.logFaction.send("[-] " + event.getPlayer().getName());
    }
}
