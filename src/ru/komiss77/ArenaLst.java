package ru.komiss77;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import de.marcely.bedwars.api.event.arena.ArenaOutOfTimeEvent;
import de.marcely.bedwars.api.event.arena.ArenaRegenerationStartEvent;
import de.marcely.bedwars.api.event.arena.ArenaRegenerationStopEvent;
import de.marcely.bedwars.api.event.arena.ArenaStatusChangeEvent;
import de.marcely.bedwars.api.event.arena.RoundEndEvent;
import de.marcely.bedwars.api.event.arena.TeamEliminateEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import ru.komiss77.enums.Game;
import ru.komiss77.enums.GameState;
import ru.komiss77.enums.Stat;
import ru.komiss77.events.WorldsLoadCompleteEvent;
import ru.komiss77.modules.games.GM;
import ru.komiss77.modules.player.Oplayer;
import ru.komiss77.modules.player.PM;
import ru.komiss77.modules.world.WorldManager;
import ru.komiss77.utils.TCUtil;


class ArenaLst implements Listener {

    private static final Component helpRu;
    private static final Component helpEn;

    static {
        helpRu = TCUtil.form("§7Суть Игры §8: §eВы должны защищать свою кровать. На всех базах есть специальный торговец, который снабжает бойцов экипировкой, броней, едой, блоками и другими важными вещами. Победу одержат игроки, разрушившие кровать и перебившие соперников.");
        helpEn = TCUtil.form("§7Essence of the Game §8: §eYou must protect your bed. All bases have a special merchant who supplies fighters with equipment, armor, food, blocks and other important things. The victory will be won by the players who destroy the bed and kill their opponents.");
    }
 


    
    @EventHandler (ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onCreatureSpawnEvent(CreatureSpawnEvent e) {
        if (e.getSpawnReason()==CreatureSpawnEvent.SpawnReason.NATURAL) {
            e.getEntity().remove();
            e.setCancelled(true);
        }
        
    }    

    //@EventHandler (priority = EventPriority.MONITOR)
    public void onRegen (final ArenaRegenerationStartEvent e) {
//Ostrov.log("ArenaRegenerationStartEvent "+e.getArena().getName());
    }
    
    //фикс бага - пустая карта после первой игры,висит в воздухе
    @EventHandler (priority = EventPriority.MONITOR)
    public void onRegen (final ArenaRegenerationStopEvent e) {
//Ostrov.log("ArenaRegenerationStopEvent "+e.getArena().getName());
        final World w = e.getArena().getGameWorld();
        Bukkit.unloadWorld(w, false);
        WorldManager.load(Bukkit.getConsoleSender(), w.getName(), w.getEnvironment(), WorldManager.Generator.Empty);
    }
    
    
    @EventHandler (priority = EventPriority.MONITOR)
    public void onStatusChange (final ArenaStatusChangeEvent e) {
        final Arena a = e.getArena();
//Ostrov.log(" ---- onArenaStatusUpdateEvent --- "+e.getArena().getName()+" "+e.getOldStatus()+" -> "+e.getNewStatus());
        switch (e.getNewStatus()) {

            case LOBBY -> {
//Ostrov.log("onStatusChange LOBBY "+e.getArena().getDisplayName());
                if (!a.getName().equals(a.getDisplayName())) { //отсылает английские названия при старте!!!
                    BwAdd.sendLobbyState(a, a.getPlayers().size());
                }
                int r = 0;
                for (Entity en : a.getGameWorld().getEntities()) {
                    if (en.getType() == EntityType.ITEM || en.getType() == EntityType.WOLF) {
                        en.remove();
                        r++;
                    }
                }
                if (r>0) {
                    Ostrov.log("Арена "+a.getCustomName()+", удалено [ITEM,WOLF] : "+r);
                }
                return;
            }

            case RESETTING -> {
                GM.sendArenaData(
                    Game.BW, 
                    a.getDisplayName(), 
                    GameState.РЕГЕНЕРАЦИЯ, 
                    a.getPlayers().size(),
                    "§bBedWars",
                    "§5"+a.getDisplayName(),
                    "§eРегенерация",
                    ""
                );  
                return;
            }


            case STOPPED -> {
                GM.sendArenaData(
                    Game.BW, 
                    a.getDisplayName(), 
                    GameState.ВЫКЛЮЧЕНА, 
                    a.getPlayers().size(),
                    "§bBedWars §1"+a.getEnabledTeams().size()+"x"+a.getPlayersPerTeam(),                        //line0
                    "§5"+a.getDisplayName(),                        //line1
                    "§4Выключена",
                    ""
                );  
                return;
            }

            case RUNNING -> {
                Oplayer op;
                for (Player p : a.getPlayers()) {
                    final Team team = a.getPlayerTeam(p);
                    op = PM.getOplayer(p);
                    //op.setLocalChat(true);
                    //op.tag(false);
                    if (team !=null) {
                        //op.tag(TCUtil.toChat(team.getDyeColor()), null);
                        op.beforeName(TCUtil.toChat(team.getDyeColor()), p);
                    }
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
                GM.sendArenaData(
                    Game.BW, 
                    a.getDisplayName(), 
                    GameState.ФИНИШ, 
                    a.getPlayers().size(),
                    "§bBedWars §1"+a.getEnabledTeams().size()+"x"+a.getPlayersPerTeam(),                         //line0
                    "§5"+a.getDisplayName(),                        //line1
                    "§5Заканчивается",
                    ""
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
//Ostrov.log("getRunningTime="+e.getArena().getRunningTime().getSeconds()+" fast?"+fast);
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
    
