package ru.komiss77.extraSpecItem_old;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import de.marcely.bedwars.tools.PersistentBlockData;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class EggBridgerHandler extends CustomSpecialItemUseSession implements Listener {

    private BukkitTask task;
    private Egg egg;

    public EggBridgerHandler(PlayerUseSpecialItemEvent event) {
        super(event);
    }

    @Override
    public void run(PlayerUseSpecialItemEvent event) {
        this.takeItem();
        Player player = event.getPlayer();
        Arena arena = event.getArena();
        Team team = arena.getPlayerTeam(player);
        DyeColor color = team != null ? team.getDyeColor() : DyeColor.WHITE;
        PersistentBlockData data = PersistentBlockData.fromMaterial(ConfigValue.egg_bridger_block_material).getDyedData(color);

        this.egg = (Egg) player.launchProjectile(Egg.class);
        this.task = (new EggBridgerHandler.BridgeBlockPlacerTask(this.egg, player.getLocation(), this, arena, data)).runTaskTimer(ExtraSpecialItemsPlugin.getInstance(), 0L, 1L);
    }

    @Override
    protected void handleStop() {
        HandlerList.unregisterAll(this);
        if (this.egg != null && this.egg.isValid()) {
            this.egg.remove();
        }

        if (this.task != null) {
            this.task.cancel();
        }

    }

    @EventHandler
    public void onCreatureSpawn(PlayerEggThrowEvent event) {
        if (event.getEgg() == this.egg) {
            event.setHatching(false);
        }

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
            Location eggLocation = this.egg.getLocation();

            if (this.egg.isValid() && this.playerLocation.distance(eggLocation) <= (double) ConfigValue.egg_bridger_max_length && this.playerLocation.getY() - eggLocation.getY() <= (double) ConfigValue.egg_bridger_max_y_variation) {
                Bukkit.getScheduler().runTaskLater(ExtraSpecialItemsPlugin.getInstance(), () -> {
                    if (this.session.isActive()) {
                        if (this.playerLocation.distanceSquared(eggLocation.clone().add(0.0D, 1.0D, 0.0D)) > 12.25D) {
                            this.placeBlock(eggLocation.clone().subtract(0.0D, 2.0D, 0.0D).getBlock());
                            this.placeBlock(eggLocation.clone().subtract(1.0D, 2.0D, 0.0D).getBlock());
                            this.placeBlock(eggLocation.clone().subtract(0.0D, 2.0D, 1.0D).getBlock());
                            this.egg.getWorld().playSound(eggLocation, ConfigValue.egg_bridger_place_sound, 1.0F, 1.0F);
                        }

                    }
                }, 2L);
            } else {
                this.session.stop();
                this.cancel();
            }

        }

        private void placeBlock(Block block) {
            if (block.getType().equals(Material.AIR)) {
                if (this.arena != null && this.arena.canPlaceBlockAt(block.getLocation())) {
                    this.data.place(block, true);
                    this.arena.setBlockPlayerPlaced(block, true);
                }

            }
        }
    }
}
