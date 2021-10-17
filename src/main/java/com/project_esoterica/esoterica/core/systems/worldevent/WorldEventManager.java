package com.project_esoterica.esoterica.core.systems.worldevent;

import com.project_esoterica.esoterica.common.capability.ChunkDataCapability;
import com.project_esoterica.esoterica.common.capability.WorldDataCapability;
import com.project_esoterica.esoterica.common.worldevent.starfall.StarfallInstance;
import com.project_esoterica.esoterica.core.config.CommonConfig;
import com.project_esoterica.esoterica.core.registry.block.BlockTagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.ArrayList;

public class WorldEventManager {

    public static <T extends WorldEventInstance> T addWorldEvent(ServerLevel level, T instance, boolean inbound) {
        return inbound ? addInboundWorldEvent(level, instance) : addWorldEvent(level, instance);
    }

    public static <T extends WorldEventInstance> T addInboundWorldEvent(ServerLevel level, T instance) {
        WorldDataCapability.getCapability(level).ifPresent(capability -> {
            capability.INBOUND_WORLD_EVENTS.add(instance);
            instance.start(level);
        });
        return instance;
    }

    public static <T extends WorldEventInstance> T addWorldEvent(ServerLevel level, T instance) {
        WorldDataCapability.getCapability(level).ifPresent(capability -> {
            capability.ACTIVE_WORLD_EVENTS.add(instance);
            instance.start(level);
        });
        return instance;
    }

    public static void worldTick(ServerLevel level) {
        WorldDataCapability.getCapability(level).ifPresent(capability -> {
            for (WorldEventInstance instance : capability.ACTIVE_WORLD_EVENTS) {
                instance.tick(level);
            }
            capability.ACTIVE_WORLD_EVENTS.removeIf(e -> e.invalidated);
            capability.ACTIVE_WORLD_EVENTS.addAll(capability.INBOUND_WORLD_EVENTS);
            capability.INBOUND_WORLD_EVENTS.clear();
        });
    }

    public static void serializeNBT(WorldDataCapability capability, CompoundTag tag) {
        tag.putInt("worldEventCount", capability.ACTIVE_WORLD_EVENTS.size());
        for (int i = 0; i < capability.ACTIVE_WORLD_EVENTS.size(); i++) {
            WorldEventInstance instance = capability.ACTIVE_WORLD_EVENTS.get(i);
            CompoundTag instanceTag = new CompoundTag();
            instance.serializeNBT(instanceTag);
            tag.put("worldEvent_" + i, instanceTag);
        }
    }

    public static void deserializeNBT(WorldDataCapability capability, CompoundTag tag) {
        capability.ACTIVE_WORLD_EVENTS.clear();
        int starfallCount = tag.getInt("worldEventCount");
        for (int i = 0; i < starfallCount; i++) {
            CompoundTag instanceTag = tag.getCompound("worldEvent_" + i);
            StarfallInstance instance = StarfallInstance.deserializeNBT(instanceTag);
            capability.ACTIVE_WORLD_EVENTS.add(instance);
        }
    }

    public static boolean heightmapCheck(ServerLevel level, BlockPos pos, int range) {
        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                LevelChunk chunk = level.getChunk(SectionPos.blockToSectionCoord(pos.getX()) + x, SectionPos.blockToSectionCoord(pos.getZ()) + z);
                int heightmapChanges = ChunkDataCapability.getHeightmapChanges(chunk);
                if (heightmapChanges >= CommonConfig.MAXIMUM_HEIGHTMAP_CHANGES.get()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static ArrayList<BlockPos> nearbyBlockList(ServerLevel level, BlockPos centerPos) {
        int size = CommonConfig.STARFALL_SAFETY_CHECK_RANGE.get();
        ArrayList<BlockPos> result = new ArrayList<>();
        //this is REALLY bad, preferably turn it into an iterable or stream.
        //I used the thing below earlier but that for some reason had the very first point stored in every single member of the stream? ? ? ??
        //return BlockPos.betweenClosedStream(Mth.floor(aabb.minX), Mth.floor(aabb.minY), Mth.floor(aabb.minZ), Mth.floor(aabb.maxX), Mth.floor(aabb.maxY), Mth.floor(aabb.maxZ)).filter(p -> !level.getBlockState(p).isAir());

        for (int x = -size; x <= size;x++)
        {
            for (int y = (int) (-size/4f); y <= size/4f; y++)
            {
                for (int z = -size; z <= size;z++)
                {
                    BlockPos pos = new BlockPos(centerPos.offset(x,y,z));
                    if (!level.getBlockState(pos).isAir())
                    {
                        result.add(pos);
                    }
                }
            }
        }
        return result;
    }

    public static boolean blockCheck(ServerLevel level, ArrayList<BlockPos> arrayList) {
        int failed = 0;
        int failToAbort = (int) (arrayList.size()*0.2f);
        for (BlockPos pos : arrayList) {
            BlockState state = level.getBlockState(pos);
            if (level.isFluidAtPosition(pos, p -> !p.isEmpty()))
            {
                return false;
            }
            if (state.is(BlockTags.FEATURES_CANNOT_REPLACE))
            {
                return false;
            }
            if (!blockEntityCheck(level, pos))
            {
                return false;
            }
            if (!blockCheck(level, state))
            {
                failed++;
                if (failed >= failToAbort)
                {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean blockEntityCheck(ServerLevel level, BlockPos pos) {
        return level.getBlockEntity(pos) == null;
    }

    @SuppressWarnings("all")
    public static boolean blockCheck(ServerLevel level, BlockState state) {
        if (!state.getMaterial().isSolid() || state.getMaterial().isReplaceable() || !state.getMaterial().blocksMotion())
        {
            return true;
        }
        Tag.Named<Block>[] tags = new Tag.Named[]{BlockTagRegistry.STARFALL_ALLOWED, BlockTags.LOGS, BlockTags.LEAVES, BlockTags.LUSH_GROUND_REPLACEABLE, BlockTags.SNOW, BlockTags.MUSHROOM_GROW_BLOCK};
        for (Tag.Named<Block> tag : tags)
        {
            if (state.is(tag))
            {
                return true;
            }
        }
        return false;
    }
}