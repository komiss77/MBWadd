package ru.komiss77.extraSpecItem_old;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.tools.Helper;
import ru.komiss77.extraSpecItem_old.ExtraSpecialItemsPlugin;
import ru.komiss77.extraSpecItem_old.ConfigValue;
import ru.komiss77.extraSpecItem_old.CustomSpecialItemUseSession;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitTask;

public class IceBridgerHandler extends CustomSpecialItemUseSession {

    private BukkitTask task;
    public Arena arena;

    public IceBridgerHandler(PlayerUseSpecialItemEvent event) {
        super(event);
    }

    public void run(PlayerUseSpecialItemEvent event) {
        this.takeItem();
        this.arena = event.getArena();
        final Location playerLocation = event.getPlayer().getLocation().clone();

        playerLocation.setPitch(0.0F);
        this.task = Bukkit.getScheduler().runTaskTimer(ExtraSpecialItemsPlugin.getInstance(), new Runnable() {
            int i = 2;

            public void run() {
                if (this.i <= ConfigValue.ice_bridger_max_distance && IceBridgerHandler.this.isActive()) {
                    int yaw = (int) playerLocation.getYaw() % 180;
                    Location blockLoc = playerLocation.add(playerLocation.getDirection().multiply(this.i)).add(0.0D, -1.0D, 0.0D);
                    Block block = blockLoc.getBlock();
                    World world = block.getWorld();
                    Sound sound = Helper.get().getSoundByName("BLOCK_SNOW_BREAK");

                    IceBridgerHandler.this.setIce(block);
                    if (sound != null) {
                        world.playSound(blockLoc, sound, 1.0F, 1.0F);
                    }

                    if ((yaw < 45 || yaw >= 135) && (yaw < -135 || yaw >= -45)) {
                        IceBridgerHandler.this.setIce(block.getRelative(1, 0, 0));
                        IceBridgerHandler.this.setIce(block.getRelative(-1, 0, 0));
                        IceBridgerHandler.this.setIce(block.getRelative(2, 0, 0));
                        IceBridgerHandler.this.setIce(block.getRelative(-2, 0, 0));
                        IceBridgerHandler.this.setIce(block.getRelative(3, 0, 0));
                        IceBridgerHandler.this.setIce(block.getRelative(-3, 0, 0));
                    } else {
                        IceBridgerHandler.this.setIce(block.getRelative(0, 0, 1));
                        IceBridgerHandler.this.setIce(block.getRelative(0, 0, -1));
                        IceBridgerHandler.this.setIce(block.getRelative(0, 0, 2));
                        IceBridgerHandler.this.setIce(block.getRelative(0, 0, -2));
                        IceBridgerHandler.this.setIce(block.getRelative(0, 0, 3));
                        IceBridgerHandler.this.setIce(block.getRelative(0, 0, -3));
                    }

                    ++this.i;
                } else {
                    IceBridgerHandler.this.stop();
                    IceBridgerHandler.this.task.cancel();
                }

            }
        }, 0L, 2L);
    }

    private void setIce(Block block) {
        if (this.arena.canPlaceBlockAt(block.getLocation()) && block.getType() == Material.AIR) {
            block.setType(ConfigValue.ice_bridger_material);
            Bukkit.getScheduler().runTaskLater(ExtraSpecialItemsPlugin.getInstance(), () -> {
                if (this.arena.getStatus() == ArenaStatus.RUNNING) {
                    block.setType(Material.AIR);
                }

            }, 70L);
        }

    }

    protected void handleStop() {
        if (this.task != null) {
            this.task.cancel();
        }

    }
}
