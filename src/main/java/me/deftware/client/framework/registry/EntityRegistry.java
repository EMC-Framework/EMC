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
public enum EntityRegistry implements IRegistry<EntityCapsule, Class<? extends Entity>> {

    INSTANCE;

    private final HashMap<String, EntityCapsule> entities = new HashMap<>();

    @Override
    public Optional<EntityCapsule> find(String id) {
        return stream().filter(item -> {
            return  item.getTranslationKey().equalsIgnoreCase(id) ||
                    item.getTranslationKey().startsWith("minecraft:") && item.getTranslationKey().substring("minecraft:".length()).equalsIgnoreCase(id);
        }).findFirst();
    }

    @Override
    public Stream<EntityCapsule> stream() {
        return entities.values().stream();
    }

    @Override
    public void register(String id, Class<? extends Entity> object) {
        throw new UnsupportedOperationException("Unsupport method");
    }

    public void register(String id, Class<? extends Entity> object, ResourceLocation key) {
        entities.putIfAbsent(id, new EntityCapsule(id, object, key));
    }

}
