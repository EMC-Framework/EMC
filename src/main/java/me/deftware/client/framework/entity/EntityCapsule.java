package me.deftware.client.framework.entity;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

/**
 * @author Deftware
 */
public class EntityCapsule {

    private final Class<? extends Entity> entityType;
    private final String entityName;
    private ResourceLocation texture;
    private final String id;

    public <T extends Entity> EntityCapsule(String id, Class<T> entityType, String entityName) {
        this.entityType = entityType;
        this.id = id;
        this.entityName = entityName;
    }

    public me.deftware.client.framework.entity.Entity create() {
        return me.deftware.client.framework.entity.Entity.newInstance(EntityList.createEntityByName(entityName,
                Objects.requireNonNull(Minecraft.getMinecraft().theWorld)
        ));
    }

    public String getId() {
        return id;
    }

    public Class<? extends Entity> getRaw() {
        return entityType;
    }

    public ChatMessage getName() {
        return new ChatMessage().fromString(entityName);
    }

    public void setTexture(ResourceLocation identifier) {
        texture = identifier;
    }

    public MinecraftIdentifier getTexture() {
        if (texture == null) return null;
        return new MinecraftIdentifier(texture);
    }

    public MinecraftIdentifier getIdentifier() {
        return new MinecraftIdentifier(entityName);
    }

    public String getTranslationKey() {
        return entityName;
    }

}
