package me.deftware.client.framework.entity.types.main;

import me.deftware.client.framework.conversion.ConvertedList;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.inventory.Slot;
import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.math.position.BlockPosition;
import me.deftware.client.framework.math.vector.Vector3d;
import me.deftware.client.framework.minecraft.Minecraft;
import me.deftware.client.framework.world.EnumFacing;
import me.deftware.mixin.imp.IMixinEntityPlayerSP;
import me.deftware.mixin.imp.IMixinEntityRenderer;
import me.deftware.mixin.imp.IMixinMinecraft;
import me.deftware.mixin.imp.IMixinPlayerControllerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * The main Minecraft player
 *
 * @author Deftware
 */
public class MainEntityPlayer extends RotationLogic {

	private final ConvertedList<Slot, net.minecraft.inventory.Slot> inventorySlots =
			new ConvertedList<>(() -> Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().player).inventoryContainer.inventorySlots, pair ->
					pair.getLeft().getMinecraftSlot() == Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().player).inventoryContainer.inventorySlots.get(pair.getRight())
					, Slot::new);

	public MainEntityPlayer(EntityPlayer entity) {
		super(entity);
	}

	@Override
	public EntityPlayerSP getMinecraftEntity() {
		return (EntityPlayerSP) entity;
	}

	public boolean processRightClickBlock(BlockPosition pos, EnumFacing facing, Vector3d vector3d) {
		return Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().playerController).processRightClickBlock(net.minecraft.client.Minecraft.getInstance().player,
				net.minecraft.client.Minecraft.getInstance().world, pos.getMinecraftBlockPos(), facing.getFacing(), vector3d.getMinecraftVector(), EnumHand.MAIN_HAND) == EnumActionResult.SUCCESS;
	}

	public void swapHands() {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().player).connection.sendPacket(new CPacketPlayerDigging(
				CPacketPlayerDigging.Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, net.minecraft.util.EnumFacing.DOWN));
	}

	public void setRightClickDelayTimer(int delay) {
		((IMixinMinecraft) net.minecraft.client.Minecraft.getInstance()).setRightClickDelayTimer(delay);
	}

	public void processRightClick(boolean offhand) {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().playerController).processRightClick(net.minecraft.client.Minecraft.getInstance().player, net.minecraft.client.Minecraft.getInstance().world, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
	}

	public void resetBlockRemoving() {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().playerController).resetBlockRemoving();
	}

	public void setPlayerHittingBlock(boolean state) {
		((IMixinPlayerControllerMP) Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().playerController)).setPlayerHittingBlock(state);
	}

	public float getPlayerFovMultiplier() {
		return ((IMixinEntityRenderer) net.minecraft.client.Minecraft.getInstance().gameRenderer).getFovMultiplier();
	}

	public void updatePlayerFovMultiplier(float newValue) {
		((IMixinEntityRenderer) net.minecraft.client.Minecraft.getInstance().gameRenderer).updateFovMultiplier(newValue);
	}

	public void swingArmClientSide() {
		getMinecraftEntity().swingArm(EnumHand.MAIN_HAND);
	}

	public void attackEntity(Entity entity) {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().playerController)
				.attackEntity(getMinecraftEntity(), entity.getMinecraftEntity());
		swingArmClientSide();
	}

	public void setHorseJumpPower(float f) {
		Objects.requireNonNull(((IMixinEntityPlayerSP) net.minecraft.client.Minecraft.getInstance().player))
				.setHorseJumpPower(f);
	}

	public void sendChatMessage(String message, Class<?> sender) {
		Objects.requireNonNull(((IMixinEntityPlayerSP) net.minecraft.client.Minecraft.getInstance().player)).sendChatMessageWithSender(message, sender);
	}

	private MovementInput getInput() {
		return getMinecraftEntity().movementInput;
	}

	public double getForward() {
		return getInput().moveForward;
	}

	public double getStrafe() {
		return getInput().moveStrafe;
	}

	public void toggleSkinLayers() {
		Set<?> activeParts = net.minecraft.client.Minecraft.getInstance().gameSettings.getModelParts();
		for (EnumPlayerModelParts part : EnumPlayerModelParts.values()) {
			net.minecraft.client.Minecraft.getInstance().gameSettings.setModelPartEnabled(part, !activeParts.contains(part));
		}
	}

	public void closeHandledScreen() {
		getMinecraftEntity().closeScreen();
	}

	/*
		Inventory management
	 */

	public List<Slot> getInventorySlots() {
		return inventorySlots.poll();
	}

	/**
	 * Moves an item from the main inventory into the hotbar
	 */
	public void moveToHotBar(int slot, int hotbar, int windowId) {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().playerController).windowClick(windowId, slot, hotbar, ClickType.SWAP,
				net.minecraft.client.Minecraft.getInstance().player);
		Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().playerController).tick();
	}

	public boolean placeStackInHotbar(ItemStack stack) {
		for (int index = 0; index < 9; index++) {
			if (Objects.requireNonNull(Minecraft.getPlayer()).getInventory().getStackInSlot(index).isEmpty()) {
				Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().player).connection
						.sendPacket(new CPacketCreativeInventoryAction(36 + index, stack.getMinecraftItemStack()));
				return true;
			}
		}
		return false;
	}

	public void windowClick(int id, int next, WindowClickAction type) {
		windowClick(0, id, next, type);
	}

	public void windowClick(int windowID, int id, int next, WindowClickAction type) {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().playerController).windowClick(windowID, id, next,
				type.getMinecraftActionType(), net.minecraft.client.Minecraft.getInstance().player);
	}

}
