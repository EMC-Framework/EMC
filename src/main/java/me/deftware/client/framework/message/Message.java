package me.deftware.client.framework.message;

import me.deftware.client.framework.minecraft.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.Optional;
import java.util.function.BiFunction;

/**
 * @author Deftware
 */
public interface Message extends com.mojang.brigadier.Message {

    Message SPACE = (Message) new TextComponentString(" ");

    Message EMPTY = (Message) new TextComponentString("");

    String CHEVRON = String.valueOf((char) 187);

    /**
     * Visits each component of this message, and their respective style.
     */
    default Optional<Message> visit(BiFunction<Appearance, String, Optional<Message>> visitor) {
        for (ITextComponent text : (ITextComponent) this) {
            Optional<Message> result = visitor.apply((Appearance) text.getStyle(), text.getUnformattedComponentText());
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }

    /**
     * Appends a message to this message
     *
     * @param text The message to append
     */
    default Message append(Message text) {
        ((ITextComponent) this).appendSibling((ITextComponent) text);
        return this;
    }

    /**
     * Sets the style of this message
     *
     * @param style The style to set
     */
    default Message style(Appearance style) {
        ((ITextComponent) this).setStyle((Style) style);
        return this;
    }

    /**
     * Visits each component of this message, and
     * allows for mutation of the component.
     *
     * @param visitor Visitor function to apply
     * @return The mutated message
     */
    default Message mutate(BiFunction<Appearance, String, Optional<Message>> visitor) {
        Builder builder = new Builder(null);
        visit((style, text) -> {
            Optional<Message> result = visitor.apply(style, text);
            result.ifPresent(builder::append);
            return Optional.empty();
        });
        return builder.build();
    }

    /**
     * @return A copy of the message
     */
    default Message copy() {
        return (Message) ((ITextComponent) this).createCopy();
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
        return true;
    }

    /**
     * Creates a message from a string
     *
     * @param text The text to create the message from
     * @return The message
     */
    static Message of(String text) {
        return (Message) new TextComponentString(text);
    }

    /**
     * Creates a message from a translation key
     *
     * @param key The minecraft translation key
     * @return The translated message
     */
    static Message translated(String key, Object... args) {
        return (Message) new TextComponentTranslation(key, args);
    }

    /**
     * Prints the message to the in-game chat
     */
    default void print() {
        Minecraft.getMinecraftGame().getGameChat().append(this);
    }

    default boolean isTranslatable() {
        return this instanceof TextComponentTranslation;
    }

    default String getTranslationKey() {
        if (!isTranslatable()) {
            throw new IllegalStateException("Message is not translatable");
        }
        return ((TextComponentTranslation) this).getKey();
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
            Message message = Message.of(text);
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
