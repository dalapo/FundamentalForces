package team.lodestar.fufo.common.magic.spell.effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import team.lodestar.fufo.core.magic.MagicElementType;
import team.lodestar.fufo.core.magic.spell.SpellInstance;

import java.util.function.Supplier;

public class ToggledPotionSpellEffect extends ToggledEffect{
    public final Supplier<MobEffectInstance> effectInstanceSupplier;
    public ToggledPotionSpellEffect(Supplier<MobEffectInstance> effectInstanceSupplier, MagicElementType element) {
        super(element);
        this.effectInstanceSupplier = effectInstanceSupplier;
    }

    @Override
    public void tick(SpellInstance spell, Level level, @Nullable ServerPlayer player) {
    }

    @Override
    public void toggleOn(SpellInstance spell, ServerPlayer player) {
        player.addEffect(effectInstanceSupplier.get());
    }

    @Override
    public void toggleOff(SpellInstance spell, ServerPlayer player) {
        player.removeEffect(effectInstanceSupplier.get().getEffect());
    }
}
