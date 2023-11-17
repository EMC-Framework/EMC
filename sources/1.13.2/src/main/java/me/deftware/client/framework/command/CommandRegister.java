package me.deftware.client.framework.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.command.ISuggestionProvider;
import me.deftware.client.framework.main.bootstrap.Bootstrap;

import java.util.ArrayList;
import java.util.Map;

/**
 * Handles custom EMC commands
 */
public class CommandRegister {

    private static CommandDispatcher<ISuggestionProvider> dispatcher = new CommandDispatcher<>();

    /**
     * @return Brigadier dispatcher object
     */
    public static CommandDispatcher<ISuggestionProvider> getDispatcher() {
        return dispatcher;
    }

    /**
     * Clears the Brigadier dispatcher object
     * (restores to the dafault state - no commands loaded)
     */
    public static void clearDispatcher() {
        dispatcher = new CommandDispatcher<>();
    }

    /**
     * Registers a commandbuilder
     *
     * @param command
     */
    public static synchronized void registerCommand(CommandBuilder<?> command) {
        CommandNode<ISuggestionProvider> node = dispatcher.register(command.build());
        for (Object alias : command.getAliases()) {
            LiteralArgumentBuilder<ISuggestionProvider> argumentBuilder = LiteralArgumentBuilder.literal((String) alias);
            dispatcher.register(argumentBuilder.redirect(node));
        }
    }

    /**
     * Registers a EMCModCommand
     *
     * @param modCommand
     */
    public static void registerCommand(EMCModCommand modCommand) {
        registerCommand(modCommand.getCommandBuilder());
    }

    /**
     * Returns an array of all registered commands, without any argument usage
     *
     * @return
     */
    public static ArrayList<String> listCommands() {
        ArrayList<String> commands = new ArrayList<>();
        RootCommandNode<?> rootNode = dispatcher.getRoot();
        for (CommandNode<?> child : rootNode.getChildren()) {
            commands.add(child.getName());
        }
        return commands;
    }

    /**
     * Returns an array of all registered commands, with argument usage
     *
     * @return
     */
    public static ArrayList<String> getCommandsAndUsage() {
        Map<CommandNode<ISuggestionProvider>, String> map = getSmartUsage();
        return new ArrayList<>(map.values());
    }

    /**
     * Returns a map of all root commands with their correct usage
     *
     * @return
     */
    public static Map<CommandNode<ISuggestionProvider>, String> getSmartUsage() {
        return dispatcher.getSmartUsage(dispatcher.getRoot(), net.minecraft.client.Minecraft.getInstance().player.connection.getSuggestionProvider());
    }

    /**
     * Returns the command trigger used to trigger commands, default is a .
     *
     * @return
     */
    public static String getCommandTrigger() {
        return Bootstrap.EMCSettings.getPrimitive("commandtrigger", ".");
    }

}
