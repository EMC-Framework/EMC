package me.deftware.client.framework.command.commands;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import me.deftware.client.framework.command.CommandBuilder;
import me.deftware.client.framework.command.EMCModCommand;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.client.framework.render.batching.RenderStack;

/**
 * @author Deftware
 */
public class CommandScale extends EMCModCommand {

    private void setScale(float scale) {
        RenderStack.setScale(scale);
        Bootstrap.EMCSettings.putPrimitive("RENDER_SCALE", scale);
        print("Scale has been set to '" + scale + "'!");
    }

    @Override
    public CommandBuilder<?> getCommandBuilder() {
        return new CommandBuilder<>().set(LiteralArgumentBuilder.literal("scale")
                .then(
                        LiteralArgumentBuilder.literal("set")
                                .then(
                                        RequiredArgumentBuilder.argument("size", FloatArgumentType.floatArg(0.2f, 4.0f))
                                                .executes(c -> {
                                                    setScale(c.getArgument("size", Float.class));
                                                    return 1;
                                                })
                                )
                )
                .then(
                        LiteralArgumentBuilder.literal("reset")
                                .executes(c -> {
                                    setScale(1.0f);
                                    return 1;
                                })
                )
        );
    }

}
