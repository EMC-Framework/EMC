package me.deftware.client.framework.message;

import me.deftware.client.framework.minecraft.Minecraft;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

import java.util.Optional;
import java.util.function.BiFunction;

/**
 * @author Deftware
 */
public interface Message extends com.mojang.brigadier.Message {

    Message SPACE = (Message) Text.literal(" ");

    Message EMPTY = (Message) Text.literal("");

    String CHEVRON = String.valueOf((char) 187);

    /**
     * Visits each component of this message, and their respective style.
     */
    default Optional<Message> visit(BiFunction<Appearance, String, Optional<Message>> visitor) {
        return ((Text) this).visit((style, text) -> visitor.apply((Appearance) style, text), Style.EMPTY);
    }

    /**
     * Appends a message to this message
     *
     * @param text The message to append
     */
    default Message append(Message text) {
        if (this instanceof MutableText message) {
            message.append((Text) text);
            return this;
        }
        throw new UnsupportedOperationException("Cannot append to immutable message");
    }

    /**
     * Sets the style of this message
     *
     * @param style The style to set
     */
    default Message style(Appearance style) {
        if (this instanceof MutableText message) {
            message.setStyle((Style) style);
            return this;
        }
        throw new UnsupportedOperationException("Cannot style immutable message");
    }

    /**
     * Visits each component of this message, and
     * allows for mutation of the component.
     *
     * @param visitor Visitor function to apply
     * @return The mutated message
     */
    default Message mutate(BiFunction<Appearance, String, Optional<Message>> visitor) {
        var builder = new Builder(null);
        visit((style, text) -> {
            var result = visitor.apply(style, text);
            result.ifPresent(builder::append);
            return Optional.empty();
        });
        return builder.build();
    }

    /**
     * @return A copy of the message
     */
    default Message copy() {
        return (Message) ((Text) this).copy();
    }

    /**
     * @return This message as a string
     */
    default String string() {
        return this.getString();
    }

    /**
     * @return Whether this message is mutable
     */
    default boolean isMutable() {
        return this instanceof MutableText;
    }

    /**
     * Creates a message from a string
     *
     * @param text The text to create the message from
     * @return The message
     */
    static Message of(String text) {
        return (Message) Text.literal(text);
    }

    /**
     * Creates a message from a translation key
     *
     * @param key The minecraft translation key
     * @return The translated message
     */
    static Message translated(String key, Object... args) {
        return (Message) Text.translatable(key, args);
    }

    /**
     * Prints the message to the in-game chat
     */
    default void print() {
        Minecraft.getMinecraftGame().getGameChat().append(this);
    }

    default boolean isTranslatable() {
        return ((Text) this).getContent() instanceof TranslatableTextContent;
    }

    default String getTranslationKey() {
        if (!isTranslatable()) {
            throw new IllegalStateException("Message is not translatable");
        }
        return ((TranslatableTextContent) ((Text) this).getContent()).getKey();
    }

    /**
     * Builder class for messages
     */
    class Builder {

        private Message message;

        public Builder(Message message) {
            this.message = message;
        }

        public Builder() {
            // Style is inherited from the first message, so we need to add a dummy message
            // to inherit the style from later on.
            this(Message.of("").style(Appearance.of(DefaultColors.WHITE)));
        }

        public Builder append(Message message) {
            if (this.message == null) {
                this.message = message;
            } else {
                this.message.append(message);
            }
            return this;
        }

        public Builder append(Message message, Appearance style) {
            message.style(style);
            return append(message);
        }

        public Builder append(String text, Appearance style) {
            var message = Message.of(text);
            message.style(style);
            return append(message);
        }

        public Builder append(String text) {
            return append(Message.of(text));
        }

        public Message build() {
            if (message == null) {
                throw new IllegalStateException("Cannot build empty message");
            }
            return message;
        }

    }

}
