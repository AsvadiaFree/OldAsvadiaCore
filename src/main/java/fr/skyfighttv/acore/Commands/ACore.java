package fr.skyfighttv.acore.Commands;

import fr.skyfighttv.acore.Commands.SubCommand.*;
import fr.skyfighttv.acore.Main;
import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import fr.skyfighttv.acore.Utils.Messages;
import fr.skyfighttv.acore.Utils.Repair;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class ACore implements CommandExecutor {
    public static HashMap<Long, Integer> ActionBar_timer = new HashMap<>();
    public static HashMap<Long, Integer>  ActionBar_TaskID = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.isOp()) {
                if (args.length == 0) return true;
                Player player = ((Player) sender);

                if (args[0].equalsIgnoreCase("setItem")) {
                    if (args[1].equalsIgnoreCase("Block")) {
                        CoreSetItem.setCustomDropBlock(args[2], player.getInventory().getItemInHand(), Double.parseDouble(args[3].replaceAll(",", ".")), player);
                    } else if (args[1].equalsIgnoreCase("Entity")) {
                        CoreSetItem.setCustomDropEntity(args[2], player.getInventory().getItemInHand(), Double.parseDouble(args[3].replaceAll(",", ".")), player);
                    } else if (args[1].equalsIgnoreCase("Plantation")) {
                        CoreSetItem.setCustomDropEntity(args[2], player.getInventory().getItemInHand(), Double.parseDouble(args[3].replaceAll(",", ".")), player);
                    }
                }
                else if (args[0].equalsIgnoreCase("setPermission")) {
                    CoreSetPermission.setCustomPermission(player.getInventory().getItemInHand(), player);
                }
                else if (args[0].equalsIgnoreCase("getItemStack")) {
                    CoreGetItemstack.getCustomItemStack(player);
                }
                else if (args[0].equalsIgnoreCase("setEffect") && args.length != 1) {
                    String UnclaimfinderChoose = null;
                    if (args.length >= 3) {
                        UnclaimfinderChoose = args[2];
                    }

                    CoreSetEffect.setEffect(args[1], player, UnclaimfinderChoose);
                }
                else if (args[0].equalsIgnoreCase("reload")) {
                    if (args.length == 2 && args[1] != null) {
                        CoreReload.initialize(player, args[1]);
                    }
                }
                else if (args[0].equalsIgnoreCase("messages") && args.length >= 5) {
                    int arg3 = Integer.parseInt(args[3]);
                    if (args[1].equalsIgnoreCase("Actionbar")) {
                        if (args[2].equalsIgnoreCase("all") && arg3 != 0 && args[4] != null) {
                            player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.General.SuccessFullSend").replaceAll("%TYPE%", "une ActionBar").replaceAll("%PLAYER%", "tout le serveur"));
                            Long time = System.currentTimeMillis();
                            ActionBar_TaskID.put(time, Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstances(), () -> {
                                ActionBar_timer.putIfAbsent(time, 0);
                                if (ActionBar_timer.get(time) == arg3) {
                                    ActionBar_timer.remove(time);
                                    Bukkit.getServer().getScheduler().cancelTask(ActionBar_TaskID.get(time));
                                }
                                StringBuilder list = new StringBuilder(args[4].replaceAll("&", "§"));
                                if (args.length != 5)
                                    for (int i = 5; i != args.length; i++)
                                        list.append(" ").append(args[i]);

                                for (Player player1 : Bukkit.getOnlinePlayers())
                                    if (YamlConfiguration.loadConfiguration(new File(Main.getInstances().getDataFolder() + "/Players/" + player1.getName() + ".yml")).getBoolean("Toggle.ActionBar"))
                                        Messages.sendActionBar(player1, list.toString());
                                ActionBar_timer.put(time, ActionBar_timer.get(time) + 1);
                            }, 0, 20));
                        }
                        else if (Bukkit.getPlayer(args[2]) != null && arg3 != 0 && args[4] != null) {
                            player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.General.SuccessFullSend").replaceAll("%TYPE%", "une ActionBar").replaceAll("%PLAYER%", Bukkit.getPlayer(args[2]).getName()));
                            if (YamlConfiguration.loadConfiguration(new File(Main.getInstances().getDataFolder() + "/Players/" + Bukkit.getPlayer(args[2]).getName() + ".yml")).getBoolean("Toggle.ActionBar")) {
                                Long time = System.currentTimeMillis();
                                ActionBar_TaskID.put(time, Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstances(), () -> {
                                    ActionBar_timer.putIfAbsent(time, 0);
                                    if (Bukkit.getPlayer(args[2]) == null || ActionBar_timer.get(time) == arg3) {
                                        ActionBar_timer.remove(time);
                                        Bukkit.getServer().getScheduler().cancelTask(ActionBar_TaskID.get(time));
                                    }

                                    StringBuilder list = new StringBuilder(args[4].replaceAll("&", "§"));
                                    if (args.length != 5)
                                        for (int i = 5; i != args.length; i++)
                                            list.append(" ").append(args[i]);

                                    Messages.sendActionBar(Bukkit.getPlayer(args[2]), list.toString());
                                    ActionBar_timer.put(time, ActionBar_timer.get(time) + 1);
                                }, 0, 20));
                            }
                        }
                    }
                    else if (args[1].equalsIgnoreCase("Title")) {
                        if (args[2].equalsIgnoreCase("all") && arg3 != 0 && args[4] != null) {
                            player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.General.SuccessFullSend").replaceAll("%TYPE%", "une Title").replaceAll("%PLAYER%", "tout le serveur"));
                            StringBuilder string = new StringBuilder(args[4].replaceAll("&", "§"));
                            if (args.length != 5)
                                for (int i = 5; i != args.length; i++)
                                    string.append(" ").append(args[i]);

                            String[] list = string.toString().split("&&");
                            for (Player player1 : Bukkit.getOnlinePlayers())
                                if (YamlConfiguration.loadConfiguration(new File(Main.getInstances().getDataFolder() + "/Players/" + player1.getName() + ".yml")).getBoolean("Toggle.Title"))
                                    Messages.sendTitle(player1, list[0], list[1], 5, arg3 * 20, 5);

                        } else if (Bukkit.getPlayer(args[2]) != null && arg3 !=0 && args[4] != null) {
                            player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.General.SuccessFullSend").replaceAll("%TYPE%", "un Title").replaceAll("%PLAYER%", Bukkit.getPlayer(args[2]).getName()));
                            StringBuilder string = new StringBuilder(args[4].replaceAll("&", "§"));
                            if (args.length != 5)
                                for (int i = 5; i != args.length; i++)
                                    string.append(" ").append(args[i]);

                            String[] list = string.toString().split("&&");
                            if (YamlConfiguration.loadConfiguration(new File(Main.getInstances().getDataFolder() + "/Players/" + Bukkit.getPlayer(args[2]).getName() + ".yml")).getBoolean("Toggle.Title"))
                                Messages.sendTitle(Bukkit.getPlayer(args[2]), list[0], list[1], 5, Integer.parseInt(args[3]) * 20, 5);
                        }
                    }
                    else if (args[1].equalsIgnoreCase("BossBar")) {
                        if (args[2].equalsIgnoreCase("all") && arg3 != 0 && args[4] != null && args[5] != null) {
                            player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.General.SuccessFullSend").replaceAll("%TYPE%", "une BossBar").replaceAll("%PLAYER%", "tout le serveur"));

                            StringBuilder string = new StringBuilder(args[5].replaceAll("&", "§"));
                            if (args.length != 6)
                                for (int i = 6; i != args.length; i++)
                                    string.append(" ").append(args[i]);

                            for (Player player1 : Bukkit.getOnlinePlayers())
                                if (YamlConfiguration.loadConfiguration(new File(Main.getInstances().getDataFolder() + "/Players/" + player1.getName() + ".yml")).getBoolean("Toggle.BossBar"))
                                    Messages.sendBossBar(player1, string.toString(), Double.parseDouble(args[3]), args[4]);
                        } else if (Bukkit.getPlayer(args[2]) != null && arg3 != 0 && args[4] != null && args[5] != null) {
                            player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.General.SuccessFullSend").replaceAll("%TYPE%", "une BossBar").replaceAll("%PLAYER%", Bukkit.getPlayer(args[2]).getName()));
                            StringBuilder string = new StringBuilder(args[5].replaceAll("&", "§"));
                            if (args.length != 6)
                                for (int i = 6; i != args.length; i++)
                                    string.append(" ").append(args[i]);

                            if (YamlConfiguration.loadConfiguration(new File(Main.getInstances().getDataFolder() + "/Players/" + Bukkit.getPlayer(args[2]).getName() + ".yml")).getBoolean("Toggle.BossBar"))
                                Messages.sendBossBar(Bukkit.getPlayer(args[2]), string.toString(), Double.parseDouble(args[3]), args[4]);
                        }
                    }
                }
                else if (args[0].equalsIgnoreCase("repairall")) {
                    if(player.hasPermission(FileManager.getValues().get(Files.Config).getString("General.Permissions.RepairAllPermission"))) {
                        File file = new File(Main.getInstances().getDataFolder() + "/Players/" + player.getName() + ".yml");
                        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
                        long currentTime = System.currentTimeMillis();
                        if(yamlConfiguration.getLong("RepairAllTime") == 0 || yamlConfiguration.getLong("RepairAllTime") <= currentTime) {
                            Repair.repairAll(player.getPlayer());
                            yamlConfiguration.set("RepairAllTime", currentTime +  FileManager.getValues().get(Files.Config).getLong("General.RepairAll.TimeBetweenUse"));
                            try {
                                yamlConfiguration.save(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.General.SuccessFullRepairAll"));
                        } else {
                            player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.General.WaitBeforeRepairAll").replaceAll("%TIME%", new SimpleDateFormat("dd:hh:mm:ss").format(yamlConfiguration.getLong("RepairAllTime") - currentTime)));
                        }
                        return true;
                    }
                    player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.General.NoPermissionRepairAll"));
                    return true;
                }
            }
            else if(!sender.isOp() && FileManager.getValues().get(Files.Config).getBoolean("General.noOpCommandEnable")){
                Player player = ((Player) sender);
                if(args.length >= 2) {
                    if (args[0].equalsIgnoreCase("Toggle")) {
                        File file = new File(Main.getInstances().getDataFolder() + "/Players/" + player.getName() + ".yml");
                        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
                        if(args[1].equalsIgnoreCase("ActionBar")) {
                            if(yamlConfiguration.getBoolean("Toggle.ActionBar")) {
                                yamlConfiguration.set("Toggle.ActionBar", false);
                                player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Toggle.ActionBar.Messages.SetOFF"));
                            } else {
                                yamlConfiguration.set("Toggle.ActionBar", true);
                                player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Toggle.ActionBar.Messages.SetON"));
                            }
                            try {
                                yamlConfiguration.save(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return true;
                        } else if(args[1].equalsIgnoreCase("BossBar")) {
                            if(yamlConfiguration.getBoolean("Toggle.BossBar")) {
                                yamlConfiguration.set("Toggle.BossBar", false);
                                player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Toggle.BossBar.Messages.SetOFF"));
                            } else {
                                yamlConfiguration.set("Toggle.BossBar", true);
                                player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Toggle.BossBar.Messages.SetON"));
                            }
                            try {
                                yamlConfiguration.save(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return true;
                        } else if(args[1].equalsIgnoreCase("DurabitlityBar")) {
                            if(yamlConfiguration.getBoolean("Toggle.DurabilityBar")) {
                                yamlConfiguration.set("Toggle.DurabilityBar", false);
                                player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Toggle.DurabilityBossBar.Messages.SetOFF"));
                            } else {
                                yamlConfiguration.set("Toggle.DurabilityBar", true);
                                player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Toggle.DurabilityBossBar.Messages.SetON"));
                            }
                            try {
                                yamlConfiguration.save(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return true;
                        } else if(args[1].equalsIgnoreCase("Title")) {
                            if(yamlConfiguration.getBoolean("Toggle.Title")) {
                                yamlConfiguration.set("Toggle.Title", false);
                                player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Toggle.Title.Messages.SetOFF"));
                            } else {
                                yamlConfiguration.set("Toggle.Title", true);
                                player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Toggle.Title.Messages.SetON"));
                            }
                            try {
                                yamlConfiguration.save(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return true;
                        }
                        player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.General.ToggleTab"));
                    }
                }
                else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("repair")) {
                        if(player.hasPermission(FileManager.getValues().get(Files.Config).getString("General.Permissions.RepairPermission"))) {
                            File file = new File(Main.getInstances().getDataFolder() + "/Players/" + player.getName() + ".yml");
                            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

                            long currentTime = System.currentTimeMillis();
                            if(yamlConfiguration.getLong("RepairTime") == 0 || yamlConfiguration.getLong("RepairTime") <= currentTime) {
                                boolean isRepair = Repair.repair(player.getItemInHand());
                                if (isRepair) {
                                    yamlConfiguration.set("RepairTime", currentTime + FileManager.getValues().get(Files.Config).getLong("General.Repair.TimeBetweenUse"));
                                    try {
                                        yamlConfiguration.save(file);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.General.SuccessFullRepair"));
                                } else {
                                    player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.General.ErrorRepair"));
                                }
                            } else {
                                player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.General.WaitBeforeRepair").replaceAll("%TIME%", new SimpleDateFormat("dd:hh:mm:ss").format(yamlConfiguration.getLong("RepairTime") - currentTime)));
                            }
                            return true;
                        }
                        player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.General.NoPermissionRepair"));
                        return true;
                    }
                }
                player.sendMessage(FileManager.getValues().get(Files.Config).getString("General.Messages.General.BaseTab"));
            }
        }
        return false;
    }
}