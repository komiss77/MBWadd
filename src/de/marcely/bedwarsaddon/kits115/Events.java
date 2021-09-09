package de.marcely.bedwarsaddon.kits115;



import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.event.arena.ArenaStatusChangeEvent;
import de.marcely.bedwars.api.event.player.PlayerJoinArenaEvent;
import de.marcely.bedwars.api.event.player.PlayerQuitArenaEvent;
import java.util.Random;

import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


import ru.komiss77.BwAdd;



public class Events implements Listener {
 /*   public static Random rand;
    
    static {
        Events.rand = new Random();
    }
    
    @EventHandler
    public void onPlayerJoinArenaEvent(final PlayerJoinArenaEvent event) {
        if (BedwarsAddonKits.kits.size() >= 1) {
            BedwarsAddonKits.selectedKits.put(event.getPlayer(), BedwarsAddonKits.kits.get(Events.rand.nextInt(BedwarsAddonKits.kits.size())));
        }
    }
    
    @EventHandler
    public void onPlayerQuitArenaEvent(final PlayerQuitArenaEvent event) {
        BedwarsAddonKits.selectedKits.remove(event.getPlayer());
    }
    
    @EventHandler
    public void onArenaStatusUpdateEvent(final ArenaStatusChangeEvent event) {
        if (BedwarsAddonKits.kits.size() >= 1 && event.getOldStatus()== ArenaStatus.LOBBY && event.getNewStatus()== ArenaStatus.RUNNING) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (final Player player : event.getArena().getPlayers()) {
                        for (final ItemStack is : BedwarsAddonKits.selectedKits.get(player).getItems()) {
                            player.getInventory().addItem(new ItemStack[] { is });
                        }
                    }
                }
            }.runTaskLater(BwAdd.instance, 20L);
        }
    }*/
}
