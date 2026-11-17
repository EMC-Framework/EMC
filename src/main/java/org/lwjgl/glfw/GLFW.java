package org.lwjgl.glfw;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import org.lwjgl.sdl.SDLMouse;

import java.nio.DoubleBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Compatibility and SDL3-to-GLFW input translation shim for Minecraft 26.3.
 */
public class GLFW {

    public static final int GLFW_TRUE = 1;
    public static final int GLFW_FALSE = 0;

    public static final int GLFW_RELEASE = 0;
    public static final int GLFW_PRESS = 1;
    public static final int GLFW_REPEAT = 2;

    public static final int GLFW_KEY_UNKNOWN = -1;
    public static final int GLFW_KEY_SPACE = 32;
    public static final int GLFW_KEY_APOSTROPHE = 39;
    public static final int GLFW_KEY_COMMA = 44;
    public static final int GLFW_KEY_MINUS = 45;
    public static final int GLFW_KEY_PERIOD = 46;
    public static final int GLFW_KEY_SLASH = 47;
    public static final int GLFW_KEY_0 = 48;
    public static final int GLFW_KEY_1 = 49;
    public static final int GLFW_KEY_2 = 50;
    public static final int GLFW_KEY_3 = 51;
    public static final int GLFW_KEY_4 = 52;
    public static final int GLFW_KEY_5 = 53;
    public static final int GLFW_KEY_6 = 54;
    public static final int GLFW_KEY_7 = 55;
    public static final int GLFW_KEY_8 = 56;
    public static final int GLFW_KEY_9 = 57;
    public static final int GLFW_KEY_SEMICOLON = 59;
    public static final int GLFW_KEY_EQUAL = 61;
    public static final int GLFW_KEY_A = 65;
    public static final int GLFW_KEY_B = 66;
    public static final int GLFW_KEY_C = 67;
    public static final int GLFW_KEY_D = 68;
    public static final int GLFW_KEY_E = 69;
    public static final int GLFW_KEY_F = 70;
    public static final int GLFW_KEY_G = 71;
    public static final int GLFW_KEY_H = 72;
    public static final int GLFW_KEY_I = 73;
    public static final int GLFW_KEY_J = 74;
    public static final int GLFW_KEY_K = 75;
    public static final int GLFW_KEY_L = 76;
    public static final int GLFW_KEY_M = 77;
    public static final int GLFW_KEY_N = 78;
    public static final int GLFW_KEY_O = 79;
    public static final int GLFW_KEY_P = 80;
    public static final int GLFW_KEY_Q = 81;
    public static final int GLFW_KEY_R = 82;
    public static final int GLFW_KEY_S = 83;
    public static final int GLFW_KEY_T = 84;
    public static final int GLFW_KEY_U = 85;
    public static final int GLFW_KEY_V = 86;
    public static final int GLFW_KEY_W = 87;
    public static final int GLFW_KEY_X = 88;
    public static final int GLFW_KEY_Y = 89;
    public static final int GLFW_KEY_Z = 90;
    public static final int GLFW_KEY_LEFT_BRACKET = 91;
    public static final int GLFW_KEY_BACKSLASH = 92;
    public static final int GLFW_KEY_RIGHT_BRACKET = 93;
    public static final int GLFW_KEY_GRAVE_ACCENT = 96;
    public static final int GLFW_KEY_WORLD_1 = 161;
    public static final int GLFW_KEY_WORLD_2 = 162;

