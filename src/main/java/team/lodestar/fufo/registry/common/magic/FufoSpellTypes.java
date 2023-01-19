package team.lodestar.fufo.registry.common.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import team.lodestar.fufo.FufoMod;
import team.lodestar.fufo.common.entity.magic.spell.tier1.SpellBolt;
import team.lodestar.fufo.common.magic.spell.PlayerSpellHandler;
import team.lodestar.fufo.common.magic.spell.SpellStorage;
import team.lodestar.fufo.common.magic.spell.attributes.DamageAttribute;
import team.lodestar.fufo.common.magic.spell.attributes.LifespanAttribute;
import team.lodestar.fufo.common.magic.spell.attributes.CooldownAttribute;
import team.lodestar.fufo.common.magic.spell.attributes.VelocityAttribute;
import team.lodestar.fufo.common.magic.spell.effects.ProjectileEffect;
import team.lodestar.fufo.common.magic.spell.effects.ToggledPotionSpellEffect;
import team.lodestar.fufo.common.starfall.impact.features.MeteoriteFeature;
import team.lodestar.fufo.core.magic.spell.*;
import team.lodestar.fufo.registry.common.FufoMobEffects;
import team.lodestar.fufo.unsorted.util.DevSpellResponder;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static team.lodestar.fufo.core.magic.spell.SpellType.createSpellType;

public class FufoSpellTypes {
    public static final HashMap<ResourceLocation, SpellType> SPELL_TYPES = new HashMap<>();

    public static final SpellType EMPTY = register(new EmptySpellType());

    public static final SpellType FORCE_BOLT = register(FufoMod.fufoPath("force_bolt"),
            r -> createSpellType(r, new ProjectileEffect(SpellBolt::new))
                    .addAttributes(new LifespanAttribute(80), new VelocityAttribute(2f), new DamageAttribute(6f), new CooldownAttribute(30)));

    public static final SpellType WINDSTEP = register(FufoMod.fufoPath("windstep"),
            r -> createSpellType(r, new ToggledPotionSpellEffect(FufoMobEffects.ULTRAKILL_MOVEMENT))
                    .setSpellElement(FufoMagicElements.AIR)
                    .addAttributes(new CooldownAttribute(10)));

    public static SpellType register(ResourceLocation id, Function<ResourceLocation, SpellType.SpellTypeBuilder> spellTypeFunction) {
        SpellType spellType = spellTypeFunction.apply(id).build();
        SPELL_TYPES.put(id, spellType);
        return spellType;
    }

    public static SpellType register(SpellType spellType) {
        SPELL_TYPES.put(spellType.id, spellType);
        return spellType;
    }

    public static class DebugSpellTypes {

        public static final SpellType DEV_TINKER_WITH_BLOCK = register(FufoMod.fufoPath("dev_tinker_with_block"),
                r -> createSpellType(r, new SpellEffect(SpellEffect.CastLogicHandler.ONLY_BLOCK) {
                    @Override
                    public void blockEffect(SpellInstance spell, ServerPlayer player, BlockHitResult result) {
                        BlockEntity te = player.level.getBlockEntity(result.getBlockPos());
                        if (te instanceof DevSpellResponder dev) {
                            dev.respondToDev(new UseOnContext(player, InteractionHand.MAIN_HAND, result));
                        }
                    }
                })
                        .setSpellElement(FufoMagicElements.DEV)
        );

        public static final SpellType DEV_SPEAK_WITH_BLOCK = register(FufoMod.fufoPath("dev_speak_with_block"),
                r -> createSpellType(r, new SpellEffect(SpellEffect.CastLogicHandler.ONLY_BLOCK) {
                    @Override
                    public void blockEffect(SpellInstance spell, ServerPlayer player, BlockHitResult result) {
                        BlockEntity te = player.level.getBlockEntity(result.getBlockPos());
                        if (te instanceof DevSpellResponder dev) {
                            dev.speakToDev(player.isCrouching());
                        }
                    }
                })
                        .setSpellElement(FufoMagicElements.DEV)
        );

        public static final SpellType DEV_METEORITE = register(FufoMod.fufoPath("dev_spawn_meteorite"),
                r -> createSpellType(r, new SpellEffect(SpellEffect.CastLogicHandler.ONLY_BLOCK) {
                    @Override
                    public void blockEffect(SpellInstance spell, ServerPlayer player, BlockHitResult result) {
                        final ServerLevel level = player.getLevel();
                        MeteoriteFeature.generateCrater(level, level.getChunkSource().getGenerator(), result.getBlockPos(), level.random);
                    }
                })
                        .setSpellElement(FufoMagicElements.DEV)
        );

        public static PlayerSpellHandler allTheDevSpellsPlease() {
            SpellStorage storage = new SpellStorage(9);
            final List<SpellInstance> devSpells = SPELL_TYPES.values().stream().filter(t -> t.id.getPath().startsWith("dev_")).map(t -> t.defaultInstanceSupplier.apply(t)).collect(Collectors.toList());

            for (int i = 0; i < devSpells.size(); i++) {
                storage.spells.set(2 + i * 2, devSpells.get(i));
            }
            return new PlayerSpellHandler(storage);
        }
    }
}