package me.deftware.client.framework.command;

import me.deftware.client.framework.message.Appearance;
import me.deftware.client.framework.message.DefaultColors;
import me.deftware.client.framework.message.Message;

/**
 * @author Deftware
 */
public abstract class EMCModCommand {

	public abstract CommandBuilder<?> getCommandBuilder();

	public static void print(Message message) {
		new Message.Builder()
				.append("EMC ", Appearance.of(Appearance.BOLD, DefaultColors.AQUA))
				.append(Message.CHEVRON + " ", Appearance.of(Appearance.BOLD, DefaultColors.GRAY))
				.append(message)
				.build()
				.print();
	}

	public static void print(String text) {
		print(Message.of(text));
	}

	public static void error(String text) {
		print(Message.of(text).style(Appearance.of(Appearance.BOLD, DefaultColors.RED)));
	}

}
