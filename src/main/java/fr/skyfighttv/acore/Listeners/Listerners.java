package fr.skyfighttv.acore.Listeners;

import fr.skyfighttv.acore.Main;
import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import fr.skyfighttv.acore.Utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.io.*;

public class Listerners implements Listener {
    @EventHandler
    private void onJoin(PlayerJoinEvent event) throws IOException {
        File file = new File(Main.getInstances().getDataFolder() + "/Players/" + event.getPlayer().getName() + ".yml");
        if (!file.exists()) {
            InputStream fileStream = Main.getInstances().getResource(Files.DefaultPlayerFile.getName() + ".yml");
            byte[] buffer = new byte[fileStream.available()];
            fileStream.read(buffer);

            OutputStream outStream = new FileOutputStream(file);
            outStream.write(buffer);
        }

        if(FileManager.getValues().get(Files.Config).getBoolean("General.EnableModules.JoinLeave"))
            for(Player player : Bukkit.getOnlinePlayers())
                if(player != null)
                    Messages.sendActionBar(player, FileManager.getValues().get(Files.Config).getString("General.JoinLeave.JoinMessage").replaceAll("%PLAYER%", event.getPlayer().getName()));
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        /*if (Messages.sendBarPlayer.containsKey(event.getPlayer())) {
            Bukkit.getServer().getScheduler().cancelTask(Messages.sendBarPlayer.get(event.getPlayer()));
            Messages.sendBarPlayer.remove(event.getPlayer());
        }*/

        if(FileManager.getValues().get(Files.Config).getBoolean("General.EnableModules.JoinLeave"))
            for(Player player : Bukkit.getOnlinePlayers())
                if(player != null)
                    Messages.sendActionBar(player, FileManager.getValues().get(Files.Config).getString("General.JoinLeave.LeaveMessage").replaceAll("%PLAYER%", event.getPlayer().getName()));
    }

    @EventHandler
    private void onDeath(PlayerDeathEvent event) {
        if (FileManager.getValues().get(Files.Config).getBoolean("General.EnableModules.DisableDeathMessage"))
            event.setDeathMessage("");
    }

    @EventHandler
    private void onRespawn(PlayerRespawnEvent event) {
        for (String fromWorld : FileManager.getValues().get(Files.Config).getStringList("General.AutoRespawn.From")) {
            if (event.getPlayer().getWorld().getName().equals(fromWorld)) {
                event.setRespawnLocation(new Location(Bukkit.getWorld(FileManager.getValues().get(Files.Config).getString("General.AutoRespawn.RespawnWorld")), 0.5, 80, -0.5, -180, -10));
            }
        }
    }
}
