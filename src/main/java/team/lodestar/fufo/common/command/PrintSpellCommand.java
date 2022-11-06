package team.lodestar.fufo.common.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import team.lodestar.fufo.common.capability.FufoPlayerDataCapability;
import team.lodestar.fufo.core.magic.spell.SpellInstance;

public class PrintSpellCommand {
    public PrintSpellCommand() {
    }

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("printspell")
                .requires(cs -> cs.hasPermission(2))
                .then(Commands.argument("target", EntityArgument.player())
                        .then(Commands.argument("slot", IntegerArgumentType.integer(1, 9))
                                .executes(context -> {
                                    CommandSourceStack source = context.getSource();
                                    Player target = EntityArgument.getPlayer(context, "target");
                                    FufoPlayerDataCapability.getCapabilityOptional(target).ifPresent(p -> {
                                        SpellInstance instance = p.hotbarHandler.spellStorage.getSpell(context.getArgument("slot", Integer.class) - 1);
                                        source.sendSuccess(Component.literal(instance.serializeNBT().toString()), true);
                                    });
                                    return 1;
                                })))
                .then(Commands.argument("target", EntityArgument.player())
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            Player target = EntityArgument.getPlayer(context, "target");
                            FufoPlayerDataCapability.getCapabilityOptional(target).ifPresent(p -> {
                                SpellInstance instance = p.hotbarHandler.spellStorage.getSelectedSpell(target);
                                source.sendSuccess(Component.literal(instance.serializeNBT().toString()), true);
                            });
                            return 1;
                        }));
    }
}