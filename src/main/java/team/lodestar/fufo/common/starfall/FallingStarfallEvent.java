package team.lodestar.fufo.common.starfall;

import com.mojang.math.Vector3f;
import team.lodestar.fufo.client.rendering.postprocess.EnergyScanFx;
import team.lodestar.fufo.client.rendering.postprocess.EnergySphereFx;
import team.lodestar.fufo.client.rendering.postprocess.WorldHighlightFx;
import team.lodestar.fufo.common.starfall.actors.AbstractStarfallActor;
import team.lodestar.fufo.config.CommonConfig;
import team.lodestar.fufo.registry.client.FufoPostProcessingEffects;
import team.lodestar.fufo.registry.common.worldevent.FufoStarfallActors;
import team.lodestar.fufo.registry.common.worldevent.FufoWorldEventTypes;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.helpers.BlockHelper;
import team.lodestar.lodestone.helpers.EntityHelper;
import team.lodestar.lodestone.helpers.VecHelper;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.screenshake.PositionedScreenshakeInstance;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public class FallingStarfallEvent extends WorldEventInstance {

    public AbstractStarfallActor actor;

    public final ArrayList<EntityHelper.PastPosition> pastPositions = new ArrayList<>();
    public BlockPos targetedPos;
    public Vec3 position;
    public Vec3 positionOld = Vec3.ZERO;
    public Vec3 motion;

    public float acceleration = .1F;
    public float speed;

    public int startingHeight;
    public int atmosphericEntryHeight;

    public WorldHighlightFx highlight;

    public FallingStarfallEvent(AbstractStarfallActor actor, Vec3 position, Vec3 motion, BlockPos targetedPos) {
        super(FufoWorldEventTypes.FALLING_STARFALL);
        this.actor = actor;
        this.startingHeight = (int) position.y;
        this.atmosphericEntryHeight = startingHeight - CommonConfig.STARFALL_SPAWN_HEIGHT.getConfigValue() + CommonConfig.STARFALL_ATMOSPHERE_ENTRY_HEIGHT.getConfigValue();
        this.position = position;
        this.motion = motion;
        this.targetedPos = targetedPos;
    }

    @Override
    public CompoundTag serializeNBT(CompoundTag tag) {
        tag.putString("actor", actor.id);
        tag.putIntArray("targetedPos", new int[]{targetedPos.getX(), targetedPos.getY(), targetedPos.getZ()});
        tag.putDouble("posX", position.x());
        tag.putDouble("posY", position.y());
        tag.putDouble("posZ", position.z());
        tag.putDouble("motionX", motion.x());
        tag.putDouble("motionY", motion.y());
        tag.putDouble("motionZ", motion.z());
        tag.putFloat("speed", speed);
        tag.putFloat("acceleration", acceleration);
        tag.putInt("startingHeight", startingHeight);
        tag.putInt("atmosphericEntryHeight", atmosphericEntryHeight);
        return super.serializeNBT(tag);
    }

    public static FallingStarfallEvent deserializeNBT(CompoundTag tag) {
        int[] positions = tag.getIntArray("targetedPos");
        FallingStarfallEvent fallingStarfallEvent = new FallingStarfallEvent(
                FufoStarfallActors.ACTORS.get(tag.getString("actor")),
                new Vec3(tag.getDouble("posX"), tag.getDouble("posY"), tag.getDouble("posZ")),
                new Vec3(tag.getDouble("motionX"), tag.getDouble("motionY"), tag.getDouble("motionZ")),
                new BlockPos(positions[0], positions[1], positions[2]));
        fallingStarfallEvent.speed = tag.getFloat("speed");
        fallingStarfallEvent.acceleration = tag.getFloat("acceleration");
        fallingStarfallEvent.startingHeight = tag.getInt("startingHeight");
        fallingStarfallEvent.atmosphericEntryHeight = tag.getInt("atmosphericEntryHeight");
        return fallingStarfallEvent;
    }

    @Override
    public void tick(Level level) {
        move();
        trackPastPositions();

        if (level.isClientSide) {
            FallingStarfallEvent.ClientOnly.createHighlightFx(this);
        }

        if (position.y() <= targetedPos.getY()) {
            end(level);
        }
    }

    @Override
    public void end(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            actor.act(serverLevel, targetedPos);
        } else {
            ScreenshakeHandler.addScreenshake(new PositionedScreenshakeInstance(80, VecHelper.fromBlockPos(targetedPos), 10f, 800f, Easing.EXPO_OUT).setIntensity(3f, 0));
            FallingStarfallEvent.ClientOnly.createEventEndFx(this);
        }
        discarded = true;
    }


    @Override
    public boolean isClientSynced() {
        return true;
    }

    private void move() {
        positionOld = position;
        speed += acceleration;
        position = position.add(motion.multiply(speed, speed, speed));
    }

    public void trackPastPositions() {
        EntityHelper.trackPastPositions(pastPositions, position, 0.01f);
        removeOldPositions(pastPositions);
    }

    public void removeOldPositions(ArrayList<EntityHelper.PastPosition> pastPositions) {
        int amount = pastPositions.size() - 1;
        ArrayList<EntityHelper.PastPosition> toRemove = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            EntityHelper.PastPosition excess = pastPositions.get(i);
            if (excess.time > 30) {
                toRemove.add(excess);
            }
        }
        pastPositions.removeAll(toRemove);
    }

    public static class ClientOnly {

        public static void createHighlightFx(FallingStarfallEvent event) {
            if (event.highlight == null) {
                event.highlight = new WorldHighlightFx(new Vector3f(event.position), 200F, new Vector3f(2F, 1F, 4F));
                FufoPostProcessingEffects.WORLD_HIGHLIGHT.addFxInstance(event.highlight);
            }
        }

        public static void updateHighlightFx(FallingStarfallEvent event) {
            if (event.highlight != null) { //TODO: discarded events shouldn't render. highlight is only null after the event is discarded. Once we update lodestone in 1.19.2 this line can be removed.
                event.highlight.center.set((float) event.position.x, (float) event.position.y, (float) event.position.z);
            }
        }

        public static void createEventEndFx(FallingStarfallEvent event) {
            event.highlight.remove();
            event.highlight = null;
            playImpactEffect(new Vector3f(event.targetedPos.getX() + 0.5f, event.targetedPos.getY() + 0.5f, event.targetedPos.getZ() + 0.5f));
        }

        private static void playImpactEffect(Vector3f position) {
            Runnable energyReleaseEffect = () -> {
                FufoPostProcessingEffects.ENERGY_SCAN.addFxInstance(new EnergyScanFx(position) {
                    @Override
                    public void update(double deltaTime) {
                        super.update(deltaTime);

                        float t = getTime() / 7.5F;
                        if (t < 1) {
                            t = Easing.CIRC_OUT.ease(t, 0F, 1F, 1F);
                        }

                        virtualRadius = t * 300F;
                        if (virtualRadius > 1300F) {
                            remove();
                        }
                    }
                });
                FufoPostProcessingEffects.ENERGY_SPHERE.addFxInstance(new EnergySphereFx(position, 0, 1) {
                    @Override
                    public void update(double deltaTime) {
                        super.update(deltaTime);

                        float t = getTime() / 7.5F;

                        if (t > 1) {
                            remove();
                            return;
                        }
                        t = Easing.CIRC_OUT.ease(t, 0F, 1F, 1F);

                        this.radius = t * 300F;
                        this.intensity = (300F - radius) / 300F;
                        this.intensity = (float) Mth.clamp(intensity, 0., 1.);
                    }
                });
            };

            if (!FufoPostProcessingEffects.IMPACT_FRAME.playEffect(position, .75F, energyReleaseEffect)) {
                energyReleaseEffect.run();
            }

            FufoPostProcessingEffects.WORLD_HIGHLIGHT.addFxInstance(new WorldHighlightFx(position, 400F, new Vector3f(8F, 4F, 16F)) {
                @Override
                public void update(double deltaTime) {
                    radius -= deltaTime * 20F;
                    if (radius < 0) remove();
                }
            });
        }
    }
}