package fr.skyfighttv.acore.Utils;

import fr.skyfighttv.acore.Main;
import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import net.minecraft.server.v1_16_R3.Block;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.PacketPlayOutBlockChange;
import net.minecraft.server.v1_16_R3.PacketPlayOutMapChunk;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class XRay {
    private static HashMap<Player, List<Chunk>> chunkAlreadyXray = new HashMap<>();
    private static HashMap<Player, GameMode> saveGamemode = new HashMap<>();
    private static HashMap<Player, Integer> taskId = new HashMap<>();

    public static void on(Player player) {
        YamlConfiguration config = FileManager.getValues().get(Files.Staff);

        saveGamemode.put(player, player.getGameMode());
        player.setGameMode(GameMode.SPECTATOR);
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 99999, 255));
        taskId.put(player, Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(Main.getInstances(), () -> {
            Chunk chunkPlayer = player.getLocation().getChunk();
            List<Integer> chunksx = new ArrayList<>(Arrays.asList(-16, -16, -16, 0, 0, 0, 16, 16, 16));
            List<Integer> chunksz = new ArrayList<>(Arrays.asList(-16, 0, 16, -16, 0, 16, -16, 0, 16));
            for (int i = 0; i < 9; i++) {
                Chunk chunk = new Location(chunkPlayer.getWorld(), player.getLocation().getX() + chunksx.get(i), 0, player.getLocation().getZ() + chunksz.get(i)).getChunk();
                if (!chunkAlreadyXray.containsKey(player) || !chunkAlreadyXray.get(player).contains(chunk)) {
                    int X = chunkPlayer.getX() * 16 + chunksx.get(i);
                    int Z = chunkPlayer.getZ() * 16 + chunksz.get(i);
                    List<Chunk> thisChunk = chunkAlreadyXray.get(player) == null ? new ArrayList<>() : chunkAlreadyXray.get(player);
                    thisChunk.add(chunk);
                    chunkAlreadyXray.put(player, thisChunk);
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            for (int y = 0; y < 128; y++) {
                                if (!config.getStringList("XRayBlockVisible").contains(chunkPlayer.getWorld().getBlockAt(X + x, y, Z + z).getType().name())) {
                                    BlockPosition blockPosition = new BlockPosition(X + x, y, Z + z);
                                    PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(((CraftWorld) chunkPlayer.getWorld()).getHandle(), blockPosition);
                                    packet.block = Block.getByCombinedId(Material.LEGACY_AIR.getId());
                                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                                }
                            }
                        }
                    }
                }
            }
        }, 0, 40));
    }

    public static void off(Player player) {
        if(taskId.get(player) != null)
            Bukkit.getServer().getScheduler().cancelTask(taskId.get(player));
        taskId.remove(player);
        player.setGameMode(saveGamemode.get(player) == null ? GameMode.CREATIVE : saveGamemode.get(player));
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        saveGamemode.remove(player);
        for(Chunk chunk : chunkAlreadyXray.get(player) == null ? new ArrayList<Chunk>() : chunkAlreadyXray.get(player)) {
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutMapChunk(((CraftChunk)chunk).getHandle(), 255));
        }
        chunkAlreadyXray.remove(player);
    }
}
