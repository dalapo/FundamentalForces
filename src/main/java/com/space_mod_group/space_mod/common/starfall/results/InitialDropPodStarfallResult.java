package com.space_mod_group.space_mod.common.starfall.results;

import com.space_mod_group.space_mod.core.systems.starfall.StarfallResult;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class InitialDropPodStarfallResult extends DropPodStarfallResult {
    public InitialDropPodStarfallResult() {
        super(FIRST_TIME_STARTING_COUNTDOWN);
    }

    @Override
    public void fall(ServerLevel level, BlockPos pos) {
        level.setBlock(pos, Blocks.DIAMOND_BLOCK.defaultBlockState(), 2);
        super.fall(level, pos);
    }
}
