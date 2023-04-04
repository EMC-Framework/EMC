package me.deftware.client.framework.message;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum DefaultColors implements Appearance.FormattingColor {

    BLACK("BLACK", '0', 0),
    DARK_BLUE("DARK_BLUE", '1', 170),
    DARK_GREEN("DARK_GREEN", '2', 43520),
    DARK_AQUA("DARK_AQUA", '3', 43690),
    DARK_RED("DARK_RED", '4', 11141120),
    DARK_PURPLE("DARK_PURPLE", '5', 11141290),
    GOLD("GOLD", '6', 16755200),
    GRAY("GRAY", '7', 11184810),
    DARK_GRAY("DARK_GRAY", '8', 5592405),
    BLUE("BLUE", '9', 5592575),
    GREEN("GREEN", 'a', 5635925),
    AQUA("AQUA", 'b', 5636095),
    RED("RED", 'c', 16733525),
    LIGHT_PURPLE("LIGHT_PURPLE", 'd', 16733695),
    YELLOW("YELLOW", 'e', 16777045),
    WHITE("WHITE", 'f', 16777215);

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static final Map<Character, Appearance.FormattingColor> CODE_TO_RGB = Arrays.stream(values()).collect(
            Collectors.toMap(v -> v.getCode().get(), Function.identity())
    );

    private final String name;
    private final char code;
    private final int rgb;

    DefaultColors(String name, char code, int rgb) {
        this.name = name;
        this.code = code;
        this.rgb = rgb;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<Character> getCode() {
        return Optional.of(code);
    }

    @Override
    public int getColor() {
        return rgb;
    }

}
