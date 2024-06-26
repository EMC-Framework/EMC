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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

/**
 * The main Minecraft player
 *
 * @author Deftware
 */
public class MainEntityPlayer extends EntityPlayer {

	public MainEntityPlayer(PlayerEntity entity) {
		super(entity);
	}

	@Override
	public ClientPlayerEntity getMinecraftEntity() {
		return (ClientPlayerEntity) entity;
	}

	public boolean processRightClickBlock(BlockPosition pos, EnumFacing facing, Vector3<Double> vector3d) {
		return processRightClickBlock(pos, facing, vector3d, EntityHand.MainHand);
	}

	public boolean processRightClickBlock(BlockPosition pos, EnumFacing facing, Vector3<Double> vector3d, EntityHand hand) {
		BlockHitResult customHitResult = new BlockHitResult((Vec3d) vector3d, facing.getFacing(), (BlockPos) pos, false);
		return Objects.requireNonNull(MinecraftClient.getInstance().interactionManager).interactBlock(MinecraftClient.getInstance().player, hand.getMinecraftHand(), customHitResult) == ActionResult.SUCCESS;
	}

	public void swapHands() {
		Objects.requireNonNull(MinecraftClient.getInstance().player).networkHandler.sendPacket(new PlayerActionC2SPacket(
				PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
	}

	public void processRightClick(boolean offhand) {
		Objects.requireNonNull(MinecraftClient.getInstance().interactionManager).interactItem(MinecraftClient.getInstance().player, offhand ? Hand.OFF_HAND : Hand.MAIN_HAND);
	}

	public void resetBlockRemoving() {
		Objects.requireNonNull(MinecraftClient.getInstance().interactionManager).cancelBlockBreaking();
	}

	public void setPlayerHittingBlock(boolean state) {
		((IMixinPlayerControllerMP) Objects.requireNonNull(MinecraftClient.getInstance().interactionManager)).setPlayerHittingBlock(state);
	}

	public float getPlayerFovMultiplier() {
		return ((IMixinEntityRenderer) MinecraftClient.getInstance().gameRenderer).getFovMultiplier();
	}

	public void updatePlayerFovMultiplier(float newValue) {
		((IMixinEntityRenderer) MinecraftClient.getInstance().gameRenderer).updateFovMultiplier(newValue);
	}

	public void swingArmClientSide() {
		swingArmClientSide(EntityHand.MainHand);
	}

	public void swingArmClientSide(EntityHand hand) {
		getMinecraftEntity().swingHand(hand.getMinecraftHand());
	}

	public void attackEntity(Entity entity) {
		Objects.requireNonNull(MinecraftClient.getInstance().interactionManager)
				.attackEntity(getMinecraftEntity(), entity.getMinecraftEntity());
		swingArmClientSide();
	}

	public void setHorseJumpPower(float f) {
		Objects.requireNonNull(((IMixinEntityPlayerSP) MinecraftClient.getInstance().player))
				.setHorseJumpPower(f);
	}

	public void sendMessage(String text, Class<?> sender) {
		Minecraft.getMinecraftGame().getChatSender().send(text, sender);
	}

	public void sendMessage(String text) {
		var networkHandler = this.getMinecraftEntity().networkHandler;
		if (text.startsWith("/")) {
			text = text.substring(1);
			networkHandler.sendChatCommand(text);
		} else {
			networkHandler.sendChatMessage(text);
		}
	}

	private Input getInput() {
		return getMinecraftEntity().input;
	}

	public double getForward() {
		return getInput().movementForward;
	}

	public double getStrafe() {
		return getInput().movementSideways;
	}

	public void toggleSkinLayers() {
		for (PlayerModelPart part : PlayerModelPart.values()) {
			MinecraftClient.getInstance().options.togglePlayerModelPart(part, !MinecraftClient.getInstance().options.isPlayerModelPartEnabled(part));
		}
	}

	public void closeHandledScreen() {
		getMinecraftEntity().closeHandledScreen();
	}

	/*
		Inventory management
	 */

	/**
	 * Moves an item from the main inventory into the hotbar
	 */
	public void moveToHotBar(int slot, int hotbar, int windowId) {
		Objects.requireNonNull(MinecraftClient.getInstance().interactionManager).clickSlot(windowId, slot, hotbar, SlotActionType.SWAP,
				MinecraftClient.getInstance().player);
		Objects.requireNonNull(MinecraftClient.getInstance().interactionManager).tick();
	}

	public boolean placeStackInHotbar(ItemStack stack) {
		for (int index = 0; index < 9; index++) {
			if (Objects.requireNonNull(Minecraft.getMinecraftGame()._getPlayer()).getInventory().getStackInSlot(index).isEmpty()) {
				Objects.requireNonNull(MinecraftClient.getInstance().player).networkHandler
						.sendPacket(new CreativeInventoryActionC2SPacket(36 + index, (net.minecraft.item.ItemStack) stack));
				return true;
			}
		}
		return false;
	}

	public void windowClick(int id, int next, WindowClickAction type) {
		windowClick(0, id, next, type);
	}

	public void windowClick(int windowID, int id, int next, WindowClickAction type) {
		Objects.requireNonNull(MinecraftClient.getInstance().interactionManager).clickSlot(windowID, id, next,
				type.getMinecraftActionType(), MinecraftClient.getInstance().player);
	}

}
