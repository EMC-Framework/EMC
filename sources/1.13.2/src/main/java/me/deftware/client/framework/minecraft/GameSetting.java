package me.deftware.client.framework.minecraft;

import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;

public abstract class GameSetting<T> {

    public static GameSetting<Integer> VIEW_DISTANCE = getSimpleOption(() -> Minecraft.getInstance().gameSettings.renderDistanceChunks,
            value -> Minecraft.getInstance().gameSettings.renderDistanceChunks = value);
    public static GameSetting<Double> GAMMA = getSimpleOption(() -> Minecraft.getInstance().gameSettings.gammaSetting,
            value -> Minecraft.getInstance().gameSettings.gammaSetting = value);
    public static GameSetting<Integer> MAX_FPS = getSimpleOption(() -> Minecraft.getInstance().gameSettings.limitFramerate,
            value -> {
                Minecraft.getInstance().gameSettings.limitFramerate = value;
            });
    public static GameSetting<Boolean> DEBUG_INFO = getSimpleOption(() -> Minecraft.getInstance().gameSettings.showDebugInfo,
            state -> Minecraft.getInstance().gameSettings.showDebugInfo = state);

    public static <E> GameSetting<E> getSimpleOption(Supplier<E> supplier, Consumer<E> consumer) {
        return new GameSetting<E>() {
            @Override
            public E get() {
                return supplier.get();
            }

            @Override
            public void set(E value) {
                consumer.accept(value);
            }
        };
    }

    public abstract T get();

    public abstract void set(T value);

    public static class GuiScaleSetting extends GameSetting<Integer> {
        public static GuiScaleSetting INSTANCE = new GuiScaleSetting();

        @Override
        public Integer get() {
            return Minecraft.getInstance().gameSettings.guiScale;
        }

        @Override
        public void set(Integer value) {
            Minecraft.getInstance().gameSettings.guiScale = value;
        }

        public double getScaleFactor() {
            return Minecraft.getInstance().mainWindow.getGuiScaleFactor();
        }
    }

}

