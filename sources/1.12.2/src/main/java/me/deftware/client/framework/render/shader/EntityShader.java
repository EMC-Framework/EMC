package me.deftware.client.framework.render.shader;

import me.deftware.client.framework.resource.ModResourceManager;
import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Deftware
 */
public class EntityShader extends Shader {

    public static final List<EntityShader> SHADERS = new ArrayList<>();

    private boolean render = false, enabled = false;
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

    public boolean isRender() {
        return render;
    }

    public void setRender(boolean render) {
        this.render = render;
    }

}
