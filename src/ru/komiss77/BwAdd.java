package ru.komiss77;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import de.marcely.bedwars.MBedwars;
import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.game.lobby.LobbyItem;
import de.marcely.bedwars.api.game.lobby.LobbyItemHandler;
import de.marcely.bedwars.api.game.spectator.Spectator;
import de.marcely.bedwars.api.game.spectator.SpectatorItem;
import de.marcely.bedwars.api.game.spectator.SpectatorItemHandler;
import ru.komiss77.enums.Game;
import ru.komiss77.enums.GameState;
import ru.komiss77.modules.games.GM;
import ru.komiss77.utils.TCUtils;
import ru.komiss77.utils.inventory.SmartInventory;


public class BwAdd extends JavaPlugin {
    
    public static BwAdd instance;    
    public static MBedwars marcelyBWplugin;

    static {
    }
    
    
    @Override
    public void onEnable() {
        
        instance = this;
        marcelyBWplugin = (MBedwars) Bukkit.getPluginManager().getPlugin("MBedwars");

        Bukkit.getPluginManager().registerEvents(new ChatLst(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLst(), this);
        Bukkit.getPluginManager().registerEvents(new ArenaLst(), this);
        Bukkit.getPluginManager().registerEvents(new LobbyLst(), this);

        BedwarsAPI.onReady(() -> {
            regItems();
            startTimer();
        });

    }    
    
    
    private void regItems() {
       
        final LobbyItemHandler ssd = new LobbyItemHandler("selectshopdesign", this) {
            @Override
            public void handleUse(Player p, Arena a, LobbyItem li) {
                SmartInventory.builder()
                    .id("ssd")
                    .provider(new SelectShopDesign())
                    .size(3, 9)
                    .title("§aДизайн Магазина")
                    .build()
                    .open(p);
            }
            
            @Override
            public boolean isVisible(Player p, Arena a, LobbyItem li) {
                return true;//a.getStatus()==ArenaStatus.LOBBY && p.getClientOption(ClientOption.LOCALE).equals("ru_ru");
            }
        };
        BedwarsAPI.getGameAPI().registerLobbyItemHandler(ssd);

        
        final SpectatorItemHandler sihRu = new SpectatorItemHandler("nextArena", this) {
            @Override
            public void handleUse(Spectator sp, SpectatorItem si) {
                List<String> arenaInGame=new ArrayList();
            
                for (Arena a:BedwarsAPI.getGameAPI().getArenas()) {
                    if ( a.getStatus()==ArenaStatus.RUNNING) { //перебираем работающие арены
                        if ( !a.getGameWorldName().equals(sp.getPlayer().getWorld().getName()) ) continue; //перебираем до арены, на которой находимся
                        if ( !a.getGameWorldName().equals(sp.getPlayer().getWorld().getName()) ) arenaInGame.add(a.getDisplayName()); //добавляем остальные кроме текущей, что далее по списку
                    }
                }

                if (arenaInGame.isEmpty()) {        //если далее по списку не найдено работающих арен, делаем еще один проход сначала
                    for (Arena a:de.marcely.bedwars.api.GameAPI.get().getArenas()) {
                        if ( a.getStatus()==ArenaStatus.RUNNING) { //перебираем работающие арены
                            if ( a.getGameWorldName().equals(sp.getPlayer().getWorld().getName()) ) break; //если дошли до нашей, значит, больше не искать
                            arenaInGame.add(a.getDisplayName()); //добавляем те, что найдены ДО нашей
                        }
                    }
                }

                if (arenaInGame.isEmpty()) {
                    sp.getPlayer().sendMessage("§5Больше нет арен для наблюдения!");
                } else {
                    sp.getPlayer().performCommand("bw join "+arenaInGame.get(0));
                    sp.getPlayer().sendMessage("§5Переходим к арене "+arenaInGame.get(0));
                }
            }

            @Override
            public boolean isVisible(Spectator sp, SpectatorItem si) {
                return  true; //sp.getPlayer().getClientOption(ClientOption.LOCALE).equals("ru_ru");
            }
        };
        BedwarsAPI.getGameAPI().registerSpectatorItemHandler(sihRu);
    }

    
    @Override
    public void onDisable() {
        
        for (Arena a:BedwarsAPI.getGameAPI().getArenas()) {
            
            GM.sendArenaData(
                    Game.BW, 
                    a.getDisplayName(),
                    GameState.ВЫКЛЮЧЕНА, 
                    0, 
                    "§4█████████", 
                    "§bBedWars §1"+a.getEnabledTeams().size()+"x"+a.getPlayersPerTeam(),
                    "§5"+a.getDisplayName(), 
                    "§4█████████"
            );
        }
        
    }
    
    
    
    public static void log_ok(String s) {   Bukkit.getConsoleSender().sendMessage("§7BwAdd §2"+ s); }
    public static void log_warn(String s) {   Bukkit.getConsoleSender().sendMessage("§7BwAdd §6"+ s); }
    public static void log_err(String s) {   Bukkit.getConsoleSender().sendMessage("§7BwAdd §c"+ s); }    
    

    
    
    
    
    
    
    private static void startTimer() {
                
        new BukkitRunnable() {
            int sec=0;
            
            @Override
            public void run() {
                
                for (Arena a:de.marcely.bedwars.api.GameAPI.get().getArenas()) {
                    if (a.getPlayers().isEmpty()) continue;
                    
                    switch (a.getStatus()) {
                        
                        
                        case LOBBY -> {
                            if (a.getLobbyTimeRemaining()>0 && a.getLobbyTimeRemaining()<90) {
                                GM.sendArenaData(
                                        Game.BW, 
                                        a.getDisplayName(), 
                                        GameState.СТАРТ, 
                                        a.getPlayers().size(),
                                        "§bBedWars §1"+a.getEnabledTeams().size()+"x"+a.getPlayersPerTeam(),
                                        "§5"+a.getDisplayName(),
                                        "§1"+a.getPlayers().size()+" / "+a.getMaxPlayers(),
                                        "§6До Старта: §4"+((int)a.getLobbyTimeRemaining())
                                );
                            }
                        }
                          
                        
                        case RUNNING -> {
                            String info = "";
                            for (Team t : a.getEnabledTeams()) { //getRemainingTeams ??
                                info = info + TCUtils.toChat(t.getDyeColor()) + ( a.getPlayersInTeam(t).isEmpty() ? "X" : a.getPlayersInTeam(t).size()+" ");
                            }   
                            if (info.length()>15) info = info.substring(0,15);

                                GM.sendArenaData(
                                    Game.BW, 
                                    a.getDisplayName(), 
                                    GameState.ИГРА, 
                                    a.getPlayers().size(),
                                    "§f>Зритель<",
                                    "§5"+a.getDisplayName(),
                                    "§4Игра: §l"+getFormattedTimeLeft(a.getIngameTimeRemaining()),
                                    info
                                );
                        }
                            
                        
                        case END_LOBBY -> {
                            
                            GM.sendArenaData(
                                Game.BW, 
                                a.getDisplayName(), 
                                GameState.ФИНИШ, 
                                a.getPlayers().size(),
                                "§bBedWars §1"+a.getEnabledTeams().size()+"x"+a.getPlayersPerTeam(),
                                "§5"+a.getDisplayName(),
                                "§5Заканчивается",
                                "§4"+(a.getPlayers().isEmpty()?"":((int)a.getLobbyTimeRemaining()))
                            );
                        }
                            
                        default -> {}
                            
                    }
                }

                sec++;

                if (sec==40) {
                    //перезагрузить таблички и инфо арен
                    GM.onWorldsLoadDone();
                    for (Arena a:de.marcely.bedwars.api.GameAPI.get().getArenas()) {
                        sendLobbyState(a);
                    }
                }
            
            }
        }.runTaskTimer(instance, 20, 20);
        
    }
    
    
    public static void sendLobbyState(final Arena a) {
            GM.sendArenaData(
                Game.BW, 
                a.getDisplayName(), 
                GameState.ОЖИДАНИЕ, 
                a.getPlayers().size(),
                "§bBedWars §1"+ (a.getEnabledTeams().isEmpty() ? "" : a.getEnabledTeams().size()+"x"+a.getPlayersPerTeam()),
                "§5"+a.getDisplayName(),
                "§2Заходите!",
                "§1"+a.getPlayers().size()+" / "+a.getMaxPlayers()
            );        
    }    
    
        
    
    
    
  //  public static int getTimeLeft(final Arena arena) {
   //     return arena.getIngameTimeRemaining();//ConfigValue.timer - (int) (( System.currentTimeMillis() - arena.RoundStartTime()) /1000);
        //this.N = ConfigValue.timer;
        //int playTime = ConfigValue.timer;
        //int usedTime = (int) (( System.currentTimeMillis() - arena.getGameStartTime() ) /1000);
        //int timeLeft = playTime - usedTime;
//System.out.println(" playTime="+playTime+" getGameStartTime="+arena.getGameStartTime()+" usedTime="+usedTime+" timeLeft="+timeLeft);
        //final int n = timeLeft / 60;
        //String s = String.valueOf(timeLeft - n * 60);
        //if (s.length() == 1) {
        //    s = "0" + s;
        //}
        //return s;
   // }
    
    
    public static String getFormattedTimeLeft(int time) {
        int j = time % 3600 / 60;    //мин
       
        if (time<20) return "§4"+twoDigitString(j) + ":" + twoDigitString(time % 60);
        else if (time<120) return "§6"+twoDigitString(j) + ":" + twoDigitString(time % 60);
        else return "§2"+twoDigitString(j) + ":" + twoDigitString(time % 60);
        
    }
    public static String twoDigitString(int i) {
        return i == 0 ? "00" : (i / 10 == 0 ? "0" + i : String.valueOf(i));
    }


   
    
    
    
    
    
}
