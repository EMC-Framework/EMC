package me.deftware.client.framework.minecraft;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;

public abstract class GameSetting<T> {

    public static GameSetting<Integer> VIEW_DISTANCE = getSimpleOption(Minecraft.getInstance().options.renderDistance());
    public static GameSetting<Double> GAMMA = getSimpleOption(Minecraft.getInstance().options.gamma());
    public static GameSetting<Integer> MAX_FPS = getSimpleOption(Minecraft.getInstance().options.framerateLimit());

    // TODO: Add real setter, not just a toggle
    public static GameSetting<Boolean> DEBUG_INFO = getSimpleOption(() -> Minecraft.getInstance().getDebugOverlay().showDebugScreen(),
            state -> Minecraft.getInstance().debugEntries.toggleDebugOverlay());

    private static <E> GameSetting<E> getSimpleOption(OptionInstance<E> option) {
        return new GameSetting<>() {
            @Override
            public E get() {
                return option.get();
            }

            @Override
            public void set(E value) {
                option.set(value);
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

        private OptionInstance<Integer> getSetting() {
            return Minecraft.getInstance().options.guiScale();
        }

        @Override
        public Integer get() {
            return this.getSetting().get();
        }

        @Override
        public void set(Integer value) {
            this.getSetting().set(value);
        }

        public double getScaleFactor() {
            return Minecraft.getInstance().getWindow().getGuiScale();
        }
    }

}
