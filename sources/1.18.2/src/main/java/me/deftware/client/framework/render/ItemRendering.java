package me.deftware.client.framework.render;

import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.render.gl.GLX;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemRenderer;

import java.util.HashMap;

public class ItemRendering {

	private static final ItemRendering INSTANCE = new ItemRendering();

	private final HashMap<Block, ItemStack> blockToStack = new HashMap<>();
	private final HashMap<Item, ItemStack> itemToStack = new HashMap<>();

	public static ItemRendering getInstance() {
		return INSTANCE;
	}

	public void drawBlock(GLX context, int x, int y, Block block) {
		drawBlock(context, x, y, 0, block);
	}

	public void drawBlock(GLX context, int x, int y, int z, Block block) {
		drawItemStack(context, blockToStack.computeIfAbsent(
				block, k -> ItemStack.of(k, 1)
		), x, y, z);
	}

	public void drawItem(GLX context, int x, int y, Item item) {
		drawItem(context, x, y, 0, item);
	}

	public void drawItem(GLX context, int x, int y, int z, Item item) {
		drawItemStack(context, itemToStack.computeIfAbsent(
				item, k -> ItemStack.of(k, 1)
		), x, y, z);
	}

	public void drawItemStack(GLX context, ItemStack stack, int x, int y) {
		drawItemStack(context, stack, x, y, 0);
	}

	public void drawItemStack(GLX context, ItemStack stack, int x, int y, int z) {
		render(context, z, () -> getRenderItem().renderInGui((net.minecraft.item.ItemStack) stack, x, y));
	}

	public void drawStackLabel(GLX context, ItemStack stack, int x, int y, String count) {
		drawStackLabel(context, stack, x, y, 0, count);
	}

	public void drawStackLabel(GLX context, ItemStack stack, int x, int y, int z, String count) {
		render(context, z, () ->
				getRenderItem().renderGuiItemOverlay(MinecraftClient.getInstance().textRenderer,
						(net.minecraft.item.ItemStack) stack, x, y, count));
	}

	private ItemRenderer getRenderItem() {
		return MinecraftClient.getInstance().getItemRenderer();
	}


	private void render(GLX ctx, int zOffset, Runnable runnable) {
		ctx.modelViewStack(() -> {
			float prev = getRenderItem().zOffset;
			getRenderItem().zOffset = zOffset;
			runnable.run();
			getRenderItem().zOffset = prev;
		});
	}

}
