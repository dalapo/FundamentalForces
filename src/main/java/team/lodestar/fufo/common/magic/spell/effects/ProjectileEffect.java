package team.lodestar.fufo.common.magic.spell.effects;

import team.lodestar.fufo.common.entity.magic.spell.AbstractSpellProjectile;
import team.lodestar.fufo.common.magic.spell.datas.LifespanAttribute;
import team.lodestar.fufo.common.magic.spell.datas.DamageAttribute;
import team.lodestar.fufo.core.magic.MagicElementType;
import team.lodestar.fufo.core.magic.spell.SpellEffect;
import team.lodestar.fufo.core.magic.spell.SpellInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import team.lodestar.fufo.registry.common.magic.FufoSpellDataKeys;

import java.awt.*;
import java.util.function.Function;

public class ProjectileEffect extends SpellEffect {
    public Function<Level, AbstractSpellProjectile> projectileSupplier;
    public Color firstColor = new Color(16777215);
    public Color secondColor = new Color(16777215);

    public ProjectileEffect(Function<Level, AbstractSpellProjectile> projectileSupplier, MagicElementType element){
        super(CastLogicHandler.ALWAYS_DEFAULT_CAST, element);
        this.projectileSupplier = projectileSupplier;
    }
    @Override
    public void effect(SpellInstance spell, ServerPlayer player) {
        int lifespan = spell.attributes.getMandatorySpellAttribute(FufoSpellDataKeys.LIFESPAN_KEY).lifespan;
        float velocity = spell.attributes.getMandatorySpellAttribute(FufoSpellDataKeys.VELOCITY_KEY).velocity;
        AbstractSpellProjectile projectile = projectileSupplier.apply(player.level)
                .setElement(element)
                .setColor(firstColor, secondColor)
                .setLifetime(lifespan);

        projectile.setOwner(player);
        projectile.setPos(player.getEyePosition());
        projectile.fireImmune();
        projectile.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, velocity, 0);
        player.level.addFreshEntity(projectile);
        player.swing(InteractionHand.MAIN_HAND, true);
    }

    @Override
    public void effect(SpellInstance spell, ServerPlayer player, BlockHitResult result) {}
}
