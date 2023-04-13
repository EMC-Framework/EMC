package me.deftware.client.framework.entity.types.main;

import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.entity.EntityHand;
import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.minecraft.Minecraft;
import me.deftware.client.framework.world.EnumFacing;
import me.deftware.mixin.imp.IMixinEntityPlayerSP;
import me.deftware.mixin.imp.IMixinEntityRenderer;
import me.deftware.mixin.imp.IMixinPlayerControllerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Vec3;

import java.util.Objects;
import java.util.Set;

/**
 * The main Minecraft player
 *
 * @author Deftware
 */
public class MainEntityPlayer extends me.deftware.client.framework.entity.types.EntityPlayer {

	public MainEntityPlayer(EntityPlayer entity) {
		super(entity);
	}

	@Override
	public EntityPlayerSP getMinecraftEntity() {
		return (EntityPlayerSP) entity;
	}

	public boolean processRightClickBlock(BlockPosition pos, EnumFacing facing, Vector3<Double> vector3d) {
		return processRightClickBlock(pos, facing, vector3d, EntityHand.MainHand);
	}

	public boolean processRightClickBlock(BlockPosition pos, EnumFacing facing, Vector3<Double> vector3d, EntityHand hand) {
		return Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().playerController).onPlayerRightClick(net.minecraft.client.Minecraft.getMinecraft().thePlayer,
				net.minecraft.client.Minecraft.getMinecraft().theWorld, net.minecraft.client.Minecraft.getMinecraft().thePlayer.getHeldItem(), (BlockPos) pos, facing.getFacing(), (Vec3) vector3d);
	}

	public void swapHands() { }

	public void processRightClick(boolean offhand) {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().playerController)
				.onPlayerRightClick(net.minecraft.client.Minecraft.getMinecraft().thePlayer, net.minecraft.client.Minecraft.getMinecraft().theWorld,
						net.minecraft.client.Minecraft.getMinecraft().thePlayer.getHeldItem(),
						net.minecraft.client.Minecraft.getMinecraft().thePlayer.getPosition(),
						net.minecraft.client.Minecraft.getMinecraft().thePlayer.getHorizontalFacing(), net.minecraft.client.Minecraft.getMinecraft().thePlayer.getLookVec());
	}

	public void resetBlockRemoving() {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().playerController).resetBlockRemoving();
	}

	public void setPlayerHittingBlock(boolean state) {
		((IMixinPlayerControllerMP) Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().playerController)).setPlayerHittingBlock(state);
	}

	public float getPlayerFovMultiplier() {
		return ((IMixinEntityRenderer) net.minecraft.client.Minecraft.getMinecraft().entityRenderer).getFovMultiplier();
	}

	public void updatePlayerFovMultiplier(float newValue) {
		((IMixinEntityRenderer) net.minecraft.client.Minecraft.getMinecraft().entityRenderer).updateFovMultiplier(newValue);
	}

	public void swingArmClientSide() {
		swingArmClientSide(EntityHand.MainHand);
	}

	public void swingArmClientSide(EntityHand hand) {
		getMinecraftEntity().swingItem();
	}

	public void attackEntity(Entity entity) {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().playerController)
				.attackEntity(getMinecraftEntity(), entity.getMinecraftEntity());
		swingArmClientSide();
	}

	public void setHorseJumpPower(float f) {
		Objects.requireNonNull(((IMixinEntityPlayerSP) net.minecraft.client.Minecraft.getMinecraft().thePlayer))
				.setHorseJumpPower(f);
	}

	public void sendMessage(String message) {
		this.getMinecraftEntity().sendChatMessage(message);
	}

	public void sendMessage(String message, Class<?> sender) {
		((IMixinEntityPlayerSP) this.getMinecraftEntity()).sendChatMessageWithSender(message, sender);
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
		Set<?> activeParts = net.minecraft.client.Minecraft.getMinecraft().gameSettings.getModelParts();
		for (EnumPlayerModelParts part : EnumPlayerModelParts.values()) {
			net.minecraft.client.Minecraft.getMinecraft().gameSettings.setModelPartEnabled(part, !activeParts.contains(part));
		}
	}

	public void closeHandledScreen() {
		getMinecraftEntity().closeScreen();
	}

	/*
		Inventory management
	 */

	/**
	 * Moves an item from the main inventory into the hotbar
	 */
	public void moveToHotBar(int slot, int hotbar, int windowId) {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().playerController).windowClick(windowId, slot, hotbar, 2,
				net.minecraft.client.Minecraft.getMinecraft().thePlayer);
		Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().playerController).updateController();
	}

	public boolean placeStackInHotbar(ItemStack stack) {
		for (int index = 0; index < 9; index++) {
			if (Objects.requireNonNull(Minecraft.getMinecraftGame()._getPlayer()).getInventory().getStackInSlot(index).isEmpty()) {
				Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().thePlayer).sendQueue
						.addToSendQueue(new C10PacketCreativeInventoryAction(36 + index, stack.getMinecraftItemStack()));
				return true;
			}
		}
		return false;
	}

	public void windowClick(int id, int next, WindowClickAction type) {
		windowClick(0, id, next, type);
	}

	public void windowClick(int windowID, int id, int next, WindowClickAction type) {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().playerController).windowClick(windowID, id, next,
				type.getMinecraftActionType(), net.minecraft.client.Minecraft.getMinecraft().thePlayer);
	}

}
