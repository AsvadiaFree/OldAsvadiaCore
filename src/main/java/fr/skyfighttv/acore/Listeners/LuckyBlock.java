package fr.skyfighttv.acore.Listeners;

import fr.skyfighttv.acore.Main;
import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LuckyBlock implements Listener {
    private String pathConfigReward = "Rewards";

    @EventHandler
    private void onBreakLuckyBLock(BlockBreakEvent event) {
        if(event.getBlock().getType().name().equals(FileManager.getValues().get(Files.LuckyBlock).getString("Material"))) {
            FileConfiguration config = FileManager.getValues().get(Files.LuckyBlock);
            Location blockLocation = event.getBlock().getLocation();

            blockLocation.getWorld().spawnParticle(Particle.valueOf(config.getString("Particles.Type")), blockLocation.add(0.5, config.getInt("Particles.Height") / 2.0 + 0.5, 0.5), config.getInt("Particles.Number"), 0, config.getInt("Particles.Height") / 2.0 - 0.5, 0, 60);
            event.getBlock().setType(Material.AIR);

            int rewardNumber =  new Random().nextInt(config.getConfigurationSection("Rewards").getKeys(false).size()) + 1;
            event.getPlayer().sendMessage("§aVous avez ouvert un luckyblock.");
            String rewardType = config.getString(pathConfigReward + "." + rewardNumber + ".Type");

            if (rewardType.equalsIgnoreCase("DROP")) {
                blockLocation.getWorld().dropItemNaturally(event.getBlock().getLocation().add( 0.5, 0.5, 0.5), config.getItemStack(pathConfigReward + "." + rewardNumber + ".Item"));
            } else if (rewardType.equalsIgnoreCase("EFFECT")) {
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.getByName(config.getString(pathConfigReward + "." + rewardNumber + ".PotionType")), config.getInt(pathConfigReward + "." + rewardNumber + ".Duration") * 20, config.getInt(pathConfigReward + "." + rewardNumber + ".Amplifier")));
            } else if (rewardType.equalsIgnoreCase("COMMAND")) {
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), config.getString(pathConfigReward + "." + rewardNumber + ".Command").replaceAll("%PLAYER%", event.getPlayer().getName()).replaceAll("/", ""));
            } else if(rewardType.equalsIgnoreCase("MALUS_ENDERCHEST")) {
                ArrayList<ItemStack> enderchest = new ArrayList<>(Arrays.asList(event.getPlayer().getEnderChest().getContents()));
                for (int i = 0; i < 27; i++) {
                    int rdm = new Random().nextInt(enderchest.size());
                    event.getPlayer().getEnderChest().setItem(i, enderchest.get(rdm));
                    enderchest.remove(rdm);
                }
            }

            Firework firework = (Firework) event.getPlayer().getWorld().spawnEntity(event.getBlock().getLocation(), EntityType.FIREWORK);
            FireworkMeta meta = firework.getFireworkMeta();
            meta.addEffect(FireworkEffect.builder().withColor(Color.GREEN, Color.LIME).with(FireworkEffect.Type.BALL).withFade(Color.LIME).withTrail().build());
            meta.setPower(1);
            firework.setFireworkMeta(meta);
        }
    }
    
}
