package fr.skyfighttv.acore.Utils;

import fr.skyfighttv.acore.Main;
import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PressionOR {
    public static void start() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstances(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if(player.getLocation().getBlock().getType().equals(Material.LIGHT_WEIGHTED_PRESSURE_PLATE)) {
                    player.damage(FileManager.getValues().get(Files.Config).getDouble("General.Recipes.GOLD_PLATE.Damage"));
                }
            }
        }, 0, FileManager.getValues().get(Files.Config).getInt("General.Recipes.GOLD_PLATE.Interval"));
    }
}
