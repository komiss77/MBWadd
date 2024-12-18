package ru.komiss77.extra;

import java.util.ArrayDeque;
import java.util.Queue;
import org.bukkit.block.data.type.Ladder;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.tools.Pair;
import de.marcely.bedwars.tools.PersistentBlockData;
import ru.komiss77.BwAdd;

public class TowerHandler extends CustomSpecialItemUseSession {
    
  public static String tower_icon_name = "PopUpTower";
  public static ItemStack tower_icon_material = new ItemStack(Material.SCULK_SHRIEKER);//Helper.get().parseItemStack("CHEST");
  public static Material tower_block_material = Material.WHITE_WOOL; //Helper.get().getMaterialByName("WHITE_WOOL");
  public static int tower_block_place_interval = 1;
  public static int tower_block_placed_per_interval = 2;
  public static Sound tower_place_place_sound = Sound.ENTITY_CHICKEN_EGG;//Helper.get().getSoundByName("ENTITY_CHICKEN_EGG");
  
  private static PersistentBlockData blockData;

  private BukkitTask buildTask;
  private BlockFace direction;
  private Block chest;
  private DyeColor color;
  private final Queue<Pair<Block, Boolean>> towerBlock = new ArrayDeque<>();

  public TowerHandler(PlayerUseSpecialItemEvent event) {
    super(event);
  }

  public static void init() {
    blockData = PersistentBlockData.fromMaterial(tower_block_material);
  }

  @Override
  public void run(PlayerUseSpecialItemEvent event) {
    final Player player = event.getPlayer();
    final Arena arena = event.getArena();
    final Team team = arena.getPlayerTeam(player);
    final Block clicked = event.getClickedBlock();
    final BlockFace blockFace = event.getClickedBlockFace();

    // Check if placeable
    if (clicked == null ||
        blockFace == null ||
        blockFace == BlockFace.DOWN ||
        clicked.getRelative(blockFace).getType() != Material.AIR ||
        !arena.canPlaceBlockAt(clicked.getRelative(blockFace))) {

      event.setTakingItem(false);
      this.stop();
      return;
    }

    // Take item
    this.takeItem();

    double rotation = (player.getLocation().getYaw() - 90.0F) % 360.0F;

    if (rotation < 0.0D)
      rotation += 360.0D;

    if (45.0D <= rotation && rotation < 135.0D) {
      direction = BlockFace.SOUTH;
    } else if (225.0D <= rotation && rotation < 315.0D) {
      direction = BlockFace.NORTH;
    } else if (135.0D <= rotation && rotation < 225.0D) {
      direction = BlockFace.WEST;
    } else { // if (0.0D <= rotation && rotation < 45.0D || 315.0D <= rotation && rotation < 360.0D) {
      direction = BlockFace.EAST;
    }

    color = team != null ? team.getDyeColor() : DyeColor.WHITE;
    chest = clicked.getRelative(blockFace);

    buildTask = Bukkit.getScheduler().runTaskAsynchronously(BwAdd.instance, () -> {
      // Construct Async
      constructTowerBlocks();

      // Lets build Sync
      this.buildTask = Bukkit.getScheduler().runTaskTimer(BwAdd.instance, () -> {
        if (!this.isActive()) {
          this.stop();
          return;
        }

        for (int i = 0; i < tower_block_placed_per_interval; i++) {
          if (this.towerBlock.peek() == null) {
            this.stop();
            return;
          }

          final Pair<Block, Boolean> blockBooleanPair = this.towerBlock.poll();
          final Block block = blockBooleanPair.getKey();

          // Is block there?
          if (block == null || !block.getType().equals(Material.AIR))
            continue;

          // Is block inside region
          if (arena.canPlaceBlockAt(block.getLocation())) {
            block.getWorld().playSound(block.getLocation(), tower_place_place_sound, 1, 1);
            placeBlock(arena, blockBooleanPair.getValue(), block, color);
          }
        }

      }, 0L, tower_block_place_interval);
    });
  }


  @Override
  protected void handleStop() {
    if (this.buildTask != null)
      this.buildTask.cancel();
  }

  private void placeBlock(Arena arena, boolean isLadder, Block block, DyeColor color) {
    // Re-Dye
    //if (ConfigValue.dye_tower_ukraine)
      //color = block.getY() - this.chest.getY() > 3 ? DyeColor.BLUE : DyeColor.YELLOW;

    // Place Block
    if (!isLadder) {
      final PersistentBlockData data = blockData.getDyedData(color);
      data.place(block, true);
    } else {
      block.setType(Material.LADDER);

      // We do not want to initialize legacy material support (causes lag)
        final Ladder ladder = (Ladder) block.getBlockData();

        ladder.setFacing(direction);
        block.setBlockData(ladder);

    }

    arena.setBlockPlayerPlaced(block, true);
  }


