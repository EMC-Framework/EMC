package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.inventory.Inventory;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.container.Container;
import net.minecraft.container.Slot;
import net.minecraft.util.Lazy;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ContainerScreen.class)
public abstract class MixinGuiContainer<T extends Container> extends MixinGuiScreen implements me.deftware.client.framework.gui.screens.ContainerScreen {

    @Shadow
    protected Slot focusedSlot;

    @Shadow
    @Final
    protected T container;

    @Unique
    private final Lazy<Inventory> inventoryLazy = new Lazy<>(() -> new Inventory(
            getHandlerInventory()
    ));

    @Override
    public Slot getMinecraftSlot() {
        return focusedSlot;
    }

    @Override
    public Container getScreenHandler() {
        return container;
    }

    @Override
    public Inventory getContainerInventory() {
        if (getHandlerInventory() == null)
            return null;
        return inventoryLazy.get();
    }

    @Override
    public Message getInventoryName() {
        return (Message) ((Screen) (Object) this).getTitle();
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void onPostDraw(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        this.onPostDrawEvent(mouseX, mouseY, delta);
    }

}
