package team.lodestar.fufo.registry.common.magic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.fufo.common.magic.spell.attributes.*;
import team.lodestar.fufo.common.magic.spell.states.DashState;
import team.lodestar.fufo.core.magic.spell.SpellAttribute;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static team.lodestar.fufo.FufoMod.fufoPath;

public class FufoPlayerStateKeys {
    public static final Map<ResourceLocation, PlayerStateKey<? extends FufoPlayerState>> DATA_KEYS = new HashMap<>();

    public static final PlayerStateKey<DashState> DASH = registerDataKey(fufoPath("dash"), DashState.class);

    public static <T extends FufoPlayerState> PlayerStateKey<T> registerDataKey(ResourceLocation location, Class<T> classType) {
        PlayerStateKey<T> value = new PlayerStateKey<>(location, classType);
        DATA_KEYS.put(location, value);
        return value;
    }

    public static class PlayerStateKey<T extends FufoPlayerState> {
        public final ResourceLocation id;
        public final Class<T> classType;

        public PlayerStateKey(ResourceLocation id, Class<T> classType) {
            this.id = id;
            this.classType = classType;
        }

        @SuppressWarnings("unchecked")
        public void putState(PlayerStateMap<FufoPlayerState> playerStateMap, FufoPlayerState state) {
            playerStateMap.put((PlayerStateKey<FufoPlayerState>) this, state);
        }

        public T getState(PlayerStateMap<?> playerStateMap) {
            return classType.cast(playerStateMap.get(this));
        }

        public boolean isInState(PlayerStateMap<?> playerStateMap) {
            return playerStateMap.containsKey(this);
        }

        @SuppressWarnings("unchecked")
        public void removeState(PlayerStateMap<FufoPlayerState> playerStateMap) {
            playerStateMap.remove((PlayerStateKey<FufoPlayerState>) this);
        }
    }

    public static class PlayerStateMap<T extends FufoPlayerState> extends HashMap<PlayerStateKey<T>, T> {
    }

    public interface FufoPlayerState {

    }
}