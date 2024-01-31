package ru.komiss77;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import de.marcely.bedwars.api.event.arena.ArenaOutOfTimeEvent;
import de.marcely.bedwars.api.event.arena.ArenaStatusChangeEvent;
import de.marcely.bedwars.api.event.arena.RoundEndEvent;
import de.marcely.bedwars.api.event.arena.TeamEliminateEvent;
import net.kyori.adventure.text.Component;
import ru.komiss77.enums.GameState;
import ru.komiss77.enums.Stat;
import ru.komiss77.modules.player.Oplayer;
import ru.komiss77.modules.player.PM;
import ru.komiss77.utils.TCUtils;


class ArenaLst implements Listener {

    private static final Component helpRu;
    private static final Component helpEn;

    static {
        helpRu = TCUtils.format("§7Суть Игры §8: §eВы должны защищать свою кровать. На всех базах есть специальный торговец, который снабжает бойцов экипировкой, броней, едой, блоками и другими важными вещами. Победу одержат игроки, разрушившие кровать и перебившие соперников.");
        helpEn = TCUtils.format("§7Essence of the Game §8: §eYou must protect your bed. All bases have a special merchant who supplies fighters with equipment, armor, food, blocks and other important things. The victory will be won by the players who destroy the bed and kill their opponents.");
    }


    
    @EventHandler (ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onCreatureSpawnEvent(CreatureSpawnEvent e) {
        if (e.getSpawnReason()==CreatureSpawnEvent.SpawnReason.NATURAL) {
            e.getEntity().remove();
            e.setCancelled(true);
        }
        
    }    
  

    
    
    
    @EventHandler (priority = EventPriority.MONITOR)
    public void onStatusChange (final ArenaStatusChangeEvent e) {
//Ostrov.log(" ---- onArenaStatusUpdateEvent --- "+e.getArena().getName()+" "+e.getOldStatus()+" -> "+e.getNewStatus());
        switch (e.getNewStatus()) {

            case LOBBY -> {
                BwAdd.sendLobbyState(e.getArena());
                return;
            }

            case RESETTING -> {
                ApiOstrov.sendArenaData(
                        e.getArena().getDisplayName(),                        //arena name
                        GameState.РЕГЕНЕРАЦИЯ,
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
                        GameState.ВЫКЛЮЧЕНА,
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
                Oplayer op;
                for (Player p : e.getArena().getPlayers()) {
                    op = PM.getOplayer(p);
                    op.setLocalChat(true);
                    op.tag(false);
                    p.sendMessage("§8Чат переключен на Арену");
                    if (op.getStat(Stat.BW_game)<5) {
                        if (op.eng) {
                            p.sendMessage(helpEn);
                        } else {
                            p.sendMessage(helpRu);
                        }
                    }
                }
                return;
            }
            

            case END_LOBBY -> {
                Oplayer op;
                for (Player p : e.getArena().getPlayers()) {
                    op = PM.getOplayer(p);
                    op.setLocalChat(false);
                }
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


        }

    }
    
    
    
    
    //@EventHandler  //команда уничтожена
    public void onTeamEliminateEvent (final TeamEliminateEvent e) {
        
    }        

    
    
    
    //@EventHandler //время вышло, по эвенту можно продлить
    public void onArenaOutOfTimeEvent (final ArenaOutOfTimeEvent e) { 
    }        
    
    
    
    
    @EventHandler
    public void onRoundEndEvent (final RoundEndEvent e) {  //вызывается если определилась команда-победители, после эвента выполняется ConfigValue.prize_commands
        final boolean fast = e.getArena().getRunningTime().getSeconds()<60;
        Ostrov.log("getRunningTime="+e.getArena().getRunningTime().getSeconds()+" fast?"+fast);
        for (Player p : e.getArena().getPlayers()) {
            if (e.getWinners().contains(p)) {
                ApiOstrov.addStat(p, Stat.BW_game);
                ApiOstrov.addStat(p, Stat.BW_win);
                
            } else {
                ApiOstrov.addStat(p, Stat.BW_game);
                ApiOstrov.addStat(p, Stat.BW_loose);
            }
        }
    }        
    
    
    
    
    
    
    
}
    
