package de.marcely.bedwarsaddon.multiplebeds;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.command.CommandSender;

import de.marcely.bedwars.config.ConfigValue;
import de.marcely.bedwars.api.Team;
import de.marcely.bedwars.api.Arena;
import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.Language;
import de.marcely.bedwars.api.Util;
import de.marcely.bedwars.api.BedwarsAddon;
import ru.komiss77.BwAdd;


public class BedwarsAddonMultipleBeds {
    public static List<AArena> arenas;

    
    public static void load() {
        BedwarsAddonMultipleBeds.arenas = new ArrayList<>();
        
        new BukkitRunnable() {
            @Override
            public void run() {
                Config.load();
            }
        }.runTaskLater(BwAdd.instance, 2L);
        
        Bukkit.getPluginManager().registerEvents(new Events(), BwAdd.instance);
        
        BwAdd.bedwarsAddon.registerCommand((BedwarsAddon.BedwarsAddonCommand)new BedwarsAddon.BedwarsAddonCommand("setbedsamount", "<arena name> <amount>") {
            @Override
            public void onWrite(final CommandSender sender, final String[] args, final String fullUsage) {
                if (args.length >= 2) {
                    final AArena arena = AUtil.getAArena(args[0]);
                    if (arena != null) {
                        if (Util.isInteger(args[1])) {
                            final int amount = Integer.valueOf(args[1]);
                            if (amount >= 1) {
                                arena.setMaxBedsAmount(amount);
                                Config.save();
                                sender.sendMessage(ChatColor.GREEN + "The maximum amount of beds in the arena " + ChatColor.DARK_GREEN + arena.getName() + ChatColor.GREEN + " has been changed to " + ChatColor.DARK_GREEN + args[1]);
                            }
                            else {
                                sender.sendMessage(ChatColor.RED + "The number " + ChatColor.DARK_RED + args[1] + ChatColor.RED + " must be higher than 0!");
                            }
                        }
                        else {
                            sender.sendMessage(Language.Number_NotOne.getMessage().replace("{number}", args[1]));
                        }
                    }
                    else {
                        sender.sendMessage(Language.NotFound_Arena.getMessage().replace("{arena}", args[0]));
                    }
                }
                else {
                    sender.sendMessage(Language.Usage.getMessage().replace("{usage}", fullUsage));
                }
            }
        });
        
        BwAdd.bedwarsAddon.registerCommand((BedwarsAddon.BedwarsAddonCommand)new BedwarsAddon.BedwarsAddonCommand("getbedsamount", "") {
            @Override
            public void onWrite(final CommandSender sender, final String[] args, final String fullUsage) {
                sender.sendMessage("");
                if (BedwarsAPI.getArenas().size() >= 1) {
                    final List<Integer> amounts = new ArrayList<>();
                    for (final Arena bwArena : BedwarsAPI.getArenas()) {
                        final int a = AUtil.getAArena(bwArena.getName()).getMaxBedsAmount();
                        if (!amounts.contains(a)) {
                            amounts.add(a);
                        }
                    }
                    Collections.sort(amounts);
                    for (final int n : amounts) {
                        for (final Arena bwArena2 : BedwarsAPI.getArenas()) {
                            final AArena arena = AUtil.getAArena(bwArena2.getName());
                            if (arena.getMaxBedsAmount() == n) {
                                sender.sendMessage(ChatColor.GRAY + arena.getName() + " " + ((arena.getMaxBedsAmount() > 1) ? ChatColor.GREEN : ChatColor.RED) + arena.getMaxBedsAmount());
                            }
                        }
                    }
                }
                else {
                    sender.sendMessage(ChatColor.RED + "There is no arena!");
                }
                sender.sendMessage("");
            }
        });
        
        BwAdd.bedwarsAddon.registerCommand((BedwarsAddon.BedwarsAddonCommand)new BedwarsAddon.BedwarsAddonCommand("getteambed", "<Color> <ID>") {
            @Override
            public void onWrite(final CommandSender sender, final String[] args, final String fullUsage) {
                if (sender instanceof Player) {
                    final Player player = (Player)sender;
                    if (args.length >= 2) {
                        final Team team = Team.getTeamByName(args[0]);
                        if (team != null) {
                            if (Util.isInteger(args[1])) {
                                final int amount = Integer.valueOf(args[1]);
                                if (amount >= 1) {
                                    //ItemStack is = new ItemStack(ConfigValue.bed_block);
                                    //if (ConfigValue.bed_block == Material.BED_BLOCK) {
                                        ItemStack is = new ItemStack(Material.RED_BED, 1);
                                    //}
                                    is = Util.renameItemstack(is, ChatColor.YELLOW + "Bed " + ChatColor.DARK_GRAY + "> " + ChatColor.GRAY + args[1] + ChatColor.DARK_GRAY + " <" + ChatColor.AQUA + " >> " + team.getChatColor() + team.name());
                                    player.getInventory().addItem(new ItemStack[] { is });
                                    sender.sendMessage(ChatColor.GREEN + "You got a bed with the id " + ChatColor.DARK_GREEN + args[1]);
                                }
                                else {
                                    sender.sendMessage(ChatColor.RED + "The number " + ChatColor.DARK_RED + args[1] + ChatColor.RED + " must be higher than 0!");
                                }
                            }
                            else {
                                sender.sendMessage(Language.Number_NotOne.getMessage().replace("{number}", args[1]));
                            }
                        }
                        else {
                            sender.sendMessage(Language.NotAvaible_Color.getMessage().replace("{color}", args[0]));
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
    }
}
