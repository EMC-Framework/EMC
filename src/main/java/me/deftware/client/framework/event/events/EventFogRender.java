package me.deftware.client.framework.event.events;

import lombok.Getter;
import lombok.Setter;
import me.deftware.client.framework.event.Event;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.fog.FogRenderer;

public class EventFogRender extends Event {

    private @Getter @Setter Camera camera;
    private @Getter @Setter FogRenderer.FogMode fogType;
    private @Getter @Setter float viewDistance;
    private @Getter @Setter boolean thickFog;

    public EventFogRender create(Camera camera, FogRenderer.FogMode fogType, float viewDistance, boolean thickFog) {
        setCanceled(false);
        this.camera = camera;
        this.fogType = fogType;
        this.viewDistance = viewDistance;
        this.thickFog = thickFog;
        return this;
    }

}
