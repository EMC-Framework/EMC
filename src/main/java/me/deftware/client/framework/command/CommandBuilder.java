package me.deftware.client.framework.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Wrapper for building EMC commands
 */
public class CommandBuilder<T> {

    private LiteralArgumentBuilder<SuggestionProvider<?>> builder;
    private List<String> aliases = new ArrayList<>();

    /**
     * Adds a single command (.test for example) with no argument, recommended for simple
     * no argument commands
     *
     * @param command
     * @param execution
     * @return CommandBuilder
     */
    public CommandBuilder<?> addCommand(String command, Consumer<CommandResult> execution) {
        return set(LiteralArgumentBuilder.literal(command).executes(source -> {
            execution.accept(new CommandResult(source));
            return 1;
        }));
    }

    /**
     * Set's the LiteralArgumentBuilder of this commandbuilder, used for building your own command tree
     *
     * @param argument
     * @return
     */
    public CommandBuilder<?> set(LiteralArgumentBuilder<?> argument) {
        builder = (LiteralArgumentBuilder<SuggestionProvider<?>>) argument;
        return this;
    }

    /**
     * Appends a literal or an argument to the existing command tree
     *
     * @param argument
     * @return
     */
    public CommandBuilder<?> append(LiteralArgumentBuilder<SuggestionProvider<?>> argument) {
        if (builder == null) {
            builder = argument;
        } else {
            builder.then(argument);
        }
        return this;
    }

    /**
     * Registers an alternate alias for the command
     *
     * @param alias
     * @return
     */
    public CommandBuilder<?> registerAlias(String alias) {
        aliases.add(alias);
        return this;
    }

    /**
     * Returns a list of all aliases for this command
     *
     * @return
     */
    public List<String> getAliases() {
        return aliases;
    }

    protected LiteralArgumentBuilder<SuggestionProvider<?>> build() {
        return builder;
    }

    @FunctionalInterface
    public interface CommandExecution {

        void onExecute(CommandResult result);

    }

}
