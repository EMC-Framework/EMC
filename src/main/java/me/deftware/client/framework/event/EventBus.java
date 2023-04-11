package me.deftware.client.framework.event;

import me.deftware.client.framework.event.events.EventMatrixRender;
import me.deftware.client.framework.event.events.EventRender2D;
import me.deftware.client.framework.event.events.EventRender3D;
import me.deftware.mixin.mixins.render.BufferBuilderAccessor;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author Deftware
 */
public final class EventBus {

    public final static EventBus INSTANCE = new EventBus();

    private final Map<Class<? extends Event>, Manager> managers = new HashMap<>();

    private final Logger logger = LogManager.getLogger(EventBus.class);

    public synchronized Manager getManager(Class<? extends Event> event) {
        if (!managers.containsKey(event)) {
            managers.put(event, new Manager());
        }
        return managers.get(event);
    }

    public void broadcast(Event event) {
        getManager(event.getClass()).broadcast(event);
    }

    private final class Manager {

        private final List<Listener> listeners = new ArrayList<>();

        public synchronized void register(Listener listener) {
            listeners.add(listener);
            listeners.sort(Comparator.comparingInt(Listener::getPriority));
        }

        public synchronized void unregister(Object instance, Method method) {
            listeners.removeIf(listener ->
                listener.getClassInstance() == instance && listener.getMethod().equals(method)
            );
        }

        public synchronized void broadcast(Event event) {
            Iterator<Listener> iterator = listeners.iterator();
            while (iterator.hasNext()) {
                Listener listener = iterator.next();
                try {
                    listener.invoke(event);
                } catch (Throwable ex) {
                    error(ex.getCause(), listener, event);
                    iterator.remove();
                }
            }
        }

        private void error(Throwable cause, Listener listener, Event event) {
            Class<?> clazz = listener.getClassInstance().getClass();
            logger.error("\"{}\" occurred whilst dispatching \"{}\" to method \"{}\" in class \"{}\" due to \"{}\"",
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
            cause.printStackTrace();
            Runnable cleanup = cleanupHandlers.get(event.getClass());
            if (cleanup != null) {
                cleanup.run();
            }
        }

    }

    /* Helper methods */

    public void registerClass(Object instance) {
        registerClass(instance, null);
    }

    public void registerClass(Object instance, Consumer<Throwable> exceptionHandler) {
        walkMethods(instance.getClass(), (event, handler, method) -> {
            Manager manager = getManager(event);
            Listener listener = new Listener(method, instance, handler.priority());
            listener.setExceptionHandler(exceptionHandler);
            manager.register(listener);
        });
    }

    public void unRegisterClass(Object instance) {
        walkMethods(instance.getClass(), (event, handler, method) -> {
            Manager manager = getManager(event);
            manager.unregister(instance, method);
        });
    }

    private void walkMethods(Class<?> clazz, EventMethod consumer) {
        while (clazz != null) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(EventHandler.class)) {
                    method.setAccessible(true);
                    consumer.accept(
                            method.getParameterTypes()[0].asSubclass(Event.class),
                            method.getAnnotation(EventHandler.class),
                            method
                    );
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    private final Runnable abortRendering = () -> {
        BufferBuilder builder = Tessellator.getInstance().getBuffer();
        if (((BufferBuilderAccessor) builder).isBuilding()) {
            logger.warn("Closing open buffer builder");
            builder.end();
        }
    };

    private final Map<Class<? extends Event>, Runnable> cleanupHandlers = new HashMap<>();

	private EventBus() {
		cleanupHandlers.put(EventMatrixRender.class, abortRendering);
		cleanupHandlers.put(EventRender3D.class, abortRendering);
		cleanupHandlers.put(EventRender2D.class, abortRendering);
	}

    @FunctionalInterface
    private interface EventMethod {

        void accept(Class<? extends Event> event, EventHandler handler, Method method);

    }

}
