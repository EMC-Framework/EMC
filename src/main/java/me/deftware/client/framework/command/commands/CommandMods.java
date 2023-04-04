package me.deftware.client.framework.command.commands;

import me.deftware.client.framework.command.CommandBuilder;
import me.deftware.client.framework.command.EMCModCommand;
import me.deftware.client.framework.main.bootstrap.Bootstrap;

/**
 * @author Deftware
 */
public class CommandMods extends EMCModCommand {

	@Override
	public CommandBuilder<?> getCommandBuilder() {
		return new CommandBuilder<>().addCommand("mods", result -> {
			if (Bootstrap.modsInfo == null || Bootstrap.getMods().isEmpty()) {
				print("No EMC mods are loaded");
				return;
			}
			print(":: Loaded modules ::");
			Bootstrap.getMods().values().forEach(mod -> {
				String text = String.format(
						"%s by %s version %s",
						mod.getMeta().getName(),
						mod.getMeta().getAuthor(),
						mod.getMeta().getVersion()
				);
				print(text);
			});
		});
	}

}
