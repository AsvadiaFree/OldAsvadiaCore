package fr.skyfighttv.acore.Listeners;

import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class HoeSell implements Listener {
    HashMap<Player, Boolean> sell = new HashMap<Player, Boolean>();

    @EventHandler
    public void OnClicks(PlayerInteractEvent event) {
        FileConfiguration config = FileManager.getValues().get(Files.HoeSell);
        FileConfiguration mainConfig = FileManager.getValues().get(Files.Config);

        if(event.getMaterial().equals(Material.getMaterial(config.getString("Item-Type")))) {
            ItemStack itemStack = new ItemStack(Material.getMaterial(config.getString("Item-Type")));
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(config.getString("Name"));
            List<String> lore = event.getItem().getItemMeta().getLore();
            if (event.getItem().getItemMeta().hasLore()) {
                for (int i = 0; i != lore.size(); i++) {
                    if (lore.get(i).contains("%USE%")) {
                        lore.set(i, lore.get(i).replace("%USE%", "" + config.getInt("Lore-Utilisation")));
                        break;
                    }
                }
            }
            itemMeta.setLore(lore);
            if(event.getItem().getItemMeta().equals(itemMeta)) {
                Player player = event.getPlayer();
                if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    if (!player.isSneaking()) {
                        for (Object string : config.getList("Allow-Sell")) {
                            if (event.getClickedBlock().getType().name().equals(string)) {
                                sell.put(player, true);
                                for (int i = 0; i != lore.size(); i++) {
                                    for (int i1 = 1; i1 != config.getInt("Lore-Utilisation") + 1; i1++) {
                                        if (lore.get(i).contains("1")) {
                                            event.getItem().setAmount(0);
                                            player.sendMessage(mainConfig.getString("General.Messages.HoeSell.Break-Item") + "");
                                            break;
                                        }
                                        if (lore.get(i).contains("" + i1)) {
                                            lore.set(i, lore.get(i).replace("" + i1, "" + (i1 - 1)));
                                            itemMeta.setLore(lore);
                                            event.getItem().setItemMeta(itemMeta);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        player.sendMessage(mainConfig.getString("General.Messages.HoeSell.Snicking-Refuse"));
                    }
                }
            }
        }
    }

    @EventHandler
    public void Sell(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();

        if(sell.containsKey(player) && sell.get(player)) {
            YamlConfiguration config = FileManager.getValues().get(Files.HoeSell);
            YamlConfiguration mainConfig = FileManager.getValues().get(Files.Config);

            player.sendMessage(mainConfig.getString("General.Messages.HoeSell.Start-Sale"));
            int total = 0;
            for (int i = 0; i != event.getInventory().getSize(); i++) {
                if(event.getInventory().getItem(i) == null) {
                    continue;
                }
                int shop = config.getInt("ItemsSales." + event.getInventory().getItem(i).getType().name());
                if(shop != 0) {
                    total += (shop * event.getInventory().getItem(i).getAmount());
                    String sale = mainConfig.getString("General.Messages.HoeSell.Successful-Sale");
                    String e = event.getInventory().getItem(i).getType().name().toLowerCase();
                    e = e.replaceAll("_", " ");
                    e = e.replaceAll("item", "");
                    sale = sale.replaceAll("%MATERIAL%", "" + e);
                    sale = sale.replaceAll("%AMOUNT%", (shop * event.getInventory().getItem(i).getAmount()) + "");
                    sale = sale.replaceAll("%NUMBER%", "x" + event.getInventory().getItem(i).getAmount());
                    player.sendMessage(sale);
                    event.getInventory().getItem(i).setAmount(0);
                }
            }
            if(total != 0) {
                String command = config.get("config.Command-Give-Money") + "";
                command = command.replaceAll("%PLAYER%", player.getDisplayName());
                command = command.replaceAll("%AMOUNT%", total + "");
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
            } else {
                player.sendMessage(mainConfig.getString("General.Messages.HoeSell.No-Item-Sale"));
            }
            sell.remove(player);
            sell.put(player, false);
        }
    }
}
