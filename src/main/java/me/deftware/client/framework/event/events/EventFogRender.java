package me.deftware.client.framework.event.events;

import com.mojang.blaze3d.platform.GlStateManager;
import lombok.Getter;
import lombok.Setter;
import me.deftware.client.framework.event.Event;
import net.minecraft.client.render.Camera;

public class EventFogRender extends Event {
    private @Getter @Setter Camera camera;
    private @Getter @Setter GlStateManager.FogMode fogType;
    private @Getter @Setter float viewDistance;
    private @Getter @Setter boolean thickFog;

    public EventFogRender(Camera camera, GlStateManager.FogMode fogType, float viewDistance, boolean thickFog) {
        this.camera = camera;
        this.fogType = fogType;
        this.viewDistance = viewDistance;
        this.thickFog = thickFog;
    }
}
