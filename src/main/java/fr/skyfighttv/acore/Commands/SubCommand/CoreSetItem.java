package fr.skyfighttv.acore.Commands.SubCommand;

import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CoreSetItem {

    public static void setCustomDropBlock(String modifiedBlock, ItemStack newDrop, Double luck, Player player) {
        YamlConfiguration yamlConfiguration = FileManager.getValues().get(Files.CustomDrops);

        if (!yamlConfiguration.contains("Block." + modifiedBlock)) {
            yamlConfiguration.createSection("Block." + modifiedBlock);
        }

        ConfigurationSection allChance = yamlConfiguration.getConfigurationSection("Block." + modifiedBlock);
        String chance = String.valueOf(getChance(allChance, luck)).replaceAll("\\.", ",");

        yamlConfiguration.set("Block." + modifiedBlock + "." + chance, newDrop);

        FileManager.save(Files.CustomDrops);
        player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.CustomDrops.SuccessfulAddedCustomBlock"));
    }

    public static void setCustomDropEntity(String modifiedEntity, ItemStack newDrop, Double luck, Player player) {

        YamlConfiguration yamlConfiguration = FileManager.getValues().get(Files.CustomDrops);

        if (!yamlConfiguration.contains("Entity." + modifiedEntity)) {
            yamlConfiguration.createSection("Entity." + modifiedEntity);
        }

        ConfigurationSection allChance = yamlConfiguration.getConfigurationSection("Entity." + modifiedEntity);
        String chance = String.valueOf(getChance(allChance, luck)).replaceAll("\\.", ",");

        yamlConfiguration.set("Entity." + modifiedEntity+ "." + chance, newDrop);

        FileManager.save(Files.CustomDrops);
        player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.CustomDrops.SuccessfulAddedCustomEntity"));
    }

    public static void setCustomDropPlantation(String modifiedEntity, ItemStack newDrop, Double luck, Player player) {
        YamlConfiguration yamlConfiguration = FileManager.getValues().get(Files.CustomDrops);

        if (!yamlConfiguration.contains("Plantation." + modifiedEntity)) {
            yamlConfiguration.createSection("Plantation." + modifiedEntity);
        }

        ConfigurationSection allChance = yamlConfiguration.getConfigurationSection("Plantation." + modifiedEntity);
        String chance = String.valueOf(getChance(allChance, luck)).replaceAll("\\.", ",");

        yamlConfiguration.set("Plantation." + modifiedEntity+ "." + chance, newDrop);

        FileManager.save(Files.CustomDrops);
        player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.CustomDrops.SuccessfulAddedCustomPlantation"));
    }

    private static double getChance(ConfigurationSection allChance, Double luck) {
        List<String> loots = new ArrayList<>(allChance.getKeys(false));
        double chance = 0.0;
        for (String str : loots) {
            chance += Double.parseDouble(str);
        }
        chance += luck;
        return chance;
    }
}
