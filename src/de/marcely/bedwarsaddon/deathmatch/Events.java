package de.marcely.bedwarsaddon.deathmatch;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.event.OutOfArenaEvent;
import de.marcely.bedwarsaddon.multiplebeds.AArena;
import de.marcely.bedwarsaddon.multiplebeds.AUtil;
import de.marcely.bedwars.api.Team;
import de.marcely.bedwars.api.event.ArenaOutOfTimeEvent;
import de.marcely.bedwars.api.Arena;



public class Events implements Listener
{
    private List<Arena> alreadyInDeathmatch;
    private List<Player> teleportingPlayers;
    
    public Events() {
        this.alreadyInDeathmatch = new ArrayList<Arena>();
        this.teleportingPlayers = new ArrayList<Player>();
    }
    
    @EventHandler
    public void onArenaOutOfTimeEvent(final ArenaOutOfTimeEvent event) {
        final Arena arena = event.getArena();
        if (!this.alreadyInDeathmatch.contains(arena) && BedwarsAddonDeathmatch.deathmatchLocations.containsKey(arena)) {
            this.alreadyInDeathmatch.add(arena);
            event.setNewTime(BedwarsAddonDeathmatch.deathmatchTime);
            for (final Team team : arena.GetTeamColors().GetEnabledTeams()) {
                arena.GetTeamColors()._0setBedDestroyed(team, true);
            }
            final Plugin multipleBeds = Bukkit.getPluginManager().getPlugin("MBedwars_MultipleBeds");
            if (multipleBeds != null) {
                final AArena aa = AUtil.getAArena(arena.getName());
                for (final Team team2 : arena.GetTeamColors().GetEnabledTeams()) {
                    aa.setDestroyedBedsAmount(team2, aa.getMaxBedsAmount());
                }
            }
            for (final Entity e : arena.getWorld().getEntities()) {
                if (e.getType() == EntityType.ENDER_PEARL && arena.isInside(e.getLocation())) {
                    e.remove();
                }
            }
            this.teleportingPlayers.addAll(arena.getPlayers());
            for (final Player player : arena.getPlayers()) {
                player.teleport((Location)BedwarsAddonDeathmatch.deathmatchLocations.get(arena));
            }
        }
    }
    
    @EventHandler
    public void onOutOfArenaEvent(final OutOfArenaEvent event) {
        if (this.teleportingPlayers.contains(event.getPlayer())) {
            event.kickPlayer(false);
            event.setCancelled(false);
            this.teleportingPlayers.remove(event.getPlayer());
        }
    }
    
    @EventHandler
    public void onBlockPlaceEvent(final BlockPlaceEvent event) {
        final Arena arena = BedwarsAPI.getArena(event.getPlayer());
        if (arena != null && this.alreadyInDeathmatch.contains(arena)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockBreakEvent(final BlockBreakEvent event) {
        final Arena arena = BedwarsAPI.getArena(event.getPlayer());
        if (arena != null && this.alreadyInDeathmatch.contains(arena)) {
            event.setCancelled(true);
        }
    }
}
