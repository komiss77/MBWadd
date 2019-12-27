package ru.komiss77;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import de.marcely.bedwars.api.Arena;
import de.marcely.bedwars.api.ArenaStatus;
import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.event.ArenaOutOfTimeEvent;
import de.marcely.bedwars.api.event.BedBreakEvent;
import de.marcely.bedwars.api.event.PlayerEarnAchievementEvent;
import de.marcely.bedwars.api.event.PlayerJoinArenaSpectatorEvent;
import de.marcely.bedwars.api.event.PlayerKillPlayerEvent;
import de.marcely.bedwars.api.event.PlayerQuitArenaEvent;
import de.marcely.bedwars.api.event.PlayerQuitArenaSpectatorEvent;
import de.marcely.bedwars.api.event.PlayerRoundDeathEvent;
import de.marcely.bedwars.api.event.PlayerUseExtraItemEvent;
import de.marcely.bedwars.api.event.RoundEndEvent;
import de.marcely.bedwars.api.event.TeamEliminateEvent;
import me.clip.deluxechat.DeluxeChat;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ru.komiss77.Enums.Data;
import ru.komiss77.Events.BsignLocalArenaClick;
import ru.komiss77.Events.BungeeStatRecieved;
import ru.komiss77.Events.FriendTeleportEvent;
import ru.komiss77.Managers.PM;
import ru.komiss77.ProfileMenu.E_Stat;
import ru.komiss77.utils.LocationUtil;






class PlayerListener implements Listener {

    public PlayerListener() {}
    
    
    
    
    
    
    
    
    
    @EventHandler (priority = EventPriority.MONITOR)
    public void onJoin (final PlayerJoinEvent e) {
        
        LobbyListener.lobbyJoin(e.getPlayer());
        
        
        
        
        new BukkitRunnable() {
            @Override
            public void run() {
                switchLocalGlobal(e.getPlayer(), false);
            }
        }.runTaskLater(BwAdd.instance, 1);
    }
    

    
    
    
    
    
    
