package me.deftware.client.framework.message;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface Appearance {

    int OBFUSCATED = 0b00000001;
    int BOLD = 0b00000010;
    int STRIKETHROUGH = 0b00000100;
    int UNDERLINED = 0b00001000;
    int ITALIC = 0b00010000;

    static Appearance empty() {
        return (Appearance) Style.EMPTY;
    }

    static Appearance of(FormattingColor color) {
        return of(0, color);
    }

    static Appearance of(int flags) {
        return of(flags, null);
    }

    static Appearance of(int flags, FormattingColor color) {
        var style = (Appearance) Style.EMPTY;
        if (flags > 0) {
            style = style.format(flags);
        }
        if (color != null) {
            style = style.color(color);
        }
        return style;
    }

    default Appearance format(int flags) {
        List<Formatting> options = new ArrayList<>();
        if ((flags & ITALIC) != 0) options.add(Formatting.ITALIC);
        if ((flags & BOLD) != 0) options.add(Formatting.BOLD);
        if ((flags & UNDERLINED) != 0) options.add(Formatting.UNDERLINE);
        if ((flags & STRIKETHROUGH) != 0) options.add(Formatting.STRIKETHROUGH);
        if ((flags & OBFUSCATED) != 0) options.add(Formatting.OBFUSCATED);
        return (Appearance) ((Style) this).withFormatting(options.toArray(new Formatting[0]));
    }

    default Appearance color(FormattingColor color) {
        return (Appearance) ((Style) this).withColor(color.getColor());
    }

    default Appearance withClickEvent(ClickAction action, String value) {
        var event = new ClickEvent(action.getAction(), value);
        return (Appearance) ((Style) this).withClickEvent(event);
    }

    default Appearance withTextHoverEvent(Message text) {
        var event = new HoverEvent(HoverEvent.Action.SHOW_TEXT, (Text) text);
        return (Appearance) ((Style) this).withHoverEvent(event);
    }

    boolean isItalic();

    boolean isBold();

    boolean isUnderlined();

    boolean isStrikethrough();

    boolean isObfuscated();

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
        COPY_TO_CLIPBOARD(ClickEvent.Action.COPY_TO_CLIPBOARD);

        private final ClickEvent.Action action;

        ClickAction(ClickEvent.Action action) {
            this.action = action;
        }

        public ClickEvent.Action getAction() {
            return action;
        }

    }

}
