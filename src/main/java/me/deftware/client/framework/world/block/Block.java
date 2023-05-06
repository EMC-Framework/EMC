package me.deftware.client.framework.world.block;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.fonts.FontRenderer;
import me.deftware.client.framework.gui.widgets.SelectableList;
import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.item.Itemizable;
import me.deftware.client.framework.item.items.BlockItem;
import me.deftware.client.framework.render.ItemRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Deftware
 */
public interface Block extends Itemizable, SelectableList.ListItem {

    int getID();

    Message getName();

    default InputStream getAsset() throws IOException {
        ResourceLocation blockResource = net.minecraft.block.Block.REGISTRY.getNameForObject((net.minecraft.block.Block) this);
		ResourceLocation blockTexture = new ResourceLocation(blockResource.getNamespace(), "textures/block/" + blockResource.getPath() + ".png");
        return Minecraft.getMinecraft().getResourceManager().getResource(blockTexture).getInputStream();
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
