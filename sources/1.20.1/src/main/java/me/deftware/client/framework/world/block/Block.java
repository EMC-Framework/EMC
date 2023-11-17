package me.deftware.client.framework.world.block;

import me.deftware.client.framework.fonts.FontRenderer;
import me.deftware.client.framework.gui.widgets.SelectableList;
import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.item.Itemizable;
import me.deftware.client.framework.item.items.BlockItem;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.render.ItemRendering;
import me.deftware.client.framework.render.gl.GLX;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

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
        Identifier blockResource = Registries.BLOCK.getId((net.minecraft.block.Block) this);
        Identifier blockTexture = new Identifier(blockResource.getNamespace(), "textures/block/" + blockResource.getPath() + ".png");
        Optional<Resource> resource = MinecraftClient.getInstance().getResourceManager().getResource(blockTexture);
        return resource.orElseThrow(() -> new IOException("Unable to find resource")).getInputStream();
    }

    @Override
    default void render(GLX context, int index, int x, int y, int entryWidth, int entryHeight, int mouseX, int mouseY, float tickDelta) {
        ItemRendering.getInstance().drawBlock(context, x, y + 5, this);
        FontRenderer.drawString(context, getName(), x + 28, y + ((entryHeight / 2) - (FontRenderer.getFontHeight() / 2)) - 3, 0xFFFFFF);
    }

    static Block of(Item item) {
        if (!(item instanceof BlockItem)) {
            throw new IllegalArgumentException("Supplied item must be a block item");
        }
        return (Block) net.minecraft.block.Block.getBlockFromItem((net.minecraft.item.Item) item);
    }

}
