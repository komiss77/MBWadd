package ru.komiss77;


import de.marcely.bedwars.api.player.DefaultPlayerAchievement;
import de.marcely.bedwars.api.player.PlayerAchievement;
import de.marcely.bedwars.api.player.PlayerAchievements;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.komiss77.utils.ItemBuilder;
import ru.komiss77.utils.ItemUtils;
import ru.komiss77.utils.inventory.ClickableItem;
import ru.komiss77.utils.inventory.InventoryContent;
import ru.komiss77.utils.inventory.InventoryProvider;
import ru.komiss77.utils.inventory.Pagination;
import ru.komiss77.utils.inventory.SlotIterator;
import ru.komiss77.utils.inventory.SlotPos;





public class AchivMenu implements InventoryProvider {
    
    
    
    private static final ItemStack fill = new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).name("§8.").build();;
    private final PlayerAchievements acm;
    

    
    public AchivMenu(final PlayerAchievements acm) {
        this.acm = acm;
    }
    
    
    
    @Override
    public void init(final Player p, final InventoryContent contents) {
        p.playSound(p.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);
        contents.fillRect(0,0,  4,8, ClickableItem.empty(fill));
        final Pagination pagination = contents.pagination();
        
        
        
        
        
        final ArrayList<ClickableItem> menuEntry = new ArrayList<>();
        

        
        //final PlayerAchievements pac = BedwarsAPIa.getPlayerDataAPI().getAchievements(p.getUniqueId()).get();
        
        PlayerAchievement pa;
        for (int i=1; i<=28; i++) {
            pa = get(i);
            
            //if (pac.has(pa)) {
                
            menuEntry.add(ClickableItem.empty(new ItemBuilder(acm.has(pa) ? Material.TURTLE_HELMET : Material.CLAY_BALL)
                .name(pa.getName() )
                .lore(pa.getDescription())
                .build()));            
                
           // }
            
        }




     
            
            
        
        
        

        
        
            
            
        
        
        
        
        
        
        
        
        pagination.setItems(menuEntry.toArray(new ClickableItem[menuEntry.size()]));
        pagination.setItemsPerPage(21);
        

        

        
        
        contents.set( 5, 4, ClickableItem.of( new ItemBuilder(Material.OAK_DOOR).name("закрыть").build(), e -> 
            p.closeInventory()
        ));
        

        
        if (!pagination.isLast()) {
            contents.set(5, 8, ClickableItem.of(ItemUtils.nextPage, e 
                    -> contents.getHost().open(p, pagination.next().getPage()) )
            );
        }

        if (!pagination.isFirst()) {
            contents.set(5, 0, ClickableItem.of(ItemUtils.previosPage, e 
                    -> contents.getHost().open(p, pagination.previous().getPage()) )
            );
        }
        
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, SlotPos.of(1, 1)).allowOverride(false));
        

        
        

    }
    
    
    
    
    
    public static class SelectPlayer implements InventoryProvider {



        private static final ItemStack fill = new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).name("§8.").build();;



        public SelectPlayer() {
        }



        @Override
        public void init(final Player p, final InventoryContent contents) {
            p.playSound(p.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);
            contents.fillRect(0,0,  4,8, ClickableItem.empty(fill));
            final Pagination pagination = contents.pagination();

            final ArrayList<ClickableItem> menuEntry = new ArrayList<>();



            for (Player target : Bukkit.getOnlinePlayers()) {

                menuEntry.add(ClickableItem.of(new ItemBuilder( Material.PLAYER_HEAD )
                    .name("§7"+target.getName() )
                    .build(), e -> {
                        p.performCommand("operm "+target.getName());
                    }));            

            }


            pagination.setItems(menuEntry.toArray(new ClickableItem[menuEntry.size()]));
            pagination.setItemsPerPage(21);


            contents.set( 5, 4, ClickableItem.of( new ItemBuilder(Material.OAK_DOOR).name("закрыть").build(), e -> 
                p.closeInventory()
            ));



            if (!pagination.isLast()) {
                contents.set(5, 8, ClickableItem.of(ItemUtils.nextPage, e 
                        -> contents.getHost().open(p, pagination.next().getPage()) )
                );
            }

            if (!pagination.isFirst()) {
                contents.set(5, 0, ClickableItem.of(ItemUtils.previosPage, e 
                        -> contents.getHost().open(p, pagination.previous().getPage()) )
                );
            }

            pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, SlotPos.of(1, 1)).allowOverride(false));


        }


    }

    
    
    
    
    
    private static PlayerAchievement get(int num) {
        switch (num) {
            case 1: {
                return DefaultPlayerAchievement.RAGE_QUIT;
            }
            case 2: {
                return DefaultPlayerAchievement.WIN_ROUND;
            }
            case 3: {
                return DefaultPlayerAchievement.LOSE_ROUND;
            }
            case 4: {
                return DefaultPlayerAchievement.OWN_BED_DESTROYED;
            }
            case 5: {
                return DefaultPlayerAchievement.OP_BOW;
            }
            case 6: {
                return DefaultPlayerAchievement.USE_RESCUE_PLATFORM;
            }
            case 7: {
                return DefaultPlayerAchievement.USE_ENDERPEARL;
            }
            case 8: {
                return DefaultPlayerAchievement.USE_MINISHOP;
            }
            case 9: {
                return DefaultPlayerAchievement.KILL_WITH_BOW;
            }
            case 10: {
                return DefaultPlayerAchievement.WIN_100_ROUNDS;
            }
            case 11: {
                return DefaultPlayerAchievement.KILL_WITH_HALF_HEART;
            }
            case 12: {
                return DefaultPlayerAchievement.WIN_100_ROUNDS;
            }
            case 13: {
                return DefaultPlayerAchievement.USE_BRIDGE;
            }
            case 14: {
                return DefaultPlayerAchievement.USE_GUARD_DOG;
            }
            case 15: {
                return DefaultPlayerAchievement.USE_MAGNET_SHOES;
            }
            case 16: {
                return DefaultPlayerAchievement.USE_TELEPORTER;
            }
            case 17: {
                return DefaultPlayerAchievement.USE_TNT_SHEEP;
            }
            case 18: {
                return DefaultPlayerAchievement.USE_TRACKER;
            }
            case 19: {
                return DefaultPlayerAchievement.PLACE_TRAP;
            }
            case 20: {
                return DefaultPlayerAchievement.WALK_OVER_TRAP;
            }
            case 21: {
                return DefaultPlayerAchievement.RANKING_TOP_3;
            }
            case 22: {
                return DefaultPlayerAchievement.GOOD_KD;
            }
            case 23: {
                return DefaultPlayerAchievement.HIGH_PLAY_TIME;
            }
            case 24: {
                return DefaultPlayerAchievement.WRITE_GG_IN_ENDLOBBY;
            }
            case 25: {
                return DefaultPlayerAchievement.WIN_WITHOUT_BED;
            }
            case 26: {
                return DefaultPlayerAchievement.DIE_10_SECONDS_AFTER_BED_DESTRUCTION;
            }
            case 27: {
                return DefaultPlayerAchievement.OBTAIN_EVERY_ACHIEVEMENT;
            }
            case 28: {
                return DefaultPlayerAchievement.USE_FIREBALL;
            }
        }
        return null;

    }    
    
    
    
    
}