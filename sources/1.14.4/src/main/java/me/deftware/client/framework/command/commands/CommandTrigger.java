package me.deftware.client.framework.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import me.deftware.client.framework.command.CommandBuilder;
import me.deftware.client.framework.command.EMCModCommand;
import me.deftware.client.framework.main.bootstrap.Bootstrap;

/**
 * @author Deftware
 */
public class CommandTrigger extends EMCModCommand {

    @Override
    public CommandBuilder<?> getCommandBuilder() {
        return new CommandBuilder<>().set(LiteralArgumentBuilder.literal("trigger")
                .then(
                        LiteralArgumentBuilder.literal("set")
                                .then(
                                        RequiredArgumentBuilder.argument("prefix", StringArgumentType.string())
                                                .executes(c -> {
                                                    String prefix = StringArgumentType.getString(c, "prefix");
                                                    if (prefix.isEmpty()) {
                                                        print("Prefix cannot be empty");
                                                        return 1;
                                                    }
                                                    Bootstrap.EMCSettings.putPrimitive("commandtrigger", prefix);
                                                    print("Command trigger has been set to \"" + prefix + "\"");
                                                    return 1;
                                                })
                                )
                )
                .then(
                        LiteralArgumentBuilder.literal("restore")
                                .executes(c -> {
                                    Bootstrap.EMCSettings.putPrimitive("commandtrigger", ".");
                                    print("Restored command trigger to \".\" (single dot)");
                                    return 1;
                                })
                )
        );
    }

}