    @EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBsignLocalArenaClick (final BsignLocalArenaClick e) {
        if (BedwarsAPI.getArena(e.arenaName)!=null) {
            e.player.performCommand("bw join "+e.arenaName);
        } else {
            e.player.sendMessage("§cНе найдена арена "+e.arenaName+" !");
        }
//System.out.println(" ---- BsignLocalArenaClick --- "+e.player.getName()+" "+e.arenaName);
        
    }
    
    
    
    
    
@EventHandler(priority = EventPriority.MONITOR)
    public void onBungeeStatRecieved(final BungeeStatRecieved e) {  
        //final Oplayer op = ru.komiss77.Managers.PM.getOplayer(e.getPlayer().getName());
//System.out.println( "ArenaJoinEvent ->"+e.getOplayer().getBungeeData(Data.WANT_ARENA_JOIN) );
        final String wantToArena = e.getOplayer().getBungeeData(Data.WANT_ARENA_JOIN);
        if (wantToArena.isEmpty() || wantToArena.equals("any")) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                e.getPlayer().performCommand("bw join "+wantToArena);
            }
        }.runTaskLater(BwAdd.instance, 5); //не уменьшать!! на 1 - срабатывает lobbyJoin
        
       /* final Player p = e.getPlayer();
        final Arena currentArena = BedwarsAPI.getArena(p);
        final Arena wantToArena = BedwarsAPI.getArena(e.getOplayer().getBungeeData(Data.WANT_ARENA_JOIN));
        new BukkitRunnable() {
            @Override
            public void run() {
//System.out.println("-- BungeeStatRecieved "+p.getName()+" wantTo="+e.getOplayer().getBungeeData(Data.WANT_ARENA_JOIN)+" currentArena="+currentArena+" wantToArena="+wantToArena);
                if (currentArena==null && wantToArena!=null) {
                    if (wantToArena.GetStatus()==ArenaStatus.Lobby) {
                        wantToArena.addPlayer(p);
                    } else if (wantToArena.GetStatus()==ArenaStatus.Running) {
                        BedwarsAPI.enterSpectatorMode(p, wantToArena, SpectateReason.PLUGIN);
                    } else {
                        //wantToArena.addPlayer(p);
                    } 
                }
                
                //AM.addPlayer(e.getPlayer(), e.getOplayer().getBungeeData(Data.WANT_ARENA_JOIN));
            }
        }.runTaskLater(BwAdd.instance, 10);*/

    }

    
     
    
    
    
    
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onBedClick(PlayerInteractEvent e) {
        final Player player = e.getPlayer();
//System.out.println("onSpectatorInteract (кровать??) "+e.getAction()+"   "+e.getItem()+" ClickedBlock="+e.getClickedBlock());                
        if (player.getWorld().getName().equals("lobby") || e.getAction()==Action.PHYSICAL ) return;
        
        if (e.getClickedBlock()!=null && e.getClickedBlock().getType().toString().contains("_BED")) {
            if (e.getAction()==Action.LEFT_CLICK_BLOCK) {
                final Arena arena = BedwarsAPI.getArena(player);
                if (arena==null || arena.GetStatus()!=ArenaStatus.Running) return;
                if (e.hasItem()) {
                    player.sendMessage("§cСломать цель можно только рукой!");
                    e.setCancelled(true);
                } else if(LocationUtil.getDistance( e.getPlayer().getLocation(), e.getClickedBlock().getLocation())>3) {
                    e.getPlayer().sendMessage("Вы должны быть ближе к цели, чтобы сломать её!");
                    e.setCancelled(true);
                } 
                //return;
            } else  if (e.getAction()==Action.RIGHT_CLICK_BLOCK) {
                e.setCancelled(true);
            }
        }
    }
        
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = false)
    public void onItemClick(PlayerInteractEvent e) {
        final Player player = e.getPlayer();
//System.out.println("onSpectatorInteract 2");                
        if (e.getItem()==null || !e.getItem().hasItemMeta() || !e.getItem().getItemMeta().hasDisplayName()) return;
        if (e.getItem().getType()==LobbyListener.nextArenaSpectate.getType() && e.getItem().getItemMeta().getDisplayName().equals(LobbyListener.nextArenaSpectate.getItemMeta().getDisplayName())) {
//System.out.println("onSpectatorInteract Next Arena");
            List<String> arenaInGame=new ArrayList();
            
            for (Arena a:BedwarsAPI.getArenas()) {
                if ( a.GetStatus()==ArenaStatus.Running) { //перебираем работающие арены
                    if ( !a.getWorld().getName().equals(player.getWorld().getName()) ) continue; //перебираем до арены, на которой находимся
                    if ( !a.getWorld().getName().equals(player.getWorld().getName()) ) arenaInGame.add(a.getName()); //добавляем остальные кроме текущей, что далее по списку
                }
            }
            
            if (arenaInGame.isEmpty()) {        //если далее по списку не найдено работающих арен, делаем еще один проход сначала
                for (Arena a:BedwarsAPI.getArenas()) {
                    if ( a.GetStatus()==ArenaStatus.Running) { //перебираем работающие арены
                        if ( a.getWorld().getName().equals(player.getWorld().getName()) ) break; //если дошли до нашей, значит, больше не искать
                        arenaInGame.add(a.getName()); //добавляем те, что найдены ДО нашей
                    }
                }
            }

            if (arenaInGame.isEmpty()) {
                player.sendMessage("§5Больше нет арен для наблюдения!");
            } else {
                player.performCommand("bw join "+arenaInGame.get(0));
                player.sendMessage("§5Переходим к арене "+arenaInGame.get(0));
            }
        }
        
    }
    
    
    
    
    
    
    @EventHandler (ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onWorldChange (final PlayerChangedWorldEvent e) {
//System.out.println("PlayerChangedWorldEvent from="+e.getFrom().getName());
        //final Player p = e.getPlayer();
        //final Arena arena = BedwarsAPI.getArena(p);
        new BukkitRunnable() {
            final Player p = e.getPlayer();
            @Override
            public void run() {
                    switchLocalGlobal(p, true);
                    LobbyListener.perWorldTabList(e.getPlayer());
                    if (PM.nameTagManager!=null && !e.getPlayer().getWorld().getName().equals("lobby")) {   
                        PM.nameTagManager.setNametag(e.getPlayer().getName(), "", "");
                    }
                if (p.isOnline() && p.getWorld().getName().equals("lobby")) {
                    LobbyListener.lobbyJoin(p);
                }
            }
        }.runTaskLater(BwAdd.instance, 21);
        
        
        
        
    }
    

    
    
    
    @EventHandler (ignoreCancelled = false, priority = EventPriority.LOWEST)
    public void onTeleport (final PlayerTeleportEvent e) {
        //if (!e.getFrom().getWorld().getName().equals(e.getTo().getWorld().getName())) return; //перемещения между мирами не интересуют
System.out.println(">> Teleport "+" canceled?"+e.isCancelled()+" cause="+e.getCause()+" from="+LocationUtil.StringFromLoc(e.getFrom())+" to="+LocationUtil.StringFromLoc(e.getTo())+" gamemode ="+e.getPlayer().getGameMode()  );
        if ( e.getTo().getWorld().getName().equals("lobby")) { //перемещения внутри лобби
            if (e.getCause()==PlayerTeleportEvent.TeleportCause.PLUGIN) {
                if (e.getFrom().getWorld().getName().equals("lobby")) {
System.out.println("PlayerTeleportEvent - подмена точки на исходую!!!!");
                    e.setTo( e.getFrom().clone() );
                } //else {
System.out.println("PlayerTeleportEvent - разброс!!!!");
                    e.setTo( e.getTo().clone().add( ApiOstrov.randInt(-1, 1), 0, ApiOstrov.randInt(-1, 1)) );
                //}
                return;
            }
        }

        
        if ( !e.getTo().getWorld().getName().equals("lobby") && e.getPlayer().getGameMode()==GameMode.SPECTATOR) {
System.out.println("PlayerTeleportEvent - Зритель на 3 блока выше!");
            e.setTo(e.getTo().clone().add(0, 3, 0));
        }
        
    }
    
    
    
    
    
        
//эвенты игроков        
    @EventHandler (priority = EventPriority.MONITOR)
    public void onBedBreakEvent (final BedBreakEvent e) {
System.out.println("BedBreakEvent distance="+LocationUtil.getDistance( e.getPlayer().getLocation(), e.getLocation()));
        ApiOstrov.addIntStat(e.getPlayer(), E_Stat.BW_bed);
    }        
    
        
    @EventHandler (priority = EventPriority.MONITOR)//добавить проверку - если кровати уже нет, то BW_loose
    public void onPlayerKillPlayerEvent (final PlayerKillPlayerEvent e) {
System.out.println("PlayerKillPlayerEvent ");
        ApiOstrov.addIntStat(e.getDamager(), E_Stat.BW_kill);
    }   
    
    //VOID 1, после  PlayerTeleportEvent cause=PLUGIN from=lobby<>24<>71<>9 to=lobby<>24<>71<>9 canceled?false gamemode =SURVIVAL - подмена точки
    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerRoundDeathEvent (final PlayerRoundDeathEvent e) {
        ApiOstrov.addIntStat(e.getPlayer(), E_Stat.BW_death);
System.out.println("PlayerRoundDeathEvent RespawnLocation="+LocationUtil.StringFromLoc(e.getParentEvent().getRespawnLocation()));
            //final Player p = e.getPlayer();
            //if (e.getArena()!=null && e.getArena().GetStatus()==ArenaStatus.Running && e.getArena().getPlayers().contains(p)) {
            //    e.getParentEvent().setRespawnLocation(e.getArena().getWorld().getSpawnLocation());
//System.out.println("PlayerRoundDeathEvent 2 RespawnLocation="+LocationUtil.StringFromLoc(e.getParentEvent().getRespawnLocation()));
            //}
//System.out.println("");
           /* final Player  p = e.getPlayer();
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (p.isOnline()) p.spigot().respawn();
                }
            }.runTaskLater(BwAdd.instance, 30L);*/
    }        
       

    
@EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDie(PlayerDeathEvent e) {
        e.setDeathMessage(null);
System.out.println( "PlayerDeathEvent loc=" + LocationUtil.StringFromLoc(e.getEntity().getLocation()) );
            new BukkitRunnable() {
            final Player p = e.getEntity();
                @Override
                public void run() {
                    if (p!=null && p.isOnline()) p.spigot().respawn();
//System.out.println(" !!! EnumClientCommand.PERFORM_RESPAWN !!!    getLastDamageCause="+p.getLastDamageCause().getCause());
                    //if (p.getLastDamageCause().getCause()==DamageCause.VOID) {
                    if (p.getLocation().getBlockY()<10) {
                        //p.setGameMode(GameMode.SPECTATOR);
                        //final Arena arena = BedwarsAPI.getArena(p);
                        //if (arena!=null) {
                            p.setGameMode(GameMode.SPECTATOR);
                            p.teleport(LocationUtil.getNearestPlayer(p).clone().add(0, 5, 0));
System.out.println( "PERFORM_RESPAWN loc=" + LocationUtil.StringFromLoc(e.getEntity().getLocation()) +" getNearestPlayer="+LocationUtil.StringFromLoc(LocationUtil.getNearestPlayer(p)));
                        //}
                    }
                }
            }.runTaskLater(BwAdd.instance, 5L);
    }
    
    

    
    
//@EventHandler(priority = EventPriority.HIGHEST)
//    public void onBukkitPlayerRespawn(org.bukkit.event.player.PlayerRespawnEvent e) {
//System.out.println("onBukkitPlayerRespawn RespawnLocation="+LocationUtil.StringFromLoc(e.getRespawnLocation()) );
        /*final Player p = e.getEntity();
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.spigot().respawn();
                }
            }.runTaskLater(BwAdd.instance, 1L);*/
//    }
    
    
//@EventHandler(priority = EventPriority.HIGHEST)
//    public void onBwPlayerRespawn(de.marcely.bedwars.api.event.PlayerRespawnEvent e) {
        /*final Player p = e.getEntity();
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.spigot().respawn();
                }
            }.runTaskLater(BwAdd.instance, 1L);*/
//    }
    
    
    
    
    
    
    
//    @EventHandler (priority = EventPriority.MONITOR)
//    public void onPlayerRoundRespawnEvent (final PlayerRoundRespawnEvent e) {
//System.out.println("onPlayerRoundRespawnEvent ");
//System.out.println("");
//    }        


    
    
    
    
    
    
    
    
    
    
    
    @EventHandler (priority = EventPriority.MONITOR)  //dсюда можно повесить BW_loose если вышел во врея игры
    public void onPlayerQuitArenaEvent (final PlayerQuitArenaEvent e) {
System.out.println(" ---- onPlayerQuitArenaEvent --- "+e.getArena().getName()+" "+e.getPlayer().getName()+" arena contains?"+e.getArena().getPlayers().contains(e.getPlayer())+" KickReason="+e.getReason()  );
       //final Player p = e.getPlayer();
        new BukkitRunnable() {
        final Player p = e.getPlayer();
            @Override
            public void run() {
                if (p.isOnline() && p.getWorld().getName().equals("lobby")) {
System.out.println(" ---- onPlayerQuitArenaEvent --- lobbyJoin !!");
                    LobbyListener.lobbyJoin(p);
                }
            }
        }.runTaskLater(BwAdd.instance, 30);
        //if ( e.getReason()==KickReason.Leave ) LobbyListener.lobbyJoin(e.getPlayer());
    }        
    
    
    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerJoinArenaSpectatorEvent (final PlayerJoinArenaSpectatorEvent e) { //переход игрока арены в статус зрителя после гибели
        final Player p = e.getPlayer();
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10, 1), true);
        final Arena arena = BedwarsAPI.getArena(e.getPlayer());
