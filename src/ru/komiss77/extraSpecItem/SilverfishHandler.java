package ru.komiss77.extraSpecItem;

import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.message.Message;
import ru.komiss77.extraSpecItem.ExtraSpecialItemsPlugin;
import ru.komiss77.extraSpecItem.ConfigValue;
import ru.komiss77.extraSpecItem.CustomSpecialItemUseSession;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitTask;

public class SilverfishHandler extends CustomSpecialItemUseSession implements Listener {

    private Arena arena;
    private Team team;
    private Snowball snowball;
    private Silverfish silverfish;
    private BukkitTask task;

    public SilverfishHandler(PlayerUseSpecialItemEvent event) {
        super(event);
    }

    public void run(PlayerUseSpecialItemEvent event) {
        this.takeItem();
        Player player = event.getPlayer();

        this.arena = event.getArena();
        this.team = event.getArena().getPlayerTeam(player);
        this.snowball = (Snowball) player.launchProjectile(Snowball.class);
        this.task = Bukkit.getScheduler().runTaskLater(ExtraSpecialItemsPlugin.getInstance(), () -> {
            this.snowball.remove();
        }, 160L);
        Bukkit.getPluginManager().registerEvents(this, ExtraSpecialItemsPlugin.getInstance());
    }

    private void startUpdatingDisplayName() {
        if (ConfigValue.silverfish_name_tag != null && !ConfigValue.silverfish_name_tag.isEmpty()) {
            final String teamName = this.team.getDisplayName();
            final String color = this.team.getBungeeChatColor().toString();
            final int amountOfTags = ConfigValue.silverfish_name_tag.size();
            long updateTime = (long) (ConfigValue.silverfish_life_duration / amountOfTags);

            this.silverfish.setCustomNameVisible(true);
            this.task = Bukkit.getScheduler().runTaskTimer(ExtraSpecialItemsPlugin.getInstance(), new Runnable() {
                int i = 0;

                public void run() {
                    if (SilverfishHandler.this.silverfish.isValid() && this.i < amountOfTags) {
                        String unformattedDisplayName = (String) ConfigValue.silverfish_name_tag.get(this.i);
                        String displayName = Message.build(unformattedDisplayName != null ? unformattedDisplayName : "").placeholder("team-color", color).placeholder("team-name", teamName).placeholder("sqr", "■").done();

                        SilverfishHandler.this.silverfish.setCustomName(displayName);
                        ++this.i;
                    } else {
                        SilverfishHandler.this.stop();
                        SilverfishHandler.this.task.cancel();
                    }
                }
            }, 0L, updateTime);
        }
    }

    protected void handleStop() {
        HandlerList.unregisterAll(this);
        if (this.task != null) {
            this.task.cancel();
        }

        if (this.snowball != null && this.snowball.isValid()) {
            this.snowball.remove();
        }

        if (this.silverfish != null && this.silverfish.isValid()) {
            this.silverfish.remove();
        }

    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() == this.snowball) {
            if (this.task != null) {
                this.task.cancel();
            }

            this.silverfish = (Silverfish) this.snowball.getWorld().spawnEntity(this.snowball.getLocation(), EntityType.SILVERFISH);
            this.startUpdatingDisplayName();
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        if (event.getEntity() == this.silverfish) {
            if (!(event.getTarget() instanceof Player)) {
                event.setCancelled(true);
            } else {
                Player player = (Player) event.getTarget();

                if (!this.arena.getPlayers().contains(player) || this.arena.getPlayerTeam(player) == this.team) {
                    event.setCancelled(true);
                }

            }
        }
    }

    @EventHandler
    public void onSilverfishDeath(EntityDeathEvent event) {
        if (event.getEntity() == this.silverfish) {
            this.stop();
        }

    }

    @EventHandler
    public void onSilverfishBurrow(EntityChangeBlockEvent event) {
        if (event.getEntity() == this.silverfish) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        Player player;
        Arena playerArena;

        if (event.getDamager() == this.silverfish) {
            if (event.getEntity() instanceof Player) {
                player = (Player) event.getEntity();
                playerArena = BedwarsAPI.getGameAPI().getArenaByPlayer(player);
                if (playerArena == null || this.arena != playerArena || this.arena.getPlayerTeam(player) == this.team) {
                    event.setCancelled(true);
                    return;
                }
            }

            event.setDamage(1.5D);
        } else if (event.getEntity() == this.silverfish && event.getDamager() instanceof Player) {
            player = (Player) event.getDamager();
            playerArena = BedwarsAPI.getGameAPI().getArenaByPlayer(player);
            if (playerArena == null || this.arena != playerArena || this.arena.getPlayerTeam(player) == this.team) {
                event.setCancelled(true);
            }
        }

    }
}
