package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.message.Message;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import me.deftware.client.framework.gui.screens.ContainerScreen;
import me.deftware.client.framework.inventory.Inventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class MixinGuiContainer<T extends AbstractContainerMenu> extends MixinGuiScreen implements ContainerScreen {

    @Shadow
    protected Slot hoveredSlot;

    @Shadow
    @Final
    protected T menu;

    @Override
    public Slot getMinecraftSlot() {
        return hoveredSlot;
    }

    @Override
    public AbstractContainerMenu getScreenHandler() {
        return menu;
    }

    @Override
    public Inventory getContainerInventory() {
        return (Inventory) getHandlerInventory();
    }

    @Override
    public Message getInventoryName() {
        return (Message) ((Screen) (Object) this).getTitle();
    }

    @Inject(method = "extractRenderState", at = @At("RETURN"))
    private void onPostDraw(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        this.onPostDrawEvent(context, mouseX, mouseY, delta);
    }

}
