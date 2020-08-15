package me.deftware.client.framework.wrappers.toast;

import me.deftware.client.framework.utils.render.Texture;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import org.lwjgl.opengl.GL11;

/**
 * A custom toast implementation that can be used by EMC mods
 *
 * @author Deftware
 */
public class ToastImpl implements IToast {

	public Texture icon;
	public String title;
	public String[] text;
	public long transitionTime = 2000L, visibilityTime = transitionTime;
	public int width = 160, height = 32, index = 0;
	public int iconWidth = 25, iconHeight = 25;

	public ToastImpl(String title, String...text) {
		this(null, title, text);
	}

	public ToastImpl(Texture icon, String title, String...text) {
		this.text = text;
		this.title = title;
		this.icon = icon;
		if (text != null && text.length > 0) {
			visibilityTime *= text.length;
		}
	}

	@Override
	public Visibility draw(GuiToast manager, long startTime) {
		// Texture
		manager.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		manager.drawTexturedModalRect(0, 0, 0, 0, width, height);

		// Title
		manager.getMinecraft().fontRenderer.drawString(title, 30.0F, 7.0F, -1, false);

		// Draw text
		if (text != null && text.length != 0) {
			if (text.length > 1) {
				if (startTime > transitionTime * (index + 1) && index + 1 < text.length) {
					index++;
				}
			}
			manager.getMinecraft().fontRenderer.drawString(text[index], 30.0F, 18f, -1, false);
		}

		// Icon
		if (icon != null) {
			GL11.glPushMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			icon.updateTexture();
			GuiScreen.drawModalRectWithCustomSizedTexture(4, 4, 0, 0, iconWidth, iconHeight, iconWidth, iconHeight);
			GL11.glPopMatrix();
		}
		return startTime >= visibilityTime ? Visibility.HIDE : Visibility.SHOW;
	}

}
