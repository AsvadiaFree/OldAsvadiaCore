package fr.skyfighttv.acore.Commands.SubCommand;

import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CoreGetItemstack {
    public static void getCustomItemStack(Player player) {
        YamlConfiguration yamlConfiguration = FileManager.getValues().get(Files.ItemStacks);

        yamlConfiguration.set("ItemStacks." + yamlConfiguration.getKeys(false).size(), player.getInventory().getItemInHand());

        FileManager.save(Files.ItemStacks);

        player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.ItemStacks.SuccessfulGetCustomItemStack"));
    }
}