System.out.println(" ---- onPlayerJoinArenaSpectatorEvent --- "+e.getArena().getName()+" "+e.getPlayer().getName()+" arena="+(arena==null?"null": arena.getName()+" contains?"+e.getArena().getPlayers().contains(e.getPlayer()) ) );
        if (arena==null) { //только для чистого зрителя
            e.getPlayer().setPlayerListName("§8[Зритель] "+e.getPlayer().getName());
            new BukkitRunnable() {
                @Override
                public void run() {
    //System.out.println(" ---- onPlayerJoinArenaSpectatorEvent 1 give elytra item="+p.getInventory().getItem(0));                   
                    if (p.getInventory().getItem(0)!=null && p.getInventory().getItem(0).getType()==Material.CLOCK && 
                            p.getInventory().getItem(0).hasItemMeta() && p.getInventory().getItem(0).getItemMeta().hasDisplayName()) {
    //System.out.println(" ---- onPlayerJoinArenaSpectatorEvent 2 give elytra ");                   
                                p.getInventory().setItem(7, LobbyListener.nextArenaSpectate);
                                p.updateInventory();
                    }
                }
            }.runTaskLater(BwAdd.instance, 21);
        }
        //if (arena!=null && arena.getPlayers().contains(e.getPlayer()))
            //e.getPlayer().getInventory().setItem(7, LobbyListener.nextArenaSpectate.clone());
    }
    
    
    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerQuitArenaSpectatorEvent (final PlayerQuitArenaSpectatorEvent e) {
System.out.println(" ---- onPlayerQuitArenaSpectatorEvent --- "+e.getArena().getName()+" "+e.getPlayer().getName()+" arena contains?"+e.getArena().getPlayers().contains(e.getPlayer()));
        new BukkitRunnable() {
            final Player p = e.getPlayer();
            @Override
            public void run() {
                if (p.isOnline() && p.getWorld().getName().equals("lobby")) {
                    LobbyListener.lobbyJoin(p);
                }
            }
        }.runTaskLater(BwAdd.instance, 40);
        //if ( !e.getArena().getPlayers().contains(e.getPlayer()) ) LobbyListener.lobbyJoin(e.getPlayer());
    }
    

    
    
    
    
    
    
    
    
    
    @EventHandler  //команда уничтожена
    public void onTeamEliminateEvent (final TeamEliminateEvent e) {
System.out.println("TeamEliminateEvent getFinalPlayer="+e.getFinalPlayer().getName()+" isCausingEnd?"+e.isCausingEnd());
//System.out.println("");
    }        

    
    
    
    @EventHandler //время вышло, по эвенту можно продлить
    public void onArenaOutOfTimeEvent (final ArenaOutOfTimeEvent e) { 
//System.out.println("ArenaOutOfTimeEvent ");
//System.out.println("");
    }        
    
    
   // @EventHandler
  //  public void onOutOfArenaEvent (final OutOfArenaEvent e) { //вообще не используется вроде
