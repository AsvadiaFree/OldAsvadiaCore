package fr.skyfighttv.acore.Commands.SubCommand;

import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import org.bukkit.entity.Player;

public class CoreReload {
    public static void initialize(Player player, String type) {
        if (type != null) {
            if (type.equalsIgnoreCase("all")) {
                reloadAll();
                player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.General.ReloadAll"));
            } else if (Files.valueOf(type) != null) {
                String types = type;
                if (types.equalsIgnoreCase("config"))
                    types = "Config";
                reload(types);
                player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.General.UniqueReload").replaceAll("%CONFIG%", type));
            } else {
                player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.General.ReloadConfigNotFound"));
            }
        }
    }

    private static void reloadAll() {
        for (Files files : Files.values()) {
            FileManager.reload(files);
        }
    }

    private static void reload(String type) {
        FileManager.reload(Files.valueOf(type));
    }
}
