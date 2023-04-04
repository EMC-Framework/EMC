package me.deftware.client.framework.command.types;

import com.google.common.collect.Lists;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import me.deftware.client.framework.command.CommandBuilder;
import me.deftware.client.framework.command.CommandRegister;
import me.deftware.client.framework.command.CommandResult;
import me.deftware.client.framework.command.EMCModCommand;
import me.deftware.client.framework.message.Appearance;
import me.deftware.client.framework.message.DefaultColors;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.minecraft.Minecraft;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Deftware
 */
public abstract class AbstractPagedOutputCommand extends EMCModCommand {

    protected final Message title;
    protected final String command;
    protected int chunkSize = 6;

    public AbstractPagedOutputCommand(String command, Message title) {
        this.command = command;
        this.title = title;
    }

    public abstract List<Message> list();

    public List<List<Message>> getChunks() {
        return Lists.partition(list(), chunkSize);
    }

    @Override
    public CommandBuilder<?> getCommandBuilder() {
        return new CommandBuilder<>().set(LiteralArgumentBuilder.literal(command)
                .then(
                        RequiredArgumentBuilder.argument("page", IntegerArgumentType.integer(1))
                                .executes(c ->
                                        onExecute(new CommandResult(c).getInteger("page") - 1)
                                )
                )
                .executes(c -> onExecute(0))
        );
    }

    private final List<String> previousOuput = new ArrayList<>();

    protected void removePreviousOutput() {
        Minecraft.getMinecraftGame().getGameChat().remove(previousOuput::contains);
        previousOuput.clear();
    }

    protected void send(Message message) {
        previousOuput.add(message.string());
        message.print();
    }

    protected int onExecute(int page) {
        String prefix = CommandRegister.getCommandTrigger();

        // Remove old output
        if (!previousOuput.isEmpty()) {
            removePreviousOutput();
        }

        if (list().isEmpty()) {
            send(title);
            send(Message.of("Nothing added").style(Appearance.of(DefaultColors.GRAY)));
        } else {
            // Get chunk
            List<List<Message>> chunks = getChunks();
            if (page < 0) page = 0;
            if (page >= chunks.size()) page = chunks.size() - 1;
            List<Message> chunk = chunks.get(page);

            // Send title
            var aqua = Appearance.of(DefaultColors.AQUA);
            send(
                    new Message.Builder()
                            .append(title)
                            .append(" ")
                            .append(String.format("Page (%s/%s)", page + 1, chunks.size()), aqua)
                            .build()
            );

            // Send chunk
            chunk.forEach(this::send);

            if (chunks.size() > 1) {
                var navigation = new Message.Builder();
                if (page > 0) {
                    var left = Message.of("<< ")
                            .style(
                                    Appearance.of(DefaultColors.AQUA)
                                            .withClickEvent(Appearance.ClickAction.RUN_COMMAND, String.format("%s%s %s", prefix, command, page))
                                            .withTextHoverEvent(Message.of("Previous page"))
                            );
                    navigation.append(left);
                }
                if (page + 1 < chunks.size()) {
                    var right = Message.of(">>")
                            .style(
                                    Appearance.of(DefaultColors.AQUA)
                                            .withClickEvent(Appearance.ClickAction.RUN_COMMAND, String.format("%s%s %s", prefix, command, page + 2))
                                            .withTextHoverEvent(Message.of("Next page"))
                            );
                    navigation.append(right);
                }
                send(navigation.build());
            }
        }

        return 1;
    }

}
