package me.deftware.mixin.mixins.gui;

import me.deftware.mixin.imp.IMixinGuiContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiContainer.class)
public class MixinGuiContainer extends MixinGuiScreen implements IMixinGuiContainer {

    @Shadow
    protected Slot theSlot;

    @Override
    public Slot getHoveredSlot() {
        return theSlot;
    }

}
