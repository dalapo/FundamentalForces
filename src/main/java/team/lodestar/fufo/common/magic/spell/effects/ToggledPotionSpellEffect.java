package team.lodestar.fufo.common.magic.spell.effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import team.lodestar.fufo.core.magic.MagicElementType;
import team.lodestar.fufo.core.magic.spell.SpellInstance;

import java.util.function.Supplier;

public class ToggledPotionSpellEffect extends ToggledEffect{
    public final Supplier<MobEffect> effectSupplier;
    public ToggledPotionSpellEffect(Supplier<MobEffect> effectSupplier) {
        super();
        this.effectSupplier = effectSupplier;
    }

    @Override
    public void toggleOn(SpellInstance spell, ServerPlayer player) {
        MobEffectInstance pEffectInstance = new MobEffectInstance(effectSupplier.get(), 100000, 1);
        pEffectInstance.setNoCounter(true);
        player.addEffect(pEffectInstance);
    }

    @Override
    public void toggleOff(SpellInstance spell, ServerPlayer player) {
        player.removeEffect(effectSupplier.get());
    }
}
