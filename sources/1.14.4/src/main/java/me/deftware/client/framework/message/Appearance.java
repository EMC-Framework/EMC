package me.deftware.client.framework.message;

import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.util.Optional;

public interface Appearance {

    int OBFUSCATED = 0b00000001;
    int BOLD = 0b00000010;
    int STRIKETHROUGH = 0b00000100;
    int UNDERLINED = 0b00001000;
    int ITALIC = 0b00010000;

    static Appearance empty() {
        return (Appearance) new Style();
    }

    static Appearance of(FormattingColor color) {
        return of(0, color);
    }

    static Appearance of(int flags) {
        return of(flags, null);
    }

    static Appearance of(int flags, FormattingColor color) {
        Appearance style = empty();
        if (flags > 0) {
            style = style.format(flags);
        }
        if (color != null) {
            style = style.color(color);
        }
        return style;
    }

    default Appearance format(int flags) {
        Style copy = ((Style) this).deepCopy();
        if ((flags & ITALIC) != 0) copy.setItalic(true);
        if ((flags & BOLD) != 0) copy.setBold(true);
        if ((flags & UNDERLINED) != 0) copy.setUnderline(true);
        if ((flags & STRIKETHROUGH) != 0) copy.setStrikethrough(true);
        if ((flags & OBFUSCATED) != 0) copy.setObfuscated(true);
        return (Appearance) copy;
    }

    default Appearance color(FormattingColor color) {
        Style copy = ((Style) this).deepCopy();
        if (color.getCode().isPresent()) {
            copy.setColor(Formatting.byCode(color.getCode().get()));
        } else {
            ((Appearance) copy).setFormatting(color);
        }
        return (Appearance) copy;
    }

    default Appearance withClickEvent(ClickAction action, String value) {
        if (action == ClickAction.COPY_TO_CLIPBOARD) {
            return this; // Not supported in <= 1.14.4
        }
        Style copy = ((Style) this).deepCopy();
        ClickEvent event = new ClickEvent(action.getAction(), value);
        copy.setClickEvent(event);
        return (Appearance) copy;
    }

    default Appearance withTextHoverEvent(Message text) {
        Style copy = ((Style) this).deepCopy();
        HoverEvent event = new HoverEvent(HoverEvent.Action.SHOW_TEXT, (Text) text);
        copy.setHoverEvent(event);
        return (Appearance) copy;
    }

    boolean isItalic();

    boolean isBold();

    boolean isUnderlined();

    boolean isStrikethrough();

    boolean isObfuscated();

    Optional<FormattingColor> getFormatting();

    void setFormatting(FormattingColor color);

    interface FormattingColor {

        /**
         * @return Name of the color
         */
        String getName();

        /**
         * @return Legacy color code
         */
        Optional<Character> getCode();

        /**
         * @return Rgb value of the color
         */
        int getColor();

        static FormattingColor ofRGB(int rgb) {
            return new FormattingColor() {
                @Override
                public String getName() {
                    return "RGB";
                }

                @Override
                public Optional<Character> getCode() {
                    return Optional.empty();
                }

                @Override
                public int getColor() {
                    return rgb;
                }
            };
        }

    }

    enum ClickAction {

        OPEN_URL(ClickEvent.Action.OPEN_URL),
        OPEN_FILE(ClickEvent.Action.OPEN_FILE),
        RUN_COMMAND(ClickEvent.Action.RUN_COMMAND),
        SUGGEST_COMMAND(ClickEvent.Action.SUGGEST_COMMAND),
        CHANGE_PAGE(ClickEvent.Action.CHANGE_PAGE),
        COPY_TO_CLIPBOARD(null);

        private final ClickEvent.Action action;

        ClickAction(ClickEvent.Action action) {
            this.action = action;
        }

        public ClickEvent.Action getAction() {
            return action;
        }

    }

}
