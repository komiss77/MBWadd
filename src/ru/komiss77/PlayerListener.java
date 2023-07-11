package ru.komiss77;

import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.arena.AddPlayerIssue;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.event.player.PlayerQuitEvent;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.event.arena.ArenaBedBreakEvent;
import de.marcely.bedwars.api.event.arena.ArenaOutOfTimeEvent;
import de.marcely.bedwars.api.event.arena.ArenaStatusChangeEvent;
import de.marcely.bedwars.api.event.arena.RoundEndEvent;
import de.marcely.bedwars.api.event.arena.TeamEliminateEvent;
import de.marcely.bedwars.api.event.player.PlayerEarnAchievementEvent;
import de.marcely.bedwars.api.event.player.PlayerIngameDeathEvent;
import de.marcely.bedwars.api.event.player.PlayerJoinArenaEvent;
import de.marcely.bedwars.api.event.player.PlayerKillPlayerEvent;
import de.marcely.bedwars.api.event.player.PlayerOpenShopEvent;
import de.marcely.bedwars.api.event.player.PlayerQuitArenaEvent;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.event.player.SpectatorJoinArenaEvent;
import de.marcely.bedwars.api.event.player.SpectatorQuitArenaEvent;
import de.marcely.bedwars.api.game.shop.layout.ShopLayoutType;
import ru.komiss77.utils.LocationUtil;
import ru.komiss77.enums.Data;
import ru.komiss77.enums.Stat;
import ru.komiss77.events.BsignLocalArenaClick;
import ru.komiss77.events.BungeeDataRecieved;
import ru.komiss77.events.FriendTeleportEvent;
import ru.komiss77.modules.games.ArenaInfo;
import ru.komiss77.modules.games.GM;
import ru.komiss77.modules.player.Oplayer;
import ru.komiss77.modules.player.PM;





class PlayerListener implements Listener {

    protected static final Map <String,ShopLayoutType> shopDesign = new HashMap();
    
