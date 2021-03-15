package me.deftware.client.framework.gui.toast;

import me.deftware.client.framework.chat.ChatMessage;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import me.deftware.client.framework.render.gl.GLX;
import me.deftware.client.framework.render.texture.GlTexture;
import org.lwjgl.opengl.GL11;

/**
 * A custom toast implementation that can be used by EMC mods
 *
 * @author Deftware
 */
public class ToastImpl implements IToast {

	public GlTexture icon;
	public ChatMessage title;
	public ChatMessage[] text;
	public long transitionTime = 2000L, visibilityTime = transitionTime;
	public int width = 160, height = 32, index = 0;
	public int iconWidth = 25, iconHeight = 25;

	public ToastImpl(ChatMessage title, ChatMessage... text) {
		this(null, title, text);
	}

	public ToastImpl(GlTexture icon, ChatMessage title, ChatMessage... text) {
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
		GLX.INSTANCE.color(1, 1, 1, 1);
		manager.drawTexturedModalRect(0, 0, 0, 0, width, height);

		// Title
		manager.getMinecraft().fontRenderer.drawString(title.toString(true), 30, 7, -1);

		// Draw text
		if (text != null && text.length != 0) {
			if (text.length > 1) {
				if (startTime > transitionTime * (index + 1) && index + 1 < text.length) {
					index++;
				}
			}
			manager.getMinecraft().fontRenderer.drawString(text[index].toString(true), 30, 18, -1);
		}

		// Icon
		if (icon != null) {
			GLX.INSTANCE.color(1, 1, 1, 1);
			icon.bind().draw(4, 4, iconWidth, iconHeight);
		}
		return startTime >= visibilityTime ? Visibility.HIDE : Visibility.SHOW;
	}

}

