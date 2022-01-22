package fr.skyfighttv.acore.Listeners;

import fr.ChadOW.cinventory.CInventory;
import fr.ChadOW.cinventory.CItem;
import fr.ChadOW.cinventory.ItemCreator;
import fr.skyfighttv.acore.Commands.AStaff;
import fr.skyfighttv.acore.Main;
import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import fr.skyfighttv.acore.Utils.Staff.FreezeGUI;
import fr.skyfighttv.acore.Utils.XRay;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StaffListeners implements Listener {
    private List<Player> vanishPlayers = new ArrayList<>();
    private List<Player> canOpenPlayerGui = new ArrayList<>();
    private List<Player> canFreezePlayer = new ArrayList<>();
    private List<Player> freezePlayers = new ArrayList<>();
    private HashMap<Player, Boolean> isStaffMod = AStaff.isStaffMod;

    @EventHandler
    private void onClick(PlayerInteractEvent event) {
        if(isStaffMod.containsKey(event.getPlayer())) {
            if (isStaffMod.get(event.getPlayer())) {
                Player player = event.getPlayer();
                ItemStack item = event.getItem();
                Action action = event.getAction();
                YamlConfiguration config = FileManager.getValues().get(Files.Staff);
                YamlConfiguration mainConfig = FileManager.getValues().get(Files.Config);

                if (item != null
                        && item.hasItemMeta()
                        && item.getItemMeta().hasLore()) {
                    if (item.getItemMeta().getLore().equals(config.getStringList("Stuff.2.Lore"))) {
                        List<Player> randomPlayer = new ArrayList<>(Bukkit.getOnlinePlayers());
                        randomPlayer.remove(player);
                        if (randomPlayer.size() != 0) {
                            int random = (int)(Math.random() * randomPlayer.size());
                            player.teleport(randomPlayer.get(random));
                            player.sendMessage(mainConfig.getString("General.Messages.Staff.SuccessfulTeleport").replaceAll("%PLAYER%", randomPlayer.get(random).getName()));
                        } else
                            player.sendMessage(mainConfig.getString("General.Messages.Staff.AnyPlayerFound"));
                    }
                    else if (item.getItemMeta().getLore().equals(config.getStringList("Stuff.4.Lore"))) {
                        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                            if (vanishPlayers.contains(player)) {
                                for (Player player1 : Bukkit.getOnlinePlayers()) {
                                    player1.showPlayer(player);
                                }

                                for (Player vanishedPlayer : vanishPlayers) {
                                    player.hidePlayer(vanishedPlayer);
                                }

                                vanishPlayers.remove(player);

                                ItemMeta meta = player.getItemInHand().getItemMeta();
                                meta.removeEnchant(Enchantment.DURABILITY);
                                player.getItemInHand().setItemMeta(meta);

                                player.setGameMode(GameMode.SURVIVAL);
                                player.sendMessage(mainConfig.getString("General.Messages.Staff.VanishOFF"));
                            } else if (!vanishPlayers.contains(player)) {
                                for (Player player1 : Bukkit.getOnlinePlayers()) {
                                    player1.hidePlayer(player);
                                }
                                for (Player vanishedPlayer : vanishPlayers) {
                                    vanishedPlayer.showPlayer(player);
                                    player.showPlayer(vanishedPlayer);
                                }
                                vanishPlayers.add(player);

                                ItemMeta meta = player.getItemInHand().getItemMeta();
                                meta.addEnchant(Enchantment.DURABILITY, 1, false);
                                player.getItemInHand().setItemMeta(meta);

                                player.setGameMode(GameMode.CREATIVE);
                                player.sendMessage(mainConfig.getString("General.Messages.Staff.VanishOn"));
                            }
                        }
                    }
                    else if (item.getItemMeta().getLore().equals(config.getStringList("Stuff.6.Lore"))) {
                        if (event.getClickedBlock() != null
                                && event.getClickedBlock().getType().isInteractable()
                                && event.getAction() == Action.RIGHT_CLICK_BLOCK
                                && !canOpenPlayerGui.contains(player)) {
                            canOpenPlayerGui.add(player);

                            CInventory chest = new CInventory(54, "§2Inspection du coffre");

                            for (ItemStack chestItem : ((Chest) event.getClickedBlock().getState()).getInventory().getContents()) {
                                chest.addElement(new CItem(new ItemCreator(chestItem)));
                            }

                            chest.open(player);

                            Bukkit.getServer().getScheduler().runTaskLater(Main.getInstances(), () -> {
                                canOpenPlayerGui.remove(player);
                            }, 10);
                        }
                    }
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    private void onClick(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if(event.getRightClicked() instanceof Player && isStaffMod.containsKey(player) && isStaffMod.get(player)) {
            Player clickedPlayer = (Player) event.getRightClicked();
            ItemStack item = event.getPlayer().getItemInHand();
            YamlConfiguration config = FileManager.getValues().get(Files.Staff);
            YamlConfiguration mainConfig = FileManager.getValues().get(Files.Config);

            if (item != null
                    && item.hasItemMeta()
                    && item.getItemMeta().hasLore()) {

                if (item.getItemMeta().getLore().equals(config.getStringList("Stuff.6.Lore"))) {
                    if (!canOpenPlayerGui.contains(player)) {
                        canOpenPlayerGui.add(player);
                        AStaff.guiPlayer(player, clickedPlayer);
                        Bukkit.getServer().getScheduler().runTaskLater(Main.getInstances(), ( ) -> {
                            canOpenPlayerGui.remove(player);
                        }, 10);
                    }
                } else if (item.getItemMeta().getLore().equals(config.getStringList("Stuff.8.Lore"))) {
                    if(!canFreezePlayer.contains(player)) {
                        canFreezePlayer.add(player);
                        if (!freezePlayers.contains(clickedPlayer)) {
                            clickedPlayer.sendMessage(mainConfig.getString("General.Messages.Staff.ClickedPlayerFreezeOn").replaceAll("%PLAYER%", player.getName()));
                            player.sendMessage(mainConfig.getString("General.Messages.Staff.FreezeOn").replaceAll("%PLAYER%", clickedPlayer.getName()));

                            freezePlayers.add(clickedPlayer);

                            new FreezeGUI(clickedPlayer, player);
                        } else {
                            freezePlayers.remove(clickedPlayer);

                            FreezeGUI.close(clickedPlayer);

                            clickedPlayer.sendMessage(mainConfig.getString("General.Messages.Staff.ClickedPlayerFreezeOFF").replaceAll("%PLAYER%", player.getName()));
                            player.sendMessage(mainConfig.getString("General.Messages.Staff.FreezeOFF").replaceAll("%PLAYER%", clickedPlayer.getName()));
                        }
                        Bukkit.getServer().getScheduler().runTaskLater(Main.getInstances(), () -> {
                            canFreezePlayer.remove(player);
                        }, 10);
                    }
                }
            }
        }
    }

    @EventHandler
    private void onCommand(PlayerCommandPreprocessEvent event) {
        if (freezePlayers.contains(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (freezePlayers.contains(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        for (Player vanish : vanishPlayers)
            event.getPlayer().hidePlayer(vanish);
    }

    @EventHandler
    private void onPickup(PlayerPickupItemEvent event) {
        if((isStaffMod.containsKey(event.getPlayer()) && isStaffMod.get(event.getPlayer())))
            event.setCancelled(true);
    }

    @EventHandler
    private void onDrop(PlayerDropItemEvent event) {
        if((isStaffMod.containsKey(event.getPlayer()) && isStaffMod.get(event.getPlayer())))
            event.setCancelled(true);
    }

    @EventHandler
    private void onDamage(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player)
            if(freezePlayers.contains(event.getDamager()) || freezePlayers.contains(event.getEntity()))
                event.setCancelled(true);
    }

    @EventHandler
    private void onDamageStaff(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player)
            if (isStaffMod.containsKey(event.getEntity()) && isStaffMod.get(event.getEntity()))
                event.setCancelled(true);
    }

    @EventHandler
    private void onChangeWorld(PlayerChangedWorldEvent event) {
        if (isStaffMod.containsKey(event.getPlayer()) && isStaffMod.get(event.getPlayer()))
            event.getPlayer().setGameMode(GameMode.CREATIVE);
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        if (isStaffMod.containsKey(event.getPlayer()) && isStaffMod.get(event.getPlayer())) {
            File staffPlayerFile = new File(Main.getInstances().getDataFolder() + "/StaffPlayers/" + event.getPlayer().getName() + ".yml");
            YamlConfiguration staffPlayerConfig = YamlConfiguration.loadConfiguration(staffPlayerFile);

            AStaff.disableStaffMod(event.getPlayer(), staffPlayerFile, staffPlayerConfig);
        }
    }

    @EventHandler
    private void onInteract(InventoryClickEvent event) {
        if ((isStaffMod.containsKey(event.getWhoClicked()) && isStaffMod.get(event.getWhoClicked())) && event.getClickedInventory() != null) {
            YamlConfiguration config = FileManager.getValues().get(Files.Staff);

            if (event.getClickedInventory().getHolder() == event.getWhoClicked()) {
                Player player = (Player) event.getWhoClicked();
                ItemStack current = event.getCurrentItem();

                if (current != null
                        && current.hasItemMeta()
                        && current.getItemMeta().hasLore()) {
                    if (current.getItemMeta().getLore().equals(config.getStringList("Stuff.FactionTopLuck.Lore"))) {
                        TopLuck.topLuck(player);
                        event.setCancelled(true);
                    } else if (current.getItemMeta().getLore().equals(config.getStringList("Stuff.XRay.Lore"))) {
                        event.setCancelled(true);
                        player.updateInventory();
                        if (!current.getItemMeta().hasEnchants()) {
                            current.addEnchantment(Enchantment.DURABILITY, 1);
                            XRay.on(player);
                        } else {
                            current.setType(Material.AIR);
                            player.getInventory().setItem(config.getInt("Stuff.XRay.Slot"), new ItemCreator(Material.getMaterial(config.getString("Stuff.XRay.Material")), 0).setName(config.getString("Stuff.XRay.Name")).setLores(config.getStringList("Stuff.XRay.Lore")).getItem());
                            XRay.off(player);
                        }
                    }
                }
                event.setCancelled(true);
            } else if (event.getView().getTitle().contains(config.getString("Gui.Player.Title").replaceAll("%PLAYER%", ""))) {
                event.setCancelled(true);
            }
        }
    }
}