    public PlayerListener() {}
    
    
    
    
    
    
    @EventHandler (priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onOpenShop (final PlayerOpenShopEvent e) {
        final String name = e.getPlayer().getName();
        if (shopDesign.containsKey(name) && shopDesign.get(name)!=e.getLayout().getType()) {
            e.setLayout(shopDesign.get(name).getLayout());
        }
    }
     
    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onQuit (final PlayerQuitEvent e) {
        if (shopDesign.containsKey(e.getPlayer().getName())) {
           shopDesign.remove(e.getPlayer().getName());
        }
    }
    
    
    
    
  //  @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = false)
  //  public void test (final PlayerInteractEvent e) {
      //  if (e.getAction()==Action.LEFT_CLICK_AIR ) {
           // final Player p = e.getPlayer();
            //p.sendMessage("speed="+p.getWalkSpeed());
            
           // p.sendMessage("--------- getStoredKeys ");
//p.sendMessage (BedwarsAPI.getPlayerDataAPI().getPropertiesNow(p.getUniqueId()).get().getStoredKeys().toString());
            //BedwarsAPI.getPlayerDataAPI().getPropertiesNow(p.getUniqueId()).get().getStoredKeys();

            //p.sendMessage("--------- getShopLayouts");

            //for (ShopLayout shop : BedwarsAPI.getGameAPI().getShopLayouts()) {
           //     p.sendMessage( shop.getName() );
           // }
          //  for (LobbyItem li : BedwarsAPI.getGameAPI().getLobbyItems()) {
          //      p.sendMessage( li.getName() );
          //      LobbyItemHandler lih = BedwarsAPI.getGameAPI().getLobbyItemHandler(li.getName());
          //  }

          //  p.sendMessage("---------");
      //  }
   // }
    
        
    
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
    

    
    @EventHandler (priority = EventPriority.MONITOR)
    public void onStatusChange (final ArenaStatusChangeEvent e) {
//System.out.println(" -----  onStatusChange "+e.getOldStatus()+"->"+e.getNewStatus());
        if (e.getOldStatus()==ArenaStatus.END_LOBBY) {
            e.getArena().getPlayers().forEach( (p) -> {
                LobbyListener.lobbyJoin(p);
            } );
            e.getArena().getSpectators().forEach( (p) -> {
                LobbyListener.lobbyJoin(p);
                //BedwarsAPI.getGameAPI().
            } );
        }
        
        //if (e.getOldStatus()==ArenaStatus.RUNNING && e.getNewStatus()==ArenaStatus.END_LOBBY) {
        //    e.getArena().getSpectators().forEach( (p) -> {
        //        LobbyListener.lobbyJoin(p);
                //BedwarsAPI.getGameAPI().
        //    } );
       // }
//System.out.println("PlayerRoundDeathEvent RespawnLocation="+LocationUtil.StringFromLoc(e.getPlayer().getRespawnLocation()));
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
       
    @EventHandler (priority = EventPriority.MONITOR)  //dсюда можно повесить BW_loose если вышел во врея игры
    public void onPlayerJoinArenaEvent (final PlayerJoinArenaEvent e) {
        
        
        
        final Oplayer op = PM.getOplayer(e.getPlayer());
//System.out.println(" ---- PlayerJoinArenaEvent --- "+e.getArena().getDisplayName()+"("+e.getArena().getName()+") lvl="+op.getStat(Stat.LEVEL)+" rep="+op.getStat(Stat.REPUTATION));
        final ArenaInfo ai = GM.getGameInfo(GM.GAME).getArena(Ostrov.MOT_D, e.getArena().getDisplayName());
        //if (ai==null) { //багает при отсутствии связи с островом, лучше без него
        //    BwAdd.log_err("нет ArenaInfo для арены "+e.getArena().getName());
        //    e.addIssue(AddPlayerIssue.PLUGIN);
         //   e.getPlayer().sendMessage("§cАрены ещё не готовы к работе!");
         //   return;
        //}
//System.out.println("ai lvl="+ai.level+" ai rep="+ai.reputation);
//if (e.getPlayer().getInventory().getItemInMainHand().getType()==Material.STICK) 
        if (op.getStat(Stat.LEVEL)<ai.level) {
            e.getPlayer().sendMessage("§cАрена будет доступна с уровня §e"+ai.level);
            e.addIssue(AddPlayerIssue.PLUGIN);
            return;
        }
        if (op.getStat(Stat.REPUTATION)<ai.reputation) {
            e.getPlayer().sendMessage("§cАрена будет доступна при репутации выше §e"+ai.reputation);
            e.addIssue(AddPlayerIssue.PLUGIN);
            return;
        }
        //e.addIssue(AddPlayerIssue.PLUGIN);
        
        e.getPlayer().getInventory().clear();
    }        
    //@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = false)
   // public void respawn (final PlayerRespawnEvent e) {
     //   final Player p = e.getPlayer();
//p.sendMessage("-----speed="+p.getWalkSpeed());
    //    p.setWalkSpeed(0.2f);
   // }
    
    
    
    
    @EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBsignLocalArenaClick (final BsignLocalArenaClick e) {
//System.out.println(" ---- BsignLocalArenaClick --- "+e.player.getName()+" "+e.arenaName);
        //final String displayname = Chatc
        if (BedwarsAPI.getGameAPI().getArenaByPlayer(e.player)!=null) {
            e.player.sendMessage("§cВы уже присоединились к арене "+BedwarsAPI.getGameAPI().getArenaByPlayer(e.player).getDisplayName()+" !");
            return;
        }
        for (final Arena arena : BedwarsAPI.getGameAPI().getArenas()) {
            if (arena.getDisplayName().equalsIgnoreCase(e.arenaName)) {
                e.player.performCommand("bw join "+arena.getName());
                return;
            }
        }
        //if (de.marcely.bedwars.api.GameAPI.get().getArenaByName(e.arenaName)!=null) {
        //    e.player.performCommand("bw join "+e.arenaName);
        //} else {
        
        e.player.sendMessage("§cНе найдена арена "+e.arenaName+" !");
        //}
        
    }
    
    
    
    
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBungeeStatRecieved(final BungeeDataRecieved e) {  
        //final Oplayer op = ru.komiss77.Managers.PM.getOplayer(e.getPlayer().getName());
//System.out.println( "ArenaJoinEvent ->"+e.getOplayer().getBungeeData(Data.WANT_ARENA_JOIN) );
        final String wantToArena = e.getOplayer().getDataString(Data.WANT_ARENA_JOIN);
        if (wantToArena.isEmpty() || wantToArena.equals("any")) return;
        for (final Arena arena : BedwarsAPI.getGameAPI().getArenas()) {
            if (arena.getDisplayName().equalsIgnoreCase(wantToArena)) {
                //e.player.performCommand("bw join "+arena.getName());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        e.getPlayer().performCommand("bw join "+arena.getName());
                    }
                }.runTaskLater(BwAdd.instance, 5); //не уменьшать!! на 1 - срабатывает lobbyJoin
                return;
            }
        }
       // new BukkitRunnable() {
      //      @Override
      //      public void run() {
       //         e.getPlayer().performCommand("bw join "+wantToArena);
       //     }
       // }.runTaskLater(BwAdd.instance, 5); //не уменьшать!! на 1 - срабатывает lobbyJoin
        
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
                final Arena arena = de.marcely.bedwars.api.GameAPI.get().getArenaByPlayer(player);
                if (arena==null || arena.getStatus()!=ArenaStatus.RUNNING) return;
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
     /*   
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = false)
    public void onItemClick(PlayerInteractEvent e) {
        final Player player = e.getPlayer();
//System.out.println("onSpectatorInteract 2");                
        //if (e.getItem()==null || !e.getItem().hasItemMeta() || !e.getItem().getItemMeta().hasDisplayName()) return;
        if (ItemUtils.compareItem(e.getItem(), LobbyListener.nextArenaSpectate, true)) {
        //if (e.getItem().getType()==LobbyListener.nextArenaSpectate.getType() && e.getItem().getItemMeta().getDisplayName().equals(LobbyListener.nextArenaSpectate.getItemMeta().getDisplayName())) {
//System.out.println("onSpectatorInteract Next Arena");
            List<String> arenaInGame=new ArrayList();
            
            for (Arena a:de.marcely.bedwars.api.GameAPI.get().getArenas()) {
                if ( a.getStatus()==ArenaStatus.RUNNING) { //перебираем работающие арены
                    if ( !a.getGameWorldName().equals(player.getWorld().getName()) ) continue; //перебираем до арены, на которой находимся
                    if ( !a.getGameWorldName().equals(player.getWorld().getName()) ) arenaInGame.add(a.getDisplayName()); //добавляем остальные кроме текущей, что далее по списку
                }
            }
            
            if (arenaInGame.isEmpty()) {        //если далее по списку не найдено работающих арен, делаем еще один проход сначала
                for (Arena a:de.marcely.bedwars.api.GameAPI.get().getArenas()) {
                    if ( a.getStatus()==ArenaStatus.RUNNING) { //перебираем работающие арены
                        if ( a.getGameWorldName().equals(player.getWorld().getName()) ) break; //если дошли до нашей, значит, больше не искать
                        arenaInGame.add(a.getDisplayName()); //добавляем те, что найдены ДО нашей
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
    
    */
    
    
    
    
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
                    if (!e.getPlayer().getWorld().getName().equals("lobby")) {   
                        PM.getOplayer(p).tag("", "");
                    }
                //if (p.isOnline() && p.getWorld().getName().equals("lobby")) {
                //    LobbyListener.lobbyJoin(p);
                //}
            }
        }.runTaskLater(BwAdd.instance, 21);
        
        
        
        
    }
    

    
    
    
    @EventHandler (ignoreCancelled = false, priority = EventPriority.LOWEST)
    public void onTeleport (final PlayerTeleportEvent e) {
        //if (!e.getFrom().getWorld().getName().equals(e.getTo().getWorld().getName())) return; //перемещения между мирами не интересуют
//System.out.println(">> Teleport "+" canceled?"+e.isCancelled()+" cause="+e.getCause()+" from="+LocationUtil.StringFromLoc(e.getFrom())+" to="+LocationUtil.StringFromLoc(e.getTo())+" gamemode ="+e.getPlayer().getGameMode()  );
        if ( e.getTo().getWorld().getName().equals("lobby")) { //перемещения внутри лобби
            if (e.getCause()==PlayerTeleportEvent.TeleportCause.PLUGIN) {
                if (e.getFrom().getWorld().getName().equals("lobby")) {
//System.out.println("PlayerTeleportEvent - подмена точки на исходую!!!!");
                    e.setTo( e.getFrom().clone() );
                } //else {
//System.out.println("PlayerTeleportEvent - разброс!!!!");
                    e.setTo( e.getTo().clone().add( ApiOstrov.randInt(-1, 1), 0, ApiOstrov.randInt(-1, 1)) );
                //}
                return;
            }
        }

        
        if ( !e.getTo().getWorld().getName().equals("lobby") && e.getPlayer().getGameMode()==GameMode.SPECTATOR) {
//System.out.println("PlayerTeleportEvent - Зритель на 3 блока выше!");
            e.setTo(e.getTo().clone().add(0, 3, 0));
        }
        
    }
    
    
    
   // @EventHandler (priority = EventPriority.MONITOR)//добавить проверку - если кровати уже нет, то BW_loose
   // public void onStatEvent (final PlayerStatChangeEvent e) {
