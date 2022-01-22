package fr.skyfighttv.acore.Commands;

import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class AHoeSell implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            YamlConfiguration config = FileManager.getValues().get(Files.HoeSell);
            YamlConfiguration mainConfig = FileManager.getValues().get(Files.Config);

            if(player.isOp()) {
                ItemStack itemStack = new ItemStack(Material.getMaterial(config.getString("Item-Type")));
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(config.getString("Name"));
                List<String> lore = config.getStringList("Lore");
                for(int i = 0; i != lore.size(); i++) {
                    if (lore.get(i).contains("%USE%")) {
                        lore.set(i, lore.get(i).replace("%USE%", "" + config.getInt("Lore-Utilisation")));
                        break;
                    }
                }
                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);
                player.getInventory().addItem(itemStack);
                player.sendMessage(mainConfig.getString("General.Messages.HoeSell.Give-Item"));
            } else {
                player.sendMessage(mainConfig.getString("General.Messages.HoeSell.Command-HoeSell-No-Permission"));
            }
        }
        return false;
    }
}
