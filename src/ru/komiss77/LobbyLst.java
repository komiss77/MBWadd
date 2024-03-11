package ru.komiss77;

import de.marcely.bedwars.api.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import ru.komiss77.modules.player.Oplayer;
import ru.komiss77.modules.player.PM;
import ru.komiss77.utils.ItemBuilder;


class LobbyLst implements Listener {
    
    private static ItemStack arenaSelector;
    private static ItemStack stat;
    private static ItemStack achivki;
    private static ItemStack profile;
    private static ItemStack exit;

    
    public LobbyLst() {
        arenaSelector = new ItemBuilder(Material.CAMPFIRE).name("§aВыбор Арены").build();
        achivki = new ItemBuilder(Material.EMERALD).name("§5Достижения").build();
        stat = new ItemBuilder(Material.END_CRYSTAL).name("§6Статистика").build();
        profile = new ItemBuilder(Material.WARD_ARMOR_TRIM_SMITHING_TEMPLATE).name("§bПрофиль").build();
        exit = new ItemBuilder(Material.MAGMA_CREAM).name("§4Вернуться в лобби").build();
    }
    

    
    public static void lobbyJoin (final Player p) {
//Ostrov.log("------lobbyJoin");
        //if (!player.getWorld().getName().equals("lobby")) player.teleport(Bukkit.getWorld("lobby").getSpawnLocation(),PlayerTeleportEvent.TeleportCause.COMMAND);  //зациклило на onPlayerQuitArenaSpectatorEvent
        //player.setGameMode(GameMode.SURVIVAL);
        //player.getInventory().setArmorContents(new ItemStack[4]);
        p.getInventory().clear();
        //player.updateInventory();

        //player.setAllowFlight(false);
      //  player.setFlying(false);
    //    player.setExp(0.0F);
   //     player.setLevel(0);
      //  player.setSneaking(false);
       // player.setSprinting(false);
     //   player.setFoodLevel(20);
        //player.setMaxHealth(20.0D);
    //    player.setHealth(20.0D);
        p.setFireTicks(0);
    //    player.playerListName(TCUtils.format("§7"+player.getName()));
      //  perWorldTabList(player);
        //player.resetPlayerTime();
        //player.resetPlayerWeather();
        p.getInventory().setItem(0, arenaSelector.clone());
        p.getInventory().setItem(2, achivki.clone());
        p.getInventory().setItem(4, stat.clone());
        p.getInventory().setItem(6, profile.clone());
        p.getInventory().setItem(8, exit.clone());
        p.updateInventory();

        final Oplayer op = PM.getOplayer(p);
        op.tabSuffix("§7[§3Лобби§7]", p);
        op.tag(true);
        //PM.getOplayer(p).setLocalChat(false);
    }    

    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent e) {
        
        Player p = (Player) e.getWhoClicked();
        if (!p.getWorld().getName().equals("lobby")) return;
        Arena arena = de.marcely.bedwars.api.GameAPI.get().getArenaByPlayer(p);
        if (arena==null) {
            ItemStack item = e.getCurrentItem();
            if ( item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName() ) return;
            e.setCancelled(true);

            switch (item.getType()) {
                case CAMPFIRE -> {
                    p.playSound(p.getLocation(), Sound.BLOCK_PISTON_EXTEND , 20.0F, 20.0F);
                    //player.performCommand("mbedwars arenasgui");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mbedwars arenasgui open default_basic "+p.getName());
                    return;
                }
                    
                case EMERALD -> {
                    p.performCommand("bw achievements");
                    return;
                }
                    
                case END_CRYSTAL -> {
                    p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST , 20.0F, 20.0F);
                    p.performCommand("mbedwars stats");
                    return;
                }
                    
                case WARD_ARMOR_TRIM_SMITHING_TEMPLATE -> {
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 20.0F, 20.0F);
                    p.performCommand("profile");
                    return;
                }
                    
                case MAGMA_CREAM -> {
                    p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP , 20.0F, 20.0F);
                    ApiOstrov.sendToServer(p, "lobby0", "");
                    return;
                }
            }
        }
                 
    }


    
    
    
    
    
    
    
    
    
    
    
    
 // ---------------- INTERACT ---------------------------
    
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = false)
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!p.getWorld().getName().equals("lobby") || e.getAction()==Action.PHYSICAL ) return;
        Arena arena = de.marcely.bedwars.api.GameAPI.get().getArenaByPlayer(p);
        if (arena==null) {
            ItemStack item = e.getItem();
            if ( item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName() ) return;
            e.setCancelled(true);
            switch (item.getType()) {
                case CAMPFIRE -> {
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 20.0F, 20.0F);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mbedwars arenasgui open default_basic "+p.getName());
                    return;
                }
                    
                case EMERALD -> {
                    p.performCommand("bw achievements");
                    return;
                }
                    
                case END_CRYSTAL -> {
                    p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST , 20.0F, 20.0F);
                    p.performCommand("mbedwars stats");
                    return;
                }
                    
                case WARD_ARMOR_TRIM_SMITHING_TEMPLATE -> {
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP , 20.0F, 20.0F);
                    p.performCommand("profile");
                    return;
                }
                    
                case MAGMA_CREAM -> {
                    p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP , 20.0F, 20.0F);
                    ApiOstrov.sendToServer(p, "lobby0", "");
                    return;
                }
            }

        }


    }
    



    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPickup(EntityPickupItemEvent e) {
        if (e.getEntityType()==EntityType.PLAYER && e.getEntity().getWorld().getName().equals("lobby") ) {
            e.setCancelled(true);
            e.getItem().remove();
        }
    }

            
        
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent e) {
       if (e.getPlayer().getWorld().getName().equals("lobby") ) {
            e.setCancelled(true);
            e.getItemDrop().remove();
        }
    }

    
    
    
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onSleep(PlayerBedEnterEvent e) {
        e.setCancelled(true);
    }

    
    
    
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onFly(PlayerToggleFlightEvent e) {
        if(!e.getPlayer().isOp()) e.setCancelled(true);
    }

    

    //@EventHandler(priority = EventPriority.HIGH)
    //public void onHunger(FoodLevelChangeEvent e) {
    //		e.setCancelled(false);
    //}
    
   @EventHandler
    public void onPlayerSwapoffHand(PlayerSwapHandItemsEvent e) {
        if (e.getPlayer().getWorld().getName().equals("lobby") ) e.setCancelled(true);
    }
    
    
    
    

    
    @EventHandler (ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerDamage(EntityDamageEvent e) { 

        if ( e.getEntityType()!=EntityType.PLAYER ) return;

        final Player p = (Player) e.getEntity();

        if (p.getWorld().getName().equals("lobby")) {
            e.setDamage(0);
            if (e.getCause()==EntityDamageEvent.DamageCause.VOID) {
                p.setFallDistance(0);
                p.teleport(Bukkit.getServer().getWorlds().get(0).getSpawnLocation(), PlayerTeleportEvent.TeleportCause.COMMAND); //от PLUGIN блокируются
                return;
            }
            e.setCancelled(true);
            return;
        }
            
        if (e.getCause()==EntityDamageEvent.DamageCause.VOID) { //падение в бездну - сразу гибель
           if (p.getGameMode()==GameMode.SURVIVAL) {
               e.setDamage(21);
//System.out.println("onPlayerDamage  DamageCause.VOID "+p.getName());        
           }
            //p.playSound(p.getLocation(), Sound.ENTITY_PARROT_IMITATE_SKELETON , 20.0F, 20.0F);
        }
    }      

    
    
    
    
    
    
     
    @EventHandler(ignoreCancelled = true,priority = EventPriority.LOWEST)    
    public void onPlace(BlockPlaceEvent e) {
        //PM.getOplayer(e.getPlayer().getName()).last_breack=Timer.Единое_время();
        if ( !ApiOstrov.isLocalBuilder(e.getPlayer(), false) && e.getPlayer().getWorld().getName().equals("lobby") ) e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true,priority = EventPriority.LOWEST)    
    public void onBreak(BlockBreakEvent e) {
        if ( !ApiOstrov.isLocalBuilder(e.getPlayer(), false) && e.getPlayer().getWorld().getName().equals("lobby") ) e.setCancelled(true);
    }
 

    
    
    
    
}

