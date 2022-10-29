package team.lodestar.fufo.common.magic.spell.effects;

import team.lodestar.fufo.core.magic.MagicElementType;
import team.lodestar.fufo.core.magic.spell.SpellEffect;
import team.lodestar.fufo.core.magic.spell.SpellInstance;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class PlaceBlockSpellEffect extends SpellEffect {
    BlockEntry<?> entry;
    public PlaceBlockSpellEffect(BlockEntry<?> entry, MagicElementType element){
        super(CastLogicHandler.ONLY_BLOCK, element);
        this.entry = entry;
    }

    @Override
    public void blockEffect(SpellInstance spell, ServerPlayer player, BlockHitResult result) {
        BlockState state = entry.getDefaultState();
        Level level = player.level;
        BlockPos pos = result.getBlockPos();
        pos = level.getBlockState(pos).getMaterial().isReplaceable() ? pos :pos.relative(result.getDirection());
        if(level.getBlockState(pos).getMaterial().isReplaceable()){
            level.setBlock(pos,state,3);
        }
    }
}
