package me.deftware.client.framework.minecraft;

import net.minecraft.network.message.ChatMessageSigner;
import net.minecraft.text.Text;

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

    interface Consumer {

        void apply(ChatMessageSigner signer, String message, Text preview);

    }

}
