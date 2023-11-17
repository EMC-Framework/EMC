package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.gui.Drawable;
import net.minecraft.client.gui.GuiSlot;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GuiSlot.class)
public class MixinGuiList implements Drawable {

	@Override
	public void onRender(int mouseX, int mouseY, float delta) {
		((GuiSlot) (Object) this).drawScreen(mouseX, mouseY, delta);
	}

}
