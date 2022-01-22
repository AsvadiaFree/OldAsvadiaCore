package fr.skyfighttv.acore.Commands;

import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ACustomCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        YamlConfiguration config = FileManager.getValues().get(Files.CustomCommands);

        for (String commands : config.getKeys(false)) {
            if (commands.equalsIgnoreCase(cmd.getName())) {
                if (sender instanceof Player) {
                    if (config.getBoolean(commands + ".playercommand")) {
                        Bukkit.dispatchCommand(sender, config.getString(commands + ".command"));
                    } else {
                        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), config.getString(commands + ".command").replaceAll("%PLAYER%", sender.getName()));
                    }
                } else if (!config.getBoolean(commands + ".onlyplayer") && sender instanceof ConsoleCommandSender) {
                    Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), config.getString(commands + ".command").replaceAll("%PLAYER%", sender.getName()));
                }
                break;
            }
        }

        return false;
    }
}
