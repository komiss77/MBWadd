package de.marcely.bedwarsaddon.kits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.marcely.bedwars.api.gui.GUIItem;
import de.marcely.bedwars.api.Util;
import de.marcely.bedwars.api.gui.GUI;
import de.marcely.bedwars.api.VersionAPI;
import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.Language;
import de.marcely.bedwars.api.CustomLobbyItem;
import de.marcely.bedwars.api.BedwarsAddon;
import ru.komiss77.BwAdd;

public class BedwarsAddonKits {
    
    public static List<Kit> kits;
    public static String kitsGUITitle;
    public static CustomLobbyItem lobbyItem;
    public static boolean permissionsEnabled;
    public static String permissionsMissingItemName;
    public static String message_noPermissions;
    public static String message_setKit;
    public static String message_loreItems;
    public static String message_loreItemsEach;
    public static HashMap<Player, Kit> selectedKits;

    
    public static void load () {
        
        BedwarsAddonKits.kits = new ArrayList<>();
        BedwarsAddonKits.kitsGUITitle = ChatColor.GOLD + "Наборы";
        BedwarsAddonKits.lobbyItem = null;
        BedwarsAddonKits.permissionsEnabled = false;
        BedwarsAddonKits.permissionsMissingItemName = "{name} " + ChatColor.RED + "нет права";
        BedwarsAddonKits.message_noPermissions = ChatColor.RED + "нет права!";
        BedwarsAddonKits.message_setKit = ChatColor.GREEN + "Вы выбрали набор " + ChatColor.DARK_GREEN + "{kit}";
        BedwarsAddonKits.message_loreItems = new StringBuilder().append(ChatColor.GRAY).append(ChatColor.UNDERLINE).append("Items:").toString();
        BedwarsAddonKits.message_loreItemsEach = ChatColor.DARK_PURPLE + " {material}" + ChatColor.LIGHT_PURPLE + " {material-amount}";
        BedwarsAddonKits.selectedKits = new HashMap<>();

        
        Bukkit.getPluginManager().registerEvents(new Events(), BwAdd.instance);
        loadConfig();
        BwAdd.bedwarsAddon.registerCommand((BedwarsAddon.BedwarsAddonCommand)new BedwarsAddon.BedwarsAddonCommand("reload") {
            @Override
            public void onWrite(final CommandSender sender, final String[] args, final String fullUsage) {
                final long startTime = System.currentTimeMillis();
                sender.sendMessage(Language.Configurations_Reload_Start.getMessage());
                loadConfig();
                sender.sendMessage(Language.Configurations_Reload_End.getMessage().replace("{time}", new StringBuilder().append((System.currentTimeMillis() - startTime) / 1000.0).toString()));
            }
        });
    }
    
    
    
    public static void loadConfig() {
        
        if (BedwarsAddonKits.lobbyItem != null) {
            BedwarsAPI.unregisterLobbyItem(BedwarsAddonKits.lobbyItem);
        }
        Config.load();
        
        for (final Kit kit : BedwarsAddonKits.kits) {
            final List<String> lore = new ArrayList<>();
            lore.add(BedwarsAddonKits.message_loreItems);
            for (final ItemStack is : kit.getItems()) {
                lore.add(BedwarsAddonKits.message_loreItemsEach.replace("{material}", AUtil.getMaterialUserFriendlyName(is.getType())).replace("{material-amount}", new StringBuilder().append(is.getAmount()).toString()));
            }
            ItemStack is = kit.getIcon();
            final ItemMeta im = is.getItemMeta();
            if (lore!=null) im.setLore(lore);
            is.setItemMeta(im);
            kit.setIcon(VersionAPI.removeAttributes(is));
        }
        
        BedwarsAPI.registerLobbyItem(new CustomLobbyItem("kits") {
            @Override
            public void onUse(final Player player) {
                final GUI gui = new GUI(BedwarsAddonKits.kitsGUITitle, 0);
                for (final Kit kit : BedwarsAddonKits.kits) {
                    if (BedwarsAddonKits.permissionsEnabled && !Util.hasPermission((CommandSender)player, BedwarsAddonKits.getPermission(kit.getName()))) {
                        final ItemStack is = Util.renameItemstack(kit.getIcon().clone(), BedwarsAddonKits.permissionsMissingItemName.replace("{name}", ChatColor.WHITE + kit.getName()));
                        gui.addItem((GUIItem)new GUIItem(is) {
                            @Override
                            public void onClick(final Player whoClicked, final boolean leftClick, final boolean shiftClick) {
                                whoClicked.sendMessage(BedwarsAddonKits.message_noPermissions);
                            }
                        });
                    }
                    else {
                        ItemStack is = Util.renameItemstack(kit.getIcon().clone(), ChatColor.WHITE + kit.getName());
                        if (BedwarsAddonKits.selectedKits.containsKey(player) && BedwarsAddonKits.selectedKits.get(player).equals(kit)) {
                            is = VersionAPI.addGlow(is);
                        }
                        gui.addItem((GUIItem)new GUIItem(is) {
                            @Override
                            public void onClick(final Player whoClicked, final boolean leftClick, final boolean shiftClick) {
                                BedwarsAddonKits.selectedKits.put(whoClicked, kit);
                                whoClicked.sendMessage(BedwarsAddonKits.message_setKit.replace("{kit}", kit.getName()));
                                whoClicked.closeInventory();
                            }
                        });
                    }
                }
                gui.open(player);
            }
        });
    }
    
    public static String getPermission(final String kit) {
        return "mbedwars.addon.kits." + kit;
    }
}
