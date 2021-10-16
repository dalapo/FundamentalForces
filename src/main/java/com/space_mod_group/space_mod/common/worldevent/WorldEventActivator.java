package com.space_mod_group.space_mod.common.worldevent;

import com.space_mod_group.space_mod.common.capability.PlayerDataCapability;
import com.space_mod_group.space_mod.common.capability.WorldDataCapability;
import com.space_mod_group.space_mod.common.worldevent.starfall.StarfallInstance;
import com.space_mod_group.space_mod.core.config.CommonConfig;
import com.space_mod_group.space_mod.core.registry.worldevent.StarfallResults;
import com.space_mod_group.space_mod.core.systems.worldevent.WorldEventInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class WorldEventActivator {
    public static void playerJoin(ServerLevel level, Player player) {
        PlayerDataCapability.getCapability(player).ifPresent(capability -> {
            if (areStarfallsAllowed(level)) {
                if (!capability.firstTimeJoin) {
                    WorldEventManager.addWorldEvent(level, new StarfallInstance(StarfallResults.INITIAL_SPACE_DEBRIS, level, player).setLooping());
                } else {
                    addSpaceDebrisIfMissing(level, player);
                }
            }
        });
    }

    public static void addSpaceDebrisIfMissing(ServerLevel level, Player player) {
        WorldDataCapability.getCapability(level).ifPresent(capability -> {
            boolean isMissingStarfall = true;
            for (WorldEventInstance instance : capability.ACTIVE_WORLD_EVENTS) {
                if (instance instanceof StarfallInstance starfallInstance) {
                    if (player.getUUID().equals(starfallInstance.targetedUUID)) {
                        isMissingStarfall = false;
                        break;
                    }
                }
            }

            if (isMissingStarfall) {
                addSpaceDebris(level, player, false);
            }
        });
    }

    public static void addSpaceDebris(ServerLevel level, LivingEntity entity, boolean inbound) {
        if (areStarfallsAllowed(level)) {
            StarfallInstance debrisInstance = WorldEventManager.addWorldEvent(level, new StarfallInstance(StarfallResults.SPACE_DEBRIS, level, entity).setLooping(), inbound);
            Double chance = CommonConfig.ASTEROID_CHANCE.get();
            int maxAsteroids = CommonConfig.MAXIMUM_ASTEROID_COUNT.get();
            for (int i = 0; i < maxAsteroids; i++) {
                if (level.random.nextFloat() < chance) {
                    WorldEventManager.addWorldEvent(level, new StarfallInstance(StarfallResults.ASTEROID, level, entity).randomizeCountdown(level, debrisInstance.startingCountdown), inbound);
                    chance *= 0.8f;
                } else {
                    break;
                }
            }
        }
    }
    public static boolean areStarfallsAllowed(ServerLevel level)
    {
        return CommonConfig.STARFALLS_ENABLED.get() && CommonConfig.STARFALL_ALLOWED_LEVELS.get().contains(level.dimension().location().toString());
    }
}