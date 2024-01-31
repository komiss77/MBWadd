package ru.komiss77.deadMatch;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Location;


public class BedwarsAddonDeathmatch {
    
/*    
    public static HashMap<Arena, Location> deathmatchLocations;
    public static int deathmatchTime;

    
    public static void load() {
        
        BedwarsAddonDeathmatch.deathmatchLocations = new HashMap<>();
        BedwarsAddonDeathmatch.deathmatchTime = 220;
        
        Bukkit.getPluginManager().registerEvents(new Events(), BwAdd.instance);
        
        new BukkitRunnable() {
            @Override
            public void run() {
                Config.load();
            }
        }.runTaskLater(BwAdd.instance, 2L);
        
        BwAdd.bedwarsAddon.registerCommand((BedwarsAddon.BedwarsAddonCommand)new BedwarsAddon.BedwarsAddonCommand("reload") {
            @Override
            public void onWrite(final CommandSender sender, final String[] args, final String fullUsage) {
                final long startTime = System.currentTimeMillis();
                BedwarsAddonDeathmatch.loadConfig();
                sender.sendMessage(Language.Configurations_Reload_End.getMessage().replace("{time}", new StringBuilder().append((System.currentTimeMillis() - startTime) / 1000.0).toString()));
            }
        });
        
        BwAdd.bedwarsAddon.registerCommand((BedwarsAddon.BedwarsAddonCommand)new BedwarsAddon.BedwarsAddonCommand("setlocation", "<arena name>") {
            @Override
            public void onWrite(final CommandSender sender, final String[] args, final String fullUsage) {
                if (sender instanceof Player) {
                    if (args.length >= 1) {
                        final Arena arena = BedwarsAPI.getArena(args[0]);
                        if (arena != null) {
                            BedwarsAddonDeathmatch.deathmatchLocations.remove(arena);
                            BedwarsAddonDeathmatch.deathmatchLocations.put(arena, ((Player)sender).getLocation());
                            Config.save();
                            sender.sendMessage(ChatColor.GREEN + "The deathmatch location of the arena " + ChatColor.DARK_GREEN + arena.getName() + ChatColor.GREEN + " has been successfully changed!");
                        }
                        else {
                            sender.sendMessage(Language.NotFound_Arena.getMessage().replace("{arena}", args[0]));
                        }
                    }
                    else {
                        sender.sendMessage(Language.Usage.getMessage().replace("{usage}", fullUsage));
                    }
                }
                else {
                    sender.sendMessage(Language.OnlyAs_Player.getMessage());
                }
            }
        });
        
        BwAdd.bedwarsAddon.registerCommand((BedwarsAddon.BedwarsAddonCommand)new BedwarsAddon.BedwarsAddonCommand("getarenaswithlocations") {
            @Override
            public void onWrite(final CommandSender sender, final String[] args, final String fullUsage) {
                final Set<Arena> arenasWith = BedwarsAddonDeathmatch.deathmatchLocations.keySet();
                final List<Arena> arenasWithout = new ArrayList<>(BedwarsAPI.getArenas());
                arenasWithout.removeAll(arenasWith);
                for (final Arena arena : arenasWithout) {
                    sender.sendMessage(ChatColor.RED + arena.getName());
                }
                for (final Arena arena : arenasWith) {
                    sender.sendMessage(ChatColor.GREEN + arena.getName());
                }
            }
        });
    }
    
    public static void loadConfig() {
        BedwarsAddonDeathmatch.deathmatchLocations.clear();
        Config.load();
    }*/
}
