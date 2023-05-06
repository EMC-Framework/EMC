package me.deftware.client.framework.render;

import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

public class ItemRenderer {

	private static final ItemRenderer INSTANCE = new ItemRenderer();

	private final HashMap<Block, ItemStack> blockToStack = new HashMap<>();
	private final HashMap<Item, ItemStack> itemToStack = new HashMap<>();

	public static ItemRenderer getInstance() {
		return INSTANCE;
	}

	public void drawBlock(int x, int y, Block block) {
		drawItemStack(x, y, blockToStack.computeIfAbsent(
				block, k -> ItemStack.of(k, 1)
		));
	}

	public void drawItem(int x, int y, Item item) {
		drawItemStack(x, y, itemToStack.computeIfAbsent(
				item, k -> ItemStack.of(k, 1)
		));
	}

	public void drawItemStack(int x, int y, ItemStack stack) {
		try {
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			renderItemAndEffectIntoGUI(stack, x, y);
			renderItemOverlays(stack, x, y);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void setRenderZLevel(float z) {
		getRenderItem().zLevel = z;
	}

	private static RenderItem getRenderItem() {
		return Minecraft.getMinecraft().getRenderItem();
	}

	public void renderItemIntoGUI(ItemStack itemStack, int x, int y) {
		getRenderItem().renderItemIntoGUI((net.minecraft.item.ItemStack) itemStack, x, y);
	}

	public void renderItemOverlays(ItemStack itemStack, int x, int y) {
		getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRenderer, (net.minecraft.item.ItemStack) itemStack, x, y);
	}

	public void renderItemAndEffectIntoGUI(ItemStack itemStack, int x, int y) {
		getRenderItem().renderItemAndEffectIntoGUI((net.minecraft.item.ItemStack) itemStack, x, y);
	}

	public void renderItemOverlayIntoGUI(ItemStack itemStack, int x, int y, String text) {
		getRenderItem().renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, (net.minecraft.item.ItemStack) itemStack, x, y, text);
	}

}
