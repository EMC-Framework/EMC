package me.deftware.client.framework.entity.types.main;

import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.entity.EntityHand;
import me.deftware.client.framework.entity.types.EntityPlayer;
import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.minecraft.Minecraft;
import me.deftware.client.framework.world.EnumFacing;
import me.deftware.mixin.imp.IMixinEntityPlayerSP;
import me.deftware.mixin.imp.IMixinEntityRenderer;
import me.deftware.mixin.imp.IMixinPlayerControllerMP;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import java.util.Objects;

/**
 * The main Minecraft player
 *
 * @author Deftware
 */
public class MainEntityPlayer extends EntityPlayer {

	public MainEntityPlayer(Player entity) {
		super(entity);
	}

	@Override
	public LocalPlayer getMinecraftEntity() {
		return (LocalPlayer) entity;
	}

	public boolean processRightClickBlock(BlockPosition pos, EnumFacing facing, Vector3<Double> vector3d) {
		return processRightClickBlock(pos, facing, vector3d, EntityHand.MainHand);
	}

	public boolean processRightClickBlock(BlockPosition pos, EnumFacing facing, Vector3<Double> vector3d, EntityHand hand) {
		BlockHitResult customHitResult = new BlockHitResult((Vec3) vector3d, facing.getFacing(), (BlockPos) pos, false);
		return Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().gameMode).useItemOn(net.minecraft.client.Minecraft.getInstance().player, hand.getMinecraftHand(), customHitResult) == InteractionResult.SUCCESS;
	}

	public void swapHands() {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().player).connection.send(new ServerboundPlayerActionPacket(
				ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ZERO, Direction.DOWN));
	}

	public void processRightClick(boolean offhand) {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().gameMode).useItem(net.minecraft.client.Minecraft.getInstance().player, offhand ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
	}

	public void resetBlockRemoving() {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().gameMode).stopDestroyBlock();
	}

	public void setPlayerHittingBlock(boolean state) {
		((IMixinPlayerControllerMP) Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().gameMode)).setPlayerHittingBlock(state);
	}

	public float getPlayerFovMultiplier() {
		return ((IMixinEntityRenderer) net.minecraft.client.Minecraft.getInstance().gameRenderer).getFovMultiplier();
	}

	public void updatePlayerFovMultiplier(float newValue) {
		((IMixinEntityRenderer) net.minecraft.client.Minecraft.getInstance().gameRenderer).updateFovMultiplier(newValue);
	}

	public void swingArmClientSide() {
		swingArmClientSide(EntityHand.MainHand);
	}

	public void swingArmClientSide(EntityHand hand) {
		getMinecraftEntity().swing(hand.getMinecraftHand(), net.minecraft.world.item.component.SwingAnimation.DEFAULT, true);
	}

	public void attackEntity(Entity entity) {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().gameMode)
				.attack(getMinecraftEntity(), entity.getMinecraftEntity());
		swingArmClientSide();
	}

	public void setHorseJumpPower(float f) {
		Objects.requireNonNull(((IMixinEntityPlayerSP) net.minecraft.client.Minecraft.getInstance().player))
				.setHorseJumpPower(f);
	}

	public void sendMessage(String text, Class<?> sender) {
		Minecraft.getMinecraftGame().getChatSender().send(text, sender);
	}

	public void sendMessage(String text) {
		var networkHandler = this.getMinecraftEntity().connection;
		if (text.startsWith("/")) {
			text = text.substring(1);
			networkHandler.sendCommand(text);
		} else {
			networkHandler.sendChat(text);
		}
	}

	private ClientInput getInput() {
		return getMinecraftEntity().input;
	}

	public double getForward() {
		return getInput().getMoveVector().y;
	}

	public double getStrafe() {
		return getInput().getMoveVector().x;
	}

	public void toggleSkinLayers() {
		var options = net.minecraft.client.Minecraft.getInstance().options;
		for (PlayerModelPart part : PlayerModelPart.values()) {
			options.setModelPart(part, !options.isModelPartEnabled(part));
		}
	}

	public void closeHandledScreen() {
		getMinecraftEntity().closeContainer();
	}

	/*
		Inventory management
	 */

	/**
	 * Moves an item from the main inventory into the hotbar
	 */
	public void moveToHotBar(int slot, int hotbar, int windowId) {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().gameMode).handleContainerInput(windowId, slot, hotbar, ContainerInput.SWAP,
				net.minecraft.client.Minecraft.getInstance().player);
		Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().gameMode).tick();
	}

	public boolean placeStackInHotbar(ItemStack stack) {
		for (int index = 0; index < 9; index++) {
			if (Objects.requireNonNull(Minecraft.getMinecraftGame()._getPlayer()).getInventory().getStackInSlot(index).isEmpty()) {
				Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().player).connection
						.send(new ServerboundSetCreativeModeSlotPacket(36 + index, (net.minecraft.world.item.ItemStack) stack));
				return true;
			}
		}
		return false;
	}

	public void windowClick(int id, int next, WindowClickAction type) {
		windowClick(0, id, next, type);
	}

	public void windowClick(int windowID, int id, int next, WindowClickAction type) {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().gameMode).handleContainerInput(windowID, id, next,
				type.getMinecraftActionType(), net.minecraft.client.Minecraft.getInstance().player);
	}

}
