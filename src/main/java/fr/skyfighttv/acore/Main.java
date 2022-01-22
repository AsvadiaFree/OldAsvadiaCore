package fr.skyfighttv.acore;

import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import fr.skyfighttv.acore.Commands.*;
import fr.skyfighttv.acore.Listeners.*;
import fr.skyfighttv.acore.Manager.WebHookManager;
import fr.skyfighttv.acore.Runnables.Armors;
import fr.skyfighttv.acore.Runnables.UnclaimFinder;
import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import fr.skyfighttv.acore.Utils.PressionOR;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends JavaPlugin {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private static Main Instances;

    public static Main getInstances() {
        return Instances;
    }

    @Override
    public void onEnable() {
        Main.Instances = this;

        System.out.println(ANSI_YELLOW + "-_-_- " + ANSI_GREEN + "Asvadia Core " + ANSI_YELLOW +"-_-_-" + ANSI_RESET);
        FileManager.init();

        File playerStorage = new File(getDataFolder() + "/Players/");
        if(!playerStorage.exists()) playerStorage.mkdirs();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        System.out.println(ANSI_CYAN + "BungeeCord : " + ANSI_GREEN + "ON" + ANSI_RESET);

        if(FileManager.getValues().get(Files.Config).getBoolean("General.EnableModules.Recipes"))
            PressionOR.start();

        getCommand("acore").setExecutor(new ACore());
        getCommand("acore").setTabCompleter(new ACoreTab());
        System.out.println(ANSI_CYAN + "AsvadiaCore Command OP : " + ANSI_GREEN + "ON" + ANSI_RESET);
        if(FileManager.getValues().get(Files.Config).getBoolean("General.noOpCommandEnable"))
            System.out.println(ANSI_CYAN + "AsvadiaCore Command NonOP : " + ANSI_GREEN + "ON" + ANSI_RESET);
        else
            System.out.println(ANSI_CYAN + "AsvadiaCore Command NonOP : " + ANSI_RED + "OFF" + ANSI_RESET);

        if(FileManager.getValues().get(Files.Config).getBoolean("General.EnableModules.ResourcePack")) {
            getServer().getPluginManager().registerEvents(new ResourcePack(), this);
            System.out.println(ANSI_CYAN + "ResourcePack : " + ANSI_GREEN + "ON" + ANSI_RESET);
        } else System.out.println(ANSI_CYAN + "ResourcePack : " + ANSI_RED + "OFF" + ANSI_RESET);

        if(FileManager.getValues().get(Files.Config).getBoolean("General.EnableModules.Effects")) {
            getServer().getPluginManager().registerEvents(new Effects(), this);
            System.out.println(ANSI_CYAN + "Effects : " + ANSI_GREEN + "ON" + ANSI_RESET);
        } else System.out.println(ANSI_CYAN + "Effects : " + ANSI_RED + "OFF" + ANSI_RESET);

        getServer().getPluginManager().registerEvents(new Listerners(), this);

        if(FileManager.getValues().get(Files.Config).getBoolean("General.EnableModules.DurabilityBossBar")) {
            getServer().getPluginManager().registerEvents(new DurabilityBar(), this);
            System.out.println(ANSI_CYAN + "DurabilityBar : " + ANSI_GREEN + "ON" + ANSI_RESET);
        } else System.out.println(ANSI_CYAN + "DurabilityBar : " + ANSI_RED + "OFF" + ANSI_RESET);

        if(FileManager.getValues().get(Files.Config).getBoolean("General.EnableModules.Permissions"))
            System.out.println(ANSI_CYAN + "Permissions : " + ANSI_GREEN + "ON" + ANSI_RESET);
        else
            System.out.println(ANSI_CYAN + "Permissions : " + ANSI_RED + "OFF" + ANSI_RESET);

        getServer().getPluginManager().registerEvents(new DropsPermsItems(), this);
        System.out.println(ANSI_CYAN + "DropsItems : " + ANSI_GREEN + "ON" + ANSI_RESET);

        if(FileManager.getValues().get(Files.Config).getBoolean("General.EnableModules.LuckyBlock")) {
            getServer().getPluginManager().registerEvents(new LuckyBlock(), this);
            System.out.println(ANSI_CYAN + "LuckyBlock : " + ANSI_GREEN + "ON" + ANSI_RESET);
        } else System.out.println(ANSI_CYAN + "LuckyBlock : " + ANSI_RED + "OFF" + ANSI_RESET);

        Armors.armorsEffects.put("obsidienne", Arrays.asList(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0)
                , new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 0)
                , new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0)
                , new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0)));
        Armors.armorsEffects.put("saphir", Arrays.asList(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1)
                , new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0)
                , new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0)));
        Armors.armorsEffects.put("rubis", Arrays.asList(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0)));
        Armors.armorsEffects.put("topaze", Arrays.asList(new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE, 0)
                , new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0)));

        Armors.azoniumEffects.put("helmet", Arrays.asList(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0)));
        Armors.azoniumEffects.put("chestplate", Arrays.asList(new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE, 1)
                , new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0)));
        Armors.azoniumEffects.put("leggings", Arrays.asList(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1)));
        Armors.azoniumEffects.put("boots", Arrays.asList(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1)));

        if(FileManager.getValues().get(Files.Config).getBoolean("General.EnableModules.Staff")) {
            getCommand("astaff").setExecutor(new AStaff());
            getServer().getPluginManager().registerEvents(new StaffListeners(), this);
            System.out.println(ANSI_CYAN + "Staff : " + ANSI_GREEN + "ON" + ANSI_RESET);
        } else System.out.println(ANSI_CYAN + "Staff : " + ANSI_RED + "OFF" + ANSI_RESET);

        if(FileManager.getValues().get(Files.Config).getBoolean("General.EnableModules.Sanction")) {
            getCommand("asanction").setExecutor(new ASanction());
            System.out.println(ANSI_CYAN + "Sanction : " + ANSI_GREEN + "ON" + ANSI_RESET);
        } else System.out.println(ANSI_CYAN + "Sanction : " + ANSI_RED + "OFF" + ANSI_RESET);

        if(FileManager.getValues().get(Files.Config).getBoolean("General.EnableModules.WebHooks")) {
            new WebHookManager();
            System.out.println(ANSI_CYAN + "WebHooks : " + ANSI_GREEN + "ON" + ANSI_RESET);
        } else System.out.println(ANSI_CYAN + "WebHooks : " + ANSI_RED + "OFF" + ANSI_RESET);

        if(FileManager.getValues().get(Files.Config).getBoolean("General.EnableModules.TopLuck")) {
            getServer().getPluginManager().registerEvents(new TopLuck(), this);
            System.out.println(ANSI_CYAN + "TopLuck : " + ANSI_GREEN + "ON" + ANSI_RESET);

            if(FileManager.getValues().get(Files.Config).getBoolean("General.EnableModules.AutoAlertTopLuck")) {
                Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
                    List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());

                    TopLuck.topLuckUpdate(playerList);

                    List<String> topLuckClassement = new ArrayList<>();
                    int number = 0;
                    boolean alert = false;

                    while (number <= 5 && playerList.size() > 0) {
                        String stronger = null;
                        double strongerValue = 0;

                        for (Player players : playerList) {
                            double temp = TopLuck.playerGlobalScore.get(players.getName());
                            if (stronger == null) {
                                stronger = players.getName();
                                strongerValue = temp;
                            } else if (strongerValue < temp) {
                                stronger = players.getName();
                                strongerValue = temp;
                            }
                        }
                        playerList.remove(Bukkit.getPlayer(stronger));

                        topLuckClassement.add(Bukkit.getPlayer(stronger).getName());
                        number++;
                    }

                    WebhookEmbed embed = new WebhookEmbedBuilder()
                            .setColor(0xFF00EE)
                            .setAuthor(new WebhookEmbed.EmbedAuthor("Alerte TopLuck", "https://www.azonia.eu/storage/img/icon.png", "https://www.azonia.eu/storage/img/icon.png"))
                            .addField(new WebhookEmbed.EmbedField(true
                                    , "1. " + (topLuckClassement.size() > 0?topLuckClassement.get(0):"N/A")
                                    , (topLuckClassement.size() == 0
                                    ?"N/A"
                                    :(TopLuck.playerGlobalScore.get(topLuckClassement.get(0)) > 7?alert = true:"")
                                            + "Global Score : " + TopLuck.playerGlobalScore.get(topLuckClassement.get(0)))
                                            + "\n "
                                            + "\nAzonium : (" + TopLuck.playerAzoriumCount.get(topLuckClassement.get(0)) + ") " + TopLuck.playerAzoriumScore.get(topLuckClassement.get(0))
                                            + "\nTopaze : (" + TopLuck.playerTopazeCount.get(topLuckClassement.get(0)) + ") " + TopLuck.playerTopazeScore.get(topLuckClassement.get(0))
                                            + "\nRubis : (" + TopLuck.playerRubisCount.get(topLuckClassement.get(0)) + ") " + TopLuck.playerRubisScore.get(topLuckClassement.get(0))
                                            + "\nSaphir : (" + TopLuck.playerSaphirCount.get(topLuckClassement.get(0)) + ") " + TopLuck.playerSaphirScore.get(topLuckClassement.get(0)) + "\n"))
                            .addField(new WebhookEmbed.EmbedField(true
                                    , "2. " + (topLuckClassement.size() > 1?topLuckClassement.get(1):"N/A")
                                    , (topLuckClassement.size() <= 1
                                    ?"N/A"
                                    :(TopLuck.playerGlobalScore.get(topLuckClassement.get(1)) > 7?alert = true:"")
                                            + "Global Score : " + TopLuck.playerGlobalScore.get(topLuckClassement.get(1)))
                                            + "\n "
                                            + "\nAzonium : (" + TopLuck.playerAzoriumCount.get(topLuckClassement.get(1)) + ") " + TopLuck.playerAzoriumScore.get(topLuckClassement.get(1))
                                            + "\nTopaze : (" + TopLuck.playerTopazeCount.get(topLuckClassement.get(1)) + ") " + TopLuck.playerTopazeScore.get(topLuckClassement.get(1))
                                            + "\nRubis : (" + TopLuck.playerRubisCount.get(topLuckClassement.get(1)) + ") " + TopLuck.playerRubisScore.get(topLuckClassement.get(1))
                                            + "\nSaphir : (" + TopLuck.playerSaphirCount.get(topLuckClassement.get(1)) + ") " + TopLuck.playerSaphirScore.get(topLuckClassement.get(1)) + "\n"))
                            .addField(new WebhookEmbed.EmbedField(true
                                    , "3. " + (topLuckClassement.size() > 2?topLuckClassement.get(2):"N/A")
                                    , (topLuckClassement.size() <= 2
                                    ?"N/A"
                                    :(TopLuck.playerGlobalScore.get(topLuckClassement.get(2)) > 7?alert = true:"")
                                            + "Global Score : " + TopLuck.playerGlobalScore.get(topLuckClassement.get(2)))
                                            + "\n "
                                            + "\nAzonium : (" + TopLuck.playerAzoriumCount.get(topLuckClassement.get(2)) + ") " + TopLuck.playerAzoriumScore.get(topLuckClassement.get(2))
                                            + "\nTopaze : (" + TopLuck.playerTopazeCount.get(topLuckClassement.get(2)) + ") " + TopLuck.playerTopazeScore.get(topLuckClassement.get(2))
                                            + "\nRubis : (" + TopLuck.playerRubisCount.get(topLuckClassement.get(2)) + ") " + TopLuck.playerRubisScore.get(topLuckClassement.get(2))
                                            + "\nSaphir : (" + TopLuck.playerSaphirCount.get(topLuckClassement.get(2)) + ") " + TopLuck.playerSaphirScore.get(topLuckClassement.get(2)) + "\n"))
                            .addField(new WebhookEmbed.EmbedField(true
                                    , "4. " + (topLuckClassement.size() > 3?topLuckClassement.get(3):"N/A")
                                    , (topLuckClassement.size() <= 3
                                    ?"N/A"
                                    :(TopLuck.playerGlobalScore.get(topLuckClassement.get(3)) > 7?alert = true:"")
                                            + "Global Score : " + TopLuck.playerGlobalScore.get(topLuckClassement.get(3)))
                                            + "\n "
                                            + "\nAzonium : (" + TopLuck.playerAzoriumCount.get(topLuckClassement.get(3)) + ") " + TopLuck.playerAzoriumScore.get(topLuckClassement.get(3))
                                            + "\nTopaze : (" + TopLuck.playerTopazeCount.get(topLuckClassement.get(3)) + ") " + TopLuck.playerTopazeScore.get(topLuckClassement.get(3))
                                            + "\nRubis : (" + TopLuck.playerRubisCount.get(topLuckClassement.get(3)) + ") " + TopLuck.playerRubisScore.get(topLuckClassement.get(3))
                                            + "\nSaphir : (" + TopLuck.playerSaphirCount.get(topLuckClassement.get(3)) + ") " + TopLuck.playerSaphirScore.get(topLuckClassement.get(3)) + "\n"))
                            .addField(new WebhookEmbed.EmbedField(true
                                    , "5. " + (topLuckClassement.size() > 4?topLuckClassement.get(4):"N/A")
                                    , (topLuckClassement.size() <= 4
                                    ?"N/A"
                                    :(TopLuck.playerGlobalScore.get(topLuckClassement.get(4)) > 7?alert = true:"")
                                            + "Global Score : " + TopLuck.playerGlobalScore.get(topLuckClassement.get(4)))
                                            + "\n "
                                            + "\nAzonium : (" + TopLuck.playerAzoriumCount.get(topLuckClassement.get(4)) + ") " + TopLuck.playerAzoriumScore.get(topLuckClassement.get(4))
                                            + "\nTopaze : (" + TopLuck.playerTopazeCount.get(topLuckClassement.get(4)) + ") " + TopLuck.playerTopazeScore.get(topLuckClassement.get(4))
                                            + "\nRubis : (" + TopLuck.playerRubisCount.get(topLuckClassement.get(4)) + ") " + TopLuck.playerRubisScore.get(topLuckClassement.get(4))
                                            + "\nSaphir : (" + TopLuck.playerSaphirCount.get(topLuckClassement.get(4)) + ") " + TopLuck.playerSaphirScore.get(topLuckClassement.get(4)) + "\n"))
                            .build();
                    WebHookManager.logTopLuck.send(embed);
                    if (alert)
                        WebHookManager.logTopLuck.send("@everyone");
                }, 0, 18000);
                System.out.println(ANSI_CYAN + "AutoAlertTopLuck : " + ANSI_GREEN + "ON" + ANSI_RESET);
            } else System.out.println(ANSI_CYAN + "AutoAlertTopLuck : " + ANSI_RED + "OFF" + ANSI_RESET);
        } else {
            System.out.println(ANSI_CYAN + "TopLuck : " + ANSI_RED + "OFF" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "AutoAlertTopLuck : " + ANSI_RED + "OFF" + ANSI_RESET);
        }

        if(FileManager.getValues().get(Files.Config).getBoolean("General.EnableModules.AutoLog")) {
            getServer().getPluginManager().registerEvents(new AutoLog(), this);
            System.out.println(ANSI_CYAN + "AutoLog : " + ANSI_GREEN + "ON" + ANSI_RESET);
        } else System.out.println(ANSI_CYAN + "AutoLog : " + ANSI_RED + "OFF" + ANSI_RESET);

        if(FileManager.getValues().get(Files.Config).getBoolean("General.EnableModules.RTP")) {
            getCommand("artp").setExecutor(new ARTP());
            System.out.println(ANSI_CYAN + "RTP : " + ANSI_GREEN + "ON" + ANSI_RESET);
        } else System.out.println(ANSI_CYAN + "RTP : " + ANSI_RED + "OFF" + ANSI_RESET);

        if(FileManager.getValues().get(Files.Config).getBoolean("General.EnableModules.HoeSell")) {
            getCommand("ahoesell").setExecutor(new AHoeSell());
            getServer().getPluginManager().registerEvents(new HoeSell(), this);
            System.out.println(ANSI_CYAN + "HoeSell : " + ANSI_GREEN + "ON" + ANSI_RESET);
        } else System.out.println(ANSI_CYAN + "HoeSell : " + ANSI_RED + "OFF" + ANSI_RESET);

        if(FileManager.getValues().get(Files.Config).getBoolean("General.EnableModules.CustomCommands")) {
            System.out.println(ANSI_CYAN + "CustomCommands : " + ANSI_GREEN + "ON" + ANSI_RESET);
        } else System.out.println(ANSI_CYAN + "CustomCommands : " + ANSI_RED + "OFF" + ANSI_RESET);

        if(FileManager.getValues().get(Files.Config).getBoolean("General.EnableModules.AutoSpawn")) {
            System.out.println(ANSI_CYAN + "CustomCommands : " + ANSI_GREEN + "ON" + ANSI_RESET);
        } else System.out.println(ANSI_CYAN + "CustomCommands : " + ANSI_RED + "OFF" + ANSI_RESET);

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Armors(), 0, 20);
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new UnclaimFinder(), 0, 40);

        System.out.println(ANSI_YELLOW + "-_- " + ANSI_GREEN + "Chargement du plugin reussi ! " + ANSI_YELLOW + "-_-" + ANSI_RESET);
    }
}
