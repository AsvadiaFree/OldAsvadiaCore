package fr.skyfighttv.acore.Manager;

import com.massivecraft.factions.*;
import fr.skyfighttv.acore.Listeners.DropsPermsItems;
import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class HammerManager {
    public void mineWE(BlockBreakEvent e) {
        Player player = e.getPlayer();
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        Block block = e.getBlock();
        Location loc = player.getLocation();
        World world = player.getWorld();
        loc.setY(loc.getY() - 1.0);
        String[] arrayOfInt1;
        int j = (arrayOfInt1 = FileManager.getValues().get(Files.Effects).getStringList("HAMMER.blockBreakable").toArray(new String[0])).length;
        for(int il = 0; il < j; il++) {
            String i = arrayOfInt1[il];

            for(int x = 0; x < 1; ++x) {
                for (int y = -1; y < 1.5; ++y) {
                    for (int z = -1; z < 1.5; ++z) {
                        if(x == 0 && y == 0 && z == 0) continue;
                        Block blockAtLoc = world.getBlockAt(block.getLocation().getBlockX() + x, block.getLocation().getBlockY() + y, block.getLocation().getBlockZ() + z);
                        if (blockAtLoc.getType() == Material.BEDROCK || blockAtLoc.getType() == Material.OBSIDIAN) {
                            return;
                        }
                        if (blockAtLoc.getType().name().equalsIgnoreCase(i)) {
                            boolean canBreak = DropsPermsItems.verifBlock(blockAtLoc, player);
                            Faction board = Board.getInstance().getFactionAt(new FLocation(blockAtLoc.getWorld().getName(), blockAtLoc.getX(), blockAtLoc.getZ()));
                            if (canBreak && (board.isWilderness() || board == fPlayer.getFaction()))
                                if (blockAtLoc.getType() != Material.AIR)
                                    blockAtLoc.breakNaturally(new ItemStack(Material.DIAMOND_PICKAXE));
                        }
                    }
                }
            }
        }
    }

    public void mineNS(BlockBreakEvent e) {
        Player player = e.getPlayer();
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        Block block = e.getBlock();
        Location loc = player.getLocation();
        loc.setY(loc.getY() - 1.0);
        World world = player.getWorld();
        String[] arrayOfInt1;
        int j = (arrayOfInt1 = FileManager.getValues().get(Files.Effects).getStringList("HAMMER.blockBreakable").toArray(new String[0])).length;
        for(int il = 0; il < j; il++) {
            String i = arrayOfInt1[il];

            for (int x = -1; x < 1.5; ++x) {
                for (int y = -1; y < 1.5; ++y) {
                    for (int z = 0; z < 1; ++z) {
                        if(x == 0 && y == 0 && z == 0) continue;
                        Block blockAtLoc = world.getBlockAt(block.getLocation().getBlockX() + x, block.getLocation().getBlockY() + y, block.getLocation().getBlockZ() + z);
                        if(blockAtLoc.getType() == Material.BEDROCK || blockAtLoc.getType() == Material.OBSIDIAN) {
                            return;
                        }
                        if(blockAtLoc.getType().name().equalsIgnoreCase(i)) {
                            boolean canBreak = DropsPermsItems.verifBlock(blockAtLoc, player);
                            Faction board = Board.getInstance().getFactionAt(new FLocation(blockAtLoc.getWorld().getName(), blockAtLoc.getX(), blockAtLoc.getZ()));
                            if (canBreak && (board.isWilderness() || board == fPlayer.getFaction()))
                                if (blockAtLoc.getType() != Material.AIR)
                                    blockAtLoc.breakNaturally(new ItemStack(Material.DIAMOND_PICKAXE));
                        }
                    }
                }
            }
        }
    }

    public void mineBH(BlockBreakEvent e) {
        Player player = e.getPlayer();
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        Block block = e.getBlock();
        Location loc = player.getLocation();
        World world = player.getWorld();
        loc.setY(loc.getY() - 1.0);
        String[] arrayOfInt1;
        int j = (arrayOfInt1 = FileManager.getValues().get(Files.Effects).getStringList("HAMMER.blockBreakable").toArray(new String[0])).length;
        for(int il = 0; il < j; il++) {
            String i = arrayOfInt1[il];

            for(int x = -1; x < 1.5; ++x) {
                for(int y = 0; y < 0.5; ++y) {
                    for(int z = -1; z < 1.5; ++z) {
                        if(x == 0 && y == 0 && z == 0) continue;
                        Block blockAtLoc = world.getBlockAt(block.getLocation().getBlockX() + x, block.getLocation().getBlockY() + y, block.getLocation().getBlockZ() + z);
                        if(blockAtLoc.getType() == Material.BEDROCK || blockAtLoc.getType() == Material.OBSIDIAN) {
                            return;
                        }
                        if(blockAtLoc.getType().name().equalsIgnoreCase(i)) {
                            boolean canBreak = DropsPermsItems.verifBlock(blockAtLoc, player);
                            Faction board = Board.getInstance().getFactionAt(new FLocation(blockAtLoc.getWorld().getName(), blockAtLoc.getX(), blockAtLoc.getZ()));
                            if (canBreak && (board.isWilderness() || board == fPlayer.getFaction()))
                                if (blockAtLoc.getType() != Material.AIR)
                                    blockAtLoc.breakNaturally(new ItemStack(Material.DIAMOND_PICKAXE));
                        }
                    }
                }
            }
        }
    }

    public String getDirection(Player player) {
        double rot = (player.getLocation().getYaw() - 90.0f) % 360.0f;
        if(rot < 0.0) {
            rot += 360.0;
        }
        if((rot >= 337.5 && rot <= 360.0) || (rot >= 0.0 && rot < 67.5)) {
            return "W";
        }
        if(67.5 <= rot && rot < 157.5) {
            return "N";
        }
        if(157.5 <= rot && rot < 247.5) {
            return "E";
        }
        if(247.5 <= rot && rot < 337.5) {
            return "S";
        }
        return null;
    }

    public String getPitch(Player player) {
        double pitch = player.getLocation().getPitch();
        if(pitch >= -30.0 && pitch <= 30.0) {
            return "F";
        }
        if(pitch > 30.0) {
            return "B";
        }
        if(pitch < -30.0) {
            return "H";
        }
        return null;
    }
}