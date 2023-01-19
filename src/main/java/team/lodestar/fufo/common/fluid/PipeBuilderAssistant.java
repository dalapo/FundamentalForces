package team.lodestar.fufo.common.fluid;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import org.jetbrains.annotations.Nullable;
import team.lodestar.fufo.core.fluid.PipeNode;
import team.lodestar.lodestone.handlers.GhostBlockHandler;
import team.lodestar.lodestone.handlers.PlacementAssistantHandler;
import team.lodestar.lodestone.setup.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.systems.placementassistance.IPlacementAssistant;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import team.lodestar.lodestone.systems.rendering.ghost.GhostBlockOptions;
import team.lodestar.lodestone.systems.rendering.ghost.GhostBlockRenderer;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static team.lodestar.lodestone.handlers.PlacementAssistantHandler.ASSISTANTS;

public class PipeBuilderAssistant implements IPlacementAssistant {

    public static final PipeBuilderAssistant INSTANCE = new PipeBuilderAssistant();

    //the cached path of block positions we will be filling with pipe segments
    public Collection<BlockPos> cachedPath = new ArrayList<>();

    //the block we are looking at
    //if it ever changes, the cachedPath is recalculated
    public BlockHitResult lookingAt;

    //the old pipe node, the thing we are building off of
    public PipeNode oldPipeNode;
    public BlockPos oldPipeNodePos;

    //the new pipe node, the thing we are about to place
    public BlockPos currentNodePos;

    public static void registerPlacementAssistant(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> ASSISTANTS.add(INSTANCE));
    }

    public BlockPos getTargetPosition(BlockHitResult blockHitResult) { //TODO: this ain't always accurate
        return blockHitResult.getBlockPos().relative(blockHitResult.getDirection());
    }

    public Collection<BlockPos> recalculatePath(Level level, BlockHitResult blockHitResult, BlockState blockState) {
        if (currentNodePos != null && blockHitResult != null) {
            if (blockHitResult.getType().equals(HitResult.Type.MISS)) {
                return Collections.emptyList();
            }
            BlockPos placedPos = getTargetPosition(blockHitResult);
            boolean matchesX = placedPos.getX() - currentNodePos.getX() == 0;
            boolean matchesY = placedPos.getY() - currentNodePos.getY() == 0;
            boolean matchesZ = placedPos.getZ() - currentNodePos.getZ() == 0;
            List<Boolean> matches = List.of(matchesX, matchesY, matchesZ);
            if (matches.stream().filter(b -> b).count() != 2) {
                return Collections.emptyList();
            }
            List<BlockPos> inBetween = BlockPos.betweenClosedStream(currentNodePos, placedPos).map(BlockPos::immutable).collect(Collectors.toList());
            inBetween.remove(0);
            inBetween.remove(inBetween.size() - 1);
            return inBetween;
        }
        return Collections.emptyList();
    }

    public InteractionResult updateSelectedNode(BlockPlaceContext pContext) {
        if (pContext.getLevel().getBlockEntity(pContext.getClickedPos().relative(pContext.getClickedFace().getOpposite())) instanceof PipeNode pipeNode) {
            PipeBuilderAssistant instance = PipeBuilderAssistant.INSTANCE;
            if (!pipeNode.getPos().equals(instance.oldPipeNodePos)) {
                instance.updateSelectedNode(pipeNode);
                return InteractionResult.SUCCESS;
            }
        }
        return null;
    }

    public void updateSelectedNode(PipeNode tile) {
        oldPipeNodePos = currentNodePos;
        currentNodePos = tile.getPos();
        oldPipeNode = tile;
        cachedPath.clear();
    }

    @Override
    public void onPlace(Level level, BlockHitResult hit, BlockState blockState) {

    }

    //TODO: this needs to run on server side too, needs some changes in lodestone.
    @Override
    public void assist(Level level, BlockHitResult blockHitResult, BlockState blockState) {
        BlockHitResult cachedPastTarget = lookingAt == null ? null : new BlockHitResult(lookingAt.getLocation(), lookingAt.getDirection(), lookingAt.getBlockPos(), lookingAt.isInside());
        lookingAt = PlacementAssistantHandler.target;
        if (!lookingAt.equals(cachedPastTarget)) {
            cachedPath = recalculatePath(level, blockHitResult, blockState);
        }
    }

    @OnlyIn(value = Dist.CLIENT)
    @Override
    public void showPlacementAssistance(ClientLevel clientLevel, BlockHitResult blockHitResult, BlockState blockState, ItemStack held) {
        for (BlockPos pos : cachedPath) {
            GhostBlockHandler.addGhost(pos, GhostBlockRenderer.STANDARD, GhostBlockOptions.create(Blocks.GLASS.defaultBlockState(), pos).withRenderType(LodestoneRenderTypeRegistry.ADDITIVE_BLOCK).withColor(0.6f, 0.95f, 1f), 0);
        }
        if (currentNodePos != null && !cachedPath.isEmpty()) {
            BlockState anchor = ((BlockItem)held.getItem()).getBlock().defaultBlockState();
            BlockPos placedPos = getTargetPosition(blockHitResult);
            GhostBlockHandler.addGhost(placedPos, GhostBlockRenderer.STANDARD, GhostBlockOptions.create(anchor, placedPos).withRenderType(LodestoneRenderTypeRegistry.ADDITIVE_BLOCK).withColor(0.6f, 0.95f, 1f).withAlpha(()-> 0.75f).withScale(() -> 1.1f), 0);
        }
    }

    @OnlyIn(value = Dist.CLIENT)
    @Override
    public void showPassiveAssistance(ClientLevel level, @Nullable BlockHitResult hit) {
        if (currentNodePos != null) {
            BlockState anchor = level.getBlockState(currentNodePos);
            GhostBlockHandler.addGhost(currentNodePos, GhostBlockRenderer.STANDARD, GhostBlockOptions.create(anchor, currentNodePos).withRenderType(LodestoneRenderTypeRegistry.ADDITIVE_BLOCK).withColor(0.6f, 0.95f, 1f).withAlpha(()-> 0.75f).withScale(() -> 1.1f), 0);
        }
    }

    @Override
    public Predicate<ItemStack> canAssist() {
        return s -> s.getItem() instanceof PipeNodeBlockItem;
    }
}