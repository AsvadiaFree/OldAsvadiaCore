package fr.skyfighttv.acore.Commands.SubCommand;

import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CoreSetEffect {

    public static void setEffect(String type, Player player, String unclaimfinderChoose) {
        YamlConfiguration yamlConfiguration = FileManager.getValues().get(Files.Effects);

        for (String effects : yamlConfiguration.getKeys(false)) {
            if (type.toUpperCase().equals(effects)) {
                if (effects.equals("BALLOON")) {
                    setName(player, yamlConfiguration.getString(effects + ".name"));
                    setLores(player, yamlConfiguration.getString(effects + ".lore").replaceAll("%CURRENTDURABILITY%", yamlConfiguration.getString(effects + ".durability")).replaceAll("%MAXDURABILITY%", yamlConfiguration.getString(effects + ".durability")));
                } else if (effects.equals("BALLOON")) {
                    setName(player, yamlConfiguration.getString(effects + ".name"));
                } else if (effects.equals("UNCLAIMFINDER")) {
                    for (String unclaimfinders : yamlConfiguration.getConfigurationSection(effects).getKeys(false)) {
                        if (unclaimfinders.equalsIgnoreCase(unclaimfinderChoose)) {
                            if(yamlConfiguration.getBoolean(effects + "." + unclaimfinders + ".enable")) {
                                setLores(player, "§7Durabilité: §a" + yamlConfiguration.getInt(effects + "." + unclaimfinders + ".durability") + "§7/§c" + yamlConfiguration.getInt(effects + "." + unclaimfinders + ".durability"));
                                setLores(player, yamlConfiguration.getString(effects + "." + unclaimfinders + ".lore").replaceAll("&", "§"));
                            } else {
                                player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.Effects.UnclaimFinderDisabled"));
                            }
                        }
                    }
                } else {
                    setLores(player, yamlConfiguration.getString(effects + ".lore"));
                }
            }
        }

    }

    public static void setName(Player player, String name) {
        if (!player.getInventory().getItemInHand().getType().equals(Material.AIR)) {
            ItemMeta m = player.getItemInHand().getItemMeta();
            m.setDisplayName(name);
            player.getItemInHand().setItemMeta(m);
            player.updateInventory();
            player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.Effects.ApplyEffect"));
        } else {
            player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.Effects.NoItemInHand"));
        }
    }

    public static void setLores(Player player, String lores) {
        if (!player.getInventory().getItemInHand().getType().equals(Material.AIR)) {
            ItemMeta m = player.getItemInHand().getItemMeta();
            List<String> list = new ArrayList<>();
            if (m.hasLore())
                list.addAll(m.getLore());
            list.add(lores);
            m.setLore(list);
            player.getItemInHand().setItemMeta(m);
            player.updateInventory();
            player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.Effects.ApplyEffect"));
        } else {
            player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.Effects.NoItemInHand"));
        }
    }
}
