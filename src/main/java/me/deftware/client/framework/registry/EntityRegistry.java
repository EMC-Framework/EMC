package me.deftware.client.framework.registry;

import me.deftware.client.framework.entity.EntityCapsule;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Deftware
 */
public enum EntityRegistry implements IRegistry.IdentifiableRegistry<EntityCapsule, Class<? extends Entity>> {

    INSTANCE;

    private final HashMap<String, EntityCapsule> entities = new HashMap<>();

    @Override
    public Stream<EntityCapsule> stream() {
        return entities.values().stream();
    }

    @Override
    public void register(String id, Class<? extends Entity> object) {
        throw new UnsupportedOperationException("Unsupport method");
    }

    public void register(String id, Class<? extends Entity> object, String entityName) {
        entities.putIfAbsent(id, new EntityCapsule(id, object, entityName));
    }

}
