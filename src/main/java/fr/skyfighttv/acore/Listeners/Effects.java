package fr.skyfighttv.acore.Listeners;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import fr.ChadOW.cinventory.CInventory;
import fr.ChadOW.cinventory.CItem;
import fr.ChadOW.cinventory.ItemCreator;
import fr.skyfighttv.acore.Main;
import fr.skyfighttv.acore.Utils.File.FileManager;
import fr.skyfighttv.acore.Utils.File.Files;
import fr.skyfighttv.acore.Utils.Messages;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

/*     */ public class Effects
/*     */   implements Listener
/*     */ {
/*  32 */   private HashMap<UUID, Long> Cooldown = new HashMap<>();
            private HashMap<Player, Location> caveblockLocation = new HashMap<>();
            private HashMap<Player, Boolean> nofallActive = new HashMap<>();
            private static HashMap<Arrow, Integer> RLTNTScheduler = new HashMap<>();
            private static List<Player> RLTNTAvailable= new ArrayList<>();
            private static HashMap<Arrow, Integer> RLNUKEScheduler = new HashMap<>();
            private static List<Player> RLNUKEAvailable = new ArrayList<>();
            private HashMap<Player, Integer> balloonTaskId = new HashMap<>();
            private HashMap<Player, ArmorStand> armorStandPlayer = new HashMap<>();
            private HashMap<Player, Bat> batPlayer = new HashMap<>();

/*     */   @EventHandler
/*     */   public void onInteract(final PlayerInteractEvent e) {
                YamlConfiguration config = FileManager.getValues().get(Files.Effects);

/*  39 */     if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
/*  40 */       if (e.getItem() != null && e.getItem().getItemMeta().hasLore() && e.getItem().getItemMeta().getLore().contains(config.getString("SPEED.lore").replaceAll("&", "§"))) {
                    if(e.getPlayer().getInventory().getItemInHand().getAmount() != 1) {
                        e.getPlayer().getInventory().getItemInHand().setAmount(e.getPlayer().getInventory().getItemInHand().getAmount() - 1);
                    } else {
                        e.getPlayer().getInventory().setItemInHand(null);
                    }
/*  41 */         PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 20 * config.getInt("SPEED.time"), config.getInt("SPEED.amplifier"));
/*  42 */         e.getPlayer().addPotionEffect(speed, true);
/*     */       } 
/*     */       
/*  45 */       if (e.getItem() != null && e.getItem().getItemMeta().hasLore() && e.getItem().getItemMeta().getLore().contains(config.getString("STRENGTH.lore").replaceAll("&", "§"))) {
                    if(e.getPlayer().getInventory().getItemInHand().getAmount() != 1) {
                        e.getPlayer().getInventory().getItemInHand().setAmount(e.getPlayer().getInventory().getItemInHand().getAmount() - 1);
                    } else {
                        e.getPlayer().getInventory().setItemInHand(null);
                    }
/*  46 */         PotionEffect force = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * config.getInt("STRENGTH.time"), config.getInt("STRENGTH.amplifier"));
/*  47 */         e.getPlayer().addPotionEffect(force, true);
/*     */       }

                if (e.getItem() != null && e.getItem().getItemMeta().hasLore() && e.getItem().getItemMeta().getLore().contains(config.getString("HASTE.lore").replaceAll("&", "§"))) {
                     if(e.getPlayer().getInventory().getItemInHand().getAmount() != 1) {
                         e.getPlayer().getInventory().getItemInHand().setAmount(e.getPlayer().getInventory().getItemInHand().getAmount() - 1);
                     } else {
                         e.getPlayer().getInventory().setItemInHand(null);
                     }
/*  51 */         PotionEffect haste = new PotionEffect(PotionEffectType.FAST_DIGGING, 20 * config.getInt("HASTE.time"), config.getInt("HASTE.amplifier"));
/*  52 */         e.getPlayer().addPotionEffect(haste, true);
/*     */       } 
/*     */       
/*  55 */      if (e.getItem() != null && e.getItem().getItemMeta().hasLore() && e.getItem().getItemMeta().getLore().contains(config.getString("SCEPTRE.lore").replaceAll("&", "§")))
/*     */      {
                    if(e.getPlayer().getInventory().getItemInHand().getAmount() != 1) {
                        e.getPlayer().getInventory().getItemInHand().setAmount(e.getPlayer().getInventory().getItemInHand().getAmount() - 1);
                    } else {
                        e.getPlayer().getInventory().setItemInHand(null);
                    }
/*  57 */         if (config.getBoolean("STRENGTH.cooldown")) {
/*  58 */           if (this.Cooldown.containsKey(e.getPlayer().getUniqueId())) {
/*  59 */             long time = this.Cooldown.get(e.getPlayer().getUniqueId()) / 1000L + config.getInt("STRENGTH.cooldown_time") - System.currentTimeMillis() / 1000L;
/*     */             
/*  61 */             if (time > 0L) {
/*  62 */               Messages.sendActionBar(e.getPlayer(), FileManager.getValues().get(Files.Config).getString("General.Messages.Effects.CooldownActionBar").replaceAll("%TIME%", String.valueOf(time)));
/*  63 */               e.setCancelled(true);
/*     */               return;
/*     */             } 
/*     */           } 
/*  67 */           HealthPlayer(e.getPlayer());
                    e.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    e.getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 0));
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
/*  68 */           this.Cooldown.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
/*     */           
/*     */           return;
/*     */         } else {
                    HealthPlayer(e.getPlayer());
                    e.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    e.getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 0));
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
                    }
