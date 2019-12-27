package ru.komiss77;


import de.marcely.bedwars.MBedwars;
import de.marcely.bedwars.api.Arena;
import de.marcely.bedwars.api.ArenaStatus;
import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.BedwarsAddon;
import de.marcely.bedwars.api.Team;
import de.marcely.bedwars.config.ConfigValue;
import de.marcely.bedwarsaddon.deathmatch.BedwarsAddonDeathmatch;
import de.marcely.bedwarsaddon.kits.BedwarsAddonKits;
import de.marcely.bedwarsaddon.multiplebeds.BedwarsAddonMultipleBeds;
import de.marcely.bedwarsaddon.selectshopdesign.BedwarsAddonSelectShopDesign;
import de.marcely.configmanager2.ConfigManager;
import de.marcely.lvlshop.LVLShop;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import ru.komiss77.Enums.UniversalArenaState;
import ru.komiss77.Managers.PM;






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
        
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new GameListener(), this);
        Bukkit.getPluginManager().registerEvents(new LobbyListener(), this);
        
        
        
        
        //new BukkitRunnable() {
        //    @Override
        //    public void run() {
                for (Arena a : BedwarsAPI.getArenas()) {
                    ApiOstrov.sendArenaData(
                            a.getName(),                        //arena name
                            "§bBedWars §1",                        //line0
                            "§5Арена: §1"+a.getName(),                        //line1
                            "§2Заходите!",                        //line2
                            "",                        //line3
                            "§8включение сервера",                        //extra
                            0,                     //players
                            UniversalArenaState.РАБОТАЕТ,
                            true,                       //mysql ?
                            true                        //async ?
                    );
                }
        //    }
        //}.runTaskLater(this, 20);

        
        startTimer();
        
        bedwarsAddon = new BedwarsAddon(instance);

        ssdConfig = new ConfigManager(this, "ssdConfig.cfg");
        BedwarsAddonSelectShopDesign.load();
        
        deadmatchConfig = new ConfigManager(this, "deadmatchConfig.cfg");
        BedwarsAddonDeathmatch.load();
        
        //multibedConfig = new ConfigManager(this, "multibedConfig.cfg");
        //BedwarsAddonMultipleBeds.load();
        
        //lvlshopConfig = new ConfigManager(this, "lvlshopConfig.cfg");
        //lvlshopConfig.load();
        LVLShop.load();
        
        kitConfig = new ConfigManager(this, "kitConfig.cfg");
        BedwarsAddonKits.load();
        

    }    
    

    
    
    
    
    
    
    
    
    
    
    
    
    @Override
    public void onDisable() {
        
        for (Arena a : BedwarsAPI.getArenas()) {
            
            ApiOstrov.sendArenaData(
                    a.getName(),                        //arena name
                    "§4█████████",                        //line0
                    "§bBedWars §1"+a.GetTeamColors().GetEnabledTeams().size()+"x"+a.getPerTeamPlayers(),                        //line1
                    "§5Арена: §1"+a.getName(),                        //line2
                    "§4█████████",                        //line3
                    "§8выключение сервера",                        //extra
                    0,                     //players
                    UniversalArenaState.ВЫКЛЮЧЕНА,
                    true,                       //mysql ?
                    false                        //async ?
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
                
                for (Arena a : BedwarsAPI.getArenas()) {
                    if (a.getPlayers().isEmpty()) continue;
//System.out.println(" --tick "+a.getName()+" state="+a.GetStatus()+" getRunningTime="+a.getRunningTime()   +" getTeamPlayers="+a.getTeamPlayers()+" getPerTeamPlayers="+a.getPerTeamPlayers());
                    
                    if (a.GetStatus()==ArenaStatus.Lobby) {
                        if (a.getPlayers().get(0).getLevel()>0 && a.getPlayers().get(0).getLevel()<90) {
                            ApiOstrov.sendArenaData(
                                a.getName(),                        //arena name
                                "§bBedWars §1"+a.GetTeamColors().GetEnabledTeams().size()+"x"+a.getPerTeamPlayers(),                        //line0
                                "§5Арена: §1"+a.getName(),                       //line1
                                "§1"+a.getPlayers().size()+" / "+a.getMaxPlayers(),                        //line2
                                "§6§lДо Старта: §4"+a.getPlayers().get(0).getLevel(),                        //line3
                                "§8ожидание в лобби",                        //extra
                                a.getPlayers().size(),                     //players
                                UniversalArenaState.СТАРТ,
                                false,                       //mysql ?
                                true                        //async ?
                            );
                        }
                        
                    } else if (a.GetStatus()==ArenaStatus.Running) {
                        
                        String info = "";
                        for (Team t : a.GetTeamColors().GetEnabledTeams()) { //getRemainingTeams ??
                            info = info + t.getChatColor() + ( a.getPlayersInTeam(t).isEmpty() ? "X" : a.getPlayersInTeam(t).size()+" ");
                        }
                        if (info.length()>15) info = info.substring(0,15);
                        
                        
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
                        //final de.marcely.bedwars.game.arena.Arena mArena = de.marcely.bedwars.util.s.b(a.getName()); //арена по названию
                        
                        ApiOstrov.sendArenaData(
                                a.getName(),                        //arena name
                                "§f>Зритель<",                        //line0
                                "§5Арена: §1"+a.getName(),                        //line1
                                "§4Игра: §l"+getFormattedTimeLeft(getTimeLeft(a)),                        //line2
                                info,                        //line3
                                "идёт игра",                        //extra
                                a.getPlayers().size(),                     //players
                                UniversalArenaState.ИГРА,
                                false,                       //mysql ?
                                true                        //async ?
                        );
                        
                    } else if (a.GetStatus()==ArenaStatus.EndLobby) {
 
                        ApiOstrov.sendArenaData(
                            a.getName(),                        //arena name
                            "§bBedWars §1"+a.GetTeamColors().GetEnabledTeams().size()+"x"+a.getPerTeamPlayers(),                        //line0
                            "§5Арена: §1"+a.getName(),                        //line1
                            "§5Заканчивается",                        //line2
                            "§4"+(a.getPlayers().isEmpty()?"":a.getPlayers().get(0).getLevel()),                        //line3
                            "§8конец",                        //extra
                            a.getPlayers().size(),                     //players
                            UniversalArenaState.ФИНИШ,
                            true,                       //mysql ?
                            true                        //async ?
                        );

                    }
                }
                
            if (PM.nameTagManager!=null) {
                Arena arena;
                Team team;
                for (Player p : Bukkit.getWorld("lobby").getPlayers()) {
                    arena = BedwarsAPI.getArena(p);
                    if (arena != null) {
                        team = arena.GetPlayerTeam(p);
                        PM.nameTagManager.setNametag(p.getName(), "§3"+arena.getName()+" §f", (team==null ? " §8[Команда?]" : team.getChatColor()+" ["+team.getName()+ "]") );
                    } else {
                        PM.nameTagManager.setNametag(p.getName(), "", "");
                    }
                }
            }
            
            sec++;
            if (sec==60) {
                sec=0;
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
        return ConfigValue.timer - (int) (( System.currentTimeMillis() - arena.getGameStartTime() ) /1000);
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
