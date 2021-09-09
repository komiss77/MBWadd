package de.marcely.bedwarsaddon.kits115;

import com.meowj.langutils.lang.LanguageHelper;
import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.game.lobby.LobbyItem;
import de.marcely.bedwars.api.game.lobby.LobbyItemHandler;
import de.marcely.bedwars.tools.gui.GUI;
import de.marcely.bedwars.tools.gui.GUIItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.komiss77.BwAdd;




public class BedwarsAddonKits {
    /*
    public static List<Kit> kits;
    public static String kitsGUITitle;
    public static CustomLobbyItem lobbyItem;
    public static boolean permissionsEnabled;
    public static String permissionsMissingItemName;
    public static String message_noPermissions;
    public static String message_setKit;
    public static String message_loreItems;
    //public static String message_loreItemsEach;
    public static HashMap<Player, Kit> selectedKits;

    
    public static void load () {
        
        BedwarsAddonKits.kits = new ArrayList<>();
        
        BedwarsAddonKits.kitsGUITitle = ChatColor.GOLD + "Наборы";
        BedwarsAddonKits.lobbyItem = null;
        BedwarsAddonKits.permissionsEnabled = false;
        BedwarsAddonKits.permissionsMissingItemName = "{name} " + ChatColor.RED + "нет права";
        BedwarsAddonKits.message_noPermissions = ChatColor.RED + "нет права!";
        BedwarsAddonKits.message_setKit = ChatColor.GREEN + "Вы выбрали набор " + ChatColor.DARK_GREEN + "{kit}";
        BedwarsAddonKits.message_loreItems = new StringBuilder().append(ChatColor.GRAY).append(ChatColor.UNDERLINE).append("Предметы:").toString();
        //BedwarsAddonKits.message_loreItemsEach = ChatColor.DARK_PURPLE + " {material}" + ChatColor.LIGHT_PURPLE + " {material-amount}";
        BedwarsAddonKits.selectedKits = new HashMap<>();

        
        Bukkit.getPluginManager().registerEvents(new Events(), BwAdd.instance);
        
        loadConfig();

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
                //LanguageHelper.getItemName(armor[i], "ru_ru");
                lore.add("§6"+LanguageHelper.getItemName(is, "ru_ru")+" x "+is.getAmount());
                //lore.add(BedwarsAddonKits.message_loreItemsEach.replace("{material}", AUtil.getMaterialUserFriendlyName(is.getType())).replace("{material-amount}", new StringBuilder().append(is.getAmount()).toString()));
            }
            ItemStack is = kit.getIcon();
            final ItemMeta im = is.getItemMeta();
            if (lore!=null) im.setLore(lore);
            is.setItemMeta(im);
            kit.setIcon(VersionAPI.removeAttributes(is));
        }
        
        de.marcely.bedwars.api.GameAPI.get().registerLobbyItemHandler(new LobbyItemHandler(kitsGUITitle, plugin) {
            @Override
            public void handleUse(Player player, Arena arena, LobbyItem li) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            
            @Override
            public boolean isVisible(Player player, Arena arena, LobbyItem li) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        
        de.marcely.bedwars.api.GameAPI.get().registerLobbyItemHandler(new CustomLobbyItem("kits") {
            @Override
            public void onUse(final Player player) {
                final GUI gui = new GUI(BedwarsAddonKits.kitsGUITitle, 0);
                for (final Kit kit : BedwarsAddonKits.kits) {
                    //if (BedwarsAddonKits.permissionsEnabled && !Util.hasPermission((CommandSender)player, BedwarsAddonKits.getPermission(kit.getName()))) {
                    if (BedwarsAddonKits.permissionsEnabled && !player.hasPermission("bedwars.kit."+kit.getName())) {
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
    */
    public static String getPermission(final String kit) {
        return "mbedwars.addon.kits." + kit;
    }
}
