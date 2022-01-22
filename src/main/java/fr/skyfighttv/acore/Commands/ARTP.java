package fr.skyfighttv.acore.Commands;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import fr.skyfighttv.acore.Main;
import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ARTP implements CommandExecutor {
    private HashMap<String, Long> timeRTP = new HashMap<>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if(!sender.isOp()) {
                if (canRTP(player))
                    RTP(player);
            } else {
                if(args.length == 0) {
                    RTP(player);
                } else {
                    if(Bukkit.getPlayer(args[0]) != null) {
                        RTP(Bukkit.getPlayer(args[0]));
                        player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.RTP.SuccessfulRTPPlayer").replaceAll("%PLAYER%", Bukkit.getPlayer(args[0]).getName()));
                    }
                }
            }
        }
        return false;
    }

    private void RTP(Player player) {
        YamlConfiguration config = FileManager.getValues().get(Files.RTP);
        YamlConfiguration mainConfig = FileManager.getValues().get(Files.Config);

        for (String times : config.getStringList("Times")) {
            if (player.hasPermission(mainConfig.getString("General.Permissions.RTP").replaceAll("%TIME%", times))) {
                int number = config.getInt("Radius");

                int x;
                int z;
                List<Integer> maxY = new ArrayList<>();
                Material material;

                do {
                    do {
                        x = (new Random()).nextInt(number);
                        z = (new Random()).nextInt(number);
                        if ((new Random()).nextBoolean())
                            x *= -1;
                        if ((new Random()).nextBoolean())
                            z *= -1;

                        maxY.clear();
                        for (int y = 0; y < 256; y++) {
                            if (!new Location(Bukkit.getWorld("faction"), x, y, z).getBlock().getType().equals(Material.AIR)) {
                                maxY.add(y);
                            }
                        }

                        material = new Location(Bukkit.getWorld("faction"), x, maxY.get(maxY.size() - 1), z).getBlock().getType();
                    } while (material.equals(Material.LAVA));
                } while (!Board.getInstance().getFactionAt(new FLocation("faction", x, z)).isWilderness());

                player.teleport(new Location(Bukkit.getWorld("faction"), x, maxY.get(maxY.size() - 1), z).add(0.5, 1, 0.5));
                player.sendMessage(mainConfig.getString("General.Messages.RTP.SuccessfulRTP"));
                timeRTP.put(player.getName(), System.currentTimeMillis() + (Long.parseLong(times) * 1000));
                break;
            }
        }
    }

    private boolean canRTP(Player player) {
        Long time = System.currentTimeMillis();
        if(!timeRTP.containsKey(player.getName()) || timeRTP.get(player.getName()) <= time) {
            return true;
        } else {
            player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.RTP.WaitBeforeRTP").replaceAll("%TIME%", new SimpleDateFormat("hh:mm:ss").format(timeRTP.get(player.getName()) - time)));
            return false;
        }
    }
}