//System.out.println("PlayerKillPlayerEvent ");
//Bukkit.broadcastMessage("stat "+Bukkit.getPlayer(e.getStats().getPlayerUUID()).getName()+" ="+e.getKey());
        //ApiOstrov.addStat(e.getDamaged(), Stat.BW_kill);
   // }   
   
    
        
//эвенты игроков        
    @EventHandler (priority = EventPriority.MONITOR)
    public void onBedBreakEvent (final ArenaBedBreakEvent e) {
//System.out.println("BedBreakEvent distance="+LocationUtil.getDistance( e.getPlayer().getLocation(), e.getBedLocation()));
        ApiOstrov.addStat(e.getPlayer(), Stat.BW_bed);
    }        
    
        
    @EventHandler (priority = EventPriority.MONITOR)//добавить проверку - если кровати уже нет, то BW_loose
    public void onPlayerKillPlayerEvent (final PlayerKillPlayerEvent e) {
//System.out.println("PlayerKillPlayerEvent ");
        final Arena a = e.getArena();
        final Team t = a.getPlayerTeam(e.getPlayer());
        if (a.isBedDestroyed(t)) {
            ApiOstrov.addCustomStat(e.getKiller(), "Убийство безкроватного");
        } else {
            ApiOstrov.addStat(e.getKiller(), Stat.BW_kill);
        }
//Bukkit.broadcastMessage("PlayerKillPlayerEvent "+e.getPlayer().getName()+" damaged="+e.getDamaged().getName());
        //ApiOstrov.addStat(e.getDamaged(), Stat.BW_kill);
    }   
    
    //VOID 1, после  PlayerTeleportEvent cause=PLUGIN from=lobby<>24<>71<>9 to=lobby<>24<>71<>9 canceled?false gamemode =SURVIVAL - подмена точки
    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerRoundDeathEvent (final PlayerIngameDeathEvent e) {
        ApiOstrov.addStat(e.getPlayer(), Stat.BW_death);
        
//System.out.println("PlayerRoundDeathEvent RespawnLocation="+LocationUtil.StringFromLoc(e.getPlayer().getRespawnLocation()));
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
//System.out.println( "PlayerDeathEvent loc=" + LocationUtil.StringFromLoc(e.getEntity().getLocation()) );
            new BukkitRunnable() {
            final Player p = e.getEntity();
                @Override
                public void run() {
                    if (p!=null && p.isOnline()) p.spigot().respawn();
                    if (p.getLocation().getBlockY()<10) {
                            p.setGameMode(GameMode.SPECTATOR);
                            p.teleport(LocationUtil.getNearestPlayer(p).clone().add(0, 5, 0));
//System.out.println( "PERFORM_RESPAWN loc=" + LocationUtil.StringFromLoc(e.getEntity().getLocation()) +" getNearestPlayer="+LocationUtil.StringFromLoc(LocationUtil.getNearestPlayer(p)));
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
//System.out.println(" ---- onPlayerQuitArenaEvent --- "+e.getArena().getName()+" "+e.getPlayer().getName()+" arena contains?"+e.getArena().getPlayers().contains(e.getPlayer())+" KickReason="+e.getReason()  );
       //final Player p = e.getPlayer();
        new BukkitRunnable() {
        final Player p = e.getPlayer();
            @Override
            public void run() {
                if (p.isOnline() && p.getWorld().getName().equals("lobby")) {
//System.out.println(" ---- onPlayerQuitArenaEvent --- lobbyJoin !!");
                    LobbyListener.lobbyJoin(p);
                }
            }
        }.runTaskLater(BwAdd.instance, 30);
        //if ( e.getReason()==KickReason.Leave ) LobbyListener.lobbyJoin(e.getPlayer());
    }        
    
    
    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerJoinArenaSpectatorEvent (final SpectatorJoinArenaEvent e) { //переход игрока арены в статус зрителя после гибели
        final Player p = e.getPlayer();
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10, 1));
      /*  final Arena arena = de.marcely.bedwars.api.GameAPI.get().getArenaByPlayer(e.getPlayer());
//System.out.println(" ---- onPlayerJoinArenaSpectatorEvent --- "+e.getArena().getName()+" "+e.getPlayer().getName()+" arena="+(arena==null?"null": arena.getDisplayName()+" contains?"+e.getArena().getPlayers().contains(e.getPlayer()) ) );
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
        }*/
        //if (arena!=null && arena.getPlayers().contains(e.getPlayer()))
            //e.getPlayer().getInventory().setItem(7, LobbyListener.nextArenaSpectate.clone());
    }
    
    
    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerQuitArenaSpectatorEvent (final SpectatorQuitArenaEvent e) {
//System.out.println(" ---- onPlayerQuitArenaSpectatorEvent --- "+e.getArena().getName()+" "+e.getPlayer().getName()+" arena contains?"+e.getArena().getPlayers().contains(e.getPlayer()));
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
//System.out.println("TeamEliminateEvent getFinalPlayer="+e.getLastPlayer().getName()+" isCausingEnd?"+e.causesEnd());
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
//System.out.println("RoundEndEvent ");
//System.out.println("");
        for (Player p : e.getArena().getPlayers()) {
            if (e.getWinners().contains(p)) {
                ApiOstrov.addStat(p, Stat.BW_game);
                ApiOstrov.addStat(p, Stat.BW_win);
                //Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "reward "+p.getName()+" loni add rnd:500:1000 bedWars" );
                //Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "reward "+p.getName()+" exp add rnd:10:50 bedWars" );
            } else {
                ApiOstrov.addStat(p, Stat.BW_game);
                ApiOstrov.addStat(p, Stat.BW_loose);
            }
        }
       /* for (Player p : e.getWinners()) {
            ApiOstrov.addStat(p, Stat.BW_game);
            ApiOstrov.addStat(p, Stat.BW_win);
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "reward "+p.getName()+" loni add rnd:500:1000 bedWars" );
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "reward "+p.getName()+" exp add rnd:10:50 bedWars" );
        }
        for (Player p : e.getLosers()) {
            ApiOstrov.addStat(p, Stat.BW_game);
            ApiOstrov.addStat(p, Stat.BW_loose);
        }*/
    }        
    
    
    
    
    @EventHandler
    public void onPlayerEarnAchievementEvent (final PlayerEarnAchievementEvent e) {
//System.out.println("PlayerEarnAchievementEvent ");
        Ostrov.sync( ()->Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "reward "+e.getPlayer().getName()+" exp add rnd:5:10 bedWars" ), 0);
        //Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "reward "+e.getPlayer().getName()+" exp add rnd:5:10 bedWars" );
    }
    
    @EventHandler
    public void onPlayerUseExtraItemEvent (final PlayerUseSpecialItemEvent e) {
//System.out.println("PlayerUseExtraItemEvent ");
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "reward "+e.getPlayer().getName()+" exp add rnd:5:10 bedWars" );
    }        
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static void switchLocalGlobal(final Player p, final boolean notify) {
        final Oplayer op = PM.getOplayer(p);
        if (p.getWorld().getName().equalsIgnoreCase("lobby")) { //оказались в лобби, делаем глобальный
            if ( op.isLocalChat()){
                if (notify) p.sendMessage("§8Чат переключен на Общий");
                op.setLocalChat(false);//Ostrov.deluxechatPlugin.setGlobal(p.getUniqueId().toString());
            }
        } else {
            if ( !op.isLocalChat() )  {
                if (notify) p.sendMessage("§8Чат переключен на Арену");
                op.setLocalChat(true);//Ostrov.deluxechatPlugin.setLocal(p.getUniqueId().toString());
            }
        }
    }
    
    
    
    
    
@EventHandler
    public void FriendTeleport(FriendTeleportEvent e) {
        Arena target_player_arena =de.marcely.bedwars.api.GameAPI.get().getArenaByPlayer(e.target);
        if (target_player_arena != null && target_player_arena.getStatus()!=ArenaStatus.LOBBY) e.Set_canceled(true, "§eв игре!");
    }
    

    
//@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
 //   public void onDamage(EntityDamageEvent e) {
 //   }    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
