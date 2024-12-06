package ru.komiss77.extra;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import de.marcely.bedwars.tools.PersistentBlockData;
import ru.komiss77.BwAdd;

public class EggBridgerHandler extends CustomSpecialItemUseSession implements Listener {
    
  public static String egg_bridger_icon_name = "EggBridger";
  public static ItemStack egg_bridger_icon_material = new ItemStack(Material.EGG);//Helper.get().parseItemStack("EGG");
  public static Material egg_bridger_block_material = Material.WHITE_WOOL;//Helper.get().getMaterialByName("WHITE_WOOL");
  public static int egg_bridger_max_length = 30;
  public static int egg_bridger_max_y_variation = 18;
  public static Sound egg_bridger_place_sound = Sound.ENTITY_CHICKEN_EGG;//Helper.get().getSoundByName("ENTITY_CHICKEN_EGG");


  private BukkitTask task;
  private Egg egg;
  

  public EggBridgerHandler(PlayerUseSpecialItemEvent event) {
    super(event);
  }

  @Override
  public void run(PlayerUseSpecialItemEvent event) {
    this.takeItem();

    final Player player = event.getPlayer();
    final Arena arena = event.getArena();
    final Team team = arena.getPlayerTeam(player);
    final DyeColor color = team != null ? team.getDyeColor() : DyeColor.WHITE;
    final PersistentBlockData data = PersistentBlockData.fromMaterial(egg_bridger_block_material).getDyedData(color);

    this.egg = player.launchProjectile(Egg.class);
    this.task = new BridgeBlockPlacerTask(this.egg, player.getLocation(), this, arena, data).runTaskTimer(BwAdd.instance, 0L, 1L);
  }

  @Override
  protected void handleStop() {
    HandlerList.unregisterAll(this);

    if (egg != null && egg.isValid())
      egg.remove();

    if (task != null)
      task.cancel();
  }

  @EventHandler
  public void onCreatureSpawn(PlayerEggThrowEvent event) {
    if (event.getEgg() == egg)
      event.setHatching(false);
  }

  private static class BridgeBlockPlacerTask extends BukkitRunnable {
    private final Egg egg;
    private final SpecialItemUseSession session;
    private final Arena arena;
    private final PersistentBlockData data;
    private final Location playerLocation;

    public BridgeBlockPlacerTask(Egg egg, Location playerLocation, SpecialItemUseSession session, Arena arena, PersistentBlockData data) {
      this.egg = egg;
      this.session = session;
      this.arena = arena;
      this.data = data;
      this.playerLocation = playerLocation;
    }

    @Override
    public void run() {
      final Location eggLocation = this.egg.getLocation();

      if (this.egg.isValid()
          && this.playerLocation.distance(eggLocation) <= egg_bridger_max_length
          && this.playerLocation.getY() - eggLocation.getY() <= egg_bridger_max_y_variation) {

        // Slight delay so the egg does not fall on the new blocks
        Bukkit.getScheduler().runTaskLater(BwAdd.instance, () -> {
          if (!this.session.isActive())
            return;

          if (this.playerLocation.distanceSquared(eggLocation.clone().add(0, 1, 0)) > 12.25D) {
            placeBlock(eggLocation.clone().subtract(0.0D, 2.0D, 0.0D).getBlock());
            placeBlock(eggLocation.clone().subtract(1.0D, 2.0D, 0.0D).getBlock());
            placeBlock(eggLocation.clone().subtract(0.0D, 2.0D, 1.0D).getBlock());

            this.egg.getWorld().playSound(eggLocation, egg_bridger_place_sound, 1, 1);
          }
        }, 2L);

      } else {
        this.session.stop();
        cancel();
      }
    }

    private void placeBlock(Block block) {
      // Is block there?
      if (!block.getType().equals(Material.AIR))
        return;

      // Is block inside region
      if (this.arena != null && this.arena.canPlaceBlockAt(block.getLocation())) {
        this.data.place(block, true);
        this.arena.setBlockPlayerPlaced(block, true);
      }
    }
  }
}