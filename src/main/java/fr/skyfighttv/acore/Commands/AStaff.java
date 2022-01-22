package fr.skyfighttv.acore.Commands;

import fr.ChadOW.cinventory.CInventory;
import fr.ChadOW.cinventory.CItem;
import fr.ChadOW.cinventory.ItemCreator;
import fr.skyfighttv.acore.Main;
import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import fr.skyfighttv.acore.Listeners.TopLuck;
import fr.skyfighttv.acore.Utils.XRay;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AStaff implements CommandExecutor {
    public static HashMap<Player, Boolean> isStaffMod = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender Sender, Command cmd, String label, String[] args) {
        if(Sender instanceof Player) {
            Player player = (Player) Sender;
            if(player.hasPermission(FileManager.getValues().get(Files.Config).getString("General.Permissions.Staff"))) {
                File staffPlayerFile = new File(Main.getInstances().getDataFolder() + "/StaffPlayers/" + player.getName() + ".yml");
                YamlConfiguration staffPlayerConfig = YamlConfiguration.loadConfiguration(staffPlayerFile);
                if(!staffPlayerFile.exists()) {
                    try {
                        staffPlayerFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    staffPlayerConfig.set("ActiveStaff", false);

                    try {
                        staffPlayerConfig.save(staffPlayerFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(!staffPlayerConfig.getBoolean("ActiveStaff")) {
                    enableStaffMod(player, staffPlayerFile, staffPlayerConfig);
                } else {
                    disableStaffMod(player, staffPlayerFile, staffPlayerConfig);
                }
            } else {
                player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.Staff.NoPermission"));
            }
        }
        return false;
    }

    private void enableStaffMod(Player player, File staffPlayerFile, YamlConfiguration staffPlayerConfig) {
        YamlConfiguration config = FileManager.getValues().get(Files.Staff);

        staffPlayerConfig.set("ActiveStaff", true);
        staffPlayerConfig.set("Stuff.Inventory", player.getInventory().getContents());

        isStaffMod.put(player, true);

        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[0]);
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, true, false));

        player.setGameMode(GameMode.CREATIVE);

        try {
            staffPlayerConfig.save(staffPlayerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (player.hasPermission(config.getString("Stuff.0.Permission")))
            player.getInventory().setItem(config.getInt("Stuff.0.Slot"), new ItemCreator(Material.getMaterial(config.getString("Stuff.0.Material")), 0).addEnchantment(Enchantment.KNOCKBACK, config.getInt("Stuff.0.Enchantement.Level")).setName(config.getString("Stuff.0.Name")).setLores(config.getStringList("Stuff.0.Lore")).getItem());
        if (player.hasPermission(config.getString("Stuff.2.Permission")))
            player.getInventory().setItem(config.getInt("Stuff.2.Slot"), new ItemCreator(Material.getMaterial(config.getString("Stuff.2.Material")), 0).setName(config.getString("Stuff.2.Name")).setLores(config.getStringList("Stuff.2.Lore")).getItem());
        if (player.hasPermission(config.getString("Stuff.4.Permission")))
            player.getInventory().setItem(config.getInt("Stuff.4.Slot"), new ItemCreator(Material.getMaterial(config.getString("Stuff.4.Material")), 0).setName(config.getString("Stuff.4.Name")).setLores(config.getStringList("Stuff.4.Lore")).getItem());
        if (player.hasPermission(config.getString("Stuff.6.Permission")))
            player.getInventory().setItem(config.getInt("Stuff.6.Slot"), new ItemCreator(Material.getMaterial(config.getString("Stuff.6.Material")), 0).setName(config.getString("Stuff.6.Name")).setLores(config.getStringList("Stuff.6.Lore")).getItem());
        if (player.hasPermission(config.getString("Stuff.8.Permission")))
            player.getInventory().setItem(config.getInt("Stuff.8.Slot"), new ItemCreator(Material.getMaterial(config.getString("Stuff.8.Material")), 0).setName(config.getString("Stuff.8.Name")).setLores(config.getStringList("Stuff.8.Lore")).getItem());

        player.getInventory().setItem(11, new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, 0).setName("§7-").getItem());
        player.getInventory().setItem(20, new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, 0).setName("§7-").getItem());
        player.getInventory().setItem(29, new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, 0).setName("§7-").getItem());

        if (player.hasPermission(config.getString("Stuff.FactionTopLuck.Permission")))
            player.getInventory().setItem(config.getInt("Stuff.FactionTopLuck.Slot"), new ItemCreator(Material.getMaterial(config.getString("Stuff.FactionTopLuck.Material")), 0).setName(config.getString("Stuff.FactionTopLuck.Name")).setLores(config.getStringList("Stuff.FactionTopLuck.Lore")).getItem());

        if (FileManager.getValues().get(Files.Config).getBoolean("General.EnableModules.XRay") && player.hasPermission(FileManager.getValues().get(Files.Config).getString("General.Permissions.XRay")))
            player.getInventory().setItem(config.getInt("Stuff.XRay.Slot"), new ItemCreator(Material.getMaterial(config.getString("Stuff.XRay.Material")), 0).setName(config.getString("Stuff.XRay.Name")).setLores(config.getStringList("Stuff.XRay.Lore")).getItem());

    }

    public static void disableStaffMod(Player player, File staffPlayerFile, YamlConfiguration staffPlayerConfig) {
        player.getInventory().clear();
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        XRay.off(player);

        final List<ItemStack> itemStackList = new ArrayList<>();
        for (Object kit : staffPlayerConfig.getList("Stuff.Inventory")) {
            itemStackList.add((ItemStack) kit);
        }
        ItemStack[] itemStacks = itemStackList.toArray(new ItemStack[0]);
        player.getInventory().setContents(itemStacks);

        player.setGameMode(GameMode.SURVIVAL);

        staffPlayerConfig.set("ActiveStaff", false);
        staffPlayerConfig.set("Stuff.Inventory", new ArrayList<>());

        isStaffMod.put(player, false);

        try {
            staffPlayerConfig.save(staffPlayerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void guiPlayer(Player player, Player clickedPlayer) {
        YamlConfiguration config = FileManager.getValues().get(Files.Staff);

        CInventory inventory = new CInventory(54, config.getString("Gui.Player.Title").replaceAll("%PLAYER%", clickedPlayer.getName()));

        TopLuck.topLuckUpdate(new ArrayList<>(Bukkit.getOnlinePlayers()));

        List<String> lore = new ArrayList<>(Arrays.asList("Global Score : " + TopLuck.playerGlobalScore.get(clickedPlayer.getName())
                , " "
                , "Azonium : (" + TopLuck.playerAzoriumCount.get(clickedPlayer.getName()) + ") " + TopLuck.playerAzoriumScore.get(clickedPlayer.getName())
                , "Topaze : (" + TopLuck.playerTopazeCount.get(clickedPlayer.getName()) + ") " + TopLuck.playerTopazeScore.get(clickedPlayer.getName())
                , "Rubis : (" + TopLuck.playerRubisCount.get(clickedPlayer.getName()) + ") " + TopLuck.playerRubisScore.get(clickedPlayer.getName())
                , "Saphir : (" + TopLuck.playerSaphirCount.get(clickedPlayer.getName()) + ") " + TopLuck.playerSaphirScore.get(clickedPlayer.getName())));
        CItem clickPlayerSkull = new CItem(new ItemCreator(Material.LEGACY_SKULL_ITEM, 3).setName("§6" + clickedPlayer.getName()).setLores(lore).setOwner(clickedPlayer.getName()))
                .setSlot(0);
        inventory.addElement(clickPlayerSkull);

        CItem teleportPlayer = new CItem(new ItemCreator(Material.ENDER_PEARL, 0).setName("§eTeleportation à : §6" + clickedPlayer.getName()))
                .setSlot(1);
        teleportPlayer.addEvent((cInventory, cItem, player1, clickContext) -> {
            player.teleport(clickedPlayer);
        });
        inventory.addElement(teleportPlayer);

        CItem sanctionPlayer = new CItem(new ItemCreator(Material.NETHERITE_SHOVEL, 0).setName("§cSanction : §6" + clickedPlayer.getName()))
                .setSlot(2);
        sanctionPlayer.addEvent((cInventory, cItem, player1, clickContext) -> {
            ASanction.sanctionGui(player, clickedPlayer);
        });
        inventory.addElement(sanctionPlayer);

        CItem topLuck = new CItem(new ItemCreator(Material.DIAMOND_PICKAXE, 0).setName("§eTopLuck"))
                .setSlot(3);
        topLuck.addEvent((cInventory, cItem, player1, clickContext) -> {
            TopLuck.topLuck(player);
        });
        inventory.addElement(topLuck);

        for (int i = 4; i != 14; i++) {
            CItem glassPane = new CItem(new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, 7))
                    .setSlot(i);
            inventory.addElement(glassPane);
        }

        List<ItemStack> contents = new ArrayList<>(Arrays.asList(clickedPlayer.getInventory().getContents()));

        for (int i = 0; i < 9; i++) {
            if(contents.get(i) != null) {
                CItem item = new CItem(new ItemCreator(contents.get(i)))
                        .setSlot(45 + i);
                inventory.addElement(item);
            }
        }


        for (int i = 0; i < 27; i++) {
            if(contents.get(i + 9) != null) {
                CItem item = new CItem(new ItemCreator(contents.get(i + 9)))
                        .setSlot(18 + i);
                inventory.addElement(item);
            }
        }

        for (int i = 0; i < 4; i++) {
            if(contents.get(i + contents.size() - 5) != null) {
                CItem item = new CItem(new ItemCreator(contents.get(i + contents.size() - 5)))
                        .setSlot(17 - i);
                inventory.addElement(item);
            }
        }

        inventory.open(player);
    }
}
