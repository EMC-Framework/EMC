package me.deftware.client.framework.minecraft;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class GameSetting<T> {

    public static GameSetting<Integer> VIEW_DISTANCE = getSimpleOption(MinecraftClient.getInstance().options.getViewDistance());
    public static GameSetting<Double> GAMMA = getSimpleOption(MinecraftClient.getInstance().options.getGamma());
    public static GameSetting<Integer> MAX_FPS = getSimpleOption(MinecraftClient.getInstance().options.getMaxFps());
    public static GameSetting<Boolean> DEBUG_INFO = getSimpleOption(() -> MinecraftClient.getInstance().options.debugEnabled,
            state -> MinecraftClient.getInstance().options.debugEnabled = state);

    private static <E> GameSetting<E> getSimpleOption(SimpleOption<E> option) {
        return new GameSetting<>() {
            @Override
            public E get() {
                return option.getValue();
            }

            @Override
            public void set(E value) {
                option.setValue(value);
            }
        };
    }

    public static <E> GameSetting<E> getSimpleOption(Supplier<E> supplier, Consumer<E> consumer) {
        return new GameSetting<>() {
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

        private SimpleOption<Integer> getSetting() {
            return MinecraftClient.getInstance().options.getGuiScale();
        }

        @Override
        public Integer get() {
            return this.getSetting().getValue();
        }

        @Override
        public void set(Integer value) {
            this.getSetting().setValue(value);
        }

        public double getScaleFactor() {
            return MinecraftClient.getInstance().getWindow().getScaleFactor();
        }
    }

}
