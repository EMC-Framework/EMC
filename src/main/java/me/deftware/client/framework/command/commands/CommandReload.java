package me.deftware.client.framework.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import me.deftware.client.framework.command.CommandBuilder;
import me.deftware.client.framework.command.EMCModCommand;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.world.ClientWorld;

/**
 * @author Deftware
 */
public class CommandReload extends EMCModCommand {

    @Override
    public CommandBuilder<?> getCommandBuilder() {
        return new CommandBuilder<>().set(LiteralArgumentBuilder.literal("reload")
                .then(
                        LiteralArgumentBuilder.literal("skins")
                                .executes(c -> {
                                    print("Reloading skins...");
                                    ClientWorld.getClientWorld().getLoadedEntities().forEach(Entity::reloadSkin);
                                    print("Skins reloaded");
                                    return 1;
                                })
                )
        );
    }

}


