package team.lodestar.fufo.common.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import team.lodestar.fufo.common.capability.FufoPlayerDataCapability;
import team.lodestar.fufo.common.command.argument.SpellTypeArgumentType;
import team.lodestar.fufo.core.magic.spell.SpellInstance;
import team.lodestar.fufo.core.magic.spell.SpellType;
import team.lodestar.fufo.registry.common.magic.FufoSpellTypes;
import team.lodestar.fufo.unsorted.LangHelpers;

public class SetSpellCommand {
    public SetSpellCommand() {
    }

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("setspell")
                .requires(cs -> cs.hasPermission(2))
                .then(Commands.argument("type", new SpellTypeArgumentType())
                        .then(Commands.argument("target", EntityArgument.player())
                                .then(Commands.argument("slot", IntegerArgumentType.integer(1, 9))
                                        .executes(context -> {
                                            CommandSourceStack source = context.getSource();
                                            Player target = EntityArgument.getPlayer(context, "target");
                                            FufoPlayerDataCapability.getCapabilityOptional(target).ifPresent(p -> {
                                                SpellType result = FufoSpellTypes.SPELL_TYPES.get(context.getArgument("type", ResourceLocation.class));
                                                int slot = context.getArgument("slot", Integer.class)-1;
                                                SpellInstance instance = result.defaultInstanceSupplier.apply(result);
                                                p.hotbarHandler.spellStorage.spells.set(slot, instance);
                                                FufoPlayerDataCapability.syncTrackingAndSelf(target);
                                                source.sendSuccess(Component.translatable(LangHelpers.getCommand("set_spell_success")), true);
                                            });
                                            return 1;
                                        }))));
    }
}