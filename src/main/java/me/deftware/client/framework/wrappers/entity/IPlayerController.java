package me.deftware.client.framework.wrappers.entity;

import me.deftware.mixin.imp.IMixinPlayerControllerMP;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class IPlayerController {

    public static void windowClick(int id, int next, IClickType type) {
        windowClick(0, id, next, type);
    }

    public static void windowClick(int windowID, int id, int next, IClickType type) {
        Minecraft.getMinecraft().playerController.windowClick(windowID, id, next,
                type.equals(IClickType.THROW) ? 4 :
                        type.equals(IClickType.QUICK_MOVE) ? 1 :
                                0,
                Minecraft.getMinecraft().thePlayer);
    }

    /**
     * Looks for an item in the inventory and returns the slot id where the item is located,
     * returns -1 if the player does not have the item in their inventory
     */
    public static int getSlot(String name) {
        InventoryPlayer in = Minecraft.getMinecraft().thePlayer.inventory;
        for (int i = 0; i < in.mainInventory.length; i++) {
            ItemStack it = in.mainInventory[i];
            if (it.getItem().getUnlocalizedName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Swaps a stack in the same inventory
     */
    @SuppressWarnings("Duplicates")
    public static void swapStack(int one, int two, int windowId) {
        Minecraft.getMinecraft().playerController.windowClick(windowId, one, 0, 2,
                Minecraft.getMinecraft().thePlayer);
        Minecraft.getMinecraft().playerController.windowClick(windowId, two, 0, 2,
                Minecraft.getMinecraft().thePlayer);
        Minecraft.getMinecraft().playerController.updateController();
    }

    /**
     * Swaps a stack between one inventory to another
     */
    @SuppressWarnings("Duplicates")
    public static void swapStack(int srcInventoryId, int dstInventoryId, int srcSlot, int dstSlot){
        Minecraft.getMinecraft().playerController.windowClick(srcInventoryId, srcSlot, 0,
                2, Minecraft.getMinecraft().thePlayer);
        Minecraft.getMinecraft().playerController.windowClick(dstInventoryId, dstSlot, 0,
                2, Minecraft.getMinecraft().thePlayer);
        Minecraft.getMinecraft().playerController.updateController();
    }

    public static void moveStack(int srcInventoryId, int dstInventoryId, int srcSlot, int dstSlot){
        Minecraft.getMinecraft().playerController.windowClick(srcInventoryId, srcSlot, 0,
                1, Minecraft.getMinecraft().thePlayer);
        Minecraft.getMinecraft().playerController.windowClick(dstInventoryId, dstSlot, 0,
                1, Minecraft.getMinecraft().thePlayer);
    }

    public static void moveItem(int slotId) {
        Minecraft.getMinecraft().playerController.windowClick(0, slotId, 0, 1,
                Minecraft.getMinecraft().thePlayer);
    }

    public static void processRightClick(boolean offhand) {
        Minecraft.getMinecraft().playerController.onPlayerRightClick(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getHeldItem(), Minecraft.getMinecraft().thePlayer.getPosition(), Minecraft.getMinecraft().thePlayer.getHorizontalFacing(), Minecraft.getMinecraft().thePlayer.getLookVec());
    }

    public static void resetBlockRemoving() {
        Minecraft.getMinecraft().playerController.resetBlockRemoving();
    }

    public static void setPlayerHittingBlock(boolean state) {
        ((IMixinPlayerControllerMP) Minecraft.getMinecraft().playerController).setPlayerHittingBlock(state);
    }

    public enum IClickType {
        THROW, QUICK_MOVE, PICKUP
    }

}
