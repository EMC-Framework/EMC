package me.deftware.client.framework.render.shader;

import me.deftware.client.framework.resource.ModResourceManager;
import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import me.deftware.client.framework.world.ClientWorld;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Deftware
 */
public class EntityShader extends Shader {

    public static final List<EntityShader> SHADERS = new ArrayList<>();

    private boolean enabled = false;
    private Predicate<Object> targetPredicate = obj -> true;

    public EntityShader(MinecraftIdentifier identifier, ModResourceManager resourceManager) {
        super(identifier, resourceManager);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setTargetPredicate(Predicate<Object> targetPredicate) {
        this.targetPredicate = targetPredicate;
    }

    public Predicate<Object> getTargetPredicate() {
        return targetPredicate;
    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    public static int getMatchingOutlineColor(net.minecraft.world.entity.Entity mcEntity) {
        if (mcEntity == null || SHADERS.isEmpty()) {
            return 0;
        }
        ClientWorld world = ClientWorld.getClientWorld();
        if (world == null) {
            return 0;
        }
        me.deftware.client.framework.entity.Entity wrapped = world.getEntityByReference(mcEntity);
        if (wrapped == null) {
            return 0;
        }
        for (EntityShader shader : SHADERS) {
            if (shader.isEnabled() && shader.getTargetPredicate().test(wrapped)) {
                return shader.resolveOutlineColor();
            }
        }
        return 0;
    }

    private int resolveOutlineColor() {
        for (var entry : getUniformsF().entrySet()) {
            float[] vals = entry.getValue();
            if (vals != null && vals.length >= 3) {
                int r = Math.max(0, Math.min(255, Math.round(vals[0] * 255.0f)));
                int g = Math.max(0, Math.min(255, Math.round(vals[1] * 255.0f)));
                int b = Math.max(0, Math.min(255, Math.round(vals[2] * 255.0f)));
                int a = vals.length >= 4 ? Math.max(0, Math.min(255, Math.round(vals[3] * 255.0f))) : 255;
                if (a == 0) a = 255;
                return (a << 24) | (r << 16) | (g << 8) | b;
            }
        }
        return 0xFF00FFAA;
    }

}
