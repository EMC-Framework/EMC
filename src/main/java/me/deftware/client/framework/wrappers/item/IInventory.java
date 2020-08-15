package me.deftware.client.framework.wrappers.item;

import me.deftware.client.framework.wrappers.entity.IEntity;
import me.deftware.mixin.imp.IMixinEntityLivingBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.inventory.GuiInventory;

public class IInventory {

    private static IEntity entity;

    public static int getFirstEmptyStack() {
        return Minecraft.getMinecraft().thePlayer.inventory.getFirstEmptyStack();
    }

    public static int getCurrentItem() {
        return Minecraft.getMinecraft().thePlayer.inventory.currentItem;
    }

    public static void setCurrentItem(int id) {
        Minecraft.getMinecraft().thePlayer.inventory.currentItem = id;
    }

    public static int getItemInUseCount() {
        return ((IMixinEntityLivingBase) Minecraft.getMinecraft().thePlayer).getActiveItemStackUseCount();
    }

    public static void swapHands() {
        //Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
        //        C07PacketPlayerDigging.Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
    }

    public static void openEntityInventory(IEntity entity) {
        IInventory.entity = entity;
    }

    public static void onRender() {
        if (IInventory.entity != null) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiInventory((EntityOtherPlayerMP) IInventory.entity.getEntity()));
            IInventory.entity = null;
        }
    }

}
