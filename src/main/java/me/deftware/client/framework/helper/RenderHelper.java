package me.deftware.client.framework.helper;

import net.minecraft.client.Minecraft;

/**
 * @author Deftware
 */
public class RenderHelper {

	public static void reloadRenderers() {
		Minecraft.getInstance().worldRenderer.loadRenderers();
	}

}
