package fr.skyfighttv.acore.Listeners;

import fr.ChadOW.cinventory.CInventory;
import fr.ChadOW.cinventory.CItem;
import fr.ChadOW.cinventory.ItemCreator;
import fr.skyfighttv.acore.Commands.AStaff;
import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TopLuck implements Listener {
    public static HashMap<String, Double> playerGlobalScore = new HashMap<>();
    public static HashMap<String, Long> playerBlockCount = new HashMap<>();
    public static HashMap<String, Long> playerAzoriumCount = new HashMap<>();
    public static HashMap<String, Long> playerTopazeCount = new HashMap<>();
    public static HashMap<String, Long> playerRubisCount = new HashMap<>();
    public static HashMap<String, Long> playerSaphirCount = new HashMap<>();
    public static HashMap<String, Double> playerAzoriumScore = new HashMap<>();
    public static HashMap<String, Double> playerTopazeScore = new HashMap<>();
    public static HashMap<String, Double> playerRubisScore = new HashMap<>();
    public static HashMap<String, Double> playerSaphirScore = new HashMap<>();

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        putIfAbsent(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld() == Bukkit.getWorld("minage")) {
            putIfAbsent(event.getPlayer());

            playerBlockCount.put(player.getName(), playerBlockCount.get(player.getName()) + 1);

            Block block = event.getBlock();

            if (block.getType() == Material.LIME_GLAZED_TERRACOTTA) {
                playerAzoriumCount.put(player.getName(), playerAzoriumCount.get(player.getName()) + 1);
            } else if (block.getType() == Material.BROWN_GLAZED_TERRACOTTA) {
                playerTopazeCount.put(player.getName(), playerTopazeCount.get(player.getName()) + 1);
            } else if (block.getType() == Material.CYAN_GLAZED_TERRACOTTA) {
                playerRubisCount.put(player.getName(), playerRubisCount.get(player.getName()) + 1);
            } else if (block.getType() == Material.GREEN_GLAZED_TERRACOTTA) {
                playerSaphirCount.put(player.getName(), playerSaphirCount.get(player.getName()) + 1);
            }
        }
    }

    private static void putIfAbsent(Player player2) {
        playerBlockCount.putIfAbsent(player2.getName(), 1L);
        playerAzoriumCount.putIfAbsent(player2.getName(), 0L);
        playerTopazeCount.putIfAbsent(player2.getName(), 0L);
        playerRubisCount.putIfAbsent(player2.getName(), 0L);
        playerSaphirCount.putIfAbsent(player2.getName(), 0L);

        if (playerBlockCount.get(player2.getName()) == null) {
            playerBlockCount.put(player2.getName(), 1L);
        }
        if (playerAzoriumCount.get(player2.getName()) == null) {
            playerAzoriumCount.put(player2.getName(), 0L);
        }
        if (playerTopazeCount.get(player2.getName()) == null) {
            playerTopazeCount.put(player2.getName(), 0L);
        }
        if (playerRubisCount.get(player2.getName()) == null) {
            playerRubisCount.put(player2.getName(), 0L);
        }
        if (playerSaphirCount.get(player2.getName()) == null) {
            playerSaphirCount.put(player2.getName(), 0L);
        }
    }

    public static void topLuck(Player player) {
        int numberPlayer = 0;
        List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());

        topLuckUpdate(playerList);

        CInventory topLuckGUI = new CInventory(54, FileManager.getValues().get(Files.Staff).getString("Gui.FactionTopLuck.Title"));

        while (numberPlayer < 54 && playerList.size() > 0) {
            String stronger = null;
            double strongerValue = 0;

            for (Player players : playerList) {
                double temp = playerGlobalScore.get(players.getName());
                if (stronger == null) {
                    stronger = players.getName();
                    strongerValue = temp;
                } else if (strongerValue < temp) {
                    stronger = players.getName();
                    strongerValue = temp;
                }
            }
            playerList.remove(Bukkit.getPlayer(stronger));

            List<String> lore = new ArrayList<>(Arrays.asList("Global Score : " + playerGlobalScore.get(stronger)
                    , " "
                    , "Azonium : (" + playerAzoriumCount.get(stronger) + ") " + playerAzoriumScore.get(stronger)
                    , "Topaze : (" + playerTopazeCount.get(stronger) + ") " + playerTopazeScore.get(stronger)
                    , "Rubis : (" + playerRubisCount.get(stronger) + ") " + playerRubisScore.get(stronger)
                    , "Saphir : (" + playerSaphirCount.get(stronger) + ") " + playerSaphirScore.get(stronger)));
            CItem playerLuck = new CItem(new ItemCreator(Material.LEGACY_SKULL_ITEM, 3).setOwner(stronger).setName(stronger).setLores(lore)).setSlot(numberPlayer);
            playerLuck.addEvent((cInventory, cItem, player1, clickContext) -> {
                AStaff.guiPlayer(player, Bukkit.getPlayer(cItem.getItemCreator().getName()));
            });
            topLuckGUI.addElement(playerLuck);
            numberPlayer++;
        }
        topLuckGUI.open(player);
    }

    public static void topLuckUpdate(List<Player> playerList) {
        DecimalFormat decimalFormat = new DecimalFormat("###.####");

        for (Player players : playerList) {
            putIfAbsent(players);

            double azoriumScore = Double.parseDouble(decimalFormat.format((double) playerAzoriumCount.get(players.getName()) / (double) playerBlockCount.get(players.getName()) * 100.0).replaceAll(",", "."));
            double topazeScore = Double.parseDouble(decimalFormat.format((double) playerTopazeCount.get(players.getName()) / (double) playerBlockCount.get(players.getName()) * 100.0).replaceAll(",", "."));
            double rubisScore = Double.parseDouble(decimalFormat.format((double) playerRubisCount.get(players.getName()) / (double) playerBlockCount.get(players.getName()) * 100.0).replaceAll(",", "."));
            double saphirScore = Double.parseDouble(decimalFormat.format((double) playerSaphirCount.get(players.getName()) / (double) playerBlockCount.get(players.getName()) * 100.0).replaceAll(",", "."));

            double globalScore = Double.parseDouble(decimalFormat.format(azoriumScore + topazeScore + rubisScore + saphirScore));

            playerGlobalScore.put(players.getName(), globalScore);
            playerAzoriumScore.put(players.getName(), azoriumScore);
            playerTopazeScore.put(players.getName(), topazeScore);
            playerRubisScore.put(players.getName(), rubisScore);
            playerSaphirScore.put(players.getName(), saphirScore);
        }
    }
}
