package me.deftware.client.framework.world.block;

import lombok.Getter;
import lombok.Setter;
import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.fonts.FontRenderer;
import me.deftware.client.framework.gui.widgets.SelectableList;
import me.deftware.client.framework.item.IItem;
import me.deftware.client.framework.registry.Identifiable;
import me.deftware.client.framework.render.ItemRenderer;
import me.deftware.client.framework.world.block.types.CropBlock;
import me.deftware.client.framework.world.block.types.ShulkerBlock;
import me.deftware.client.framework.world.block.types.StorageBlock;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * @author Deftware
 */
public class Block implements IItem, SelectableList.ListItem, Identifiable {

	protected final net.minecraft.block.Block block;
	protected BlockPosition blockPosition;
	private @Setter @Getter BlockState locationBlockState = null;

	public static Block newInstance(net.minecraft.block.Block block) {
		if (block instanceof net.minecraft.block.CropBlock) {
			return new CropBlock(block);
		} else if (block instanceof ShulkerBoxBlock) {
			return new ShulkerBlock(block);
		} else if (block instanceof ChestBlock || block instanceof BarrelBlock || block instanceof EnderChestBlock) {
			return StorageBlock.newInstance(block);
		} else if (InteractableBlock.isInteractable(block)) {
			return new InteractableBlock(block);
		}
		return new Block(block);
	}

	protected Block(net.minecraft.block.Block block) {
		this.block = block;
	}

	public void setBlockPosition(BlockPosition position) {
		this.blockPosition = position;
	}

	public BlockPosition getBlockPosition() {
		return blockPosition;
	}

	public net.minecraft.block.Block getMinecraftBlock() {
		return block;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Block) {
			return ((Block) object).getMinecraftBlock() == getMinecraftBlock() ||
					((Block) object).getMinecraftBlock().getTranslationKey().equalsIgnoreCase(getMinecraftBlock().getTranslationKey());
		}
		return false;
	}

	public InputStream getAsset() throws IOException {
		Identifier blockResource = Registry.BLOCK.getId(block);
		Identifier blockTexture = new Identifier(blockResource.getNamespace(), "textures/block/" + blockResource.getPath() + ".png");
		Optional<Resource> resource = MinecraftClient.getInstance().getResourceManager().getResource(blockTexture);
		return resource.orElseThrow(() -> new IOException("Unable to find resource")).getInputStream();
	}

	public boolean isAir() {
		return block instanceof AirBlock;
	}

	public boolean isCaveAir() {
		return block == Blocks.CAVE_AIR;
	}

	public boolean isLiquid() {
		return block == Blocks.WATER || block == Blocks.LAVA || block instanceof FluidBlock;
	}

	public int getID() {
		return Registry.BLOCK.getRawId(block);
	}

	public Message getName() {
		return (Message) block.getName();
	}

	public String getIdentifierKey() {
		return Registry.BLOCK.getId(block).getPath();
	}

	public String getTranslationKey() {
		return block.getTranslationKey();
	}

	public boolean instanceOf(BlockType type) {
		return type.instanceOf(this);
	}

	@Override
	public Item getAsItem() {
		return getMinecraftBlock().asItem();
	}

	@Override
	public void render(int index, int x, int y, int entryWidth, int entryHeight, int mouseX, int mouseY, float tickDelta) {
		ItemRenderer.drawBlock(x, y + 5, this);
		FontRenderer.drawString(getName(), x + 28, y + ((entryHeight / 2) - (FontRenderer.getFontHeight() / 2)) - 3, 0xFFFFFF);
	}

}
