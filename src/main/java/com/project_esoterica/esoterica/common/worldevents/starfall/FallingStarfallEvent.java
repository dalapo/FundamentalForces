package com.project_esoterica.esoterica.common.worldevents.starfall;

import com.project_esoterica.esoterica.core.registry.worldevent.StarfallActors;
import com.project_esoterica.esoterica.core.registry.worldevent.WorldEventTypes;
import com.project_esoterica.esoterica.core.systems.screenshake.ScreenshakeHandler;
import com.project_esoterica.esoterica.core.systems.worldevent.WorldEventInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FallingStarfallEvent extends WorldEventInstance {

    public StarfallActor actor;

    public BlockPos targetedPos = BlockPos.ZERO;
    public Vec3 position = Vec3.ZERO;
    public Vec3 motion = Vec3.ZERO;
    public float acceleration = 0.01f;

    private FallingStarfallEvent() {
        super(WorldEventTypes.FALLING_STARFALL);
    }

    public FallingStarfallEvent(StarfallActor actor) {
        super(WorldEventTypes.FALLING_STARFALL);
        this.actor = actor;
    }

    public static FallingStarfallEvent fromNBT(CompoundTag tag) {
        FallingStarfallEvent instance = new FallingStarfallEvent();
        instance.deserializeNBT(tag);
        return instance;
    }

    public FallingStarfallEvent startPosition(Vec3 position) {
        this.position = position;
        return this;
    }

    public FallingStarfallEvent motion(Vec3 motion) {
        this.motion = motion;
        return this;
    }

    public FallingStarfallEvent targetPosition(BlockPos targetedPos) {
        this.targetedPos = targetedPos;
        return this;
    }

    @Override
    public void start(ServerLevel level) {
        if (existsOnClient()) {
            addToClient();
        }
    }

    @Override
    public void tick(ServerLevel level) {
        move();
        if (position.y() <= targetedPos.getY()) {
            end(level);
        }
    }

    @Override
    public void end(ServerLevel level) {
        actor.act(level, targetedPos);
        super.end(level);
    }

    @Override
    public boolean existsOnClient() {
        return true;
    }

    @Override
    public void clientTick(Level level) {
        move();
        if (position.y() <= targetedPos.getY()) {
            clientEnd(level);
        }
    }

    // TODO: make farther players experience less screenshake
    @Override
    public void clientEnd(Level level) {
        ScreenshakeHandler.addScreenshake(0.85f, 0.9f);
        super.clientEnd(level);
    }

    private void move() {
        position = position.add(motion.multiply(acceleration, acceleration, acceleration));
        acceleration += 0.01f;
    }

    @Override
    public CompoundTag serializeNBT(CompoundTag tag) {
        tag.putString("resultId", actor.id);
        tag.putIntArray("targetedPos", new int[]{targetedPos.getX(), targetedPos.getY(), targetedPos.getZ()});
        tag.putDouble("posX", position.x());
        tag.putDouble("posY", position.y());
        tag.putDouble("posZ", position.z());
        tag.putDouble("motionX", motion.x());
        tag.putDouble("motionY", motion.y());
        tag.putDouble("motionZ", motion.z());
        tag.putFloat("acceleration", acceleration);
        return super.serializeNBT(tag);
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        actor = StarfallActors.STARFALL_RESULTS.get(tag.getString("resultId"));
        int[] positions = tag.getIntArray("targetedPos");
        targetedPos = new BlockPos(positions[0], positions[1], positions[2]);
        position = new Vec3(tag.getDouble("posX"), tag.getDouble("posY"), tag.getDouble("posZ"));
        motion = new Vec3(tag.getDouble("motionX"), tag.getDouble("motionY"), tag.getDouble("motionZ"));
        acceleration = tag.getFloat("acceleration");
        super.deserializeNBT(tag);
    }
}