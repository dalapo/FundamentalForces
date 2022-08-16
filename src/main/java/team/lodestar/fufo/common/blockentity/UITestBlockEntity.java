package team.lodestar.fufo.common.blockentity;

import team.lodestar.fufo.core.registratation.BlockEntityRegistrate;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class UITestBlockEntity extends LodestoneBlockEntity {
    public boolean toggle = false;
    public UITestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    public UITestBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistrate.UI_TEST_BLOCK.get(),pos, state);
    }

    @Override
    public InteractionResult onUse(Player player, InteractionHand hand) {
        toggle = !toggle;
        return InteractionResult.SUCCESS;
    }
}
