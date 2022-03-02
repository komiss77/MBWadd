package ru.komiss77;


import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import net.md_5.bungee.api.ChatColor;
import de.marcely.bedwars.MBedwars;
import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.BedwarsAddon;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.game.lobby.LobbyItem;
import de.marcely.bedwars.api.game.lobby.LobbyItemHandler;
import de.marcely.bedwars.api.game.spectator.Spectator;
import de.marcely.bedwars.api.game.spectator.SpectatorItem;
import de.marcely.bedwars.api.game.spectator.SpectatorItemHandler;
import de.marcely.bedwars.config.ConfigValue;
import de.marcely.bedwars.libraries.configmanager2.ConfigManager;
import java.util.ArrayList;
import java.util.List;
import ru.komiss77.enums.GameState;
import ru.komiss77.modules.player.PM;
import ru.komiss77.utils.inventory.SmartInventory;






public class BwAdd extends JavaPlugin {
    
    public static BwAdd instance;    
    public static MBedwars marcelyBWplugin;
    public static BedwarsAddon bedwarsAddon;
    public static ConfigManager bwaddConfig;
    
    public static ConfigManager ssdConfig;    
    public static ConfigManager deadmatchConfig;    
    public static ConfigManager multibedConfig;    
    public static ConfigManager kitConfig;    
    //public static ConfigManager lvlshopConfig;    
    public static CustomScoreboard scoreboard;
    
    
    @Override
    public void onEnable() {
        
        instance = this;
        marcelyBWplugin = (MBedwars) Bukkit.getPluginManager().getPlugin("MBedwars");
        
        //bwaddConfig = new ConfigManager(this, "bwaddConfig.cfg");
        //bwaddConfig.load();
        
        if (Ostrov.deluxechatPlugin!=null) {

            Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
            log_ok("Подключен DeluxeChat");
        } else {
            log_ok("DeluxeChat не найден!");
        }
        
        
        scoreboard = new CustomScoreboard(this, false, 
                    "§eBW",
                    new String[] {
                        "§6§m-----------",
                        "",
                        "",
                        "§6§m-----------",
                    }
            );
        
        //scoreboard.update("§7Прячутся:", "§5"+hidersTotal, true);
        
        
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new GameListener(), this);
        Bukkit.getPluginManager().registerEvents(new LobbyListener(), this);
        
        
        
        
        //new BukkitRunnable() {
        //    @Override
        //    public void run() {
                for (Arena a:BedwarsAPI.getGameAPI().getArenas()) {
                    ApiOstrov.sendArenaData(
                            org.bukkit.ChatColor.stripColor(a.getDisplayName()),                        //arena name
                            GameState.РАБОТАЕТ,
                            "§bBedWars §1",                        //line0
                            "§5"+a.getDisplayName(),                        //line1
                            "§2Заходите!",                        //line2
                            "",                        //line3
                            "§8включение сервера",                        //extra
                            0                     //players
                    );
                }
        //    }
        //}.runTaskLater(this, 20);

        
        startTimer();
        
        
        final LobbyItemHandler lih = new LobbyItemHandler("selectshopdesign", this) {
            @Override
            public void handleUse(Player p, Arena a, LobbyItem li) {
                //p.sendMessage("handleUse ");
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
                //p.sendMessage("isVisible ");
                return a.getStatus()==ArenaStatus.LOBBY;
            }
        };
        BedwarsAPI.getGameAPI().registerLobbyItemHandler(lih);
        
        final SpectatorItemHandler sih = new SpectatorItemHandler("nextArena", this) {

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
            public boolean isVisible(Spectator spctr, SpectatorItem si) {
                return true;
            }
        };
        BedwarsAPI.getGameAPI().registerSpectatorItemHandler(sih);
        
       // bedwarsAddon = new BedwarsAddon(instance);

      //  ssdConfig = new ConfigManager(this, "ssdConfig.cfg");
      //  BedwarsAddonSelectShopDesign.load();
        
      //  deadmatchConfig = new ConfigManager(this, "deadmatchConfig.cfg");
        //BedwarsAddonDeathmatch.load();
        
        //multibedConfig = new ConfigManager(this, "multibedConfig.cfg");
        //BedwarsAddonMultipleBeds.load();
        
        //lvlshopConfig = new ConfigManager(this, "lvlshopConfig.cfg");
        //lvlshopConfig.load();
        //LVLShop.load();
        