    public static final int GLFW_KEY_ESCAPE = 256;
    public static final int GLFW_KEY_ENTER = 257;
    public static final int GLFW_KEY_TAB = 258;
    public static final int GLFW_KEY_BACKSPACE = 259;
    public static final int GLFW_KEY_INSERT = 260;
    public static final int GLFW_KEY_DELETE = 261;
    public static final int GLFW_KEY_RIGHT = 262;
    public static final int GLFW_KEY_LEFT = 263;
    public static final int GLFW_KEY_DOWN = 264;
    public static final int GLFW_KEY_UP = 265;
    public static final int GLFW_KEY_PAGE_UP = 266;
    public static final int GLFW_KEY_PAGE_DOWN = 267;
    public static final int GLFW_KEY_HOME = 268;
    public static final int GLFW_KEY_END = 269;
    public static final int GLFW_KEY_CAPS_LOCK = 280;
    public static final int GLFW_KEY_SCROLL_LOCK = 281;
    public static final int GLFW_KEY_NUM_LOCK = 282;
    public static final int GLFW_KEY_PRINT_SCREEN = 283;
    public static final int GLFW_KEY_PAUSE = 284;
    public static final int GLFW_KEY_F1 = 290;
    public static final int GLFW_KEY_F2 = 291;
    public static final int GLFW_KEY_F3 = 292;
    public static final int GLFW_KEY_F4 = 293;
    public static final int GLFW_KEY_F5 = 294;
    public static final int GLFW_KEY_F6 = 295;
    public static final int GLFW_KEY_F7 = 296;
    public static final int GLFW_KEY_F8 = 297;
    public static final int GLFW_KEY_F9 = 298;
    public static final int GLFW_KEY_F10 = 299;
    public static final int GLFW_KEY_F11 = 300;
    public static final int GLFW_KEY_F12 = 301;
    public static final int GLFW_KEY_F13 = 302;
    public static final int GLFW_KEY_F14 = 303;
    public static final int GLFW_KEY_F15 = 304;
    public static final int GLFW_KEY_F16 = 305;
    public static final int GLFW_KEY_F17 = 306;
    public static final int GLFW_KEY_F18 = 307;
    public static final int GLFW_KEY_F19 = 308;
    public static final int GLFW_KEY_F20 = 309;
    public static final int GLFW_KEY_F21 = 310;
    public static final int GLFW_KEY_F22 = 311;
    public static final int GLFW_KEY_F23 = 312;
    public static final int GLFW_KEY_F24 = 313;
    public static final int GLFW_KEY_F25 = 314;
    public static final int GLFW_KEY_KP_0 = 320;
    public static final int GLFW_KEY_KP_1 = 321;
    public static final int GLFW_KEY_KP_2 = 322;
    public static final int GLFW_KEY_KP_3 = 323;
    public static final int GLFW_KEY_KP_4 = 324;
    public static final int GLFW_KEY_KP_5 = 325;
    public static final int GLFW_KEY_KP_6 = 326;
    public static final int GLFW_KEY_KP_7 = 327;
    public static final int GLFW_KEY_KP_8 = 328;
    public static final int GLFW_KEY_KP_9 = 329;
    public static final int GLFW_KEY_KP_DECIMAL = 330;
    public static final int GLFW_KEY_KP_DIVIDE = 331;
    public static final int GLFW_KEY_KP_MULTIPLY = 332;
    public static final int GLFW_KEY_KP_SUBTRACT = 333;
    public static final int GLFW_KEY_KP_ADD = 334;
    public static final int GLFW_KEY_KP_ENTER = 335;
    public static final int GLFW_KEY_KP_EQUAL = 336;
    public static final int GLFW_KEY_LEFT_SHIFT = 340;
    public static final int GLFW_KEY_LEFT_CONTROL = 341;
    public static final int GLFW_KEY_LEFT_ALT = 342;
    public static final int GLFW_KEY_LEFT_SUPER = 343;
    public static final int GLFW_KEY_RIGHT_SHIFT = 344;
    public static final int GLFW_KEY_RIGHT_CONTROL = 345;
    public static final int GLFW_KEY_RIGHT_ALT = 346;
    public static final int GLFW_KEY_RIGHT_SUPER = 347;
    public static final int GLFW_KEY_MENU = 348;
    public static final int GLFW_KEY_LAST = GLFW_KEY_MENU;

    public static final int GLFW_MOD_SHIFT = 0x0001;
    public static final int GLFW_MOD_CONTROL = 0x0002;
    public static final int GLFW_MOD_ALT = 0x0004;
    public static final int GLFW_MOD_SUPER = 0x0008;
    public static final int GLFW_MOD_CAPS_LOCK = 0x0010;
    public static final int GLFW_MOD_NUM_LOCK = 0x0020;

