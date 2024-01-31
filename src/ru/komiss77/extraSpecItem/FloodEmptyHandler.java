package ru.komiss77.extraSpecItem;

import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.tools.VarParticle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import ru.komiss77.extraSpecItem.ExtraSpecialItemsPlugin;
import ru.komiss77.extraSpecItem.CustomSpecialItemUseSession;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitTask;

public class FloodEmptyHandler extends CustomSpecialItemUseSession {

    private BukkitTask task;
    private List starterBlocks;
    private int blocksBroken = 0;

    public FloodEmptyHandler(PlayerUseSpecialItemEvent event) {
        super(event);
    }

    public void run(PlayerUseSpecialItemEvent event) {
        Block clickedBlock = event.getClickedBlock();
        BlockFace face = event.getClickedBlockFace();

        if (clickedBlock != null && face != null) {
            event.setTakingItem(true);
            this.takeItem();
            this.starterBlocks = Collections.singletonList(clickedBlock);
            this.task = Bukkit.getScheduler().runTaskLater(ExtraSpecialItemsPlugin.getInstance(), () -> {
                if (this.isActive()) {
                    ArrayList newStarters = new ArrayList();
                    Iterator iterator = this.starterBlocks.iterator();

                    while (iterator.hasNext()) {
                        Block block = (Block) iterator.next();

                        if (this.blocksBroken >= 30) {
                            this.stopTask();
                            break;
                        }

                        if (block.getType() != Material.AIR && event.getArena().isBlockPlayerPlaced(block)) {
                            newStarters.addAll(this.getContacts(block));
                            this.removeBlock(block);
                            ++this.blocksBroken;
                        }
                    }

                    Collections.shuffle(newStarters);
                    this.starterBlocks = newStarters;
                }

            }, 4L);
        }
    }

    protected void handleStop() {
        if (this.task != null) {
            this.task.cancel();
        }

    }

    private void stopTask() {
        this.task.cancel();
        this.stop();
    }

    private List getContacts(Block starter) {
        ArrayList contacts = new ArrayList();

        contacts.add(starter.getRelative(BlockFace.NORTH));
        contacts.add(starter.getRelative(BlockFace.SOUTH));
        contacts.add(starter.getRelative(BlockFace.EAST));
        contacts.add(starter.getRelative(BlockFace.WEST));
        contacts.add(starter.getRelative(BlockFace.UP));
        contacts.add(starter.getRelative(BlockFace.DOWN));
        return contacts;
    }

    private void removeBlock(Block block) {
        Location location = block.getLocation();

        block.setType(Material.AIR);
        location.add(0.5D, 0.5D, 0.5D);
        VarParticle.PARTICLE_SMOKE.play(location);
        location.add(0.1D, 0.1D, 0.1D);
        VarParticle.PARTICLE_CLOUD.play(location);
    }
}