/*     */       }
/*     */     }

                if (e.getAction() == Action.RIGHT_CLICK_BLOCK && ((e.getClickedBlock() != null && e.getClickedBlock().getType().equals(Material.CHEST)) || e.getClickedBlock().getType().equals(Material.TRAPPED_CHEST))) {
                    if (e.getItem() != null && e.getItem().getItemMeta().hasLore() && e.getItem().getItemMeta().getLore().contains(config.getString("CHESTVIEWER.lore").replace("&", "§"))) {
                        e.setCancelled(true);
                        if(e.getPlayer().getInventory().getItemInHand().getAmount() != 1) {
                            e.getPlayer().getInventory().getItemInHand().setAmount(e.getPlayer().getInventory().getItemInHand().getAmount() - 1);
                        } else {
                            e.getPlayer().getInventory().setItemInHand(null);
                        }
                        e.getPlayer().getInventory().getItemInHand().setAmount(0);
                        Block block = e.getClickedBlock();
                        Chest coffre = (Chest)block.getState();

                        CInventory inv = new CInventory(coffre.getInventory().getSize(), config.getString("CHESTVIEWER.guiname").replace("&", "§"));

                        for (int i = 0; i < coffre.getInventory().getSize(); i++) {
                            if (coffre.getInventory().getItem(i) != null) {
                                CItem cItem = new CItem(new ItemCreator(coffre.getInventory().getItem(i)));
                                inv.addElement(cItem);
                            }
                        }
                        inv.open(e.getPlayer());
                   }
                }

                if (e.getItem() != null && e.getItem().getItemMeta().hasLore() && e.getItem().getItemMeta().getLore().contains(config.getString("CAVEBLOCK.lore").replace("&", "§"))) {
                    if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        if(!caveblockLocation.containsKey(e.getPlayer()))
                            caveblockLocation.put(e.getPlayer(), e.getClickedBlock().getLocation());
                        return;
                    }
                }

                if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) &&
                    e.getItem() != null && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasLore() &&
                    e.getItem().getItemMeta().getLore().contains(config.getString("DYNAMITE.lore").replace("&", "§"))) {

                    if(e.getPlayer().getInventory().getItemInHand().getAmount() != 1) {
                        e.getPlayer().getInventory().getItemInHand().setAmount(e.getPlayer().getInventory().getItemInHand().getAmount() - 1);
                    } else {
                        e.getPlayer().getInventory().setItemInHand(null);
                    }

                    Item tntPrimed = e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), new ItemCreator(config.getItemStack("DYNAMITE.first.Item")).setLores(Collections.singletonList("§4uncollectable")).getItem());
                    tntPrimed.setInvulnerable(true);
                    tntPrimed.setVelocity(e.getPlayer().getLocation().getDirection().normalize().multiply(config.getInt("DYNAMITE.distanceMultiply")));
                    Bukkit.getServer().getScheduler().runTaskLater(Main.getInstances(), () -> {
                        tntPrimed.setItemStack(new ItemCreator(config.getItemStack("DYNAMITE.second.Item")).setLores(Collections.singletonList("§4uncollectable")).getItem());
                        Bukkit.getServer().getScheduler().runTaskLater(Main.getInstances(), () -> {
                            tntPrimed.getWorld().createExplosion(tntPrimed.getLocation(), config.getInt("DYNAMITE.power"));
                            tntPrimed.remove();
                        }, config.getInt("DYNAMITE.second.TimeBeforeChange") * 20);
                    }, config.getInt("DYNAMITE.first.TimeBeforeChange") * 20);
                    e.setCancelled(true);
                }

                if (e.getAction() == Action.RIGHT_CLICK_BLOCK &&
                    e.getItem() != null && e.getItem().getType().equals(Material.FLINT_AND_STEEL) &&
                    e.getClickedBlock() != null && e.getClickedBlock().getType().name().equals(config.getString("NUKE.item"))) {

                    e.setCancelled(true);
                    e.getClickedBlock().setType(Material.AIR);

                    TNTPrimed tntPrimed = e.getPlayer().getWorld().spawn(e.getClickedBlock().getLocation().add(0.5,0.0,0.5), TNTPrimed.class);
                    tntPrimed.setYield(config.getInt("NUKE.power"));
                }

                if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) &&
                    e.getItem() != null && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasLore() &&
                    e.getItem().getItemMeta().getLore().contains(config.getString("PARCHMENT.defaultlore").replace("&", "§"))) {

                    Random r = new Random();
                    List<Faction> factions = new ArrayList<>(Factions.getInstance().getAllFactions());
                    int randomfaction = r.nextInt(factions.size());
                    int number = 0;
                    while(factions.get(randomfaction).getHome() == null) {
                        if(number >= 50) {
                            e.getPlayer().sendMessage(config.getString("PARCHMENT.nofhomefound"));
                            return;
                        }
                        randomfaction = r.nextInt(Factions.getInstance().getAllFactions().size());
                        number++;
                    }
                    List<String> afterUseLore = config.getStringList("PARCHMENT.afteruselore");
                    List<String> afterUseLoreModif = new ArrayList<>();
                    for (String str : afterUseLore) {
                        String string = str;
                        if (string.contains("%FACTION%"))
                            string = string.replaceAll("%FACTION%", factions.get(randomfaction).getTag());
                        else if (string.contains("%X%"))
                            string = string.replaceAll("%X%", String.valueOf(factions.get(randomfaction).getHome().getX()));
                        else if (string.contains("%Z%"))
                            string = string.replaceAll("%Z%", String.valueOf(factions.get(randomfaction).getHome().getZ()));
                        afterUseLoreModif.add(string.replaceAll("&", "§"));
                    }
                    ItemMeta itemMeta = e.getItem().getItemMeta();
                    itemMeta.setLore(afterUseLoreModif);
                    e.getItem().setItemMeta(itemMeta);
                }

                if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) &&
                    e.getItem() != null && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasLore() &&
                    e.getItem().getItemMeta().getLore().contains(config.getString("RLTNT.lore").replace("&", "§"))) {
                    if(!RLTNTAvailable.contains(e.getPlayer())) {
                        RLTNTAvailable.add(e.getPlayer());
                        Vector vector = e.getPlayer().getLocation().toVector();
                        Vector dir = e.getPlayer().getLocation().getDirection();
                        vector = vector.add(dir.multiply(2));

                        Arrow arrow = e.getPlayer().getWorld().spawn(vector.toLocation(e.getPlayer().getWorld()).add(0, 1.25, 0), Arrow.class);

                        arrow.setVelocity(e.getPlayer().getLocation().getDirection().normalize().multiply(config.getInt("RLTNT.distanceMultiply")));
                        arrow.setCustomName("§4RLTNT");

                        Bukkit.getServer().getScheduler().runTaskLater(Main.getInstances(), ()-> {
                            RLTNTAvailable.remove(e.getPlayer());
                        }, 100);

                        RLTNTScheduler.put(arrow, Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstances(), () -> {
                            if (!arrow.isDead()) {
                                arrow.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, arrow.getLocation(), 0);
                            } else {
                                Bukkit.getServer().getScheduler().cancelTask(RLTNTScheduler.get(arrow));
                                RLTNTScheduler.remove(arrow);
                            }
                        }, 0, 1));
                    } else {
                        e.getPlayer().sendMessage(config.getString("RLTNT.messages.WaitBeforeReLaunche"));
                    }
                }

                if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
                        && e.getItem() != null
                        && e.getItem().hasItemMeta()
                        && e.getItem().getItemMeta().hasLore()
                        && e.getItem().getItemMeta().getLore().contains(config.getString("RLNUKE.lore").replace("&", "§"))) {
                    if(!RLNUKEAvailable.contains(e.getPlayer())) {
                        RLNUKEAvailable.add(e.getPlayer());
                        Vector vector = e.getPlayer().getLocation().toVector();
                        Vector dir = e.getPlayer().getLocation().getDirection();
                        vector = vector.add(dir.multiply(2));

                        Arrow arrow = e.getPlayer().getWorld().spawn(vector.toLocation(e.getPlayer().getWorld()).add(0, 1.25, 0), Arrow.class);

                        arrow.setVelocity(e.getPlayer().getLocation().getDirection().normalize().multiply(config.getInt("RLNUKE.distanceMultiply")));
                        arrow.setCustomName("§4RLNUKE");

                        Bukkit.getServer().getScheduler().runTaskLater(Main.getInstances(), ()-> {
                            RLNUKEAvailable.remove(e.getPlayer());
                        }, 100);

                        RLNUKEScheduler.put(arrow, Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstances(), () -> {
                            if (!arrow.isDead()) {
                                arrow.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, arrow.getLocation(), 0);
                            } else {
                                Bukkit.getServer().getScheduler().cancelTask(RLNUKEScheduler.get(arrow));
                                RLNUKEScheduler.remove(arrow);
                            }
                        }, 0, 1));
                    } else {
                        e.getPlayer().sendMessage(config.getString("RLNUKE.messages.WaitBeforeReLaunche"));
                    }
                }

                if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) &&
                        e.getItem() != null && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasLore() &&
                        e.getItem().getItemMeta().getLore().contains(config.getString("DYNAMITESPONGE.lore"))) {

                    if(e.getPlayer().getInventory().getItemInHand().getAmount() != 1) {
                        e.getPlayer().getInventory().getItemInHand().setAmount(e.getPlayer().getInventory().getItemInHand().getAmount() - 1);
                    } else {
                        e.getPlayer().getInventory().setItemInHand(null);
                    }

                    Item tntPrimed = e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), new ItemCreator(config.getItemStack("DYNAMITESPONGE.first.Item")).setLores(Collections.singletonList("§4uncollectable")).getItem());
                    tntPrimed.setInvulnerable(true);
                    tntPrimed.setVelocity(e.getPlayer().getLocation().getDirection().normalize().multiply(config.getInt("DYNAMITESPONGE.distanceMultiply")));
                    Bukkit.getServer().getScheduler().runTaskLater(Main.getInstances(), () -> {
                        tntPrimed.setItemStack(new ItemCreator(config.getItemStack("DYNAMITESPONGE.second.Item")).setLores(Collections.singletonList("§4uncollectable")).getItem());
                        Bukkit.getServer().getScheduler().runTaskLater(Main.getInstances(), () -> {
                            TNTPrimed tntPrimed1 = tntPrimed.getWorld().spawn(tntPrimed.getLocation(), TNTPrimed.class);
                            tntPrimed1.setCustomName("§4SpongeDynamite");
                            tntPrimed1.setFuseTicks(0);
                            tntPrimed.remove();
                        }, config.getInt("DYNAMITESPONGE.second.TimeBeforeChange") * 20);
                    }, config.getInt("DYNAMITESPONGE.first.TimeBeforeChange") * 20);
                    e.setCancelled(true);
                }

                if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getPlayer().hasPermission("azoniacore.instantconsume") && e.getItem() != null && e.getItem().getType().isEdible()) {
                    if (e.getItem().getType().equals(Material.COOKED_BEEF)) {
                        if(e.getPlayer().getFoodLevel() < 20) {
                            if (e.getItem().getAmount() != 1) {
                                e.getItem().setAmount(e.getItem().getAmount() - 1);
                            } else {
                                e.getPlayer().getInventory().setItemInHand(null);
                            }
                            e.getPlayer().setFoodLevel(e.getPlayer().getFoodLevel() + 5);
                            e.getPlayer().setSaturation((float) (e.getPlayer().getSaturation() + 12.8));
                        }
                    }
                }
            }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory() != null && e.getView().getTitle().contains(FileManager.getValues().get(Files.Effects).getString("CHESTVIEWER.guiname").replace("&", "§")))
            e.setCancelled(true);
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        if (caveblockLocation.containsKey(event.getPlayer()) && caveblockLocation.get(event.getPlayer()).getX() - 2 < event.getBlock().getLocation().getX() && caveblockLocation.get(event.getPlayer()).getX() + 2 > event.getBlock().getLocation().getX() && caveblockLocation.get(event.getPlayer()).getY() - 2 < event.getBlock().getLocation().getY() && caveblockLocation.get(event.getPlayer()).getY() + 2 > event.getBlock().getLocation().getY() && caveblockLocation.get(event.getPlayer()).getZ() - 2 < event.getBlock().getLocation().getZ() && caveblockLocation.get(event.getPlayer()).getZ() + 2 > event.getBlock().getLocation().getZ()) {
            event.getBlock().setType(Material.AIR);
            Player p = event.getPlayer();
            Location location = p.getLocation();
            GameMode gameMode = p.getGameMode();
            p.setGameMode(GameMode.SPECTATOR);
            Bukkit.getServer().getScheduler().runTaskLater(Main.getInstances(), () -> {
                p.teleport(location);
                p.setGameMode(gameMode);
                caveblockLocation.remove(p);
            }, FileManager.getValues().get(Files.Effects).getInt("CAVEBLOCK.duration") * 20);
        }
    }

    @EventHandler
    private void onMove(PlayerMoveEvent event) {
        if(caveblockLocation.containsKey(event.getPlayer())) {
            Player p = event.getPlayer();
            Location location = p.getLocation();
            if(caveblockLocation.get(event.getPlayer()).getX() - FileManager.getValues().get(Files.Effects).getInt("CAVEBLOCK.distance") > p.getLocation().getX())
                p.teleport(location.add(0.5, 0, 0));
            if(caveblockLocation.get(event.getPlayer()).getX() + FileManager.getValues().get(Files.Effects).getInt("CAVEBLOCK.distance") < p.getLocation().getX())
                p.teleport(location.add(-0.5, 0, 0));
            if(caveblockLocation.get(event.getPlayer()).getY() - FileManager.getValues().get(Files.Effects).getInt("CAVEBLOCK.distance") > p.getLocation().getY())
                p.teleport(location.add(0, 0.5, 0));
            if(caveblockLocation.get(event.getPlayer()).getY() + FileManager.getValues().get(Files.Effects).getInt("CAVEBLOCK.distance") < p.getLocation().getY())
                p.teleport(location.add(0, -0.5, 0));
            if(caveblockLocation.get(event.getPlayer()).getZ() - FileManager.getValues().get(Files.Effects).getInt("CAVEBLOCK.distance") > p.getLocation().getZ())
                p.teleport(location.add(0, 0, 0.5));
            if(caveblockLocation.get(event.getPlayer()).getZ() + FileManager.getValues().get(Files.Effects).getInt("CAVEBLOCK.distance") < p.getLocation().getZ())
                p.teleport(location.add(0, 0, -0.5));
        }
    }

    @EventHandler
    private void onFallDamage(EntityDamageByBlockEvent event) {
        if (event.getEntity() instanceof Player) {
            nofallActive.putIfAbsent((Player) event.getEntity(), false);
            if(event.getCause() == EntityDamageEvent.DamageCause.FALL && nofallActive.get(event.getEntity()))
                event.setDamage(0.0);
        }
    }

    @EventHandler
    private void onExplode(EntityExplodeEvent event) {
        if(event != null) {
            if (Objects.equals(event.getEntity().getCustomName(), "§4SpongeDynamite")) {
                event.setCancelled(true);
                Location tntLocation = event.getLocation();
                if (tntLocation.getBlock().getType() == Material.AIR || tntLocation.getBlock().isLiquid()) {
                    event.getLocation().getBlock().setType(Material.SPONGE);
                    event.getLocation().getBlock().setType(Material.AIR);
                }
                return;
            }
        }

        for (Block block : event.blockList()) {
            if (block != null && block.getType().name().equals(FileManager.getValues().get(Files.Effects).getString("NUKE.item"))) {
                block.setType(Material.AIR);

                TNTPrimed tntPrimed = event.getLocation().getWorld().spawn(block.getLocation().add(0.5, 0.0, 0.5), TNTPrimed.class);
                tntPrimed.setYield(FileManager.getValues().get(Files.Effects).getInt("NUKE.power"));
            }
        }
    }

    @EventHandler
    private void onDispense(BlockDispenseEvent event) {
        if(event.getBlock().getType() == Material.DISPENSER) {
            Dispenser dispenser = (Dispenser) event.getBlock().getState();
            if (event.getItem().getType().name().equals(FileManager.getValues().get(Files.Effects).getString("NUKE.item")) && FileManager.getValues().get(Files.Effects).getBoolean("NUKE.dispense")) {
                List<ItemStack> itemStacks = new ArrayList<>();
                boolean remove = false;
                for(ItemStack item : dispenser.getInventory().getContents()) {
                    ItemStack itemStack = item;
                    if(!remove && itemStack != null && itemStack.getType().name().equals(FileManager.getValues().get(Files.Effects).getString("NUKE.item"))) {
                        itemStack.setAmount(itemStack.getAmount() - 1);
                        remove = true;
                    }
                    itemStacks.add(itemStack);
                }
                dispenser.getInventory().setContents(itemStacks.toArray(new ItemStack[0]));
                event.setCancelled(true);

                BlockFace targetFace = ((org.bukkit.material.Dispenser) event.getBlock().getState().getData()).getFacing();
                Location location = event.getBlock().getLocation().add(0.5, 0, 0.5);

                if(targetFace == BlockFace.NORTH)
                    location.add(0,0,-1);
                else if(targetFace == BlockFace.SOUTH)
                    location.add(0,0, 1);
                else if(targetFace == BlockFace.EAST)
                    location.add(1,0,0);
                else if(targetFace == BlockFace.WEST)
                    location.add(-1,0,0);
                else if(targetFace == BlockFace.DOWN)
                    location.add(0, -1,0);
                else if(targetFace == BlockFace.UP)
                    location.add(0,1,0);

                TNTPrimed tntPrimed = event.getBlock().getWorld().spawn(location, TNTPrimed.class);
                tntPrimed.setYield(FileManager.getValues().get(Files.Effects).getInt("NUKE.power"));
            }
        }
    }

    @EventHandler
    private void onPickUp(PlayerPickupItemEvent event) {
        if (event.getItem() != null
                && event.getItem().getItemStack().getItemMeta().hasLore()
                && event.getItem().getItemStack().getItemMeta().getLore().contains("§4uncollectable")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onStackItem(ItemMergeEvent event) {
        if (event.getEntity() != null
                && event.getEntity().getItemStack().getItemMeta().hasLore()
                && event.getEntity().getItemStack().getItemMeta().getLore().contains("§4uncollectable")) {
            event.setCancelled(true);
        } else if(event.getTarget() != null
                && event.getTarget().getItemStack().getItemMeta().hasLore()
                && event.getTarget().getItemStack().getItemMeta().getLore().contains("§4uncollectable")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onArrow(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            if (event.getEntity().getCustomName() != null) {
                if (event.getEntity().getCustomName().equals("§4RLTNT")) {
                    event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), FileManager.getValues().get(Files.Effects).getInt("RLTNT.power"));
                    event.getEntity().remove();
                } else if (event.getEntity().getCustomName().equals("§4RLNUKE")) {
                    event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), FileManager.getValues().get(Files.Effects).getInt("NUKE.power"));
                    event.getEntity().remove();
                }
            }
        }
    }

    @EventHandler
    private void onHasInHand(PlayerItemHeldEvent event) {
        ItemStack itemHeld = event.getPlayer().getInventory().getItem(event.getNewSlot());
        Player player = event.getPlayer();
        if(itemHeld != null
                && itemHeld.hasItemMeta()
                && itemHeld.getItemMeta().hasDisplayName()) {
            if (itemHeld.getItemMeta().getDisplayName().equals(FileManager.getValues().get(Files.Effects).getString("BALLOON.name"))) {
                createBalloon(event, player, false);
            } else if (itemHeld.getItemMeta().getDisplayName().equals(FileManager.getValues().get(Files.Effects).getString("LBALLOON.name"))) {
                createBalloon(event, player, true);
            }
        } else if(balloonTaskId.containsKey(player)) {
            armorStandPlayer.get(player).remove();
            batPlayer.get(player).remove();
            armorStandPlayer.remove(player);
            batPlayer.remove(player);
            player.removePotionEffect(PotionEffectType.LEVITATION);
            Bukkit.getServer().getScheduler().cancelTask(balloonTaskId.get(player));
            balloonTaskId.remove(player);
        }
    }

    private void createBalloon(PlayerItemHeldEvent event, Player player, boolean legendary) {
        if (balloonTaskId.containsKey(player)) {
            armorStandPlayer.get(player).remove();
            batPlayer.get(player).remove();
            armorStandPlayer.remove(player);
            batPlayer.remove(player);
            Bukkit.getServer().getScheduler().cancelTask(balloonTaskId.get(player));
            balloonTaskId.remove(player);
        }

        armorStandPlayer.put(player, player.getWorld().spawn(player.getLocation().add(0.0, 4, 0.0), ArmorStand.class));
        armorStandPlayer.get(player).setGravity(false);
        armorStandPlayer.get(player).setHelmet(new ItemStack(Material.BLUE_WOOL));
        armorStandPlayer.get(player).setInvulnerable(true);
        armorStandPlayer.get(player).setVisible(false);
        armorStandPlayer.get(player).setCollidable(false);
        batPlayer.put(player, player.getWorld().spawn(player.getLocation().add(0.0, 5, 0.0), Bat.class));
        batPlayer.get(player).setInvulnerable(true);
        batPlayer.get(player).setLeashHolder(player);
        batPlayer.get(player).setAI(false);
        batPlayer.get(player).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 255, true, false));

        balloonTaskId.put(player, Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstances(), () -> {
            player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 80, 1, true, false));

            batPlayer.get(player).teleport(player.getLocation().add(0.0, 5, 0.0));
            armorStandPlayer.get(player).teleport(player.getLocation().add(0.0, 4, 0.0));

            ItemStack updateItem = event.getPlayer().getInventory().getItemInHand();
            if (updateItem != null
                    && updateItem.hasItemMeta()
                    && updateItem.getItemMeta().hasDisplayName()
                    && (updateItem.getItemMeta().getDisplayName().equals(FileManager.getValues().get(Files.Effects).getString("BALLOON.name")) || updateItem.getItemMeta().getDisplayName().equals(FileManager.getValues().get(Files.Effects).getString("LBALLOON.name")))) {
                if(!legendary) {
                    String[] durabilitéSplit = DurabilityBar.getLoreDurabilityItem(updateItem);
                    if (durabilitéSplit[0].replaceAll(" ", "").equals("0")) {
                        event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
                        armorStandPlayer.get(player).remove();
                        batPlayer.get(player).remove();
                        armorStandPlayer.remove(player);
                        batPlayer.remove(player);
                        Bukkit.getServer().getScheduler().cancelTask(balloonTaskId.get(player));
                        balloonTaskId.remove(player);
                    } else {
                        ItemMeta itemMeta = updateItem.getItemMeta();
                        itemMeta.setLore(Collections.singletonList(updateItem.getItemMeta().getLore().get(0).replaceFirst(durabilitéSplit[0], (Integer.parseInt(durabilitéSplit[0].replaceAll(" ", "")) - 1) + " ")));
                        updateItem.setItemMeta(itemMeta);
                    }
                }
            } else {
                armorStandPlayer.get(player).remove();
                batPlayer.get(player).remove();
                armorStandPlayer.remove(player);
                batPlayer.remove(player);
                player.removePotionEffect(PotionEffectType.LEVITATION);
                Bukkit.getServer().getScheduler().cancelTask(balloonTaskId.get(player));
                balloonTaskId.remove(player);
            }
        }, 0, 20));
    }

    private void HealthPlayer(Player player) {
        if (player.getHealth() == 0.0D) {
            return;
        }
        double amount = player.getMaxHealth() - player.getHealth();
        EntityRegainHealthEvent erhe = new EntityRegainHealthEvent(player, amount, EntityRegainHealthEvent.RegainReason.CUSTOM);
        double newAmount = player.getHealth() + erhe.getAmount();
        if (newAmount > player.getMaxHealth()) {
            newAmount = player.getMaxHealth();
        }
        player.setHealth(newAmount);
    }
}