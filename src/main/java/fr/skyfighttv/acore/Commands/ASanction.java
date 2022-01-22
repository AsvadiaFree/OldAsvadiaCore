package fr.skyfighttv.acore.Commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.ChadOW.cinventory.CInventory;
import fr.ChadOW.cinventory.CItem;
import fr.ChadOW.cinventory.ItemCreator;
import fr.skyfighttv.acore.Main;
import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class ASanction implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            YamlConfiguration config = FileManager.getValues().get(Files.Config);

            if(player.hasPermission(config.getString("General.Permissions.Sanction"))) {
                if(args.length != 0) {
                    if(Bukkit.getPlayer(args[0]) != null) {
                        Player victim = Bukkit.getPlayer(args[0]);

                        sanctionGui(player, victim);

                        return true;
                    }
                }
                player.sendMessage(config.getString("General.Messages.Sanction.NotFullCommand"));
            }
        }
        return false;
    }

    public static void sanctionGui(Player player, Player victim) {
        YamlConfiguration config = FileManager.getValues().get(Files.Sanction);
        CInventory inventory = new CInventory(54, config.getString("Title"));

        for(int i = 9; i < 18; i++) {
            CItem cItem = new CItem(new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, 7)).setSlot(i);
            inventory.addElement(cItem);
        }

        for (String groups : config.getConfigurationSection("Groups").getKeys(false)) {
            if (player.hasPermission(config.getString("Groups." + groups + ".Permission"))) {
                CItem chat = new CItem(new ItemCreator(Material.getMaterial(config.getString("Groups." + groups + ".Material")), 0).setName(config.getString("Groups." + groups + ".Name")).setLores(config.getStringList("Groups." + groups + ".Lore"))).setSlot(config.getInt("Groups." + groups + ".Slot"));
                chat.addEvent((cInventory, cItem, player1, clickContext) -> {
                    cleanInventory(cInventory);
                    Reasons(inventory, victim, player, groups);
                });
                inventory.addElement(chat);
            }
        }

        inventory.open(player);
    }

    private static void cleanInventory(CInventory inventory) {
        for(int i = 18; i < 53; i++) {
            CItem cItem = new CItem(new ItemCreator(Material.AIR, 0)).setSlot(i);
            inventory.addElement(cItem);
        }
    }

    private static void Reasons(CInventory inventory, Player victim, Player player, String group) {
        YamlConfiguration config = FileManager.getValues().get(Files.Sanction);

        for (String reasons : config.getConfigurationSection("Groups." + group + ".Reasons").getKeys(false)) {
            String pathChatSanction = "Groups." + group + ".Reasons." + reasons;
            if (player.hasPermission(config.getString(pathChatSanction + ".Permission"))) {
                CItem cItem = new CItem(new ItemCreator(Material.matchMaterial(config.getString(pathChatSanction + ".Material")), 0).setName(config.getString(pathChatSanction + ".Name")).setLores(config.getStringList(pathChatSanction + ".Lore"))).setSlot(config.getInt(pathChatSanction + ".Slot"));
                cItem.addEvent((cInventory, cItem1, player1, clickContext) -> {
                    String time = config.getString(pathChatSanction + ".Sanction.Time");
                    /*long timeint = 0;
                    if(time.toLowerCase().contains("d"))
                        timeint = Integer.parseInt(time.toLowerCase().replaceAll("d", "")) * 86400000;
                    else if(time.toLowerCase().contains("h"))
                        timeint = Integer.parseInt(time.toLowerCase().replaceAll("h", "")) * 3600000;
                    else if(time.toLowerCase().contains("m"))
                        timeint = Integer.parseInt(time.toLowerCase().replaceAll("m", "")) * 60000;
                    else if(time.toLowerCase().contains("s"))
                        timeint = Integer.parseInt(time.toLowerCase().replaceAll("s", "")) * 1000;*/
                    sanction(config.getString(pathChatSanction + ".Sanction.Type"), time, victim, player1, config.getString(pathChatSanction + ".Sanction.Reason"));
                    player1.sendMessage(config.getString(pathChatSanction + ".Messages.Modo").replaceAll("%PLAYER%", victim.getName()));
                });
                inventory.addElement(cItem);
            }
        }
    }

    private static void sanction(String type, String time, Player player, Player modo, String Reason) {
        //new Punishment(player.getName(), player.getName().toLowerCase(), Reason, modo.getName(), PunishmentType.valueOf(type), System.currentTimeMillis(), System.currentTimeMillis() + time, "", -1).create();

        //Bukkit.dispatchCommand(modo, type.replaceAll("_", "").toLowerCase() + " " + player.getName() + " " + time + " " + Reason);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("sanction");

        out.writeUTF(type.replaceAll("_", "").toLowerCase());
        out.writeUTF(player.getName());
        out.writeUTF(time);
        out.writeUTF(Reason);

        modo.sendPluginMessage(Main.getInstances(), "BungeeCord", out.toByteArray());
    }
}