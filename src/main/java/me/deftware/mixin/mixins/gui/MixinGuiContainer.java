package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.gui.screens.ContainerScreen;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.inventory.Inventory;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public abstract class MixinGuiContainer extends MixinGuiScreen implements ContainerScreen {

    @Shadow
    private Slot theSlot;

    @Override
    public Slot getMinecraftSlot() {
        return theSlot;
    }

    @Override
    public Container getScreenHandler() {
        return ((GuiContainer) (Object) this).inventorySlots;
    }

    @Override
    public Inventory getContainerInventory() {
        return (Inventory) getHandlerInventory();
    }

    @Override
    public Message getInventoryName() {
        if (getScreenHandler() instanceof ContainerChest) {
            ContainerChest chest = (ContainerChest) getScreenHandler();
            return (Message) chest.getLowerChestInventory().getDisplayName();
        }
        return null;
    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    private void onPostDraw(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        this.onPostDrawEvent(mouseX, mouseY, delta);
    }

}
