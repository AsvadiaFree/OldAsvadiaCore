package fr.skyfighttv.acore.Commands.SubCommand;

import fr.ChadOW.cinventory.ItemCreator;
import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CoreSetPermission {
    public static void setCustomPermission(ItemStack permissionItem, Player player) {
        YamlConfiguration yamlConfiguration = FileManager.getValues().get(Files.Permissions);

        List<ItemStack> list1 = ((List<ItemStack>) yamlConfiguration.getList("Use"));
        list1.add(new ItemCreator(permissionItem).setAmount(1).getItem());
        yamlConfiguration.set("Use", list1);
        List<String> list2 = yamlConfiguration.getStringList("Break");
        list2.add(permissionItem.getType().name());
        yamlConfiguration.set("Break", list2);
        List<String> list3 = yamlConfiguration.getStringList("Place");
        list3.add(permissionItem.getType().name());
        yamlConfiguration.set("Place", list3);
        List<ItemStack> list4 = ((List<ItemStack>) yamlConfiguration.getList("Take"));
        list4.add(new ItemCreator(permissionItem).setAmount(1).getItem());
        yamlConfiguration.set("Take", list4);

        FileManager.save(Files.Permissions);
        player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.Permissions.SuccessfulAddedCustomPermission"));
    }
}
