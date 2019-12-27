package de.marcely.bedwarsaddon.selectshopdesign;

import de.marcely.bedwars.Language;
import de.marcely.bedwars.Sound;
import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.BedwarsAddon;
import de.marcely.bedwars.api.CustomLobbyItem;
import de.marcely.bedwars.api.Util;
import de.marcely.bedwars.api.event.PlayerOpenShopEvent;
import de.marcely.bedwars.api.event.PlayerQuitArenaEvent;
import de.marcely.bedwars.api.gui.DecGUIItem;
import de.marcely.bedwars.api.gui.GUI;
import de.marcely.bedwars.api.gui.GUIItem;
import de.marcely.bedwars.game.shop.ShopDesignData;
import de.marcely.bedwars.game.shop.ShopDesignType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import ru.komiss77.BwAdd;



public class BedwarsAddonSelectShopDesign {
    
    
    public static HashMap<ShopDesignData, ItemStack> DESIGN_ICON;
    public static List<ShopDesignData> DESIGN_DISABLED;
    private static HashMap<Player, ShopDesignData> PLAYERDESIGNS;
    public static ItemStack DESIGN_ICON_MISSING;
    
    
    public static void load() {
        
        BedwarsAddonSelectShopDesign.DESIGN_ICON = new HashMap<>();
        BedwarsAddonSelectShopDesign.DESIGN_DISABLED = new ArrayList<>();
        BedwarsAddonSelectShopDesign.PLAYERDESIGNS = new HashMap<>();
        
        BedwarsAddonSelectShopDesign.DESIGN_ICON.put(ShopDesignType.Normal.getData(), new ItemStack(Material.POTATO));
        BedwarsAddonSelectShopDesign.DESIGN_ICON.put(ShopDesignType.GommeHD.getData(), new ItemStack(Material.TURTLE_HELMET));
        BedwarsAddonSelectShopDesign.DESIGN_ICON.put(ShopDesignType.HiveMC.getData(), new ItemStack(Material.GOLDEN_APPLE));
        BedwarsAddonSelectShopDesign.DESIGN_ICON.put(ShopDesignType.HyPixel.getData(), new ItemStack(Material.WOODEN_SWORD));
        BedwarsAddonSelectShopDesign.DESIGN_ICON.put(ShopDesignType.Rewinside.getData(), new ItemStack(Material.LILY_PAD));
        
        BedwarsAddonSelectShopDesign.DESIGN_ICON_MISSING = new ItemStack(Material.SUGAR);
        
        BedwarsAddonSelectShopDesign.DESIGN_ICON.put(ShopDesignType.Minesucht.getData(), new ItemStack(Material.STONE_SWORD));
        BedwarsAddonSelectShopDesign.DESIGN_ICON.put(ShopDesignType.BergwerkLABS.getData(), new ItemStack(Material.IRON_SWORD));
        BedwarsAddonSelectShopDesign.DESIGN_ICON.put(ShopDesignType.HyPixelV2.getData(), new ItemStack(Material.GOLDEN_SWORD));
        
        new BukkitRunnable() {
            @Override
            public void run() {
                Config.load();
                Config.save();
            }
        }.runTaskLater(BwAdd.instance, 1L);
        
        
        
        BedwarsAPI.registerLobbyItem( new CustomLobbyItem("selectshopdesign") {
            
            @Override
            public void onUse(final Player player) {
                
                Util.playSound(player, Sound.LOBBY_VOTEARENA_OPEN);
                final GUI gui = new GUI(Message.GUI_TITLE.getMessage(), 0);
                
                for (final ShopDesignData data : BedwarsAPI.getShopDesigns()) {
                    if (!data.getType().isBeta()) {
                        if (BedwarsAddonSelectShopDesign.DESIGN_DISABLED.contains(data)) {
                            continue;
                        }
                        ItemStack is = (BedwarsAddonSelectShopDesign.DESIGN_ICON.containsKey(data) ? BedwarsAddonSelectShopDesign.DESIGN_ICON.get(data) : BedwarsAddonSelectShopDesign.DESIGN_ICON_MISSING).clone();
                        Util.renameItemstack(is, ChatColor.WHITE + data.getName());
                        if (BedwarsAddonSelectShopDesign.getPlayerDesign(player).equals(data)) {
                            is.addUnsafeEnchantment(Enchantment.LUCK, 1);
                            ItemMeta im = is.getItemMeta();
                            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            is.setItemMeta(im);
                            is = Util.setItemstackLore(is, new String[] { Message.DESIGN_CHOOSEN.getMessage() });
                        }
                        gui.addItem(new GUIItem(is) {
                            @Override
                            public void onClick(final Player player, final boolean paramBoolean1, final boolean paramBoolean2) {
                                if (!BedwarsAddonSelectShopDesign.PLAYERDESIGNS.containsKey(player)) {
                                    BedwarsAddonSelectShopDesign.PLAYERDESIGNS.put(player, data);
                                    player.closeInventory();
                                    player.sendMessage(Message.DESIGN_CHOOSE.getMessage().replace("{design}", data.getName()));
                                    Util.playSound(player, Sound.LOBBY_VOTEARENA_VOTE);
                                }
                                else {
                                    final ShopDesignData pd = BedwarsAddonSelectShopDesign.PLAYERDESIGNS.get(player);
                                    if (!pd.equals(data)) {
                                        BedwarsAddonSelectShopDesign.PLAYERDESIGNS.put(player, data);
                                        player.closeInventory();
                                        player.sendMessage(Message.DESIGN_CHOOSE.getMessage().replace("{design}", data.getName()));
                                        Util.playSound(player, Sound.LOBBY_VOTEARENA_VOTE);
                                    }
                                    else {
                                        player.sendMessage(Message.DESIGN_CHOOSE_ALREADY.getMessage().replace("{design}", data.getName()));
                                        Util.playSound(player, Sound.LOBBY_VOTEARENA_ALREADYVOTED);
                                    }
                                }
                            }
                        });
                    }
                }
                gui.centerAtYAll(GUI.CenterFormatType.Normal);
                gui.setBackground(new DecGUIItem(Util.renameItemstack(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " ")));
                gui.open(player);
            }
        });
        
        BwAdd.bedwarsAddon.registerCommand(new BedwarsAddon.BedwarsAddonCommand("reload", "") {
            @Override
            public void onWrite(final CommandSender sender, final String[] args, final String fullUsage) {
                final long startTime = System.currentTimeMillis();
                sender.sendMessage(Language.Configurations_Reload_Start.getMessage());
                Config.load();
                Config.save();
                sender.sendMessage(Language.Configurations_Reload_End.getMessage().replace("{time}", new StringBuilder().append((System.currentTimeMillis() - startTime) / 1000.0).toString()));
            }
        });
        
        
        
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerQuitArenaEvent(final PlayerQuitArenaEvent event) {
                BedwarsAddonSelectShopDesign.PLAYERDESIGNS.remove(event.getPlayer());
            }
            
            @EventHandler
            public void onPlayerOpenShopEvent(final PlayerOpenShopEvent event) {
                event.setDesign(BedwarsAddonSelectShopDesign.getPlayerDesign(event.getPlayer()));
            }
        }, BwAdd.instance);
        
        
        
        
        
        
    }
    
    
    
    
    
    public static final ShopDesignData getPlayerDesign(final Player player) {
        return BedwarsAddonSelectShopDesign.PLAYERDESIGNS.containsKey(player) ? BedwarsAddonSelectShopDesign.PLAYERDESIGNS.get(player) : BedwarsAPI.getShopDesign();
    }
    
    
}