     //   kitConfig = new ConfigManager(this, "kitConfig.cfg");
     //   BedwarsAddonKits.load();  //ava.lang.NullPointerException: null
        

    }    
    

    
    
    
    public static String stringToChatColor(String msg) {
        return msg.replaceAll("&", "§");
    }
    
    public static String chatColorToString(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    
    
    
    
    
    
    
    
    
    @Override
    public void onDisable() {
        
        for (Arena a:BedwarsAPI.getGameAPI().getArenas()) {
            
            ApiOstrov.sendArenaData(
                    a.getDisplayName(),                        //arena name
                    GameState.ВЫКЛЮЧЕНА,
                    "§4█████████",                        //line0
                    "§bBedWars §1"+a.getEnabledTeams().size()+"x"+a.getPlayersPerTeam(),                        //line1
                    "§5"+a.getDisplayName(),                        //line2
                    "§4█████████",                        //line3
                    "§8выключение сервера",                        //extra
                    0                     //players
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
//System.out.println(" --tick "+a.getDisplayName()+" state="+a.GetStatus()+" getRunningTime="+a.getRunningTime()   +" getTeamPlayers="+a.getTeamPlayers()+" getPerTeamPlayers="+a.getPerTeamPlayers());
                    
                    switch (a.getStatus()) {
                        
                        case LOBBY:
                            //if (a.getPlayers().get(0).getLevel()>0 && a.getPlayers().get(0).getLevel()<90) {
                            if (a.getLobbyTimeRemaining()>0 && a.getLobbyTimeRemaining()<90) {
                                ApiOstrov.sendArenaData(
                                    a.getDisplayName(),                        //arena name
                                    GameState.СТАРТ,
                                    "§bBedWars §1"+a.getEnabledTeams().size()+"x"+a.getPlayersPerTeam(),                        //line0
                                    "§5"+a.getDisplayName(),                       //line1
                                    "§1"+a.getPlayers().size()+" / "+a.getMaxPlayers(),                        //line2
                                    //"§6§lДо Старта: §4"+a.getPlayers().get(0).getLevel(),                        //line3
                                    "§6§lДо Старта: §4"+a.getLobbyTimeRemaining(),                        //line3
                                    "§8ожидание в лобби",                        //extra
                                    a.getPlayers().size()                     //players
                                );
                            }   
                            break;
                            
                        case RUNNING:
                            String info = "";
                            for (Team t : a.getEnabledTeams()) { //getRemainingTeams ??
                                info = info + t.getChatColor() + ( a.getPlayersInTeam(t).isEmpty() ? "X" : a.getPlayersInTeam(t).size()+" ");
                            }   if (info.length()>15) info = info.substring(0,15);
                            /*
                            game.getRealTeams().stream().forEach((t) -> {
                            if (t.getPlayers().isEmpty()) Main.InfoLine= Main.InfoLine+t.getChatColor()+"X ";
                            else Main.InfoLine= Main.InfoLine+t.getChatColor()+t.getPlayers().size()+" ";
                            });
                            
                            {countdown}
                            final int n = this.arena.N / 60;
                            String s = String.valueOf(this.arena.N - n * 60);
                            if (s.length() == 1) {
                            s = "0" + s;
                            }
                            */
                            //de.marcely.bedwars.game.arena.Arena mArena = de.marcely.bedwars.util.s.a(var5); //арена по игроку
                            //final de.marcely.bedwars.game.arena.Arena mArena = de.marcely.bedwars.util.s.b(a.getDisplayName()); //арена по названию
                            
                            ApiOstrov.sendArenaData(
                                    a.getDisplayName(),                        //arena name
                                    GameState.ИГРА,
                                    "§f>Зритель<",                        //line0
                                    "§5"+a.getDisplayName(),                        //line1
                                    "§4Игра: §l"+getFormattedTimeLeft(getTimeLeft(a)),                        //line2
                                    info,                        //line3
                                    "идёт игра",                        //extra
                                    a.getPlayers().size()                     //players
                            );  
                            break;
                            
                        case END_LOBBY:
                            ApiOstrov.sendArenaData(
                                    a.getDisplayName(),                        //arena name
                                    GameState.ФИНИШ,
                                    "§bBedWars §1"+a.getEnabledTeams().size()+"x"+a.getPlayersPerTeam(),                        //line0
                                    "§5"+a.getDisplayName(),                        //line1
                                    "§5Заканчивается",                        //line2
                                    "§4"+(a.getPlayers().isEmpty()?"":a.getLobbyTimeRemaining()),                        //line3
                                    "§8конец",                        //extra
                                    a.getPlayers().size()                     //players
                            );  
                            break;
                            
                        default:
                            break;
                            
                    }
                }
                
            if (PM.nameTagManager!=null) {
                Arena arena;
                Team team;
                for (Player p : Bukkit.getWorld("lobby").getPlayers()) {
                    arena = de.marcely.bedwars.api.GameAPI.get().getArenaByPlayer(p);
                    if (arena != null) {
                        team = arena.getPlayerTeam(p);
                        PM.nameTagManager.setNametag(p, "§3"+arena.getDisplayName()+" §f", (team==null ? " §8[Команда?]" : team.getChatColor()+" ["+team.getDisplayName()+ "]") );
                    } else {
                        PM.nameTagManager.setNametag(p, "", "");
                    }
                }
            }
            
            sec++;
            if (sec%60==0) {
                //sec=0;
                for (World w:Bukkit.getWorlds()) {
                    w.setTime(1000);
                    w.setFullTime(1000);
                    for (Player p : w.getPlayers()) {
                        p.setPlayerTime(1000, true);
    //System.out.println(" ---- p.setPlayerTime(1000, true);");
                    }
                }
            }
            
            }
        }.runTaskTimer(instance, 20, 20);
        
    }
    
    
    
    
    
    
    public static int getTimeLeft(final Arena arena) {
        return ConfigValue.timer - (int) (( System.currentTimeMillis() - arena.getRoundStartTime()) /1000);
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
    }
    
    
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
