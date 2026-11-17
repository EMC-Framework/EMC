package me.deftware.client.framework.message;

import me.deftware.client.framework.minecraft.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.Optional;
import java.util.function.BiFunction;

/**
 * @author Deftware
 */
public interface Message extends com.mojang.brigadier.Message {

    Message SPACE = (Message) (Object) Component.literal(" ");

    Message EMPTY = (Message) (Object) Component.literal("");

    String CHEVRON = String.valueOf((char) 187);

    /**
     * Visits each component of this message, and their respective style.
     */
    default Optional<Message> visit(BiFunction<Appearance, String, Optional<Message>> visitor) {
        return visit(((Component) this), Style.EMPTY, visitor);
    }

    /**
     * Appends a message to this message
     *
     * @param text The message to append
     */
    default Message append(Message text) {
        if (((Object) this) instanceof MutableComponent message) {
            message.append((Component) text);
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
        if (((Object) this) instanceof MutableComponent message) {
            message.setStyle((Style) (Object) style);
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
        return (Message) (Object) ((Component) this).copy();
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
        return ((Object) this) instanceof MutableComponent;
    }

    /**
     * Creates a message from a string
     *
     * @param text The text to create the message from
     * @return The message
     */
    static Message of(String text) {
        return (Message) (Object) Component.literal(text);
    }

    /**
     * Creates a message from a translation key
     *
     * @param key The minecraft translation key
     * @return The translated message
     */
    static Message translated(String key, Object... args) {
        return (Message) (Object) Component.translatable(key, args);
    }

    /**
     * Prints the message to the in-game chat
     */
    default void print() {
        Minecraft.getMinecraftGame().getGameChat().append(this);
    }

    default boolean isTranslatable() {
        return ((Component) this).getContents() instanceof TranslatableContents;
    }

    default String getTranslationKey() {
        if (!isTranslatable()) {
            throw new IllegalStateException("Message is not translatable");
        }
        return ((TranslatableContents) ((Component) this).getContents()).getKey();
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
            this(Message.of(PlainTextContents.EMPTY.text()).style(Appearance.of(DefaultColors.WHITE)));
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

    private static Optional<Message> visit(Component ref, Style superStyle, BiFunction<Appearance, String, Optional<Message>> visitor) {
        var style = ref.getStyle().applyTo(superStyle);
        Optional<Message> optional;
        if (ref.getContents() instanceof PlainTextContents content) {
            // Ensure we visit empty strings to stay consistent with older versions
            optional = visitor.apply((Appearance) (Object) style, content.text());
        } else {
            optional = ref.getContents().visit((s, text) -> visitor.apply((Appearance) (Object) s, text), style);
        }
        if (optional.isPresent()) {
            return optional;
        }
        // Siblings
        for (var sibling : ref.getSiblings()) {
            var result = visit(sibling, style, visitor);
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }

}
