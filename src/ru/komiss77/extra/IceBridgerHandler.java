package ru.komiss77.extra;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.tools.Helper;
import ru.komiss77.BwAdd;


public class IceBridgerHandler extends CustomSpecialItemUseSession {
    
  public static String ice_bridger_icon_name = "IceBridger";
  public static ItemStack ice_bridger_icon_material = new ItemStack(Material.ICE);//Helper.get().parseItemStack("ICE");
  public static Material ice_bridger_material = Material.ICE;//Helper.get().getMaterialByName("ICE");
  public static int ice_bridger_max_distance = 37;
  
  private BukkitTask task;
  public Arena arena;

  public IceBridgerHandler(PlayerUseSpecialItemEvent event) {
    super(event);
  }

  @Override
  public void run(PlayerUseSpecialItemEvent event) {
    this.takeItem();

    this.arena = event.getArena();
    final Location playerLocation = event.getPlayer().getLocation().clone().add(0, -1, 0);

    // The bridge should be flat
    playerLocation.setPitch(0);

    final Vector directionUnitVec = playerLocation.getDirection();
    final int yaw = (int) playerLocation.getYaw() % 180;
    final boolean xAxis = (yaw < 45 || yaw >= 135) && (yaw < -135 || yaw >= -45);

    this.task = Bukkit.getScheduler().runTaskTimer(BwAdd.instance, new Runnable() {
      int i = 2;

      @Override
      public void run() {
        if (this.i <= ice_bridger_max_distance && isActive()) {
          final Location blockLoc = playerLocation.add(directionUnitVec);
          final Block block = blockLoc.getBlock();
          final Sound sound = Helper.get().getSoundByName("BLOCK_SNOW_BREAK"); // TODO move to config

          setIce(block);

          if (sound != null)
            block.getWorld().playSound(blockLoc, sound, 1, 1);

          if (xAxis) {
            setIce(block.getRelative(1, 0, 0));
            setIce(block.getRelative(-1, 0, 0));
            setIce(block.getRelative(2, 0, 0));
            setIce(block.getRelative(-2, 0, 0));
            //setIce(block.getRelative(3, 0, 0));
            //setIce(block.getRelative(-3, 0, 0));

          } else {
            setIce(block.getRelative(0, 0, 1));
            setIce(block.getRelative(0, 0, -1));
            setIce(block.getRelative(0, 0, 2));
            setIce(block.getRelative(0, 0, -2));
            //setIce(block.getRelative(0, 0, 3));
            //setIce(block.getRelative(0, 0, -3));
          }

          this.i++;
        } else {
          stop();
          task.cancel();
        }
      }
    }, 0, 2L);
  }

  private void setIce(Block block) {
    if (this.arena.canPlaceBlockAt(block.getLocation()) && block.getType() == Material.AIR) {
      block.setType(ice_bridger_material);

      Bukkit.getScheduler().runTaskLater(BwAdd.instance, () -> {
        if (this.arena.getStatus() == ArenaStatus.RUNNING)
          block.setType(Material.AIR);

      }, 120L);
    }
  }

  @Override
  protected void handleStop() {
    if (this.task != null)
      this.task.cancel();
  }
}
