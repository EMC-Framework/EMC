package me.deftware.client.framework.entity;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.fonts.FontRenderer;
import me.deftware.client.framework.gui.widgets.SelectableList;
import me.deftware.client.framework.registry.Identifiable;
import me.deftware.client.framework.render.gl.GLX;
import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

/**
 * @author Deftware
 */
public class EntityCapsule implements SelectableList.ListItem, Identifiable {

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

    public Message getName() {
        return Message.of(entityName);
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

    public String getIdentifierKey() {
        return entityName;
    }

    @Override
    public String getTranslationKey() {
        return entityName;
    }

    @Override
    public void render(GLX context, int index, int x, int y, int entryWidth, int entryHeight, int mouseX, int mouseY, float tickDelta) {
        FontRenderer.drawString(context, getName(), x + 28, y + ((entryHeight / 2) - (FontRenderer.getFontHeight() / 2)) - 3, 0xFFFFFF);
    }

}
