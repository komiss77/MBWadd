package ru.komiss77;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.event.arena.ArenaStatusChangeEvent;
import de.marcely.bedwars.api.event.player.PlayerJoinArenaEvent;
import de.marcely.bedwars.api.event.player.PlayerQuitArenaEvent;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import ru.komiss77.enums.GameState;
import ru.komiss77.modules.player.Oplayer;
import ru.komiss77.modules.player.PM;
import ru.komiss77.utils.TCUtils;







class GameListener implements Listener {

    public GameListener() {
       /* cC cg = new cC(null) {
            
            @Override
            public boolean a(Player player, SpectateReason sr) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            
            @Override
            public void onUse(Player player, SpectatorItem si, Arena arena) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        SpectatorItem nextArena = new SpectatorItem(7, LobbyListener.nextArenaSpectate, "msg on use", cg);
        cB.V.add(nextArena);*/
    
    }
    
    
    
    
    @EventHandler (ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onCreatureSpawnEvent(CreatureSpawnEvent e) {
        if (e.getSpawnReason()==CreatureSpawnEvent.SpawnReason.NATURAL) {
            e.getEntity().remove();
            e.setCancelled(true);
        }
        
    }    
    
    
   // @EventHandler (priority = EventPriority.MONITOR)
  //  public void onEnableArenaEvent (final EnableArenaEvent e) {
  //  }
    
    
    public static void sendLobbyState(final Arena arena) {
        
        new BukkitRunnable() {

            @Override
            public void run() {
                ApiOstrov.sendArenaData(
                    arena.getDisplayName(),                        //arena name
                    GameState.ОЖИДАНИЕ,
                    "§bBedWars §1"+ (arena.getEnabledTeams().isEmpty() ? "" : arena.getEnabledTeams().size()+"x"+arena.getPlayersPerTeam()),                        //line0
                    "§5"+arena.getDisplayName(),                       //line1
                    "§2Заходите!",                        //line2
                    "§1"+arena.getPlayers().size()+" / "+arena.getMaxPlayers(),                        //line3
                    "§8ожидание в лобби",                        //extra
                    arena.getPlayers().size()                     //players
                );
            }
        }.runTaskLater(BwAdd.instance, 1);
    }
    
    
    
    @EventHandler (priority = EventPriority.MONITOR)
    public void onArenaStatusUpdateEvent (final ArenaStatusChangeEvent e) {
//System.out.println(" ---- onArenaStatusUpdateEvent --- "+e.getArena().getName()+" "+e.getStatusBefore()+" -> "+e.getStatus());
        
        
        if (e.getOldStatus()==ArenaStatus.LOBBY) {
            Oplayer op;
            for (Player p : e.getArena().getPlayers()) {
                op = PM.getOplayer(p);
                op.tag("", "");
            }
        }

        switch (e.getNewStatus()) {

            case LOBBY -> {
                sendLobbyState(e.getArena());
                return;
            }

            case END_LOBBY -> {
                ApiOstrov.sendArenaData(
                        e.getArena().getDisplayName(),                        //arena name
                        GameState.ОЖИДАНИЕ,
                        "§bBedWars §1"+e.getArena().getEnabledTeams().size()+"x"+e.getArena().getPlayersPerTeam(),                         //line0
                        "§5"+e.getArena().getDisplayName(),                        //line1
                        "§5Заканчивается",                        //line2
                        "",                        //line3
                        "§8конец",                        //extra
                        e.getArena().getPlayers().size()                     //players
                );
                return;
            }


            case RESETTING -> {
                ApiOstrov.sendArenaData(
                        e.getArena().getDisplayName(),                        //arena name
                        GameState.ОЖИДАНИЕ,
                        "§bBedWars",                        //line0
                        "§5"+e.getArena().getDisplayName(),                        //line1
                        "§eРегенерация",                        //line2
                        "",                        //line3
                        "§8реген",                        //extra
                        e.getArena().getPlayers().size()                     //players
                );
                return;
            }


            case STOPPED -> {
                ApiOstrov.sendArenaData(
                        e.getArena().getDisplayName(),                        //arena name
                        GameState.ОЖИДАНИЕ,
                        "§bBedWars §1"+e.getArena().getEnabledTeams().size()+"x"+e.getArena().getPlayersPerTeam(),                        //line0
                        "§5"+e.getArena().getDisplayName(),                        //line1
                        "§4Выключена",                        //line2
                        "",                        //line3
                        "§8off",                        //extra
                        e.getArena().getPlayers().size()                     //players
                );
                return;
            }

            case RUNNING -> {
                for (Team team : e.getArena().getAliveTeams()) {
                    for (Player p : e.getArena().getPlayersInTeam(team)) {
                        p.playerListName( TCUtils.format("§8["+team.getBukkitColor()+team.getDisplayName()+"§8] §f"+p.getName()) );
                    }
                }
                e.getArena().getGameWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                e.getArena().getGameWorld().setTime(1000);
                return;
            }

        }

    }
    
    
    
    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerJoinArenaEvent (final PlayerJoinArenaEvent e) {
//System.out.println(" ---- onPlayerJoinArenaEvent --- "+e.getArena().getName()+" "+e.getPlayer().getName());
        final Player p = e.getPlayer();
        if (e.getArena().getStatus()==ArenaStatus.LOBBY) {
            sendLobbyState(e.getArena());
            final Oplayer op = PM.getOplayer(p);
            op.tag(e.getArena().getDisplayName()+" ", "");
        }
        e.getPlayer().resetPlayerTime();
        e.getPlayer().resetPlayerWeather();
    }
    
    
    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerQuitArenaEvent (final PlayerQuitArenaEvent e) {
//System.out.println(" ---- onPlayerQuitArenaEvent --- "+e.getArena().getName()+" "+e.getPlayer().getName());
        final Player p = e.getPlayer();
        if (e.getArena().getStatus()==ArenaStatus.LOBBY)  {
            sendLobbyState(e.getArena());
            final Oplayer op = PM.getOplayer(p);
            op.tag("", "");
        }
    }
 
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    
    
    
    
    
    
    
}
