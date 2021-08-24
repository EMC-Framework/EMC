package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.gui.screens.ContainerScreen;
import me.deftware.client.framework.inventory.Inventory;
import me.deftware.client.framework.util.Lazy;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public abstract class MixinGuiContainer extends MixinGuiScreen implements ContainerScreen {

    @Shadow
    private Slot theSlot;

    @Unique
    private final Lazy<Inventory> inventoryLazy = new Lazy<>(() -> new Inventory(
            getHandlerInventory()
    ));

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
        if (getHandlerInventory() == null)
            return null;
        return inventoryLazy.get();
    }

    @Unique
    private final Lazy<ChatMessage> inventoryTitle = new Lazy<>(() -> {
        if (getScreenHandler() instanceof ContainerChest) {
            ContainerChest chest = (ContainerChest) getScreenHandler();
            return new ChatMessage().fromText(
                chest.getLowerChestInventory().getDisplayName(), false
            );
        }
        return null;
    });

    @Override
    public ChatMessage getInventoryName() {
        return inventoryTitle.get();
    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    private void onPostDraw(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        this.onPostDrawEvent(mouseX, mouseY, delta);
    }

}
