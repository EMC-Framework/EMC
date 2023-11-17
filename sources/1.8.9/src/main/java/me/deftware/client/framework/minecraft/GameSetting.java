package me.deftware.client.framework.minecraft;

import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public abstract class GameSetting<T> {

    public static GameSetting<Integer> VIEW_DISTANCE = getSimpleOption(() -> Minecraft.getMinecraft().gameSettings.renderDistanceChunks,
            value -> Minecraft.getMinecraft().gameSettings.renderDistanceChunks = value);
    public static GameSetting<Double> GAMMA = getSimpleOption(() -> (double) Minecraft.getMinecraft().gameSettings.gammaSetting,
            value -> Minecraft.getMinecraft().gameSettings.gammaSetting = value.floatValue());
    public static GameSetting<Integer> MAX_FPS = getSimpleOption(() -> Minecraft.getMinecraft().gameSettings.limitFramerate,
            value -> {
                Minecraft.getMinecraft().gameSettings.limitFramerate = value;
            });
    public static GameSetting<Boolean> DEBUG_INFO = getSimpleOption(() -> Minecraft.getMinecraft().gameSettings.showDebugInfo,
            state -> Minecraft.getMinecraft().gameSettings.showDebugInfo = state);

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
            return Minecraft.getMinecraft().gameSettings.guiScale;
        }

        @Override
        public void set(Integer value) {
            Minecraft.getMinecraft().gameSettings.guiScale = value;
        }

        public double getScaleFactor() {
            ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
            return resolution.getScaleFactor();
        }
    }

}