  private void constructTowerBlocks() {
    addToQueue(-1, 0, -2, false);
    addToQueue(-2, 0, -1, false);
    addToQueue(-2, 0, 0, false);
    addToQueue(-1, 0, 1, false);
    addToQueue(0, 0, 1, false);
    addToQueue(1, 0, 1, false);
    addToQueue(2, 0, 0, false);
    addToQueue(2, 0, -1, false);
    addToQueue(1, 0, -2, false);
    addToQueue(0, 0, 0, true);

    addToQueue(-1, 1, -2, false);
    addToQueue(-2, 1, -1, false);
    addToQueue(-2, 1, 0, false);
    addToQueue(-1, 1, 1, false);
    addToQueue(0, 1, 1, false);
    addToQueue(1, 1, 1, false);
    addToQueue(2, 1, 0, false);
    addToQueue(2, 1, 0, false);
    addToQueue(2, 1, -1, false);
    addToQueue(1, 1, -2, false);
    addToQueue(0, 1, 0, true);

    addToQueue(-1, 2, -2, false);
    addToQueue(-2, 2, -1, false);
    addToQueue(-2, 2, 0, false);
    addToQueue(-1, 2, 1, false);
    addToQueue(0, 2, 1, false);
    addToQueue(1, 2, 1, false);
    addToQueue(2, 2, 0, false);
    addToQueue(2, 2, -1, false);
    addToQueue(1, 2, -2, false);
    addToQueue(0, 2, 0, true);

    addToQueue(0, 3, -2, false);
    addToQueue(-1, 3, -2, false);
    addToQueue(-2, 3, -1, false);
    addToQueue(-2, 3, 0, false);
    addToQueue(-1, 3, 1, false);
    addToQueue(0, 3, 1, false);
    addToQueue(1, 3, 1, false);
    addToQueue(2, 3, 0, false);
    addToQueue(2, 3, -1, false);
    addToQueue(1, 3, -2, false);
    addToQueue(0, 3, 0, true);

    addToQueue(0, 4, -2, false);
    addToQueue(-1, 4, -2, false);
    addToQueue(-2, 4, -1, false);
    addToQueue(-2, 4, 0, false);
    addToQueue(-1, 4, 1, false);
    addToQueue(0, 4, 1, false);
    addToQueue(1, 4, 1, false);
    addToQueue(2, 4, 0, false);
    addToQueue(2, 4, -1, false);
    addToQueue(1, 4, -2, false);
    addToQueue(0, 4, 0, true);

    addToQueue(-2, 5, 1, false);
    addToQueue(-2, 5, 0, false);
    addToQueue(-2, 5, -1, false);
    addToQueue(-2, 5, -2, false);
    addToQueue(-1, 5, 1, false);
    addToQueue(-1, 5, 0, false);
    addToQueue(-1, 5, -1, false);
    addToQueue(-1, 5, -2, false);
    addToQueue(0, 5, 1, false);
    addToQueue(0, 5, -1, false);
    addToQueue(0, 5, -2, false);
    addToQueue(1, 5, 1, false);
    addToQueue(0, 5, 0, true);

    addToQueue(1, 5, 0, false);
    addToQueue(1, 5, -1, false);
    addToQueue(1, 5, -2, false);
    addToQueue(2, 5, 1, false);
    addToQueue(2, 5, 0, false);
    addToQueue(2, 5, -1, false);
    addToQueue(2, 5, -2, false);
    addToQueue(-3, 5, -2, false);
    addToQueue(-3, 5, 1, false);
    addToQueue(-2, 5, 2, false);
    addToQueue(0, 5, 2, false);
    addToQueue(2, 5, 2, false);
    addToQueue(3, 5, -2, false);
    addToQueue(3, 5, 1, false);
    addToQueue(-2, 5, -3, false);
    addToQueue(0, 5, -3, false);
    addToQueue(2, 5, -3, false);

    addToQueue(-3, 6, -2, false);
    addToQueue(-3, 6, -1, false);
    addToQueue(-3, 6, 0, false);
    addToQueue(-3, 6, 1, false);
    addToQueue(-2, 6, 2, false);
    addToQueue(-1, 6, 2, false);
    addToQueue(0, 6, 2, false);
    addToQueue(1, 6, 2, false);
    addToQueue(2, 6, 2, false);
    addToQueue(3, 6, -2, false);
    addToQueue(3, 6, -1, false);
    addToQueue(3, 6, 0, false);
    addToQueue(3, 6, 1, false);
    addToQueue(-2, 6, -3, false);
    addToQueue(-1, 6, -3, false);
    addToQueue(0, 6, -3, false);
    addToQueue(1, 6, -3, false);
    addToQueue(2, 6, -3, false);

    addToQueue(-3, 7, -2, false);
    addToQueue(-3, 7, 1, false);
    addToQueue(-2, 7, 2, false);
    addToQueue(0, 7, 2, false);
    addToQueue(2, 7, 2, false);
    addToQueue(3, 7, -2, false);
    addToQueue(3, 7, 1, false);
    addToQueue(-2, 7, -3, false);
    addToQueue(0, 7, -3, false);
    addToQueue(2, 7, -3, false);
  }

  private void addToQueue(int x, int height, int y, boolean ladder) {
    Block block = null;

    switch (this.direction) {
      case NORTH -> block = this.chest.getRelative(x, height, y);
      case SOUTH -> block = this.chest.getRelative(x * -1, height, y * -1);
      case EAST -> block = this.chest.getRelative(y * -1, height, x);
      case WEST -> block = this.chest.getRelative(y, height, x * -1);
    }

    if (block != null)
      this.towerBlock.add(new Pair<>(block, ladder));

  }
}