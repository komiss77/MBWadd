package ru.komiss77;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.Bukkit;
import org.bukkit.Tag;
import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.arena.AddPlayerIssue;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.event.arena.ArenaBedBreakEvent;
import de.marcely.bedwars.api.event.player.PlayerBuyUpgradeEvent;
import de.marcely.bedwars.api.event.player.PlayerEarnAchievementEvent;
import de.marcely.bedwars.api.event.player.PlayerIngameDeathEvent;
import de.marcely.bedwars.api.event.player.PlayerJoinArenaEvent;
import de.marcely.bedwars.api.event.player.PlayerKillPlayerEvent;
import de.marcely.bedwars.api.event.player.PlayerOpenShopEvent;
import de.marcely.bedwars.api.event.player.PlayerQuitArenaEvent;
import de.marcely.bedwars.api.event.player.PlayerRejoinArenaEvent;
import de.marcely.bedwars.api.event.player.PlayerTeamChangeEvent;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.event.player.SpectatorJoinArenaEvent;
import de.marcely.bedwars.api.event.player.SpectatorQuitArenaEvent;
import de.marcely.bedwars.api.game.shop.layout.ShopLayoutType;
import de.marcely.bedwars.api.game.spectator.KickSpectatorReason;
import de.marcely.bedwars.api.game.spectator.SpectateReason;
import ru.komiss77.utils.LocationUtil;
import ru.komiss77.enums.Data;
import ru.komiss77.enums.Game;
import ru.komiss77.enums.Stat;
import ru.komiss77.events.BsignLocalArenaClick;
import ru.komiss77.events.BungeeDataRecieved;
import ru.komiss77.events.FriendTeleportEvent;
import ru.komiss77.modules.games.ArenaInfo;
import ru.komiss77.modules.games.GM;
import ru.komiss77.modules.player.Oplayer;
import ru.komiss77.modules.player.PM;
import ru.komiss77.utils.TCUtils;


//https://javadocs.mbedwars.com/de/marcely/bedwars/api/event/player/package-summary.html


class PlayerLst implements Listener {

    protected static final Map <String,ShopLayoutType> shopDesign = new HashMap();
    
     
    
    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onQuit (final PlayerQuitEvent e) {
        shopDesign.remove(e.getPlayer().getName());
    }
        
    
    //@EventHandler (priority = EventPriority.MONITOR)
    //public void onJoin (final PlayerJoinEvent e) {
    //}
    

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBungeeStatRecieved(final BungeeDataRecieved e) {
//Ostrov.log("------BungeeDataRecieved");
        e.getOplayer().score.remove();
        LobbyLst.lobbyJoin(e.getPlayer());

        final String wantToArena = e.getOplayer().getDataString(Data.WANT_ARENA_JOIN);
        if (wantToArena.isEmpty() || wantToArena.equals("any")) return;
        for (final Arena arena : BedwarsAPI.getGameAPI().getArenas()) {
            if (arena.getDisplayName().equalsIgnoreCase(wantToArena)) {
                Ostrov.sync( () -> e.getPlayer().performCommand("bw join "+arena.getName()), 5);//не уменьшать!! на 1 - срабатывает lobbyJoin
            }
        }
    }
    
    
    @EventHandler (priority = EventPriority.MONITOR)  //dсюда можно повесить BW_loose если вышел во врея игры
    public void onPlayerJoinArenaEvent (final PlayerJoinArenaEvent e) {
        final Player p = e.getPlayer();
        final Oplayer op = PM.getOplayer(p);
        op.tabSuffix("§6[§e"+e.getArena().getDisplayName()+"§6]", p);
        final ArenaInfo ai = GM.getGameInfo(Game.BW).getArena(Ostrov.MOT_D, e.getArena().getDisplayName());
        //if (ai==null) { //багает при отсутствии связи с островом, лучше без него
        //    BwAdd.log_err("нет ArenaInfo для арены "+e.getArena().getName());
        //    e.addIssue(AddPlayerIssue.PLUGIN);
         //   e.getPlayer().sendMessage("§cАрены ещё не готовы к работе!");
         //   return;
        //}
        if (op.getStat(Stat.LEVEL)<ai.level) {
            p.sendMessage("§cАрена будет доступна с уровня §e"+ai.level);
            e.addIssue(AddPlayerIssue.NOT_BETA_USER);
            return;
        }
        if (op.getStat(Stat.REPUTATION)<ai.reputation) {
            p.sendMessage("§cАрена будет доступна при репутации выше §e"+ai.reputation);
            e.addIssue(AddPlayerIssue.NOT_BETA_USER);
            return;
        }
        if (e.getArena().getStatus()==ArenaStatus.LOBBY) {
//Ostrov.log("PlayerJoinArenaEvent size="+e.getArena().getPlayers().size()+" cause="+e.getCause());
            switch (e.getCause()) {
                case ARENAS_GUI, AUTO_JOIN, COMMAND, PLUGIN, SIGN, VOTING_SWITCH_ARENA -> {
                    BwAdd.sendLobbyState(e.getArena(), e.getArena().getPlayers().size()+1);
                    //Ostrov.sync( ()-> {
                        //if (e.getArena().getPlayers().contains(p)) {
                        //p.teleport(Bukkit.getWorld("lobby").getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                        //p.teleport(e.getArena().getLobbyLocation());
//Ostrov.log("reTP to "+e.getArena().getLobbyLocation());
                        //}
                    //},1);
                }
                default -> {}
            }
            
        }
        p.resetPlayerTime();
        p.resetPlayerWeather(); 
    }        
    
    
    @EventHandler (priority = EventPriority.MONITOR)  //dсюда можно повесить BW_loose если вышел во врея игры
    public void onPlayerQuitArenaEvent (final PlayerQuitArenaEvent e) {
        final Player p = e.getPlayer();
        LobbyLst.lobbyJoin(p);
        if (e.getArena().getStatus()==ArenaStatus.LOBBY) {
//Ostrov.log("PlayerQuitArenaEvent size="+e.getArena().getPlayers().size());
            BwAdd.sendLobbyState(e.getArena(), e.getArena().getPlayers().size());
        }
        //final Oplayer op = PM.getOplayer(p);
        //op.setLocalChat(false);
        //p.sendMessage("§8Чат переключен на Общий");
    }        
    
