package me.deftware.client.framework.command.commands;

import me.deftware.client.framework.command.CommandRegister;
import me.deftware.client.framework.command.types.AbstractPagedOutputCommand;
import me.deftware.client.framework.message.Appearance;
import me.deftware.client.framework.message.DefaultColors;
import me.deftware.client.framework.message.Message;

import java.util.List;
import java.util.stream.Collectors;

public class CommandHelp extends AbstractPagedOutputCommand {

    public CommandHelp() {
        super("help", Message.of("Client Commands").style(Appearance.of(DefaultColors.GREEN)));
    }

    @Override
    public List<Message> list() {
        return CommandRegister.getCommandsAndUsage()
                .stream()
                .map(s -> Message.of(CommandRegister.getCommandTrigger() + s).style(Appearance.of(DefaultColors.GRAY)))
                .collect(Collectors.toList());
    }

}
