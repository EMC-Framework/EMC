package me.deftware.client.framework.event;

/**
 * This class describes the way events are defined in EMC framework and handles the process of
 * delivering events to all of the loaded mods
 *
 * @author Deftware
 */
@SuppressWarnings("unchecked")
public class Event {

    private boolean canceled = false;

    /**
     * Broadcasts an event to all registered listeners
     */
    public <T extends Event> T broadcast() {
        EventBus.INSTANCE.broadcast(this);
        return (T) this;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

}
