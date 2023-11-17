package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;
import me.deftware.client.framework.render.gl.GLX;

public class EventRenderBase extends Event {

    public GLX getContext() {
        return GLX.getInstance();
    }

    public EventRenderBase setContext(GLX context) {
        // Dummy method
        return this;
    }

}
