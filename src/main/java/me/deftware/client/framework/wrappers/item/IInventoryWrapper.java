package me.deftware.client.framework.wrappers.item;

import me.deftware.client.framework.wrappers.entity.IEntity;
import me.deftware.client.framework.wrappers.entity.IEntityPlayer;
import me.deftware.client.framework.wrappers.entity.IPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;

import java.util.ArrayList;

public class IInventoryWrapper {

    public static ArrayList<IItemStack> getArmorInventory(IEntity entity) {
        ArrayList<IItemStack> array = new ArrayList<>();
        if (entity != null) {
            if (entity.isPlayer()) {
                IPlayer player = entity.getIPlayer();

                for (int index = 3; index >= 0; index--) {
                    ItemStack item = player.getPlayer().inventory.armorInventory[index];
                    IItemStack stack = new IItemStack(item);
                    array.add(stack);
                }
            } else {
                /*for (ItemStack item : entity.getEntity().getInventory()) {
                    //LogManager.getLogger("EMC-Debug").info("Debug: " + count);
                    if (item != null) {
                        LogManager.getLogger("EMC-Debug").info("Received Item: " + item.getDisplayName());
                        IItemStack stack = new IItemStack(item);
                        array.add(stack);
                    }
                }
                Collections.reverse(array);*/
            }
        }

        return array;
    }

    public static boolean hasElytra() {
        return false;
    }

    public static boolean placeStackInHotbar(IItemStack stack) {
        for (int i = 0; i < 9; i++) {
            if (IInventoryWrapper.getStackInSlot(i).isEmpty()) {
                Minecraft.getMinecraft().thePlayer.sendQueue
                        .addToSendQueue(new C10PacketCreativeInventoryAction(36 + i, stack.getStack()));
                return true;
            }
        }

        return false;
    }

    public static IItemStack getHeldItem(IEntity entity, boolean offhand) {
        ItemStack finalItem = null;
        int slotId = 0;
        if (entity != null) {
            if (entity.isPlayer()) {
                IPlayer player = entity.getIPlayer();

                finalItem = player.getPlayer().getHeldItem();
            } else {
                /*for (ItemStack item : entity.getEntity().getInventory()) {
                    LogManager.getLogger("EMC-Debug").info("Debug: " + slotId + " -- " + (item != null ? item.getDisplayName() : "NaN"));
                    if (slotId == 0 && !offhand) {
                        finalItem = item;
                        break;
                    } else if (slotId <= 1) {
                        slotId++;
                    } else {
                        break;
                    }
                }*/
            }
        }
        return new IItemStack(finalItem);
    }

    public static IItemStack getHeldInventoryItem() {
        return new IItemStack(Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem());
    }

    public static IItemStack getHeldItem(boolean offhand) {
        return IInventoryWrapper.getHeldItem(IEntityPlayer.getIPlayer(), offhand);
    }

    public static ArrayList<ISlot> getSlots() {
        if (IEntityPlayer.isNull()) {
            return new ArrayList<>();
        }
        ArrayList<ISlot> slots = new ArrayList<>();
        for (Slot d : Minecraft.getMinecraft().thePlayer.inventoryContainer.inventorySlots) {
            slots.add(new ISlot(d));
        }
        return slots;
    }

    public static IItemStack getArmorInventorySlot(int id) {
        if (IEntityPlayer.isNull()) {
            return null;
        }
        return new IItemStack(Minecraft.getMinecraft().thePlayer.inventory.armorInventory[id]);
    }

    public static IItemStack getArmorInSlot(int id) {
        if (IEntityPlayer.isNull()) {
            return null;
        }
        return new IItemStack(Minecraft.getMinecraft().thePlayer.inventory.armorItemInSlot(id));
    }

    public static IItemStack getStackInSlot(int id) {
        if (IEntityPlayer.isNull()) {
            return null;
        }
        return new IItemStack(Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(id));
    }

    public static int getFirstEmptyStack() {
        if (IEntityPlayer.isNull()) {
            return 0;
        }
        return Minecraft.getMinecraft().thePlayer.inventory.getFirstEmptyStack();
    }

}