    @EventHandler (priority = EventPriority.MONITOR)  //dсюда можно повесить BW_loose если вышел во врея игры
    public void onReJoin (final PlayerRejoinArenaEvent e) {
        if (e.hasIssues()) return;
        final Player p = e.getPlayer();
        final Oplayer op = PM.getOplayer(p);
        op.tabSuffix("§6[§e"+e.getArena().getDisplayName()+"§6]", p);

    }        


    
    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerJoinArenaSpectatorEvent (final SpectatorJoinArenaEvent e) { //переход игрока арены в статус зрителя после гибели
        if (e.getReason() == SpectateReason.ENTER || e.getReason() == SpectateReason.LOSE) {
            final Player p = e.getPlayer();
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10, 1));
            final Oplayer op = PM.getOplayer(p);
            op.tabSuffix("§8[Зритель]", p);
        }
    }
    
    
    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerQuitArenaSpectatorEvent (final SpectatorQuitArenaEvent e) {
        if (e.getReason()==KickSpectatorReason.KICK 
                || e.getReason()==KickSpectatorReason.LEAVE 
                || e.getReason()==KickSpectatorReason.END_LOBBY 
                || e.getReason()==KickSpectatorReason.ARENA_STOP) {
            final Oplayer op = PM.getOplayer(e.getPlayer());
            op.tabSuffix("§7[§3Лобби§7]", e.getPlayer());
            //зритель после финиша нет предметов и идёт игровое табло
            Ostrov.sync(() -> LobbyLst.lobbyJoin(e.getPlayer()), 1);
        }
    }
    

    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeamChange (final PlayerTeamChangeEvent e) {
        final Oplayer op = PM.getOplayer(e.getPlayer());
        Team team = e.getNewTeam();
        if (team==null) {
            op.beforeName(null, e.getPlayer());
        } else {
            op.beforeName(TCUtils.toChat(team.getDyeColor()), e.getPlayer());
        }
    }

   
    @EventHandler (priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onOpenShop (final PlayerOpenShopEvent e) {
        final String name = e.getPlayer().getName();
        if (shopDesign.containsKey(name) && shopDesign.get(name)!=e.getLayout().getType()) {
            e.setLayout(shopDesign.get(name).getLayout());
        }
    }

    
    @EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBsignLocalArenaClick (final BsignLocalArenaClick e) {
        for (final Arena arena : BedwarsAPI.getGameAPI().getArenas()) {
            if (arena.getDisplayName().equalsIgnoreCase(e.arenaName)) {
                e.player.performCommand("bw join "+arena.getName());
                return;
            }
        }
        e.player.sendMessage("§cНе найдена арена "+e.arenaName+" !");
    }
    
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onBedClick(PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        if (player.getWorld().getName().equals("lobby") || e.getAction()==Action.PHYSICAL ) return;
        
        if (e.getAction()==Action.LEFT_CLICK_BLOCK && Tag.BEDS.isTagged(e.getClickedBlock().getType()) ) {//e.getClickedBlock().getType().toString().contains("_BED")) {
            final Arena arena = de.marcely.bedwars.api.GameAPI.get().getArenaByPlayer(player);
            if (arena==null || arena.getStatus()!=ArenaStatus.RUNNING) return;
            if (e.hasItem()) {
                player.sendMessage("§cСломать цель можно только рукой!");
                e.setCancelled(true);
            } else if(LocationUtil.getDistance( e.getPlayer().getLocation(), e.getClickedBlock().getLocation())>3) {
                e.getPlayer().sendMessage("Вы должны быть ближе к цели, чтобы сломать её!");
                e.setCancelled(true);
            } 
        }
    }

    
    @EventHandler (ignoreCancelled = false, priority = EventPriority.LOWEST)
    public void onTeleport (final PlayerTeleportEvent e) {
//Ostrov.log("TP from="+e.getFrom()+" to="+e.getTo());
        if ( e.getTo().getWorld().getName().equals("lobby")) { //перемещения внутри лобби
            if (e.getCause()==PlayerTeleportEvent.TeleportCause.PLUGIN) {
                e.setTo( e.getTo().clone().add( ApiOstrov.randInt(-1, 1), 0, ApiOstrov.randInt(-1, 1)) );
              //  return;
            }
        }
       // if ( !e.getTo().getWorld().getName().equals("lobby") && e.getPlayer().getGameMode()==GameMode.SPECTATOR) {
         //   e.setTo(e.getTo().clone().add(0, 3, 0));
     //   }
        
    }
    
        
    @EventHandler (priority = EventPriority.MONITOR)
    public void onBedBreakEvent (final ArenaBedBreakEvent e) {
        if (e.getResult()!= ArenaBedBreakEvent.Result.CANCEL) {
            ApiOstrov.addStat(e.getPlayer(), Stat.BW_bed);
        }
    }        
    
        
    @EventHandler (priority = EventPriority.MONITOR)//добавить проверку - если кровати уже нет, то BW_loose
    public void onPlayerKillPlayerEvent (final PlayerKillPlayerEvent e) {
        final Arena a = e.getArena();
        final Team t = a.getPlayerTeam(e.getPlayer());
        if (a.isBedDestroyed(t)) {
            ApiOstrov.addCustomStat(e.getKiller(), "bw_team");
        } else {
            ApiOstrov.addStat(e.getKiller(), Stat.BW_kill);
        }
    }   
    
    
    //VOID 1, после  PlayerTeleportEvent cause=PLUGIN from=lobby<>24<>71<>9 to=lobby<>24<>71<>9 canceled?false gamemode =SURVIVAL - подмена точки
    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerRoundDeathEvent (final PlayerIngameDeathEvent e) {
        ApiOstrov.addStat(e.getPlayer(), Stat.BW_death);
    }        
    
    
    @EventHandler
    public void onPlayerEarnAchievementEvent (final PlayerEarnAchievementEvent e) {
        Ostrov.sync( ()->Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "reward "+e.getPlayer().getName()+" exp add rnd:5:10 bedWars" ), 0);
    }
    
    
    @EventHandler
    public void onPlayerUseExtraItemEvent (final PlayerUseSpecialItemEvent e) {
//Ostrov.log("PlayerUseSpecialItemEvent isTakingItem?"+e.isTakingItem());
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "reward "+e.getPlayer().getName()+" exp add rnd:5:10 bedWars" );
        ApiOstrov.addCustomStat(e.getPlayer(), "bw_extra");
    }        
 
    @EventHandler
    public void onPlayerBuyUpgradeEvent (final PlayerBuyUpgradeEvent e) {
//Ostrov.log("PlayerBuyUpgradeEvent getProblems?"+e.getProblems());
        if (e.getProblems().isEmpty()) {
            ApiOstrov.addCustomStat(e.getPlayer(), "bw_upgrade");
        }
    }        
    
    @EventHandler
    public void FriendTeleport(FriendTeleportEvent e) {
        Arena target_player_arena = de.marcely.bedwars.api.GameAPI.get().getArenaByPlayer(e.target);
        if (target_player_arena != null && target_player_arena.getStatus()!=ArenaStatus.LOBBY) e.setCanceled(true, "§eв игре!");
    }
    
    
    
    
    
   
}


