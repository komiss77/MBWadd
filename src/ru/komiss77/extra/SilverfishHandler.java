package ru.komiss77.extra;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.message.Message;
import ru.komiss77.BwAdd;

public class SilverfishHandler extends CustomSpecialItemUseSession implements Listener {
    
  public static String silverfish_icon_name = "Silverfish";
  public static ItemStack silverfish_icon_material = new ItemStack(Material.SHEARS);//= Helper.get().parseItemStack("SNOWBALL");
  public static int silverfish_life_duration = 400;
  public static List<String> silverfish_name_tag = Arrays.asList(
      "{team-color}&l{team-name} {team-color}[■ ■ ■ ■ ■]",
      "{team-color}&l{team-name} {team-color}[■ ■ ■ ■ &7■]",
      "{team-color}&l{team-name} {team-color}[■ ■ ■ &7■ ■]",
      "{team-color}&l{team-name} {team-color}[■ ■ &7■ ■ ■]",
      "{team-color}&l{team-name} {team-color}[■ &7■ ■ ■ ■]"
  );
  
  private Arena arena;
  private Team team;
  private Snowball snowball;
  private Silverfish silverfish;
  private BukkitTask task;

  public SilverfishHandler(PlayerUseSpecialItemEvent event) {
    super(event);
  }

  @Override
  public void run(PlayerUseSpecialItemEvent event) {
    this.takeItem();

    final Player player = event.getPlayer();

    this.arena = event.getArena();
    this.team = event.getArena().getPlayerTeam(player);
    this.snowball = player.launchProjectile(Snowball.class);
    this.task = Bukkit.getScheduler().runTaskLater(BwAdd.instance, () -> this.snowball.remove(), 8 * 20L);

    Bukkit.getPluginManager().registerEvents(this, BwAdd.instance);
  }

  private void startUpdatingDisplayName() {
    final String teamName = team.getDisplayName();
    final String color = team.getBungeeChatColor().toString();
    final int amountOfTags = silverfish_name_tag.size();
    final long updateTime = silverfish_life_duration / amountOfTags;

    this.silverfish.setCustomNameVisible(true);

    task = Bukkit.getScheduler().runTaskTimer(BwAdd.instance, new Runnable() {
      int i = 0;

      @Override
      public void run() {
        if (!silverfish.isValid() || i >= amountOfTags) {
          stop();
          task.cancel();
          return;
        }

        final String unformattedDisplayName = silverfish_name_tag.get(i);
        final String displayName = Message.build(unformattedDisplayName != null ? unformattedDisplayName : "")
            .placeholder("team-color", color)
            .placeholder("team-name", teamName)
            .placeholder("sqr", "■")
            .done();

        silverfish.setCustomName(displayName);
        i++;
      }
    }, 0, updateTime);
  }

  @Override
  protected void handleStop() {
    HandlerList.unregisterAll(this);

    if (this.task != null)
      this.task.cancel();

    if (this.snowball != null && this.snowball.isValid())
      this.snowball.remove();

    if (this.silverfish != null && this.silverfish.isValid())
      this.silverfish.remove();

  }

  @EventHandler
  public void onProjectileHit(ProjectileHitEvent event) {
    if (event.getEntity() != this.snowball)
      return;

    // No point in removing the snowball
    if (this.task != null)
      this.task.cancel();

    this.silverfish = (Silverfish) this.snowball.getWorld().spawnEntity(this.snowball.getLocation(), EntityType.SILVERFISH);

    startUpdatingDisplayName();
  }

  @EventHandler
  public void onEntityTarget(EntityTargetLivingEntityEvent event) {
    if (event.getEntity() != this.silverfish)
      return;

    if (!(event.getTarget() instanceof Player)) {
      event.setCancelled(true);
      return;
    }

    final Player player = (Player) event.getTarget();

    // Player is on the same team or not actually playing
    if (!this.arena.getPlayers().contains(player) || this.arena.getPlayerTeam(player) == this.team)
      event.setCancelled(true);

  }

  @EventHandler
  public void onSilverfishDeath(EntityDeathEvent event) {
    if (event.getEntity() == this.silverfish)
      stop();
  }

  @EventHandler
  public void onSilverfishBurrow(EntityChangeBlockEvent event) {
    if (event.getEntity() == this.silverfish)
      event.setCancelled(true);
  }

  @EventHandler
  public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
    if (event.getDamager() == this.silverfish) {
      // Stop silverfish attacking team members
      if (event.getEntity() instanceof Player player) {
        final Arena playerArena = BedwarsAPI.getGameAPI().getArenaByPlayer(player);

        if (playerArena == null || this.arena != playerArena || this.arena.getPlayerTeam(player) == this.team) {
          event.setCancelled(true);
          return;
        }
      }

      event.setDamage(1.5);

      // Stop player attacking silverfish
    } else if (event.getEntity() == this.silverfish && event.getDamager() instanceof Player) {
      final Player player = (Player) event.getDamager();
      final Arena playerArena = BedwarsAPI.getGameAPI().getArenaByPlayer(player);

      if (playerArena == null || this.arena != playerArena || this.arena.getPlayerTeam(player) == this.team)
        event.setCancelled(true);

    }
  }
}