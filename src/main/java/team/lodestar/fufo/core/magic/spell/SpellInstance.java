package team.lodestar.fufo.core.magic.spell;

import team.lodestar.fufo.common.capability.FufoPlayerDataCapability;
import team.lodestar.fufo.common.magic.spell.attributes.SpellAttributeMap;
import team.lodestar.fufo.common.packets.spell.SyncSpellCooldownPacket;
import team.lodestar.fufo.registry.common.magic.FufoSpellDataKeys;
import team.lodestar.fufo.registry.common.magic.FufoSpellTypes;
import team.lodestar.lodestone.systems.easing.Easing;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.PacketDistributor;
import team.lodestar.fufo.registry.common.FufoPackets;

import javax.annotation.Nullable;

public class SpellInstance {

    //https://tenor.com/view/fire-explosion-meme-fishing-gif-23044892
    public static final SpellInstance EMPTY = new SpellInstance(FufoSpellTypes.EMPTY);
    public final SpellType spellType;
    public SpellCooldown cooldown;
    public boolean selected;
    public int selectedTime;
    public float selectedFadeAnimation;
    public SpellCastMode castMode;
    public SpellEffect effect;
    public final SpellAttributeMap attributes = new SpellAttributeMap();

    public SpellInstance(SpellType spellType, SpellCastMode castMode) {
        this.spellType = spellType;
        this.castMode = castMode;
        this.effect = spellType.effect;
    }

    public SpellInstance(SpellType spellType) {
        this.spellType = spellType;
    }

    public void cast(ServerPlayer player, BlockPos pos, BlockHitResult hitVec) {
        if (castMode.canCast(this, player, pos, hitVec)) {
            effect.cast(this, player, hitVec);
        }
    }

    public void cast(ServerPlayer player) {
        if (castMode.canCast(this, player)) {
            effect.cast(this, player);
        }
    }

    public final void tick(Level level, @Nullable ServerPlayer player) {
        if (isOnCooldown()) {
            cooldown.tick();
        }
        selectedTime = selected ? selectedTime + 1 : 0;
        if (selected && selectedFadeAnimation < 20) {
            selectedFadeAnimation++;
        } else if (selectedFadeAnimation > 0) {
            selectedFadeAnimation -= 0.5f;
        }
        effect.tick(this, level, player);
    }

    public final void reactToDeath(ServerPlayer player) {
        effect.reactToDeath(this, player);
    }

    public float getIconFadeout() {
        Easing easing = Easing.EXPO_IN;
        if (selected) {
            easing = Easing.EXPO_OUT;
        }
        return 0.5f - easing.ease(selectedFadeAnimation, 0, 0.5f, 20);
    }

    public void setCooldown(SpellCooldown cooldown) {
        setCooldown(cooldown, null);
    }

    public void setCooldown(SpellCooldown cooldown, @Nullable ServerPlayer serverPlayer) {
        this.cooldown = cooldown;
        if (serverPlayer != null) {
            FufoPlayerDataCapability.getCapabilityOptional(serverPlayer).ifPresent(c -> FufoPackets.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SyncSpellCooldownPacket(serverPlayer.getUUID(), c.hotbarHandler.spellStorage.getSelectedSpellIndex(serverPlayer), cooldown)));
        }
    }

    public void setDefaultCooldown(ServerPlayer player) {
        attributes.getSpellAttribute(FufoSpellDataKeys.COOLDOWN_KEY).ifPresent(d -> {
            setCooldown(new SpellCooldown(d.duration), player);
        });
    }

    public boolean isOnCooldown() {
        return SpellCooldown.shouldTick(cooldown);
    }

    public boolean isEmpty() {
        return spellType == null || spellType.equals(FufoSpellTypes.EMPTY);
    }

    public CompoundTag serializeNBT() {
        CompoundTag spellTag = new CompoundTag();
        spellTag.putString("type", spellType.id.toString());
        if (castMode != null) {
            spellTag.put("castMode", castMode.serializeNBT());
        }
        if (isOnCooldown()) {
            spellTag.put("spellCooldown", cooldown.serializeNBT());
        }
        if (!isEmpty()) {
            CompoundTag attributesTag = new CompoundTag();
            for (SpellAttribute attribute : attributes.values()) {
                attributesTag.put(attribute.id.toString(), attribute.serializeNBT());
            }
            spellTag.put("attributes", attributesTag);
        }
        return spellTag;
    }

    public static SpellInstance deserializeNBT(CompoundTag tag) {
        SpellInstance spellInstance = new SpellInstance(FufoSpellTypes.SPELL_TYPES.get(new ResourceLocation(tag.getString("type"))),
                SpellCastMode.deserializeNBT(tag.getCompound("castMode")));
        if (tag.contains("spellCooldown")) {
            spellInstance.cooldown = SpellCooldown.deserializeNBT(tag.getCompound("spellCooldown"));
        }
        CompoundTag attributes = tag.getCompound("attributes");
        for (String path : attributes.getAllKeys()) {
            ResourceLocation key = new ResourceLocation(path);
            FufoSpellDataKeys.DataKey<? extends SpellAttribute> dataKey = FufoSpellDataKeys.DATA_KEYS.get(key);
            if (dataKey == null) {
                return EMPTY;
            }
            SpellAttribute attribute = dataKey.serializer.apply(attributes.getCompound(path));
            spellInstance.attributes.put(key, attribute);
        }
        return spellInstance;
    }
}