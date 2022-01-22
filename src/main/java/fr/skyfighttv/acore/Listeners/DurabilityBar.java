package fr.skyfighttv.acore.Listeners;

import fr.skyfighttv.acore.Main;
import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import fr.skyfighttv.acore.Utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class DurabilityBar implements Listener {

    private HashMap<Player, Integer> taskId = new HashMap<>() ;
    public static HashMap<Player, Boolean> updateActive = new HashMap<>() ;

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Messages.createBossBarDurability(player);
        taskId.put(event.getPlayer(), Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstances(), () -> {
            updateActive.putIfAbsent(player, true);
            if(!updateActive.get(player))
                return;
            int currentDurabilityArmor = 0;
            int baseDurabilityArmor = 0;
            for(ItemStack itemStack : player.getInventory().getArmorContents()) {
                if(itemStack != null) {
                    if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore() && itemStack.getItemMeta().getLore().get(0).startsWith("§7Durabilité:")) {
                        String[] strings = itemStack.getItemMeta().getLore().get(0).replaceAll("§7Durabilité: ", "").replaceAll("§a", "").replaceAll("§7", "").split("/");
                        currentDurabilityArmor += Integer.parseInt(strings[0].replaceAll(" ", ""));
                        baseDurabilityArmor += Integer.parseInt(strings[1].replaceAll(" ", ""));
                    } else {
                        currentDurabilityArmor += itemStack.getType().getMaxDurability() - itemStack.getDurability();
                        baseDurabilityArmor += itemStack.getType().getMaxDurability();
                    }
                }
            }
            int currentDurabilityInHand = 0;
            int baseDurabilityInHand = 0;
            if(player.getInventory().getItemInHand() != null) {
                if (player.getInventory().getItemInHand().hasItemMeta() && player.getInventory().getItemInHand().getItemMeta().hasLore() && player.getInventory().getItemInHand().getItemMeta().getLore().get(0).contains("§7Durabilité: ")) {
                    String[] strings = player.getInventory().getItemInHand().getItemMeta().getLore().get(0).replaceAll("§7Durabilité: ", "").replaceAll("§a", "").replaceAll("§7", "").split("/");
                    currentDurabilityInHand = Integer.parseInt(strings[0].replaceAll(" ", ""));
                    baseDurabilityInHand = Integer.parseInt(strings[1].replaceAll(" ", ""));
                } else {
                    currentDurabilityInHand = player.getInventory().getItemInHand().getType().getMaxDurability() - player.getInventory().getItemInHand().getDurability();
                    baseDurabilityInHand = player.getInventory().getItemInHand().getType().getMaxDurability();
                }
            }
            Messages.updateBossBarDurability(player, currentDurabilityInHand, baseDurabilityInHand, Double.parseDouble(String.valueOf(currentDurabilityArmor)), Double.parseDouble(String.valueOf(baseDurabilityArmor)));
        }, 0, FileManager.getValues().get(Files.Config).getInt("General.DurabilityBar.IntervalUpdate") * 20));
    }

    public static String[] getLoreDurabilityItem(ItemStack itemStack) {
        if (itemStack.getItemMeta().getLore().get(0).contains("§7Durabilité: ")) {
            return itemStack.getItemMeta().getLore().get(0).replaceAll("§7Durabilité: ", "").replaceAll("§a", "").replaceAll("§7", "").split("/");
        }
        return null;
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        updateActive.put(event.getPlayer(), false);
        if(taskId.get(event.getPlayer()) != null)
            Bukkit.getServer().getScheduler().cancelTask(taskId.get(event.getPlayer()));
        Messages.barPlayer.remove(event.getPlayer());
    }
}