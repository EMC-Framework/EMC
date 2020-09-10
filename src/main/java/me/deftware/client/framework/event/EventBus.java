package me.deftware.client.framework.event;

import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.client.framework.maps.MultiMap;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;

/**
 * @author Deftware, Ananas
 */
public class EventBus {

	private static final Object lock = new Object();
	private static final MultiMap<Class<?>, Listener> listeners = new MultiMap<>();

	public static synchronized void registerClass(Class<?> clazz, Object instance) {
		if (instance == null) throw new RuntimeException("Null instance of class " + clazz.getName());
		synchronized (lock) {
			Bootstrap.logger.debug(String.format("Loading event handlers in class %s", clazz.getName()));
			for (Method method : clazz.getDeclaredMethods()) {
				if (method.isAnnotationPresent(EventHandler.class)) {
					if (!method.isAccessible()) {
						Bootstrap.logger.debug(String.format("Making method %s accessible", method.getName()));
						method.setAccessible(true);
					}
					Class<? extends Event> eventType = method.getParameterTypes()[0].asSubclass(Event.class);
					int priority = method.getAnnotation(EventHandler.class).priority();
					listeners.putIfAbsent(eventType, new Listener(method, instance, priority));
					Bootstrap.logger.debug(String.format("Loaded event handler for method %s", method.getName()));
				}
			}
		}
	}

	public static synchronized void unRegisterClass(Class<?> clazz) {
		synchronized (lock) {
			HashMap<Class<?>, Listener> removeList = new HashMap<>();
			for (Class<?> event : listeners.keySet()) {
				Collection<Listener> listenerCollection = listeners.get(event);
				for (Listener listener : listenerCollection) {
					if (listener.getClassInstance().getClass() == clazz) {
						removeList.put(event, listener);
						Bootstrap.logger.debug("Unregistered " + listener.getClassInstance().getClass().getName());
					}
				}
			}
			removeList.forEach(listeners::remove);
		}
	}

	public static void clearEvents() {
		synchronized (lock) {
			listeners.clear();
		}
		System.gc(); //Clear event instances left in the VM
	}

	public static void sendEvent(Event event) {
		try {
			if (listeners.containsKey(event.getClass())) {
				listeners.get(event.getClass()).stream().sorted(Comparator.comparingInt(Listener::getPriority)).forEach(listener -> {
					try {
						listener.getMethod().invoke(listener.getClassInstance(), event);
					} catch (Exception ex) {
						System.out.println("null >> " + (listener.getClassInstance() == null));
						System.out.println(">> " + event.getClass().getName() + " >> " + listener.getClassInstance().getClass().getName());
					}
				});
			}
		} catch (Exception e) {
			Bootstrap.logger.error("Cannot invoke event listener " + e.getMessage());
		}
	}

}
