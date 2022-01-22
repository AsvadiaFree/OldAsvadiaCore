package fr.skyfighttv.acore.Listeners;

import com.massivecraft.factions.*;
import fr.ChadOW.cinventory.ItemCreator;
import fr.skyfighttv.acore.Manager.HammerManager;
import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import fr.skyfighttv.acore.Utils.Plantations;
import org.bukkit.CropState;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Crops;

import java.util.*;

public class DropsPermsItems implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    private void BreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(!event.isCancelled() && player.getGameMode() == GameMode.SURVIVAL) {
            if (player.getItemInHand() != null
                    && player.getItemInHand().hasItemMeta()
                    && player.getItemInHand().getItemMeta().hasLore()
                    && player.getItemInHand().getItemMeta().getLore().contains(FileManager.getValues().get(Files.Effects).getString("HAMMER.lore"))) {
                FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
                Faction board = Board.getInstance().getFactionAt(new FLocation(player.getWorld().getName(), event.getBlock().getX(), event.getBlock().getZ()));
                if (new HammerManager().getPitch(player).equalsIgnoreCase("H")) {
                    boolean canBreak = verifBlock(player, event);
                    if (canBreak && (board.isWilderness() || board == fPlayer.getFaction()))
                        new HammerManager().mineBH(event);
                } else if (new HammerManager().getPitch(player).equalsIgnoreCase("B")) {
                    boolean canBreak = verifBlock(player, event);
                    if (canBreak && (board.isWilderness() || board == fPlayer.getFaction()))
                        new HammerManager().mineBH(event);
                } else if (new HammerManager().getDirection(player).equalsIgnoreCase("n")) {
                    boolean canBreak = verifBlock(player, event);
                    if (canBreak && (board.isWilderness() || board == fPlayer.getFaction()))
                        new HammerManager().mineNS(event);
                } else if (new HammerManager().getDirection(player).equalsIgnoreCase("s")) {
                    boolean canBreak = verifBlock(player, event);
                    if (canBreak && (board.isWilderness() || board == fPlayer.getFaction()))
                        new HammerManager().mineNS(event);
                } else if (new HammerManager().getDirection(player).equalsIgnoreCase("w")) {
                    boolean canBreak = verifBlock(player, event);
                    if (canBreak && (board.isWilderness() || board == fPlayer.getFaction()))
                        new HammerManager().mineWE(event);
                } else if (new HammerManager().getDirection(player).equalsIgnoreCase("e")) {
                    boolean canBreak = verifBlock(player, event);
                    if (canBreak && (board.isWilderness() || board == fPlayer.getFaction()))
                        new HammerManager().mineWE(event);
                }
            } else {
                verifBlock(player, event);
            }
        }
    }

    private boolean verifBlock(Player player, BlockBreakEvent event) {
        for (Plantations plantations : Plantations.values())
            if (plantations.getMaterial().equals(event.getBlock().getLocation().add(0, 1, 0).getBlock().getType())) {
                event.setCancelled(true);
                return false;
            }

        FileConfiguration config = FileManager.getValues().get(Files.CustomDrops);

        if (FileManager.getValues().get(Files.Config).getBoolean("General.EnableModules.Permissions") && FileManager.getValues().get(Files.Permissions).getStringList("Break").contains(event.getBlock().getType().name())) {
            if (!player.hasPermission(FileManager.getValues().get(Files.Config).getString("General.Permissions.Permissions.Break").replaceAll("%ID%", event.getBlock().getType().name()))) {
                player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.Permissions.CantBreak"));
                event.setCancelled(true);
                return false;
            }
        }

        for (String type : new ArrayList<>(Arrays.asList("Block", "Plantation"))) {
            if (config.contains(type + "." + event.getBlock().getType().name())) {

                if (type.equals("Plantation")) {
                    if (!event.getBlock().getType().equals(Material.NETHER_WART)) {
                        Crops crops = (Crops) event.getBlock().getState().getData();
                        if (!crops.getState().equals(CropState.RIPE)) {
                            return false;
                        }
                    } else if (event.getBlock().getData() < 3) {
                        return false;
                    }
                }

                double random = new Random().nextInt(100) + new Random().nextDouble();

                List<String> loots = new ArrayList<>(config.getConfigurationSection(type + "." + event.getBlock().getType().name()).getKeys(false));

                for (String str : loots) {
                    if (Double.parseDouble(str.replaceAll(",", ".")) >= random) {
                        ItemStack item = config.getItemStack(type + "." + event.getBlock().getType().name() + "." + str);

                        if (config.getStringList("FortunableBlock").contains(event.getBlock().getType().name()) && player.getItemInHand() != null && player.getItemInHand().getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {
                            switch (player.getItemInHand().getEnchantments().get(Enchantment.LOOT_BONUS_BLOCKS)) {
                                case 1:
                                    if ((new Random()).nextInt(99) + 1 <= 66) {
                                        item.setAmount(1);
                                    } else {
                                        item.setAmount(2);
                                    }
                                case 2:
                                    int number = (new Random()).nextInt(99) + 1;
                                    if (number <= 50) {
                                        item.setAmount(1);
                                    } else if (number <= 75) {
                                        item.setAmount(2);
                                    } else {
                                        item.setAmount(3);
                                    }
                                case 3:
                                    int numbers = (new Random()).nextInt(99) + 1;
                                    if (numbers <= 30) {
                                        item.setAmount(1);
                                    } else if (numbers <= 70) {
                                        item.setAmount(2);
                                    } else {
                                        item.setAmount(3);
                                    }
                            }
                        }

                        event.getBlock().getWorld().dropItem(event.getBlock().getLocation().add(0.5, 0, 0.5), item);
                        event.setDropItems(false);
                        break;
                    }
                }
            }
        }
        return true;
    }

    public static boolean verifBlock(Block event, Player player) {
        for (Plantations plantations : Plantations.values())
            if (plantations.getMaterial().equals(event.getLocation().add(0, 1, 0).getBlock().getType())) {
                return false;
            }

        FileConfiguration config = FileManager.getValues().get(Files.CustomDrops);

        if (FileManager.getValues().get(Files.Config).getBoolean("General.EnableModules.Permissions") && FileManager.getValues().get(Files.Permissions).getStringList("Break").contains(event.getType().name())) {
            if (!player.hasPermission(FileManager.getValues().get(Files.Config).getString("General.Permissions.Permissions.Break").replaceAll("%ID%", event.getType().name()))) {
                player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.Permissions.CantBreak"));
                return false;
            }
        }

        for (String type : new ArrayList<>(Arrays.asList("Block", "Plantation"))) {
            if (config.contains(type + "." + event.getType().name())) {

                if (type.equals("Plantation")) {
                    if (!event.getType().equals(Material.NETHER_WART)) {
                        Crops crops = (Crops) event.getState().getData();
                        if (!crops.getState().equals(CropState.RIPE)) {
                            return false;
                        }
                    } else if (event.getData() < 3) {
                        return false;
                    }
                }

                double random = new Random().nextInt(100) + new Random().nextDouble();

                List<String> loots = new ArrayList<>(config.getConfigurationSection(type + "." + event.getType().name()).getKeys(false));

                for (String str : loots) {
                    if (Double.parseDouble(str.replaceAll(",", ".")) >= random) {
                        ItemStack item = config.getItemStack(type + "." + event.getType().name() + "." + str);

                        if (config.getStringList("FortunableBlock").contains(event.getType().name()) && player.getItemInHand() != null && player.getItemInHand().getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {
                            switch (player.getItemInHand().getEnchantments().get(Enchantment.LOOT_BONUS_BLOCKS)) {
                                case 1:
                                    if ((new Random()).nextInt(99) + 1 <= 66) {
                                        item.setAmount(1);
                                    } else {
                                        item.setAmount(2);
                                    }
                                case 2:
                                    int number = (new Random()).nextInt(99) + 1;
                                    if (number <= 50) {
                                        item.setAmount(1);
                                    } else if (number <= 75) {
                                        item.setAmount(2);
                                    } else {
                                        item.setAmount(3);
                                    }
                                case 3:
                                    int numbers = (new Random()).nextInt(99) + 1;
                                    if (numbers <= 30) {
                                        item.setAmount(1);
                                    } else if (numbers <= 70) {
                                        item.setAmount(2);
                                    } else {
                                        item.setAmount(3);
                                    }
                            }
                        }

                        event.getWorld().dropItem(event.getLocation().add(0.5, 0, 0.5), item);
                        event.setType(Material.AIR);
                        break;
                    }
                }
            }
        }
        return true;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void KillEntity(EntityDeathEvent event) {
        YamlConfiguration config = FileManager.getValues().get(Files.CustomDrops);

        if(config.contains("Entity." + event.getEntityType().toString())) {
            event.getDrops().clear();
            double random = new Random().nextInt(100) + new Random().nextDouble();

            List<String> loots = new ArrayList<>(config.getConfigurationSection("Entity." + event.getEntityType().toString()).getKeys(false));

            for (String str : loots) {
                if (Double.parseDouble(str.replaceAll(",", ".")) >= random) {
                    event.getEntity().getWorld().dropItem(event.getEntity().getLocation().add(0.5, 0, 0.5), config.getItemStack("Entity." + event.getEntityType().toString() + "." + str));
                    break;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlace(BlockPlaceEvent event) {
        if (!event.isCancelled()
                && FileManager.getValues().get(Files.Config).getBoolean("General.EnableModules.Permissions")
                && FileManager.getValues().get(Files.Permissions).getStringList("Place").contains(event.getBlock().getType().name())) {
            if (!event.getPlayer().hasPermission(FileManager.getValues().get(Files.Config).getString("General.Permissions.Permissions.Place").replaceAll("%ID%", event.getBlock().getType().name()))) {
                event.getPlayer().sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.Permissions.CantPlace"));
                event.setCancelled(true);
                event.getPlayer().updateInventory();
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onUse(PlayerInteractEvent event) {
        if(event.getItem() == null)
            return;
        ItemStack itemStack = new ItemCreator(event.getItem()).setAmount(1).getItem();
        if(!event.isCancelled()
                && FileManager.getValues().get(Files.Config).getBoolean("General.EnableModules.Permissions")
                && FileManager.getValues().get(Files.Permissions).getList("Use").contains(itemStack)
                && event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            if(!event.getPlayer().hasPermission(FileManager.getValues().get(Files.Config).getString("General.Permissions.Permissions.Use").replaceAll("%ID%", itemStack.getItemMeta().getDisplayName()))) {
                event.getPlayer().sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.Permissions.CantUse"));
                event.setCancelled(true);
                event.getPlayer().updateInventory();
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onTake(PlayerPickupItemEvent event) {
        ItemStack itemStack = new ItemCreator(event.getItem().getItemStack()).setAmount(1).getItem();
        if(!event.isCancelled()
                && FileManager.getValues().get(Files.Config).getBoolean("General.EnableModules.Permissions")
                && FileManager.getValues().get(Files.Permissions).getList("Take").contains(itemStack)
                && event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            if(!event.getPlayer().hasPermission(FileManager.getValues().get(Files.Config).getString("General.Permissions.Permissions.Take").replaceAll("%ID%", itemStack.getItemMeta().getDisplayName()))) {
                event.getPlayer().sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.Permissions.CantTake"));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onHopper(InventoryPickupItemEvent event) {
        if (!event.isCancelled()
                && event.getInventory().getType() == InventoryType.HOPPER
                && FileManager.getValues().get(Files.CustomDrops).getStringList("UnHopperableItems").contains(event.getItem().getName()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPistonExtend(BlockPistonExtendEvent event) {
        for (Block blocks : event.getBlocks())
            for (Plantations plantations : Plantations.values())
                if (plantations.getMaterial().equals(blocks.getLocation().add(0, 1, 0).getBlock().getType()))
                    event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPistonRetract(BlockPistonRetractEvent event) {
        for (Block blocks : event.getBlocks())
            for (Plantations plantations : Plantations.values())
                if (plantations.getMaterial().equals(blocks.getLocation().add(0, 1, 0).getBlock().getType()))
                    event.setCancelled(true);
    }
}
