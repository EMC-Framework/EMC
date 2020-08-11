package me.deftware.client.framework.wrappers.item;


import me.deftware.client.framework.wrappers.entity.IEntity;
import me.deftware.client.framework.wrappers.entity.IEntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;

import java.util.ArrayList;
import java.util.Collections;

public class IInventoryWrapper {

    public static ArrayList<IItemStack> getArmorInventory(IEntity entity) {
        ArrayList<IItemStack> array = new ArrayList<>();
        if (entity != null) {
            for (ItemStack item : entity.getEntity().getArmorInventoryList()) {
                if (item != null) {
                    IItemStack stack = new IItemStack(item);
                    array.add(stack);
                }
            }
        }

        Collections.reverse(array);
        return array;
    }

    public static boolean hasElytra() {
        if (IEntityPlayer.isNull()) {
            return false;
        }
        ItemStack chest = Minecraft.getMinecraft().player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (chest != null) {
            return chest.getItem() == Items.ELYTRA;
        }
        return false;
    }

    public static boolean placeStackInHotbar(IItemStack stack) {
        for (int i = 0; i < 9; i++) {
            if (IInventoryWrapper.getStackInSlot(i).isEmpty()) {
                Minecraft.getMinecraft().player.connection
                        .sendPacket(new CPacketCreativeInventoryAction(36 + i, stack.getStack()));
                return true;
            }
        }

        return false;
    }

    public static IItemStack getHeldItem(IEntity entity, boolean offhand) {
        ItemStack finalItem = null;
        int slotId = 0;
        if (entity != null) {
            for (ItemStack item : entity.getEntity().getHeldEquipment()) {
                if ((slotId == 0 && !offhand) || (slotId == 1 && offhand)) {
                    finalItem = item;
                    break;
                } else if (slotId <= 1) {
                    slotId++;
                } else {
                    break;
                }
            }
        }
        return new IItemStack(finalItem);
    }

    public static IItemStack getHeldInventoryItem() {
        return new IItemStack(Minecraft.getMinecraft().player.inventory.getCurrentItem());
    }

    public static IItemStack getHeldItem(boolean offhand) {
        return IInventoryWrapper.getHeldItem(IEntityPlayer.getIPlayer(), offhand);
    }

    public static ArrayList<ISlot> getSlots() {
        if (IEntityPlayer.isNull()) {
            return new ArrayList<>();
        }
        ArrayList<ISlot> slots = new ArrayList<>();
        for (Slot d : Minecraft.getMinecraft().player.inventoryContainer.inventorySlots) {
            slots.add(new ISlot(d));
        }
        return slots;
    }

    public static IItemStack getArmorInventorySlot(int id) {
        if (IEntityPlayer.isNull()) {
            return null;
        }
        return new IItemStack(Minecraft.getMinecraft().player.inventory.armorInventory[id]);
    }

    public static IItemStack getArmorInSlot(int id) {
        if (IEntityPlayer.isNull()) {
            return null;
        }
        return new IItemStack(Minecraft.getMinecraft().player.inventory.armorItemInSlot(id));
    }

    public static IItemStack getStackInSlot(int id) {
        if (IEntityPlayer.isNull()) {
            return null;
        }
        return new IItemStack(Minecraft.getMinecraft().player.inventory.getStackInSlot(id));
    }

    public static int getFirstEmptyStack() {
        if (IEntityPlayer.isNull()) {
            return 0;
        }
        return Minecraft.getMinecraft().player.inventory.getFirstEmptyStack();
    }

}
