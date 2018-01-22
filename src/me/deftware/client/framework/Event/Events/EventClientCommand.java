package me.deftware.client.framework.Event.Events;

import me.deftware.client.framework.Event.Event;

public class EventClientCommand extends Event {

	private String command, args, full, trigger;

	public EventClientCommand(String command, String trigger) {
		this.full = command;
		this.trigger = trigger;
		if (command.contains(" ")) {
			this.command = command.split(" ")[0];
			this.args = command.replace(this.command + " ", "");
		} else {
			this.args = "";
			this.command = command;
		}
	}

	public String getCommand() {
		return command;
	}

	public String getArgs() {
		return args;
	}

	public String getFull() {
		return full;
	}

}
