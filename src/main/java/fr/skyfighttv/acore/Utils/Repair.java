package fr.skyfighttv.acore.Utils;

import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Repair {

    public static boolean repair(ItemStack itemStack) {
        if(itemStack != null) {
            if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore() && itemStack.getItemMeta().getLore().get(0).contains("§7Durabilité: ")) {
                String[] strings = itemStack.getItemMeta().getLore().get(0).replaceAll("§7Durabilité: ", "").replaceAll("§a", "").replaceAll("§7", "").split("/");
                ItemMeta meta = itemStack.getItemMeta();
                List<String> newLore = new ArrayList<>();
                newLore.add(FileManager.getValues().get(Files.Config).getString("General.Repair.PatternLore").replaceAll("%DURABILITY%", strings[1].replaceAll(" ", "")));
                List<String> loreItem = itemStack.getItemMeta().getLore();
                loreItem.remove(0);
                newLore.addAll(loreItem);
                meta.setLore(newLore);
                itemStack.setItemMeta(meta);
                return true;
            } else if (!(itemStack.getType().getMaxDurability() <= 1)){
                itemStack.setDurability((short) 0);
                return true;
            }
        }
        return false;
    }

    public static void repairAll(Player player) {
        for (ItemStack itemStack : player.getInventory().getContents()) {
            repair(itemStack);
        }
    }
}
