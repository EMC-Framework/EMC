package me.deftware.client.framework.render;

import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.render.gl.GLX;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.function.Consumer;

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

	private static float zOffset = 0;

	public static void setRenderZLevel(float z) {
		zOffset = z;
	}

	private static net.minecraft.client.render.item.ItemRenderer getRenderItem() {
		return MinecraftClient.getInstance().getItemRenderer();
	}

	public void renderItemIntoGUI(ItemStack itemStack, int x, int y) {
		render(stack -> getRenderItem().renderGuiItemIcon(stack, (net.minecraft.item.ItemStack) itemStack, x, y));
	}

	public void renderItemOverlays(ItemStack itemStack, int x, int y) {
		render(stack -> getRenderItem().renderGuiItemOverlay(stack, MinecraftClient.getInstance().textRenderer, (net.minecraft.item.ItemStack) itemStack, x, y));
	}

	public void renderItemAndEffectIntoGUI(ItemStack itemStack, int x, int y) {
		render(stack -> getRenderItem().renderInGui(stack, (net.minecraft.item.ItemStack) itemStack, x, y));
	}

	public void renderItemOverlayIntoGUI(ItemStack itemStack, int x, int y, String text) {
		render(stack -> getRenderItem().renderGuiItemOverlay(stack, MinecraftClient.getInstance().textRenderer, (net.minecraft.item.ItemStack) itemStack, x, y, text));
	}

	private final MatrixStack stack = new MatrixStack();

	private void render(Consumer<MatrixStack> runnable) {
		GLX.INSTANCE.modelViewStack(s -> {
			stack.push();
			stack.translate(0, 0, zOffset);
			runnable.accept(stack);
			stack.pop();
		});
	}

}
