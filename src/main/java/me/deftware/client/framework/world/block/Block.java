package me.deftware.client.framework.world.block;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.fonts.FontRenderer;
import me.deftware.client.framework.gui.widgets.SelectableList;
import me.deftware.client.framework.item.IItem;
import me.deftware.client.framework.math.position.BlockPosition;
import me.deftware.client.framework.render.ItemRenderer;
import me.deftware.client.framework.world.block.types.CropBlock;
import me.deftware.client.framework.world.block.types.ShulkerBlock;
import me.deftware.client.framework.world.block.types.StorageBlock;
import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.registry.IRegistry;

/**
 * @author Deftware
 */
public class Block implements IItem, SelectableList.ListItem {

	protected final net.minecraft.block.Block block;
	protected BlockPosition blockPosition;
	private BlockState locationBlockState = null;

	public static Block newInstance(net.minecraft.block.Block block) {
		if (block instanceof BlockCrops) {
			return new CropBlock(block);
		} else if (block instanceof BlockShulkerBox) {
			return new ShulkerBlock(block);
		} else if (block instanceof BlockChest || block instanceof BlockEnderChest) {
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
			return ((Block) object).getMinecraftBlock() == getMinecraftBlock() || ((Block) object).getMinecraftBlock().getTranslationKey().equalsIgnoreCase(getMinecraftBlock().getTranslationKey());
		}
		return false;
	}

	public BlockState getDefaultBlockState() {
		return new BlockState(block.getDefaultState());
	}

	public boolean isAir() {
		return block instanceof BlockAir;
	}

	public boolean isCaveAir() {
		return block == Blocks.CAVE_AIR;
	}

	public boolean isLiquid() {
		return block == Blocks.WATER || block == Blocks.LAVA || block instanceof BlockFlowingFluid;
	}

	public int getID() {
		return IRegistry.BLOCK.getId(block);
	}

	public ChatMessage getName() {
		return new ChatMessage().fromText(block.getNameTextComponent());
	}

	public String getIdentifierKey() {
		return getTranslationKey().substring("block.minecraft.".length());
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

	public void setLocationBlockState(final BlockState locationBlockState) {
		this.locationBlockState = locationBlockState;
	}

	public BlockState getLocationBlockState() {
		return this.locationBlockState;
	}

	@Override
	public void render(int index, int x, int y, int entryWidth, int entryHeight, int mouseX, int mouseY, float tickDelta) {
		ItemRenderer.drawBlock(x, y + 5, this);
		FontRenderer.drawString(getName(), x + 28, y + ((entryHeight / 2) - (FontRenderer.getFontHeight() / 2)) - 3, 0xFFFFFF);
	}

}
