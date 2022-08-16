package team.lodestar.fufo.common.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import team.lodestar.fufo.common.command.argument.StarfallResultArgumentType;
import team.lodestar.fufo.common.worldevents.starfall.ScheduledStarfallEvent;
import team.lodestar.fufo.common.worldevents.starfall.StarfallActor;
import team.lodestar.fufo.core.data.LangHelpers;
import team.lodestar.fufo.core.setup.content.worldevent.StarfallActors;
import team.lodestar.lodestone.handlers.WorldEventHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class IssueStarfallCommand {
    public IssueStarfallCommand() {
    }

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("starfall")
                .requires(cs -> cs.hasPermission(2))
                .then(Commands.argument("result", new StarfallResultArgumentType())
                        .then(Commands.argument("position", BlockPosArgument.blockPos())
                                .executes(context -> {
                                    CommandSourceStack source = context.getSource();
                                    StarfallActor result = StarfallActors.ACTORS.get(context.getArgument("result", String.class));
                                    ServerLevel level = source.getLevel();
                                    BlockPos pos = BlockPosArgument.getSpawnablePos(context, "position");
                                    WorldEventHandler.addWorldEvent(level, new ScheduledStarfallEvent(result).randomizedStartingCountdown(level).targetPosition(pos).determined());
                                    source.sendSuccess(new TranslatableComponent(LangHelpers.getCommand("starfall_natural_position")), true);
                                    return 1;
                                }))
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(context -> {
                                    CommandSourceStack source = context.getSource();
                                    StarfallActor result = StarfallActors.ACTORS.get(context.getArgument("result", String.class));
                                    ServerLevel level = source.getLevel();
                                    Player target = EntityArgument.getPlayer(context, "target");
                                    WorldEventHandler.addWorldEvent(level, new ScheduledStarfallEvent(result).randomizedStartingCountdown(level).targetEntity(target).determined());
                                    source.sendSuccess(new TranslatableComponent(LangHelpers.getCommand("starfall_natural_target")), true);
                                    return 1;
                                })))

                .then(Commands.argument("result", new StarfallResultArgumentType())
                        .then(Commands.argument("position", BlockPosArgument.blockPos())
                                .then(Commands.argument("countdown", IntegerArgumentType.integer(0))
                                        .executes(context -> {
                                            CommandSourceStack source = context.getSource();
                                            int countdown = IntegerArgumentType.getInteger(context, "countdown");
                                            StarfallActor result = StarfallActors.ACTORS.get(context.getArgument("result", String.class));
                                            ServerLevel level = source.getLevel();
                                            BlockPos pos = BlockPosArgument.getSpawnablePos(context, "position");
                                            WorldEventHandler.addWorldEvent(level, new ScheduledStarfallEvent(result).exactStartingCountdown(countdown).targetExactPosition(pos));
                                            source.sendSuccess(new TranslatableComponent(LangHelpers.getCommand("starfall_artificial_position")), true);
                                            return 1;
                                        })))
                        .then(Commands.argument("target", EntityArgument.player())

                                .then(Commands.argument("countdown", IntegerArgumentType.integer(0))
                                        .executes(context -> {
                                            CommandSourceStack source = context.getSource();
                                            int countdown = IntegerArgumentType.getInteger(context, "countdown");
                                            StarfallActor result = StarfallActors.ACTORS.get(context.getArgument("result", String.class));
                                            ServerLevel level = source.getLevel();
                                            Player target = EntityArgument.getPlayer(context, "target");
                                            WorldEventHandler.addWorldEvent(level, new ScheduledStarfallEvent(result).exactStartingCountdown(countdown).targetEntity(target));
                                            source.sendSuccess(new TranslatableComponent(LangHelpers.getCommand("starfall_artificial_target")), true);
                                            return 1;
                                        }))));
    }
}