    public static final int GLFW_MOUSE_BUTTON_1 = 0;
    public static final int GLFW_MOUSE_BUTTON_2 = 1;
    public static final int GLFW_MOUSE_BUTTON_3 = 2;
    public static final int GLFW_MOUSE_BUTTON_4 = 3;
    public static final int GLFW_MOUSE_BUTTON_5 = 4;
    public static final int GLFW_MOUSE_BUTTON_6 = 5;
    public static final int GLFW_MOUSE_BUTTON_7 = 6;
    public static final int GLFW_MOUSE_BUTTON_8 = 7;
    public static final int GLFW_MOUSE_BUTTON_LAST = GLFW_MOUSE_BUTTON_8;
    public static final int GLFW_MOUSE_BUTTON_LEFT = GLFW_MOUSE_BUTTON_1;
    public static final int GLFW_MOUSE_BUTTON_RIGHT = GLFW_MOUSE_BUTTON_2;
    public static final int GLFW_MOUSE_BUTTON_MIDDLE = GLFW_MOUSE_BUTTON_3;

    public static final int GLFW_DONT_CARE = -1;

    public static final int GLFW_ARROW_CURSOR = 0x00036001;
    public static final int GLFW_IBEAM_CURSOR = 0x00036002;
    public static final int GLFW_CROSSHAIR_CURSOR = 0x00036003;
    public static final int GLFW_HAND_CURSOR = 0x00036004;
    public static final int GLFW_POINTING_HAND_CURSOR = 0x00036004;
    public static final int GLFW_HRESIZE_CURSOR = 0x00036005;
    public static final int GLFW_RESIZE_EW_CURSOR = 0x00036005;
    public static final int GLFW_VRESIZE_CURSOR = 0x00036006;
    public static final int GLFW_RESIZE_NS_CURSOR = 0x00036006;
    public static final int GLFW_RESIZE_NWSE_CURSOR = 0x00036007;
    public static final int GLFW_RESIZE_NESW_CURSOR = 0x00036008;
    public static final int GLFW_RESIZE_ALL_CURSOR = 0x00036009;
    public static final int GLFW_NOT_ALLOWED_CURSOR = 0x0003600A;

    private static volatile GLFWCharCallbackI charCallback;

    private static final Map<Integer, Integer> SDL_TO_GLFW = new HashMap<>();
    private static final Map<Integer, Integer> GLFW_TO_SDL = new HashMap<>();

