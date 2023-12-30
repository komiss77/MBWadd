package ru.komiss77;


import de.marcely.bedwars.api.game.shop.layout.ShopLayout;
import de.marcely.bedwars.api.game.shop.layout.ShopLayoutType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import ru.komiss77.utils.ItemBuilder;
import ru.komiss77.utils.inventory.ClickableItem;
import ru.komiss77.utils.inventory.InventoryContent;
import ru.komiss77.utils.inventory.InventoryProvider;





public class SelectShopDesign implements InventoryProvider {
    
    
    
    private static final ClickableItem fill = ClickableItem.empty(new ItemBuilder(Material.GLASS_PANE).name("§8.").build());
    

    
    public SelectShopDesign() {
    }
    
    
    
    @Override
    public void init(final Player p, final InventoryContent content) {
        p.playSound(p.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 5, 5);
        //content.fillRect(0,0, 2,8, fill);
        content.fillRow(0, fill);
        content.fillRow(2, fill);
        
        
        
        
        
        
      //  final Pagination pagination = content.pagination();
        
        
       // final ArrayList<ClickableItem> menuEntry = new ArrayList<>();
        

        
        //final PlayerAchievements pac = BedwarsAPIa.getPlayerDataAPI().getAchievements(p.getUniqueId()).get();
        
        
        
  
         
            

        ShopLayout shop;
        
        for (final ShopLayoutType type : ShopLayoutType.values()) {
            
            
            if (type.wrapper==null) continue;
            shop = type.getLayout();
            if (shop==null) continue;
            
            if (PlayerListener.shopDesign.containsKey(p.getName()) && PlayerListener.shopDesign.get(p.getName())==type) {
                
                content.add(ClickableItem.empty(new ItemBuilder(Material.SUGAR)
                    .name("§e"+shop.getName())
                    .addLore("")
                    .addLore("§aУже выбран")
                    .addLore("")
                    .build())); 

            } else {
                
                content.add(ClickableItem.of(new ItemBuilder(getMat(type))
                    .name("§e"+shop.getName() )
                    .addLore("")
                    .addLore("§7ЛКМ - выбрать")
                    .addLore("")
                    .build(), e-> {
                        if (e.isLeftClick()) {
                            p.closeInventory();
                            PlayerListener.shopDesign.put(p.getName(),type);
                            p.sendMessage("§7В игре Ваш магазин будет выгладеть как на "+type.name()+".");
                            p.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1, 1);
                        }
                    })); 
            
            }
        }

     

        content.add(ClickableItem.of(new ItemBuilder(Material.SUGAR)
            .name("§fИспользовать обычный" )
            .addLore("")
            .addLore("§7ЛКМ - выбрать")
            .addLore("")
            .build(), e-> {
                if (e.isLeftClick()) {
                    p.closeInventory();
                    PlayerListener.shopDesign.remove(p.getName());
                    p.sendMessage("§7В игре Ваш магазин будет выгладеть как у всех.");
                    p.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1, 1);
                }
            })); 

        
            
            
        
        
        
        
        
        
         /*
        
        pagination.setItems(menuEntry.toArray(new ClickableItem[menuEntry.size()]));
        pagination.setItemsPerPage(29);
        

        

       
        
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
        */

        
        

    }
    
    
    private static Material getMat (final ShopLayoutType type) {

        switch (type) {
            case NORMAL : return Material.POTATO;
            case HYPIXEL : return Material.WOODEN_SWORD;
            case HIVEMC : return Material.GOLDEN_APPLE;
            case GOMMEHD : return Material.TURTLE_HELMET;
            case REWINSIDE : return Material.LILY_PAD;
            case MINESUCHT : return Material.STONE_SWORD;
            case BERGWERKLABS : return Material.IRON_SWORD;
            case HYPIXEL_V2 : return Material.GOLDEN_SWORD;
        }
        
        return Material.SUGAR;
    }

        
        
           
    /*
        BedwarsAddonSelectShopDesign.DESIGN_ICON.put(ShopDesignType.Normal.getData(), new ItemStack(Material.POTATO));
        BedwarsAddonSelectShopDesign.DESIGN_ICON.put(ShopDesignType.GommeHD.getData(), new ItemStack(Material.TURTLE_HELMET));
        BedwarsAddonSelectShopDesign.DESIGN_ICON.put(ShopDesignType.HiveMC.getData(), new ItemStack(Material.GOLDEN_APPLE));
        BedwarsAddonSelectShopDesign.DESIGN_ICON.put(ShopDesignType.HyPixel.getData(), new ItemStack(Material.WOODEN_SWORD));
        BedwarsAddonSelectShopDesign.DESIGN_ICON.put(ShopDesignType.Rewinside.getData(), new ItemStack(Material.LILY_PAD));
        
        BedwarsAddonSelectShopDesign.DESIGN_ICON_MISSING = new ItemStack(Material.SUGAR);
        
        BedwarsAddonSelectShopDesign.DESIGN_ICON.put(ShopDesignType.Minesucht.getData(), new ItemStack(Material.STONE_SWORD));
        BedwarsAddonSelectShopDesign.DESIGN_ICON.put(ShopDesignType.BergwerkLABS.getData(), new ItemStack(Material.IRON_SWORD));
        BedwarsAddonSelectShopDesign.DESIGN_ICON.put(ShopDesignType.HyPixelV2.getData(), new ItemStack(Material.GOLDEN_SWORD));
        */    
        

               
    

    
}