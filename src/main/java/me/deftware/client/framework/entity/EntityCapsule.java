package me.deftware.client.framework.entity;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.fonts.FontRenderer;
import me.deftware.client.framework.gui.widgets.SelectableList;
import me.deftware.client.framework.registry.Identifiable;
import me.deftware.client.framework.render.gl.GLX;
import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import java.util.Objects;

/**
 * @author Deftware
 */
public class EntityCapsule implements SelectableList.ListItem, Identifiable {

    private final EntityType<? extends Entity> entityType;
    private Identifier texture;
    private final String id;

    public <T extends Entity> EntityCapsule(String id, EntityType<T> entityType) {
        this.entityType = entityType;
        this.id = id;
    }

    public me.deftware.client.framework.entity.Entity create() {
        return me.deftware.client.framework.entity.Entity.newInstance(entityType.create(
                Objects.requireNonNull(Minecraft.getInstance().level), EntitySpawnReason.NATURAL
        ));
    }

    public String getId() {
        return id;
    }

    public EntityType<? extends Entity> getRaw() {
        return entityType;
    }

    public Message getName() {
        return (Message) entityType.getDescription();
    }

    public void setTexture(Identifier identifier) {
        texture = identifier;
    }

    public MinecraftIdentifier getTexture() {
        if (texture == null) return null;
        return new MinecraftIdentifier(texture);
    }

    public MinecraftIdentifier getIdentifier() {
        return new MinecraftIdentifier(BuiltInRegistries.ENTITY_TYPE.getKey(entityType));
    }

    public String getIdentifierKey() {
        return BuiltInRegistries.ENTITY_TYPE.getKey(entityType).getPath();
    }

    @Override
    public String getTranslationKey() {
        return entityType.getDescriptionId();
    }

    @Override
    public void render(GLX context, int index, int x, int y, int entryWidth, int entryHeight, int mouseX, int mouseY, float tickDelta) {
        FontRenderer.drawString(context, getName(), x + 28, y + ((entryHeight / 2) - (FontRenderer.getFontHeight() / 2)) - 3, 0xFFFFFF);
    }

}
