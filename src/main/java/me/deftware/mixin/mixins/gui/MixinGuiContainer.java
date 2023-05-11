package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.gui.screens.ContainerScreen;
import me.deftware.client.framework.inventory.Inventory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class MixinGuiContainer<T extends ScreenHandler> extends MixinGuiScreen implements ContainerScreen {

    @Shadow
    protected Slot focusedSlot;

    @Shadow
    @Final
    protected T handler;

    @Override
    public Slot getMinecraftSlot() {
        return focusedSlot;
    }

    @Override
    public ScreenHandler getScreenHandler() {
        return handler;
    }

    @Override
    public Inventory getContainerInventory() {
        return (Inventory) getHandlerInventory();
    }

    @Override
    public Message getInventoryName() {
        return (Message) ((Screen) (Object) this).getTitle();
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void onPostDraw(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        this.onPostDrawEvent(context, mouseX, mouseY, delta);
    }

}
