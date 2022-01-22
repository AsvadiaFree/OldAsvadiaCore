package fr.skyfighttv.acore.Commands;

import com.google.common.base.Functions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import fr.skyfighttv.acore.Utils.Plantations;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ACoreTab implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if(sender.isOp()) {
            if (args.length == 1) {
                return a(args, Arrays.asList("setItem", "setPermission", "setEffect", "getItemStack", "Messages", "Reload").toArray(new String[0]));
            } else if (args[0].equalsIgnoreCase("give")) {
                List<String> tab = new ArrayList<>();
                tab.add("*");
                for (Player player : Bukkit.getOnlinePlayers()) {
                    tab.add(player.getName());
                }
                return a(args, tab);
            } else if (args[0].equalsIgnoreCase("setItem")) {
                if (args.length == 2) {
                    return a(args, Arrays.asList("Block", "Entity", "Plantation").toArray(new String[0]));
                }
                List<String> tab = new ArrayList<>();
                if (args[1].equalsIgnoreCase("Block")) {
                    for (Material material : Material.values())
                        tab.add(material.name());
                } else if (args[1].equalsIgnoreCase("Entity")) {
                    for (EntityType entityType : EntityType.values())
                        if (entityType.getName() != null)
                            tab.add(entityType.getName());
                } else if (args[1].equalsIgnoreCase("Plantation")) {
                    for (Plantations plantations : Plantations.values()) {
                        tab.add(plantations.getMaterial().name());
                    }
                }
                return a(args, tab);
            } else if (args[0].equalsIgnoreCase("reload")) {
                List<String> tab = new ArrayList<>();
                for (Files files : Files.values()) {
                    tab.add(files.getName());
                }
                tab.add("all");
                return a(args, tab);
            } else if (args[0].equalsIgnoreCase("setEffect")) {
                if (args.length == 2) return a(args, FileManager.getValues().get(Files.Effects).getKeys(false));
                if (args[1].equalsIgnoreCase("UnclaimFinder")) return new ArrayList<>(FileManager.getValues().get(Files.Effects).getConfigurationSection("UNCLAIMFINDER").getKeys(false));
            } else if (args[0].equalsIgnoreCase("messages")) {
                if (args.length == 2)
                    return a(args, Arrays.asList("ActionBar", "Title", "BossBar").toArray(new String[0]));
                List<String> tab = new ArrayList<>(Collections.singletonList("All"));
                if (args[1].equalsIgnoreCase("ActionBar")) {
                    if (args.length == 3)
                        for (Player player : Bukkit.getOnlinePlayers())
                            tab.add(player.getName());
                } else if (args[1].equalsIgnoreCase("Title")) {
                    if (args.length == 3)
                        for (Player player : Bukkit.getOnlinePlayers())
                            tab.add(player.getName());
                } else if (args[1].equalsIgnoreCase("BossBar")) {
                    if (args.length == 3)
                        for (Player player : Bukkit.getOnlinePlayers())
                            tab.add(player.getName());
                    else if (args.length == 5)
                        for (BarColor player : BarColor.values())
                            tab.add(player.toString());
                }
                return a(args, tab.toArray(new String[0]));
            }
        } else {
            if(args.length == 1)
                return a(args, Arrays.asList("Toggle", "Repair").toArray(new String[0]));
            else if(args[0].equalsIgnoreCase("Toggle"))
                return a(args, Arrays.asList("ActionBar", "BossBar", "DurabilityBossBar", "Title").toArray(new String[0]));
        }
        return null;
    }

    public static List<String> a(String[] var0, String... var1) {
        return a(var0, Arrays.asList(var1));
    }

    public static List<String> a(String[] var0, Collection<?> var1) {
        String var2 = var0[var0.length - 1];
        ArrayList var3 = Lists.newArrayList();
        if (!var1.isEmpty()) {
            Iterator var4 = Iterables.transform(var1, Functions.toStringFunction()).iterator();

            while(var4.hasNext()) {
                String var5 = (String)var4.next();
                if (a(var2, var5)) {
                    var3.add(var5);
                }
            }

            if (var3.isEmpty()) {
                var4 = var1.iterator();

                while(var4.hasNext()) {
                    Object var6 = var4.next();
                    if (a(var2, (String) var6)) {
                        var3.add(String.valueOf(var6));
                    }
                }
            }
        }

        return var3;
    }

    public static boolean a(String var0, String var1) {
        return var1.regionMatches(true, 0, var0, 0, var0.length());
    }
}
