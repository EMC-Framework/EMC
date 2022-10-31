package me.deftware.client.framework.minecraft;

import me.deftware.client.framework.chat.LiteralChatMessage;
import me.deftware.client.framework.chat.style.ChatColors;
import me.deftware.client.framework.command.CommandRegister;
import me.deftware.client.framework.event.events.EventChatSend;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

public interface Chat {

    void message(String text, Class<?> sender);

    void command(String text, Class<?> sender);

    default void send(String text, Class<?> sender) {
        if (text.startsWith("/")) {
            this.command(text.substring(1), sender);
        } else {
            this.message(text, sender);
        }
    }

    @ApiStatus.Internal
    static void send(Consumer<String> consumer, String text, Class<?> sender, EventChatSend.Type type) {
        EventChatSend event = new EventChatSend(text, sender, type).broadcast();
        if (!event.isCanceled()) {
            // Client command hook
            String trigger = CommandRegister.getCommandTrigger();
            if (text.startsWith(trigger)) {
                text = text.substring(trigger.length());
                try {
                    CommandRegister.getDispatcher().execute(text, MinecraftClient.getInstance().player.getCommandSource());
                } catch (Exception ex) {
                    new LiteralChatMessage(ex.getMessage(), ChatColors.RED).print();
                }
                return;
            }
            // Update text and preview if the message has changed
            if (!event.getMessage().equalsIgnoreCase(text)) {
                text = event.getMessage();
            }
            consumer.accept(text);
        }
    }

}
