package me.deftware.client.framework.message;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MessageUtils {

    public static final String FORMATTING = "lmnork";
    public static final String SECTION_SIGN = String.valueOf((char) 167);

    public static final String COLORS = Arrays.stream(DefaultColors.values())
            .map(e -> e.getCode().get().toString())
            .collect(Collectors.joining());

    public static Message parse(String text) {
        return parse(text, SECTION_SIGN);
    }

    /**
     * Parses a string with Minecraft formatting codes into a Message object.
     */
    public static Message parse(String text, String formattingChar) {
        if (!text.contains(formattingChar)) {
            return Message.of(text);
        }
        int formatting = 0;
        Message.Builder builder = new Message.Builder();
        Appearance.FormattingColor color = null;

        String pattern = "(" + formattingChar + "[" + FORMATTING + COLORS + "])";
        String[] sections = text.split("(?<=" + pattern + ")|(?=" + pattern + ")");
        for (String section : sections) {
            if (section.matches(pattern)) {
                char code = section.charAt(1);
                if (code == 'r') {
                    formatting = 0;
                    color = null;
                } else if (code >= 'k' && code <= 'o')  {
                    int index = code - 'k';
                    formatting |= 1 << index;
                } else {
                    color = DefaultColors.CODE_TO_RGB.get(code);
                }
                continue;
            }
            Appearance styled = Appearance.empty();
            if (color != null) styled = styled.color(color);
            if (formatting > 0) styled = styled.format(formatting);
            builder.append(section, styled);
        }

        return builder.build();
    }

}
