package me.deftware.mixin.mixins.gui;

import me.deftware.mixin.imp.IMixinShulkerBoxScreenHandler;
import net.minecraft.inventory.ContainerShulkerBox;
import net.minecraft.inventory.IInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ContainerShulkerBox.class)
public class MixinShulkerBoxScreenHandler implements IMixinShulkerBoxScreenHandler {

	@Shadow @Final private IInventory inventory;

	@Override
	public IInventory getInventory() {
		return inventory;
	}

}
