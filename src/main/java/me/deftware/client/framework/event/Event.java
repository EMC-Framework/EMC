package me.deftware.client.framework.event;

import me.deftware.client.framework.main.bootstrap.Bootstrap;

/**
 * This class describes the way events are defined in EMC framework and handles the process of
 * delivering events to all of the loaded mods
 */
public class Event {
    private boolean canceled = false;

    /**
     * Broadcasts an event to all registered listeners
     */
    @SuppressWarnings("unchecked")
    public <T extends Event> T broadcast() {
        try {
            EventBus.sendEvent(this);
        } catch (Exception ex) {
            Bootstrap.logger.warn("Failed to send event {}", this, ex);
        }
        return (T) this;
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    public void setCanceled(final boolean canceled) {
        this.canceled = canceled;
    }
}
