package me.deftware.client.framework.world.block;

import me.deftware.client.framework.fonts.FontRenderer;
import me.deftware.client.framework.gui.widgets.SelectableList;
import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.item.Itemizable;
import me.deftware.client.framework.item.items.BlockItem;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.render.ItemRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;

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
        Identifier blockResource = Registry.BLOCK.getId((net.minecraft.block.Block) this);
        Identifier blockTexture = new Identifier(blockResource.getNamespace(), "textures/block/" + blockResource.getPath() + ".png");
        Optional<Resource> resource = MinecraftClient.getInstance().getResourceManager().getResource(blockTexture);
        return resource.orElseThrow(() -> new IOException("Unable to find resource")).getInputStream();
    }

    @Override
    default void render(int index, int x, int y, int entryWidth, int entryHeight, int mouseX, int mouseY, float tickDelta) {
        ItemRenderer.getInstance().drawBlock(x, y + 5, this);
        FontRenderer.drawString(getName(), x + 28, y + ((entryHeight / 2) - (FontRenderer.getFontHeight() / 2)) - 3, 0xFFFFFF);
    }

    static Block of(Item item) {
        if (!(item instanceof BlockItem)) {
            throw new IllegalArgumentException("Supplied item must be a block item");
        }
        return (Block) net.minecraft.block.Block.getBlockFromItem((net.minecraft.item.Item) item);
    }

}
