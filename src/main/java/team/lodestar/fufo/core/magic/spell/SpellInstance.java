package team.lodestar.fufo.core.magic.spell;

import team.lodestar.fufo.common.capability.FufoPlayerDataCapability;
import team.lodestar.fufo.common.packets.spell.SyncSpellCooldownPacket;
import team.lodestar.fufo.core.magic.MagicElementType;
import team.lodestar.fufo.registry.common.magic.FufoMagicElements;
import team.lodestar.fufo.registry.common.magic.FufoSpellDataKeys;
import team.lodestar.fufo.registry.common.magic.FufoSpellDataKeys.SpellAttributeMap;
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

    public static final SpellInstance EMPTY = new SpellInstance(FufoSpellTypes.EMPTY);
    public final SpellType spellType;
    public SpellCooldown cooldown;
    public boolean selected;
    public int selectedTime;
    public float selectedFadeAnimation;
    public CompoundTag extraData = new CompoundTag();

    public SpellInstance(SpellType spellType) {
        this.spellType = spellType;
    }

    public void cast(ServerPlayer player, BlockPos pos, BlockHitResult hitVec) {
        if (getCastMode().canCast(this, player, pos, hitVec)) {
            getSpellEffect().cast(this, player, hitVec);
        }
    }

    public void cast(ServerPlayer player) {
        if (getCastMode().canCast(this, player)) {
            getSpellEffect().cast(this, player);
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
        getSpellEffect().tick(this, level, player);
    }

    public final void reactToDeath(ServerPlayer player) {
        getSpellEffect().reactToDeath(this, player);
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
        FufoSpellDataKeys.COOLDOWN_KEY.getOptionalAttribute(getSpellAttributes()).ifPresent(d -> {
            setCooldown(new SpellCooldown(d.duration), player);
        });
    }

    public SpellAttributeMap<SpellAttribute> getSpellAttributes() {
        return spellType.spellAttributes;
    }

    public MagicElementType getElement() {
        return spellType.element;
    }

    public SpellEffect getSpellEffect() {
        return spellType.effect;
    }

    public SpellCastMode getCastMode() {
        return spellType.defaultCastMode;
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
        if (getCastMode() != null) {
            spellTag.put("castMode", getCastMode().serializeNBT());
        }
        if (isOnCooldown()) {
            spellTag.put("spellCooldown", cooldown.serializeNBT());
        }
        spellTag.put("extraData", extraData);
        //TODO: once you get to making spell attribute modifiers (runes), reuse this code with rune stuffs
//        if (!isEmpty()) {
//            CompoundTag attributesTag = new CompoundTag();
//            for (SpellAttribute attribute : attributes.values()) {
//                attributesTag.put(attribute.id.toString(), attribute.serializeNBT());
//            }
//            spellTag.put("attributes", attributesTag);
//        }
        return spellTag;
    }

    public static SpellInstance deserializeNBT(CompoundTag tag) {
        SpellType type = FufoSpellTypes.SPELL_TYPES.get(new ResourceLocation(tag.getString("type")));
        if (type == null) {
            return EMPTY;
        }
        SpellInstance spellInstance = new SpellInstance(type);
        if (tag.contains("spellCooldown")) {
            spellInstance.cooldown = SpellCooldown.deserializeNBT(tag.getCompound("spellCooldown"));
        }
        spellInstance.extraData = tag.getCompound("extraData");
        //TODO: once you get to making spell attribute modifiers (runes), reuse this code with rune stuffs
//        CompoundTag attributes = tag.getCompound("attributes");
//        for (String path : attributes.getAllKeys()) {
//            FufoSpellDataKeys.SpellDataKey<? extends SpellAttribute> spellDataKey = FufoSpellDataKeys.DATA_KEYS.get(new ResourceLocation(path));
//            if (spellDataKey == null) {
//                return EMPTY;
//            }
//            SpellAttribute attribute = spellDataKey.serializer.apply(attributes.getCompound(path));
//            spellDataKey.putAttribute(spellInstance.attributes, spellDataKey.classType.cast(attribute));
//        }
        return spellInstance;
    }
}