    static {
        for (int i = 0; i < 26; i++) {
            mapKey(InputConstants.KEY_A + i, GLFW_KEY_A + i);
        }
        for (int i = 0; i < 9; i++) {
            mapKey(InputConstants.KEY_1 + i, GLFW_KEY_1 + i);
        }
        mapKey(InputConstants.KEY_0, GLFW_KEY_0);

        mapKey(InputConstants.KEY_RETURN, GLFW_KEY_ENTER);
        mapKey(InputConstants.KEY_ESCAPE, GLFW_KEY_ESCAPE);
        mapKey(InputConstants.KEY_BACKSPACE, GLFW_KEY_BACKSPACE);
        mapKey(InputConstants.KEY_TAB, GLFW_KEY_TAB);
        mapKey(InputConstants.KEY_SPACE, GLFW_KEY_SPACE);
        mapKey(InputConstants.KEY_MINUS, GLFW_KEY_MINUS);
        mapKey(InputConstants.KEY_EQUALS, GLFW_KEY_EQUAL);
        mapKey(InputConstants.KEY_LBRACKET, GLFW_KEY_LEFT_BRACKET);
        mapKey(InputConstants.KEY_RBRACKET, GLFW_KEY_RIGHT_BRACKET);
        mapKey(InputConstants.KEY_BACKSLASH, GLFW_KEY_BACKSLASH);
        mapKey(InputConstants.KEY_SEMICOLON, GLFW_KEY_SEMICOLON);
        mapKey(InputConstants.KEY_APOSTROPHE, GLFW_KEY_APOSTROPHE);
        mapKey(InputConstants.KEY_GRAVE, GLFW_KEY_GRAVE_ACCENT);
        mapKey(InputConstants.KEY_COMMA, GLFW_KEY_COMMA);
        mapKey(InputConstants.KEY_PERIOD, GLFW_KEY_PERIOD);
        mapKey(InputConstants.KEY_SLASH, GLFW_KEY_SLASH);
        mapKey(InputConstants.KEY_CAPSLOCK, GLFW_KEY_CAPS_LOCK);

        for (int i = 0; i < 12; i++) {
            mapKey(InputConstants.KEY_F1 + i, GLFW_KEY_F1 + i);
        }
        for (int i = 0; i < 12; i++) {
            mapKey(InputConstants.KEY_F13 + i, GLFW_KEY_F13 + i);
        }

        mapKey(InputConstants.KEY_PRINTSCREEN, GLFW_KEY_PRINT_SCREEN);
        mapKey(InputConstants.KEY_SCROLLLOCK, GLFW_KEY_SCROLL_LOCK);
        mapKey(InputConstants.KEY_PAUSE, GLFW_KEY_PAUSE);
        mapKey(InputConstants.KEY_INSERT, GLFW_KEY_INSERT);
        mapKey(InputConstants.KEY_HOME, GLFW_KEY_HOME);
        mapKey(InputConstants.KEY_PAGEUP, GLFW_KEY_PAGE_UP);
        mapKey(InputConstants.KEY_DELETE, GLFW_KEY_DELETE);
        mapKey(InputConstants.KEY_END, GLFW_KEY_END);
        mapKey(InputConstants.KEY_PAGEDOWN, GLFW_KEY_PAGE_DOWN);
        mapKey(InputConstants.KEY_RIGHT, GLFW_KEY_RIGHT);
        mapKey(InputConstants.KEY_LEFT, GLFW_KEY_LEFT);
        mapKey(InputConstants.KEY_DOWN, GLFW_KEY_DOWN);
        mapKey(InputConstants.KEY_UP, GLFW_KEY_UP);
        mapKey(InputConstants.KEY_NUMLOCK, GLFW_KEY_NUM_LOCK);

        mapKey(84, GLFW_KEY_KP_DIVIDE);
        mapKey(InputConstants.KEY_MULTIPLY, GLFW_KEY_KP_MULTIPLY);
        mapKey(86, GLFW_KEY_KP_SUBTRACT);
        mapKey(InputConstants.KEY_ADD, GLFW_KEY_KP_ADD);
        mapKey(InputConstants.KEY_NUMPADENTER, GLFW_KEY_KP_ENTER);
        for (int i = 0; i < 9; i++) {
            mapKey(InputConstants.KEY_NUMPAD1 + i, GLFW_KEY_KP_1 + i);
        }
        mapKey(InputConstants.KEY_NUMPAD0, GLFW_KEY_KP_0);
        mapKey(99, GLFW_KEY_KP_DECIMAL);
        mapKey(InputConstants.KEY_NUMPADEQUALS, GLFW_KEY_KP_EQUAL);

        mapKey(InputConstants.KEY_LCONTROL, GLFW_KEY_LEFT_CONTROL);
        mapKey(InputConstants.KEY_LSHIFT, GLFW_KEY_LEFT_SHIFT);
        mapKey(InputConstants.KEY_LALT, GLFW_KEY_LEFT_ALT);
        mapKey(InputConstants.KEY_LGUI, GLFW_KEY_LEFT_SUPER);
        mapKey(InputConstants.KEY_RCONTROL, GLFW_KEY_RIGHT_CONTROL);
        mapKey(InputConstants.KEY_RSHIFT, GLFW_KEY_RIGHT_SHIFT);
        mapKey(InputConstants.KEY_RALT, GLFW_KEY_RIGHT_ALT);
        mapKey(InputConstants.KEY_RGUI, GLFW_KEY_RIGHT_SUPER);
    }

    private static void mapKey(int sdlScancode, int glfwKey) {
        SDL_TO_GLFW.put(sdlScancode, glfwKey);
        GLFW_TO_SDL.put(glfwKey, sdlScancode);
    }

    public static GLFWCharCallback glfwSetCharCallback(long window, GLFWCharCallbackI cbfun) {
        charCallback = cbfun;
        return null;
    }

    public static void dispatchChar(long window, int codepoint) {
        GLFWCharCallbackI cb = charCallback;
        if (cb != null) {
            cb.invoke(window, codepoint);
        }
    }