//System.out.println("OutOfArenaEvent ");
//System.out.println("");
  //  }        
    
    
    @EventHandler
    public void onRoundEndEvent (final RoundEndEvent e) {  //вызывается если определилась команда-победители, после эвента выполняется ConfigValue.prize_commands
System.out.println("RoundEndEvent ");
//System.out.println("");
        for (Player p : e.getWinners()) {
            ApiOstrov.addIntStat(p, E_Stat.BW_game);
            ApiOstrov.addIntStat(p, E_Stat.BW_win);
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "reward "+p.getName()+" money add rnd:500:1000 bedWars" );
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "reward "+p.getName()+" exp add rnd:10:50 bedWars" );
        }
        for (Player p : e.getLosers()) {
            ApiOstrov.addIntStat(p, E_Stat.BW_game);
            ApiOstrov.addIntStat(p, E_Stat.BW_loose);
        }
    }        
    
    
    
    
    @EventHandler
    public void onPlayerEarnAchievementEvent (final PlayerEarnAchievementEvent e) {
System.out.println("PlayerEarnAchievementEvent ");
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "reward "+e.getPlayer().getName()+" exp add rnd:5:10 bedWars" );
    }
    
    @EventHandler
    public void onPlayerUseExtraItemEvent (final PlayerUseExtraItemEvent e) {
System.out.println("PlayerUseExtraItemEvent ");
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "reward "+e.getPlayer().getName()+" exp add rnd:5:10 bedWars" );
    }        
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static void switchLocalGlobal(final Player p, final boolean notify) {
        if (p.getWorld().getName().equalsIgnoreCase("lobby")) { //оказались в лобби, делаем глобальный
            if ( DeluxeChat.isLocal(p.getUniqueId().toString()) ){
                if (notify) p.sendMessage("§fЧат переключен на глобальный");
                Ostrov.deluxechatPlugin.setGlobal(p.getUniqueId().toString());
            }
        } else {
            if ( !DeluxeChat.isLocal(p.getUniqueId().toString()) )  {
                if (notify) p.sendMessage("§fЧат переключен на Игровой");
                Ostrov.deluxechatPlugin.setLocal(p.getUniqueId().toString());
            }
        }
    }
    
    
    
    
    
@EventHandler
    public void FriendTeleport(FriendTeleportEvent e) {
        Arena target_player_arena =BedwarsAPI.getArena(e.target);
        if (target_player_arena != null && target_player_arena.GetStatus()!=ArenaStatus.Lobby) e.Set_canceled(true, "§eв игре!");
    }
    

    
//@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
 //   public void onDamage(EntityDamageEvent e) {
 //   }    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
