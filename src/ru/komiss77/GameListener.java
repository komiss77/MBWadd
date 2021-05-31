package ru.komiss77;

import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import ru.komiss77.Enums.UniversalArenaState;
import me.clip.deluxechat.DeluxeChat;
import de.marcely.bedwars.api.Arena;
import de.marcely.bedwars.api.ArenaStatus;
import de.marcely.bedwars.api.Team;
import de.marcely.bedwars.api.event.ArenaStatusUpdateEvent;
import de.marcely.bedwars.api.event.PlayerJoinArenaEvent;
import de.marcely.bedwars.api.event.PlayerQuitArenaEvent;







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
                    arena.getName(),                        //arena name
                    "§bBedWars §1"+ (arena.GetTeamColors().GetEnabledTeams().isEmpty() ? "" : arena.GetTeamColors().GetEnabledTeams().size()+"x"+arena.getPerTeamPlayers() ),                        //line0
                    "§5Арена: §1"+arena.getName(),                       //line1
                    "§2Заходите!",                        //line2
                    "§1"+arena.getPlayers().size()+" / "+arena.getMaxPlayers(),                        //line3
                    "§8ожидание в лобби",                        //extra
                    arena.getPlayers().size(),                     //players
                    UniversalArenaState.ОЖИДАНИЕ,
                    true,                       //mysql ?
                    true                        //async ?
                );
            }
        }.runTaskLater(BwAdd.instance, 1);
    }
    
    
    
    @EventHandler (priority = EventPriority.MONITOR)
    public void onArenaStatusUpdateEvent (final ArenaStatusUpdateEvent e) {
//System.out.println(" ---- onArenaStatusUpdateEvent --- "+e.getArena().getName()+" "+e.getStatusBefore()+" -> "+e.getStatus());
                switch (e.getStatus()) {

                    case Lobby:
                        sendLobbyState(e.getArena());
                        return;

                    case EndLobby:
                        ApiOstrov.sendArenaData(
                            e.getArena().getName(),                        //arena name
                            "§bBedWars §1"+e.getArena().GetTeamColors().GetEnabledTeams().size()+"x"+e.getArena().getPerTeamPlayers(),                         //line0
                            "§5Арена: §1"+e.getArena().getName(),                        //line1
                            "§5Заканчивается",                        //line2
                            "",                        //line3
                            "§8конец",                        //extra
                            e.getArena().getPlayers().size(),                     //players
                            UniversalArenaState.ФИНИШ,
                            true,                       //mysql ?
                            true                        //async ?
                        );
                        return;


                    case Resetting:
                        ApiOstrov.sendArenaData(
                            e.getArena().getName(),                        //arena name
                            "§bBedWars",                        //line0
                            "§5Арена: §1"+e.getArena().getName(),                        //line1
                            "§eРегенерация",                        //line2
                            "",                        //line3
                            "§8реген",                        //extra
                            e.getArena().getPlayers().size(),                     //players
                            UniversalArenaState.РЕГЕНЕРАЦИЯ,
                            true,                       //mysql ?
                            true                        //async ?
                        );
                        return;


                    case Stopped:
                        ApiOstrov.sendArenaData(
                            e.getArena().getName(),                        //arena name
                            "§bBedWars §1"+e.getArena().GetTeamColors().GetEnabledTeams().size()+"x"+e.getArena().getPerTeamPlayers(),                        //line0
                            "§5Арена: §1"+e.getArena().getName(),                        //line1
                            "§4Выключена",                        //line2
                            "",                        //line3
                            "§8off",                        //extra
                            e.getArena().getPlayers().size(),                     //players
                            UniversalArenaState.ВЫКЛЮЧЕНА,
                            true,                       //mysql ?
                            true                        //async ?
                        );
                        return;
                        
                    case Running:
                        for (Team team : e.getArena().getRemainingTeams()) {
                            for (Player p : e.getArena().getPlayersInTeam(team)) {
                                p.setPlayerListName("§8["+team.getChatColor()+team.getName()+"§8] §f"+p.getName());
                            }
                        }
                        e.getArena().getWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                        e.getArena().getWorld().setTime(1000);
                        return;

                }

    }
    
    
    
    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerJoinArenaEvent (final PlayerJoinArenaEvent e) {
//System.out.println(" ---- onPlayerJoinArenaEvent --- "+e.getArena().getName()+" "+e.getPlayer().getName());
        if (e.getArena().GetStatus()==ArenaStatus.Lobby) sendLobbyState(e.getArena());
        e.getPlayer().resetPlayerTime();
        e.getPlayer().resetPlayerWeather();
    }
    
    
    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerQuitArenaEvent (final PlayerQuitArenaEvent e) {
//System.out.println(" ---- onPlayerQuitArenaEvent --- "+e.getArena().getName()+" "+e.getPlayer().getName());
        if (e.getArena().GetStatus()==ArenaStatus.Lobby)  sendLobbyState(e.getArena());
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
    
    
    
    
    
    
    
}
