package fr.skyfighttv.acore.Runnables;

import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class UnclaimFinder implements Runnable{
    @Override
    public void run() {
        FileConfiguration config = FileManager.getValues().get(Files.Effects);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if(player.getItemInHand() != null
                    && player.getItemInHand().hasItemMeta()
                    && player.getItemInHand().getItemMeta().hasLore()) {
                for (String unclaimfinder : config.getConfigurationSection("UNCLAIMFINDER").getKeys(false)) {
                    if (player.getItemInHand().getItemMeta().getLore().contains(config.getString( "UNCLAIMFINDER." + unclaimfinder + ".lore").replaceAll("&", "§"))) {
                        int radiusMax = config.getInt("UNCLAIMFINDER." + unclaimfinder + ".radius") * 16;
                        int radiusMin = -(config.getInt("UNCLAIMFINDER." + unclaimfinder + ".radius") * 16);
                        List<String> detecteBlock = config.getStringList("UNCLAIMFINDER." + unclaimfinder + ".detecteblock");
                        int result = 0;

                        for (int X = radiusMax; X != radiusMin; X--) {
                            for (int Z = radiusMax; Z != radiusMin; Z--) {
                                for (int Y = 0; Y < 256; Y++) {
                                    Block block = player.getWorld().getBlockAt((int) player.getLocation().getX() + X, Y, (int) player.getLocation().getZ() + Z);
                                    if (detecteBlock.contains(block.getType().name())) {
                                        result++;
                                    }
                                }
                            }
                        }

                        if (result > 100) result = 100;
                        //setNewDurability(player.getItemInHand(), 1, player);
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(config.getString("UNCLAIMFINDER." + unclaimfinder + ".actionbar").replaceAll("%result%", result + "")));
                    }
                }
            }
        }
    }

    private void setNewDurability(ItemStack armor, Integer damage, Player player) {
        ItemMeta itemMeta = armor.getItemMeta();

        List<String> lore = new ArrayList<>(armor.getItemMeta().getLore());
        String[] durability = lore.get(0).replaceAll("§7Durabilité: §a", "").split("§7/§c");
        int damageFinal = Integer.parseInt(durability[0]) - damage;

        if(damageFinal < 0) {
            player.setItemInHand(null);
            return;
        }

        lore.set(0, "§7Durabilité: §a" + damageFinal + "§7/§c" + durability[1]);
        itemMeta.setLore(lore);

        armor.setItemMeta(itemMeta);
        armor.setDurability((short) 0);
        player.setItemInHand(armor);
    }
}
