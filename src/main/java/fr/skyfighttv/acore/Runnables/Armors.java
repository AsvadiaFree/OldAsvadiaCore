package fr.skyfighttv.acore.Runnables;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class Armors implements Runnable {
    private HashMap<Player, List<String>> hasBonus = new HashMap<>();
    public static HashMap<String, List<PotionEffect>> armorsEffects = new HashMap<>();
    public static HashMap<String, List<PotionEffect>> azoniumEffects = new HashMap<>();

    @Override
    public void run() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            ItemStack helmet = player.getInventory().getHelmet();
            ItemStack chestplate = player.getInventory().getChestplate();
            ItemStack leggings = player.getInventory().getLeggings();
            ItemStack boots = player.getInventory().getBoots();

            if ((helmet != null && helmet.getType() == Material.NETHERITE_HELMET)
                    || (chestplate != null && chestplate.getType() == Material.NETHERITE_CHESTPLATE)
                    || (leggings != null && leggings.getType() == Material.NETHERITE_LEGGINGS)
                    || (boots != null && boots.getType() == Material.NETHERITE_BOOTS)) {

                if (helmet != null && helmet.getType() == Material.NETHERITE_HELMET && (!hasBonus.containsKey(player) || !hasBonus.get(player).contains("helmet"))) {
                    for (PotionEffect effect : azoniumEffects.get("helmet")) {
                        player.addPotionEffect(effect);
                    }

                    hasBonus.put(player, new ArrayList<>(Collections.singleton("helmet")));
                } else if((helmet == null || helmet.getType() != Material.NETHERITE_HELMET) && hasBonus.containsKey(player) && hasBonus.get(player).contains("helmet")) {
                    for (PotionEffect effect :  azoniumEffects.get("helmet")) {
                        player.removePotionEffect(effect.getType());
                    }

                    List<String> stuff = new ArrayList<>();
                    if(hasBonus.containsKey(player) && !hasBonus.get(player).isEmpty()) {
                        stuff.addAll(hasBonus.get(player));
                    }
                    stuff.remove("helmet");
                    hasBonus.put(player, stuff);
                }

                if (chestplate != null && chestplate.getType() == Material.NETHERITE_CHESTPLATE && (!hasBonus.containsKey(player) || !hasBonus.get(player).contains("chestplate"))) {
                    for (PotionEffect effect : azoniumEffects.get("chestplate")) {
                        player.addPotionEffect(effect);
                    }

                    List<String> stuff = new ArrayList<>();
                    if(hasBonus.containsKey(player) && !hasBonus.get(player).isEmpty()) {
                        stuff.addAll(hasBonus.get(player));
                    }
                    stuff.add("chestplate");
                    hasBonus.put(player, stuff);
                } else if((chestplate == null || chestplate.getType() != Material.NETHERITE_CHESTPLATE) && hasBonus.containsKey(player) && hasBonus.get(player).contains("chestplate")) {
                    for (PotionEffect effect :  azoniumEffects.get("chestplate")) {
                        player.removePotionEffect(effect.getType());
                    }

                    List<String> stuff = new ArrayList<>();
                    if(hasBonus.containsKey(player) && !hasBonus.get(player).isEmpty()) {
                        stuff.addAll(hasBonus.get(player));
                    }
                    stuff.remove("chestplate");
                    hasBonus.put(player, stuff);
                }

                if (leggings != null && leggings.getType() == Material.NETHERITE_LEGGINGS && (!hasBonus.containsKey(player) || !hasBonus.get(player).contains("leggings"))) {
                    for (PotionEffect effect : azoniumEffects.get("leggings")) {
                        player.addPotionEffect(effect);
                    }

                    List<String> stuff = new ArrayList<>();
                    if(hasBonus.containsKey(player) && !hasBonus.get(player).isEmpty()) {
                        stuff.addAll(hasBonus.get(player));
                    }
                    stuff.add("leggings");
                    hasBonus.put(player, stuff);
                } else if((leggings == null || leggings.getType() != Material.NETHERITE_LEGGINGS) && hasBonus.containsKey(player) && hasBonus.get(player).contains("leggings")) {
                    for (PotionEffect effect :  azoniumEffects.get("leggings")) {
                        player.removePotionEffect(effect.getType());
                    }

                    List<String> stuff = new ArrayList<>();
                    if(hasBonus.containsKey(player) && !hasBonus.get(player).isEmpty()) {
                        stuff.addAll(hasBonus.get(player));
                    }
                    stuff.remove("leggings");
                    hasBonus.put(player, stuff);
                }

                if (boots != null && boots.getType() == Material.NETHERITE_BOOTS && (!hasBonus.containsKey(player) || !hasBonus.get(player).contains("boots"))) {
                    for (PotionEffect effect : azoniumEffects.get("boots")) {
                        player.addPotionEffect(effect);
                    }

                    List<String> stuff = new ArrayList<>();
                    if(hasBonus.containsKey(player) && !hasBonus.get(player).isEmpty()) {
                        stuff.addAll(hasBonus.get(player));
                    }
                    stuff.add("boots");
                    hasBonus.put(player, stuff);
                } else if((boots == null || boots.getType() != Material.NETHERITE_BOOTS) && hasBonus.containsKey(player) && hasBonus.get(player).contains("boots")) {
                    for (PotionEffect effect :  azoniumEffects.get("boots")) {
                        player.removePotionEffect(effect.getType());
                    }

                    List<String> stuff = new ArrayList<>();
                    if(hasBonus.containsKey(player) && !hasBonus.get(player).isEmpty()) {
                        stuff.addAll(hasBonus.get(player));
                    }
                    stuff.remove("boots");
                    hasBonus.put(player, stuff);
                }
            } else if(helmet != null && helmet.getType() == Material.LEATHER_HELMET
                    && chestplate != null && chestplate.getType() == Material.LEATHER_CHESTPLATE
                    && leggings != null && leggings.getType() == Material.LEATHER_LEGGINGS
                    && boots != null && boots.getType() == Material.LEATHER_BOOTS) {

                LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
                LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplate.getItemMeta();
                LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggings.getItemMeta();
                LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();

                if (helmetMeta.getColor().equals(Color.fromRGB(0x7832C8))
                        && chestplateMeta.getColor().equals(Color.fromRGB(0x7832C8))
                        && leggingsMeta.getColor().equals(Color.fromRGB(0x7832C8))
                        && bootsMeta.getColor().equals(Color.fromRGB(0x7832C8))) {
                    if(hasBonus.containsKey(player)) {
                        continue;
                    }
                    for (PotionEffect effect : armorsEffects.get("obsidienne")) {
                        player.addPotionEffect(effect);
                    }
                    hasBonus.put(player, new ArrayList<>(Collections.singleton("obsidienne")));

                } else if (helmetMeta.getColor().equals(Color.fromRGB(0x3191BE))
                        && chestplateMeta.getColor().equals(Color.fromRGB(0x3191BE))
                        && leggingsMeta.getColor().equals(Color.fromRGB(0x3191BE))
                        && bootsMeta.getColor().equals(Color.fromRGB(0x3191BE))) {
                    if(hasBonus.containsKey(player)) {
                        continue;
                    }
                    for (PotionEffect effect : armorsEffects.get("saphir")) {
                        player.addPotionEffect(effect);
                    }
                    hasBonus.put(player, new ArrayList<>(Collections.singleton("saphir")));

                } else if (helmetMeta.getColor().equals(Color.fromRGB(0xC04547))
                        && chestplateMeta.getColor().equals(Color.fromRGB(0xC04547))
                        && leggingsMeta.getColor().equals(Color.fromRGB(0xC04547))
                        && bootsMeta.getColor().equals(Color.fromRGB(0xC04547))) {
                    if(hasBonus.containsKey(player)) {
                        continue;
                    }
                    for (PotionEffect effect : armorsEffects.get("rubis")) {
                        player.addPotionEffect(effect);
                    }
                    hasBonus.put(player, new ArrayList<>(Collections.singleton("rubis")));

                } else if (helmetMeta.getColor().equals(Color.fromRGB(0xF9801D))
                        && chestplateMeta.getColor().equals(Color.fromRGB(0xF9801D))
                        && leggingsMeta.getColor().equals(Color.fromRGB(0xF9801D))
                        && bootsMeta.getColor().equals(Color.fromRGB(0xF9801D))) {
                    if(hasBonus.containsKey(player)) {
                        continue;
                    }
                    for (PotionEffect effect : armorsEffects.get("topaze")) {
                        player.addPotionEffect(effect);
                    }
                    hasBonus.put(player, new ArrayList<>(Collections.singleton("topaze")));

                } else {
                    disableEffects(player);
                }
            } else {
                disableEffects(player);
            }
        }
    }

    private void disableEffects(Player player) {
        if(hasBonus.containsKey(player)) {
            List<String> bonusPlayer = hasBonus.get(player);

            if (bonusPlayer.contains("helmet")) {
                for (PotionEffect effect :  azoniumEffects.get("helmet")) {
                    player.removePotionEffect(effect.getType());
                }
                bonusPlayer.remove("helmet");
            }
            if (bonusPlayer.contains("chestplate")) {
                for (PotionEffect effect :  azoniumEffects.get("chestplate")) {
                    player.removePotionEffect(effect.getType());
                }
                bonusPlayer.remove("chestplate");
            }
            if (bonusPlayer.contains("leggings")) {
                for (PotionEffect effect :  azoniumEffects.get("leggings")) {
                    player.removePotionEffect(effect.getType());
                }
                bonusPlayer.remove("leggings");
            }
            if (bonusPlayer.contains("boots")) {
                for (PotionEffect effect :  azoniumEffects.get("boots")) {
                    player.removePotionEffect(effect.getType());
                }
                bonusPlayer.remove("boots");
            }

            if(bonusPlayer.size() != 0) {
                for (PotionEffect effect : armorsEffects.get(bonusPlayer.get(0))) {
                    player.removePotionEffect(effect.getType());
                }
            }

            hasBonus.remove(player);
        }
    }
}
