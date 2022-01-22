package fr.skyfighttv.acore.Utils;

import fr.skyfighttv.acore.Main;
import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Messages {
    public static HashMap<Player, BossBar> barPlayer = new HashMap<>();
    public static HashMap<Player, Integer> sendBarPlayer = new HashMap<>();
    public static HashMap<Player, Double> currentTime = new HashMap<>();
    public static HashMap<Player, Boolean> activeBar = new HashMap<>();

    public static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    public static void sendTitle(Player player, String Title, String SubTitle, int fadein, int time, int fadeout) {
        player.sendTitle(Title, SubTitle, fadein, time, fadeout);
    }

    public static void sendBossBar(Player player, String Title, Double time, String color) {
        if (sendBarPlayer.containsKey(player))
            return;
        BossBar bar = Bukkit.createBossBar(Title, BarColor.valueOf(color.toUpperCase()), BarStyle.SOLID);
        bar.setProgress(1.0);
        bar.addPlayer(player);
        currentTime.put(player, time);
        sendBarPlayer.put(player, Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstances(), () -> {
            double tempTime = 1 / time * currentTime.get(player);
            while (tempTime > 1)
                tempTime = tempTime / 10;

            if(tempTime<= 0) {
                Bukkit.getServer().getScheduler().cancelTask(sendBarPlayer.get(player));
                sendBarPlayer.remove(player);
                bar.removeAll();
            }
            bar.setProgress(tempTime);
            currentTime.put(player, currentTime.get(player) - 1);
        }, 0, 10));
    }

    public static void createBossBarDurability(Player player) {
        String string = FileManager.getValues().get(Files.Config).getString("General.DurabilityBar.Title").replaceAll("%CURRENTDURABILITY%", "0").replaceAll("%MAXDURABILITY%", "0");
        barPlayer.put(player, Bukkit.createBossBar(string, BarColor.valueOf(FileManager.getValues().get(Files.Config).getString("General.DurabilityBar.Color")), BarStyle.valueOf(FileManager.getValues().get(Files.Config).getString("General.DurabilityBar.Type"))));
        barPlayer.get(player).setProgress(1);
        barPlayer.get(player).addPlayer(player);
    }

    public static void updateBossBarDurability(Player player, Integer currentDura, Integer maxDura, Double currentDuraArmor, Double maxDuraArmor) {
        String string;
        if(maxDura != 0) {
            ItemStack itemStack = player.getInventory().getItemInHand();
            String item;
            if(itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName())
                item = itemStack.getItemMeta().getDisplayName();
            else
                item = itemStack.getType().name();

            string = FileManager.getValues().get(Files.Config).getString("General.DurabilityBar.Title").replaceAll("%ITEM%", item.replaceAll("_", "")).replaceAll("%CURRENTDURABILITY%", String.valueOf(currentDura)).replaceAll("%MAXDURABILITY%", String.valueOf(maxDura));
        } else {
            string = "";
        }
        double result;
        if(maxDuraArmor != 0) {
            result = 1 - ((maxDuraArmor - currentDuraArmor) / maxDuraArmor);
        } else {
            result = 0;
        }

        if (result == 0 && maxDura == 0.0) {
            barPlayer.get(player).removePlayer(player);
            if(!activeBar.containsKey(player) || activeBar.get(player))
                activeBar.put(player, false);
        } else {
            if(activeBar.containsKey(player) && !activeBar.get(player)) {
                activeBar.put(player, true);
                barPlayer.get(player).addPlayer(player);
            }
            barPlayer.get(player).setProgress(result);
        }
        barPlayer.get(player).setTitle(string);
    }
}
