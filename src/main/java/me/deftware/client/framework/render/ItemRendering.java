package me.deftware.client.framework.render;

import com.google.common.base.Suppliers;
import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.render.gl.GLX;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
		try {
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			render(context, z, ctx -> ctx.drawItemWithoutEntity((net.minecraft.item.ItemStack) stack, x, y));
			GL11.glDisable(GL11.GL_DEPTH_TEST);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void drawStackLabel(GLX context, ItemStack stack, int x, int y, String count) {
		drawStackLabel(context, stack, x, y, 0, count);
	}

	public void drawStackLabel(GLX context, ItemStack stack, int x, int y, int z, String count) {
		render(context, z, ctx ->
				ctx.drawItemInSlot(MinecraftClient.getInstance().textRenderer,
						(net.minecraft.item.ItemStack) stack, x, y, count));
	}

	private final Supplier<DrawContext> context = Suppliers.memoize(() -> {
		var mc = MinecraftClient.getInstance();
		var consumers = mc.getBufferBuilders().getEntityVertexConsumers();
		return new DrawContext(mc, consumers);
	});

	private void render(GLX ctx, int zOffset, Consumer<DrawContext> runnable) {
		ctx.modelViewStack(s -> {
			DrawContext drawContext = context.get();
			MatrixStack stack = drawContext.getMatrices();
			stack.push();
			stack.translate(0, 0, zOffset);
			runnable.accept(drawContext);
			stack.pop();
		});
	}

}
