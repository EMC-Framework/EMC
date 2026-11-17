package me.deftware.mixin.mixins.gui;

import me.deftware.mixin.imp.IMixinShulkerBoxScreenHandler;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ShulkerBoxMenu.class)
public class MixinShulkerBoxScreenHandler implements IMixinShulkerBoxScreenHandler {

	@Shadow @Final private Container container;

	@Override
	public Container getInventory() {
		return container;
	}

}
