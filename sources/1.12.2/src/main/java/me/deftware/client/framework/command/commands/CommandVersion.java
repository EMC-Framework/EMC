package me.deftware.client.framework.command.commands;

import me.deftware.client.framework.FrameworkConstants;

import me.deftware.client.framework.command.CommandBuilder;
import me.deftware.client.framework.command.EMCModCommand;
import me.deftware.client.framework.minecraft.Minecraft;

/**
 * @author Deftware
 */
public class CommandVersion extends EMCModCommand {

	@Override
	public CommandBuilder<?> getCommandBuilder() {
		return new CommandBuilder<>().addCommand("version", result -> {
			print(":: EMC info ::");
			print(FrameworkConstants.toDataString());
			print(String.format(
					"Minecraft version %s protocol %s",
					Minecraft.getMinecraftVersion(),
					Minecraft.getMinecraftProtocolVersion()
			));
			print("EMC mappings is " + FrameworkConstants.MAPPING_LOADER.name());
			print("EMC mapper is " + FrameworkConstants.MAPPING_SYSTEM.name());
		});
	}

}
