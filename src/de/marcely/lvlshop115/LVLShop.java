package de.marcely.lvlshop115;

import org.bukkit.entity.Entity;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.Listener;


import de.marcely.bedwars.api.BedwarsAPI;
import ru.komiss77.BwAdd;





public class LVLShop {
    
    
    public static void load() {
/*
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerExpChangeEvent(final PlayerExpChangeEvent event) {
                final Player player = event.getPlayer();
                if (player.getExp() < event.getAmount() && de.marcely.bedwars.api.GameAPI.get().getArenaByPlayer(player) != null) {
                    for (int i = 0; i < event.getAmount(); ++i) {
                        player.giveExp(player.getExpToLevel());
                    }
                    event.setAmount(0);
                }
            }
        }, BwAdd.instance);
        
        
            final Spawner spawner = new Spawner(new ItemStack(Material.EXPERIENCE_BOTTLE), "EXP") {
                @Override
                public void onDropEvent(final DropEvent event) {
                    ExperienceOrb e = getNearbyExperience(event.getLocation());
                    if (e != null) {
                        e.setExperience(e.getExperience() + 1);
                    }
                    else {
                        e = (ExperienceOrb)event.getLocation().getWorld().spawnEntity(event.getLocation(), EntityType.EXPERIENCE_ORB);
                        e.setExperience(1);
                    }
                }
                
                @Override
                public void onBuyEvent(final ShopBuyEvent event) {
                    final Player buyer = event.getBuyer();
                    final int takeAmount = event.getShopItem().getPrices().get(0).getAmount() * event.getTakeAmount();
                    if (buyer.getLevel() >= takeAmount) {
                        event.takePayment(false);
                        buyer.setLevel(buyer.getLevel() - event.getShopItem().getPrices().get(0).getAmount() * event.getTakeAmount());
                    }
                    else {
                        event.setHasEnoughMaterial(false);
                    }
                }
            };
            BedwarsAPI.registerSpawner(spawner);
*/
    }
    
    
    
    private static ExperienceOrb getNearbyExperience(final Location loc) {
        for (final Entity e : loc.getWorld().getNearbyEntities(loc, 1.0, 1.0, 1.0)) {
            if (e.getType() == EntityType.EXPERIENCE_ORB) {
                return (ExperienceOrb)e;
            }
        }
        return null;
    }
    
    
}