    public static int sdlScancodeToGlfwKey(int scancode, int keycode) {
        Integer mapped = SDL_TO_GLFW.get(scancode);
        if (mapped != null) {
            return mapped;
        }
        if (keycode >= 97 && keycode <= 122) {
            return keycode - 32; // ASCII uppercase A..Z
        }
        if (keycode >= 32 && keycode <= 96) {
            return keycode;
        }
        return GLFW_KEY_UNKNOWN;
    }

    public static int glfwKeyToSdlScancode(int glfwKey) {
        return GLFW_TO_SDL.getOrDefault(glfwKey, -1);
    }

    public static int sdlButtonToGlfwButton(int sdlButton) {
        return switch (sdlButton) {
            case InputConstants.MOUSE_BUTTON_LEFT -> GLFW_MOUSE_BUTTON_LEFT;
            case InputConstants.MOUSE_BUTTON_RIGHT -> GLFW_MOUSE_BUTTON_RIGHT;
            case InputConstants.MOUSE_BUTTON_MIDDLE -> GLFW_MOUSE_BUTTON_MIDDLE;
            default -> sdlButton >= 4 ? sdlButton - 1 : sdlButton;
        };
    }

    public static int glfwButtonToSdlButton(int glfwButton) {
        return switch (glfwButton) {
            case GLFW_MOUSE_BUTTON_LEFT -> InputConstants.MOUSE_BUTTON_LEFT;
            case GLFW_MOUSE_BUTTON_RIGHT -> InputConstants.MOUSE_BUTTON_RIGHT;
            case GLFW_MOUSE_BUTTON_MIDDLE -> InputConstants.MOUSE_BUTTON_MIDDLE;
            default -> glfwButton >= 3 ? glfwButton + 1 : glfwButton;
        };
    }

    public static int sdlActionToGlfwAction(int sdlAction) {
        if (sdlAction == InputConstants.REPEAT) {
            return GLFW_REPEAT;
        }
        if (sdlAction == InputConstants.PRESS) {
            return GLFW_PRESS;
        }
        return GLFW_RELEASE;
    }

    public static int sdlModsToGlfwMods(int sdlMods) {
        int mods = 0;
        if ((sdlMods & InputConstants.MOD_SHIFT) != 0) mods |= GLFW_MOD_SHIFT;
        if ((sdlMods & InputConstants.MOD_CONTROL) != 0) mods |= GLFW_MOD_CONTROL;
        if ((sdlMods & InputConstants.MOD_ALT) != 0) mods |= GLFW_MOD_ALT;
        if ((sdlMods & InputConstants.MOD_SUPER) != 0) mods |= GLFW_MOD_SUPER;
        if ((sdlMods & InputConstants.MOD_CAPS_LOCK) != 0) mods |= GLFW_MOD_CAPS_LOCK;
        if ((sdlMods & InputConstants.MOD_NUM_LOCK) != 0) mods |= GLFW_MOD_NUM_LOCK;
        return mods;
    }

    public static int glfwGetKey(long window, int key) {
        int sdl = glfwKeyToSdlScancode(key);
        if (sdl <= 0) {
            return GLFW_RELEASE;
        }
        return InputConstants.isKeyDown(sdl) ? GLFW_PRESS : GLFW_RELEASE;
    }

