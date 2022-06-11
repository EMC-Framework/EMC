package me.deftware.client.framework.event;

import me.deftware.client.framework.event.events.EventMatrixRender;
import me.deftware.client.framework.event.events.EventRender2D;
import me.deftware.client.framework.event.events.EventRender3D;
import me.deftware.client.framework.helper.Logger;
import me.deftware.client.framework.maps.MultiMap;
import me.deftware.mixin.mixins.render.BufferBuilderAccessor;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author Deftware, Ananas
 */
public class EventBus {

	private static final Logger LOGGER = new Logger(EventBus.class);

	private static final Object lock = new Object();
	public static final MultiMap<Class<?>, Listener> listeners = new MultiMap<>();

	public static void registerClass(Class<?> clazz, Object instance) {
		registerClass(clazz, instance, null);
	}

	public static void registerClass(Class<?> clazz, Object instance, BiConsumer<Class<? extends Event>, Listener> consumer) {
		synchronized (lock) {
			LOGGER.debug("Walking methods in class {}", clazz.getSimpleName());
			walkMethods(clazz, method -> {
				Class<? extends Event> eventType = method.getParameterTypes()[0].asSubclass(Event.class);
				int priority = method.getAnnotation(EventHandler.class).priority();
				Listener listener = new Listener(method, instance, priority);
				if (consumer != null) {
					consumer.accept(eventType, listener);
				}
				List<Listener> listeners = EventBus.listeners.getOrCreate(eventType);
				listeners.add(listener);
				listeners.sort(Comparator.comparingInt(Listener::getPriority));
				LOGGER.debug("Registering event {} with priority {}", eventType.getSimpleName(), priority);
			});
		}
	}

	public static void walkMethods(Class<?> clazz, Consumer<Method> consumer) {
		while (clazz != null) {
			for (Method method : clazz.getDeclaredMethods()) {
				if (method.isAnnotationPresent(EventHandler.class)) {
					method.setAccessible(true);
					consumer.accept(method);
				}
			}
			clazz = clazz.getSuperclass();
		}
	}

	public static void unRegisterClass(Class<?> clazz) {
		synchronized (lock) {
			LOGGER.debug("Removing all registered events for class {}", clazz.getSimpleName());
			for (Class<?> event : listeners.keySet()) {
				List<Listener> collection = listeners.get(event);
				if (!collection.isEmpty()) {
					collection.removeIf(listener -> {
						return listener.getClassInstance().getClass() == clazz;
					});
				}
			}
		}
	}

	public static void clearEvents() {
		LOGGER.warn("Clearing all registered events ({})", listeners.keySet().size());
		synchronized (lock) {
			listeners.clear();
		}
		System.gc();
	}

	private static final Runnable abortRendering = () -> {
		BufferBuilder builder = Tessellator.getInstance().getBuffer();
		if (((BufferBuilderAccessor) builder).isBuilding()) {
			LOGGER.warn("Closing open buffer builder");
			builder.finishDrawing();
		}
	};

	private static final Map<Class<? extends Event>, Runnable> cleanupHandlers;

	static {
		cleanupHandlers = new HashMap<>();
		cleanupHandlers.put(EventMatrixRender.class, abortRendering);
		cleanupHandlers.put(EventRender3D.class, abortRendering);
		cleanupHandlers.put(EventRender2D.class, abortRendering);
	}

	public static void sendEvent(Event event) {
		List<Listener> listeners = EventBus.listeners.get(event.getClass());
		if (listeners != null && !listeners.isEmpty()) {
			Iterator<Listener> iterator = listeners.iterator();
			while (iterator.hasNext()) {
				Listener listener = iterator.next();
				try {
					listener.invoke(event);
				} catch (Throwable ex) {
					Throwable cause = ex.getCause();
					Class<?> clazz = listener.getClassInstance().getClass();
					LOGGER.error("\"{}\" occurred whilst dispatching \"{}\" to method \"{}\" in class \"{}\" due to \"{}\"",
							cause.getClass().getSimpleName(),
							event.getClass().getSimpleName(),
							listener.getMethod().getName(),
							clazz.getSimpleName(),
							cause.getMessage()
					);
					Consumer<Throwable> consumer = listener.getExceptionHandler();
					if (consumer != null) {
						consumer.accept(cause);
					}
					LOGGER.warn("Removing event {} for class {}", event.getClass().getSimpleName(), clazz.getSimpleName());
					LOGGER.debug("Event dispatch stack:", ex);
					cause.printStackTrace();
					iterator.remove();

					// Run cleanup handlers
					Runnable cleanup = cleanupHandlers.get(event.getClass());
					if (cleanup != null) {
						cleanup.run();
					}
				}
			}
		}
	}

}
