package ru.komiss77.extraSpecItem;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.tools.Pair;
import de.marcely.bedwars.tools.PersistentBlockData;
import java.util.ArrayDeque;
import java.util.Queue;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class TowerHandler extends CustomSpecialItemUseSession {

    //private static boolean placeLadderLegacy;
    private static PersistentBlockData blockData;
    private BukkitTask buildTask;
    private BlockFace direction;
    private Block chest;
    private DyeColor color;
    private final Queue towerBlock = new ArrayDeque();

    public TowerHandler(PlayerUseSpecialItemEvent event) {
        super(event);
    }

    public static void init() {
        //TowerHandler.placeLadderLegacy = NMSHelper.get().getVersion() < 13;
        TowerHandler.blockData = PersistentBlockData.fromMaterial(ConfigValue.tower_block_material);
    }

    @Override
    public void run(PlayerUseSpecialItemEvent event) {
        Player player = event.getPlayer();
        Arena arena = event.getArena();
        Team team = arena.getPlayerTeam(player);
        Block clicked = event.getClickedBlock();
        BlockFace blockFace = event.getClickedBlockFace();

        if (clicked != null && blockFace != null && blockFace != BlockFace.DOWN && clicked.getRelative(blockFace).getType() == Material.AIR && arena.canPlaceBlockAt(clicked.getRelative(blockFace))) {
            this.takeItem();
            double rotation = (double) ((player.getLocation().getYaw() - 90.0F) % 360.0F);

            if (rotation < 0.0D) {
                rotation += 360.0D;
            }

            if (45.0D <= rotation && rotation < 135.0D) {
                this.direction = BlockFace.SOUTH;
            } else if (225.0D <= rotation && rotation < 315.0D) {
                this.direction = BlockFace.NORTH;
            } else if (135.0D <= rotation && rotation < 225.0D) {
                this.direction = BlockFace.WEST;
            } else {
                this.direction = BlockFace.EAST;
            }

            this.color = team != null ? team.getDyeColor() : DyeColor.WHITE;
            this.chest = clicked.getRelative(blockFace);
            this.buildTask = Bukkit.getScheduler().runTaskAsynchronously(ExtraSpecialItemsPlugin.getInstance(), () -> {
                this.constructTowerBlocks();
                this.buildTask = Bukkit.getScheduler().runTaskTimer(ExtraSpecialItemsPlugin.getInstance(), () -> {
                    if (!this.isActive()) {
                        this.stop();
                    } else {
                        for (int i = 0; i < ConfigValue.tower_block_placed_per_interval; ++i) {
                            if (this.towerBlock.peek() == null) {
                                this.stop();
                                return;
                            }

                            Pair blockBooleanPair = (Pair) this.towerBlock.poll();
                            Block block = (Block) blockBooleanPair.getKey();

                            if (block != null && block.getType().equals(Material.AIR) && arena.canPlaceBlockAt(block.getLocation())) {
                                block.getWorld().playSound(block.getLocation(), ConfigValue.tower_place_place_sound, 1.0F, 1.0F);
                                this.placeBlock(arena, (Boolean) blockBooleanPair.getValue(), block, this.color);
                            }
                        }

                    }
                }, 0L, (long) ConfigValue.tower_block_place_interval);
            });
        } else {
            event.setTakingItem(false);
            this.stop();
        }
    }

    @Override
    protected void handleStop() {
        if (this.buildTask != null) {
            this.buildTask.cancel();
        }

    }

    private void placeBlock(Arena arena, boolean isLadder, Block block, DyeColor color) {
        if (ConfigValue.dye_tower_ukraine) {
            color = block.getY() - this.chest.getY() > 3 ? DyeColor.BLUE : DyeColor.YELLOW;
        }

        if (!isLadder) {
            PersistentBlockData data = TowerHandler.blockData.getDyedData(color);

            data.place(block, true);
        } else {
            block.setType(Material.LADDER);
           // if (!TowerHandler.placeLadderLegacy) {
                PlaceLadderModern.placeLadder(block, this.direction);
          //  } else {
           //     BlockState state = block.getState();
           //     Ladder legacyLadder = new Ladder();

          //      legacyLadder.setFacingDirection(this.direction.getOppositeFace());
          //      state.setData(legacyLadder);
            //    state.update();
          //  }
        }

        arena.setBlockPlayerPlaced(block, true);
    }

    private void constructTowerBlocks() {
        this.addToQueue(-1, 0, -2, false);
        this.addToQueue(-2, 0, -1, false);
        this.addToQueue(-2, 0, 0, false);
        this.addToQueue(-1, 0, 1, false);
        this.addToQueue(0, 0, 1, false);
        this.addToQueue(1, 0, 1, false);
        this.addToQueue(2, 0, 0, false);
        this.addToQueue(2, 0, -1, false);
        this.addToQueue(1, 0, -2, false);
        this.addToQueue(0, 0, 0, true);
        this.addToQueue(-1, 1, -2, false);
        this.addToQueue(-2, 1, -1, false);
        this.addToQueue(-2, 1, 0, false);
        this.addToQueue(-1, 1, 1, false);
        this.addToQueue(0, 1, 1, false);
        this.addToQueue(1, 1, 1, false);
        this.addToQueue(2, 1, 0, false);
        this.addToQueue(2, 1, 0, false);
        this.addToQueue(2, 1, -1, false);
        this.addToQueue(1, 1, -2, false);
        this.addToQueue(0, 1, 0, true);
        this.addToQueue(-1, 2, -2, false);
        this.addToQueue(-2, 2, -1, false);
        this.addToQueue(-2, 2, 0, false);
        this.addToQueue(-1, 2, 1, false);
        this.addToQueue(0, 2, 1, false);
        this.addToQueue(1, 2, 1, false);
        this.addToQueue(2, 2, 0, false);
        this.addToQueue(2, 2, -1, false);
        this.addToQueue(1, 2, -2, false);
        this.addToQueue(0, 2, 0, true);
        this.addToQueue(0, 3, -2, false);
        this.addToQueue(-1, 3, -2, false);
        this.addToQueue(-2, 3, -1, false);
        this.addToQueue(-2, 3, 0, false);
        this.addToQueue(-1, 3, 1, false);
        this.addToQueue(0, 3, 1, false);
        this.addToQueue(1, 3, 1, false);
        this.addToQueue(2, 3, 0, false);
        this.addToQueue(2, 3, -1, false);
        this.addToQueue(1, 3, -2, false);
        this.addToQueue(0, 3, 0, true);
        this.addToQueue(0, 4, -2, false);
        this.addToQueue(-1, 4, -2, false);
        this.addToQueue(-2, 4, -1, false);
        this.addToQueue(-2, 4, 0, false);
        this.addToQueue(-1, 4, 1, false);
        this.addToQueue(0, 4, 1, false);
        this.addToQueue(1, 4, 1, false);
        this.addToQueue(2, 4, 0, false);
        this.addToQueue(2, 4, -1, false);
        this.addToQueue(1, 4, -2, false);
        this.addToQueue(0, 4, 0, true);
        this.addToQueue(-2, 5, 1, false);
        this.addToQueue(-2, 5, 0, false);
        this.addToQueue(-2, 5, -1, false);
        this.addToQueue(-2, 5, -2, false);
        this.addToQueue(-1, 5, 1, false);
        this.addToQueue(-1, 5, 0, false);
        this.addToQueue(-1, 5, -1, false);
        this.addToQueue(-1, 5, -2, false);
        this.addToQueue(0, 5, 1, false);
        this.addToQueue(0, 5, -1, false);
        this.addToQueue(0, 5, -2, false);
        this.addToQueue(1, 5, 1, false);
        this.addToQueue(0, 5, 0, true);
        this.addToQueue(1, 5, 0, false);
        this.addToQueue(1, 5, -1, false);
        this.addToQueue(1, 5, -2, false);
        this.addToQueue(2, 5, 1, false);
        this.addToQueue(2, 5, 0, false);
        this.addToQueue(2, 5, -1, false);
        this.addToQueue(2, 5, -2, false);
        this.addToQueue(-3, 5, -2, false);
        this.addToQueue(-3, 5, 1, false);
        this.addToQueue(-2, 5, 2, false);
        this.addToQueue(0, 5, 2, false);
        this.addToQueue(2, 5, 2, false);
        this.addToQueue(3, 5, -2, false);
        this.addToQueue(3, 5, 1, false);
        this.addToQueue(-2, 5, -3, false);
        this.addToQueue(0, 5, -3, false);
        this.addToQueue(2, 5, -3, false);
        this.addToQueue(-3, 6, -2, false);
        this.addToQueue(-3, 6, -1, false);
        this.addToQueue(-3, 6, 0, false);
        this.addToQueue(-3, 6, 1, false);
        this.addToQueue(-2, 6, 2, false);
        this.addToQueue(-1, 6, 2, false);
        this.addToQueue(0, 6, 2, false);
        this.addToQueue(1, 6, 2, false);
        this.addToQueue(2, 6, 2, false);
        this.addToQueue(3, 6, -2, false);
        this.addToQueue(3, 6, -1, false);
        this.addToQueue(3, 6, 0, false);
        this.addToQueue(3, 6, 1, false);
        this.addToQueue(-2, 6, -3, false);
        this.addToQueue(-1, 6, -3, false);
        this.addToQueue(0, 6, -3, false);
        this.addToQueue(1, 6, -3, false);
        this.addToQueue(2, 6, -3, false);
        this.addToQueue(-3, 7, -2, false);
        this.addToQueue(-3, 7, 1, false);
        this.addToQueue(-2, 7, 2, false);
        this.addToQueue(0, 7, 2, false);
        this.addToQueue(2, 7, 2, false);
        this.addToQueue(3, 7, -2, false);
        this.addToQueue(3, 7, 1, false);
        this.addToQueue(-2, 7, -3, false);
        this.addToQueue(0, 7, -3, false);
        this.addToQueue(2, 7, -3, false);
    }

    private void addToQueue(int x, int height, int y, boolean ladder) {
        Block block = null;

        switch (this.direction) {
            case NORTH -> block = this.chest.getRelative(x, height, y);
            case SOUTH -> block = this.chest.getRelative(x * -1, height, y * -1);
            case EAST -> block = this.chest.getRelative(y * -1, height, x);
            case WEST -> block = this.chest.getRelative(y, height, x * -1);
        }

        if (block != null) {
            this.towerBlock.add(new Pair(block, ladder));
        }

    }
}
