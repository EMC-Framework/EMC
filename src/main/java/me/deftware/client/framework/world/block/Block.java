package me.deftware.client.framework.world.block;

import me.deftware.client.framework.fonts.FontRenderer;
import me.deftware.client.framework.gui.widgets.SelectableList;
import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.item.Itemizable;
import me.deftware.client.framework.item.items.BlockItem;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.render.ItemRendering;
import me.deftware.client.framework.render.gl.GLX;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * @author Deftware
 */
public interface Block extends Itemizable, SelectableList.ListItem {

    int getID();

    Message getName();

    default InputStream getAsset() throws IOException {
        Identifier blockResource = BuiltInRegistries.BLOCK.getKey((net.minecraft.world.level.block.Block) this);
        Identifier blockTexture = Identifier.fromNamespaceAndPath(blockResource.getNamespace(), "textures/block/" + blockResource.getPath() + ".png");
        Optional<Resource> resource = Minecraft.getInstance().getResourceManager().getResource(blockTexture);
        return resource.orElseThrow(() -> new IOException("Unable to find resource")).open();
    }

    @Override
    default void render(GLX context, int index, int x, int y, int entryWidth, int entryHeight, int mouseX, int mouseY, float tickDelta) {
        ItemRendering.getInstance().drawBlock(context, x, y + 5, this);
        FontRenderer.drawString(context, getName(), x + 28, y + ((entryHeight / 2) - (FontRenderer.getFontHeight() / 2)) - 3, 0xFFFFFF);
    }

    static Block of(Item item) {
        if (!(item.asMinecraftItem() instanceof BlockItem)) {
            throw new IllegalArgumentException("Supplied item must be a block item");
        }
        return (Block) net.minecraft.world.level.block.Block.byItem(item.asMinecraftItem());
    }

    @Override
    default String getTranslationKey() {
        return ((net.minecraft.world.level.block.Block) this).asItem().getDescriptionId();
    }

}
