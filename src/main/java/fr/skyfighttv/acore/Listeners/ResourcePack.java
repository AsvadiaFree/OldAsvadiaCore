package fr.skyfighttv.acore.Listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.skyfighttv.acore.Main;
import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class ResourcePack implements Listener {
    private HashMap<Player, Boolean> activeRP = new HashMap<>();

    @EventHandler
    private void onJoin(PlayerLoginEvent event) {
        FileConfiguration config = FileManager.getValues().get(Files.ResourcePack);
        FileConfiguration mainConfig = FileManager.getValues().get(Files.Config);

        Bukkit.getServer().getScheduler().runTaskLater(Main.getInstances(), () -> {
            sendRessourcePack(event.getPlayer(), config);
            Bukkit.getServer().getScheduler().runTaskLater(Main.getInstances(), () -> {
                activeRP.putIfAbsent(event.getPlayer(), false);
                if(!activeRP.get(event.getPlayer())) {
                    if(config.getBoolean("NoReply")) {
                        event.getPlayer().kickPlayer(mainConfig.getString("General.Messages.ResourcePack.NoReplyKick"));
                    } else {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();

                        out.writeUTF("Connect");
                        out.writeUTF(config.getString("LobbyServerName"));

                        event.getPlayer().sendPluginMessage(Main.getInstances(), "BungeeCord", out.toByteArray());

                        out = ByteStreams.newDataOutput();

                        out.writeUTF("Message");
                        out.writeUTF(mainConfig.getString("General.Messages.ResourcePack.NoReplyChat"));

                        event.getPlayer().sendPluginMessage(Main.getInstances(), "BungeeCord", out.toByteArray());
                    }
                }
                activeRP.remove(event.getPlayer());
            }, config.getInt("TimeBeforeKick") * 20);
        }, config.getInt("TimeBeforeLoad") * 20);
    }

    private void sendRessourcePack(Player player, FileConfiguration config) {
        player.setResourcePack(config.getString("URLS.1_16"));
    }

    @EventHandler
    private void onRP(PlayerResourcePackStatusEvent event) {
        FileConfiguration config = FileManager.getValues().get(Files.ResourcePack);
        FileConfiguration mainConfig = FileManager.getValues().get(Files.Config);

        (new BukkitRunnable() {
            public void run() {
                if (event.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
                    event.getPlayer().sendMessage(mainConfig.getString("General.Messages.ResourcePack.SuccessLoad"));
                    activeRP.put(event.getPlayer(), true);
                } else {
                    if (event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED) {
                        if(config.getBoolean("RefuseKick")) {
                            event.getPlayer().kickPlayer(mainConfig.getString("General.Messages.ResourcePack.RefuseKick"));
                        } else {
                            ByteArrayDataOutput out = ByteStreams.newDataOutput();

                            out.writeUTF("Connect");
                            out.writeUTF(config.getString("LobbyServerName"));

                            event.getPlayer().sendPluginMessage(Main.getInstances(), "BungeeCord", out.toByteArray());

                            out = ByteStreams.newDataOutput();

                            out.writeUTF("Message");
                            out.writeUTF(mainConfig.getString("General.Messages.ResourcePack.RefuseChat"));

                            event.getPlayer().sendPluginMessage(Main.getInstances(), "BungeeCord", out.toByteArray());
                        }
                        activeRP.put(event.getPlayer(), true);
                    }
                    if (event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
                        if(config.getBoolean("ErrorKick")) {
                            event.getPlayer().kickPlayer(mainConfig.getString("General.Messages.ResourcePack.ErrorKick"));
                        } else {
                            ByteArrayDataOutput out = ByteStreams.newDataOutput();

                            out.writeUTF("Connect");
                            out.writeUTF(config.getString("LobbyServerName"));

                            event.getPlayer().sendPluginMessage(Main.getInstances(), "BungeeCord", out.toByteArray());

                            out = ByteStreams.newDataOutput();

                            out.writeUTF("Message");
                            out.writeUTF(mainConfig.getString("General.Messages.ResourcePack.ErrorChat"));

                            event.getPlayer().sendPluginMessage(Main.getInstances(), "BungeeCord", out.toByteArray());
                        }
                        activeRP.put(event.getPlayer(), true);
                    }
                }
            }
        }).runTask(Main.getInstances());
    }
}
