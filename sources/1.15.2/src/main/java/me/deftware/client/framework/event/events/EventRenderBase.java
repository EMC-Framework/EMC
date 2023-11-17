package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;
import me.deftware.client.framework.render.gl.GLX;

public class EventRenderBase extends Event {

    private GLX context;

    public GLX getContext() {
        return context;
    }

    public EventRenderBase setContext(GLX context) {
        this.context = context;
        return this;
    }

    @Override
    public <T extends Event> T broadcast() {
        T result = super.broadcast();
        context = null;
        return result;
    }

}