    public static int glfwGetMouseButton(long window, int button) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.mouseHandler == null) {
            return GLFW_RELEASE;
        }
        return switch (button) {
            case GLFW_MOUSE_BUTTON_LEFT -> mc.mouseHandler.isLeftPressed() ? GLFW_PRESS : GLFW_RELEASE;
            case GLFW_MOUSE_BUTTON_RIGHT -> mc.mouseHandler.isRightPressed() ? GLFW_PRESS : GLFW_RELEASE;
            case GLFW_MOUSE_BUTTON_MIDDLE -> mc.mouseHandler.isMiddlePressed() ? GLFW_PRESS : GLFW_RELEASE;
            default -> GLFW_RELEASE;
        };
    }

    public static void glfwGetCursorPos(long window, DoubleBuffer xpos, DoubleBuffer ypos) {
        Minecraft mc = Minecraft.getInstance();
        double x = (mc != null && mc.mouseHandler != null) ? mc.mouseHandler.xpos() : 0.0;
        double y = (mc != null && mc.mouseHandler != null) ? mc.mouseHandler.ypos() : 0.0;
        if (xpos != null && xpos.hasRemaining()) {
            xpos.put(xpos.position(), x);
        }
        if (ypos != null && ypos.hasRemaining()) {
            ypos.put(ypos.position(), y);
        }
    }

    public static int glfwGetKeyScancode(int key) {
        return glfwKeyToSdlScancode(key);
    }

    public static String glfwGetKeyName(int key, int scancode) {
        if (key >= GLFW_KEY_A && key <= GLFW_KEY_Z) {
            return String.valueOf((char) key);
        }
        if (key >= GLFW_KEY_0 && key <= GLFW_KEY_9) {
            return String.valueOf((char) key);
        }
        return switch (key) {
            case GLFW_KEY_SPACE -> "SPACE";
            case GLFW_KEY_APOSTROPHE -> "'";
            case GLFW_KEY_COMMA -> ",";
            case GLFW_KEY_MINUS -> "-";
            case GLFW_KEY_PERIOD -> ".";
            case GLFW_KEY_SLASH -> "/";
            case GLFW_KEY_SEMICOLON -> ";";
            case GLFW_KEY_EQUAL -> "=";
            case GLFW_KEY_LEFT_BRACKET -> "[";
            case GLFW_KEY_BACKSLASH -> "\\";
            case GLFW_KEY_RIGHT_BRACKET -> "]";
            case GLFW_KEY_GRAVE_ACCENT -> "`";
            default -> null;
        };
    }

    private static final Map<Integer, Long> CURSOR_CACHE = new ConcurrentHashMap<>();

    public static long glfwCreateStandardCursor(int shape) {
        return CURSOR_CACHE.computeIfAbsent(shape, s -> {
            int sdlCursor = switch (s) {
                case GLFW_IBEAM_CURSOR -> SDLMouse.SDL_SYSTEM_CURSOR_TEXT;
                case GLFW_CROSSHAIR_CURSOR -> SDLMouse.SDL_SYSTEM_CURSOR_CROSSHAIR;
                case GLFW_HAND_CURSOR -> SDLMouse.SDL_SYSTEM_CURSOR_POINTER;
                case GLFW_HRESIZE_CURSOR -> SDLMouse.SDL_SYSTEM_CURSOR_EW_RESIZE;
                case GLFW_VRESIZE_CURSOR -> SDLMouse.SDL_SYSTEM_CURSOR_NS_RESIZE;
                case GLFW_RESIZE_NWSE_CURSOR -> SDLMouse.SDL_SYSTEM_CURSOR_NWSE_RESIZE;
                case GLFW_RESIZE_NESW_CURSOR -> SDLMouse.SDL_SYSTEM_CURSOR_NESW_RESIZE;
                case GLFW_RESIZE_ALL_CURSOR -> SDLMouse.SDL_SYSTEM_CURSOR_MOVE;
                case GLFW_NOT_ALLOWED_CURSOR -> SDLMouse.SDL_SYSTEM_CURSOR_NOT_ALLOWED;
                default -> SDLMouse.SDL_SYSTEM_CURSOR_DEFAULT;
            };
            return SDLMouse.SDL_CreateSystemCursor(sdlCursor);
        });
    }

    public static void glfwSetCursor(long window, long cursor) {
        SDLMouse.SDL_SetCursor(cursor == 0L ? SDLMouse.SDL_GetDefaultCursor() : cursor);
    }

    public static void glfwDestroyCursor(long cursor) {
        // Cached standard cursors are reused across frames.
    }

    public static void glfwSetCursorPos(long window, double xpos, double ypos) {
    }

    public static double glfwGetTime() {
        return System.nanoTime() / 1_000_000_000.0;
    }

    public static String glfwGetClipboardString(long window) {
        Minecraft mc = Minecraft.getInstance();
        return (mc != null && mc.keyboardHandler != null) ? mc.keyboardHandler.getClipboard() : "";
    }

    public static void glfwSetClipboardString(long window, CharSequence string) {
        Minecraft mc = Minecraft.getInstance();
        if (mc != null && mc.keyboardHandler != null && string != null) {
            mc.keyboardHandler.setClipboard(string.toString());
        }
    }

}
