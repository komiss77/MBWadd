package ru.komiss77.extraSpecItem_old;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Ladder;

public class PlaceLadderModern {

    public static void placeLadder(Block block, BlockFace direction) {
        Ladder ladder = (Ladder) block.getBlockData();

        ladder.setFacing(direction);
        block.setBlockData(ladder);
    }
}
