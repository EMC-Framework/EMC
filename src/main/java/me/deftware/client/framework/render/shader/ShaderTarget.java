package me.deftware.client.framework.render.shader;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gl.GlFramebuffer;

import java.util.function.Predicate;

/**
 * @author Deftware
 */
public enum ShaderTarget {

	PLAYER, ENTITY, DROPPED, STORAGE;

	private @Setter @Getter Predicate<String> predicate = type -> true;
	private @Getter @Setter boolean enabled = false;
	private @Getter GlFramebuffer framebuffer;
	private @Setter Shader shader;

	public void renderBuffer() {
		if (framebuffer != null && shader != null && enabled) {
			// Bind shader
			shader.bind();
			shader.setupUniforms();
			// Draw buffer
			framebuffer.draw(net.minecraft.client.Minecraft.getInstance().window.getFramebufferWidth(), net.minecraft.client.Minecraft.getInstance().window.getFramebufferHeight(), false);
			// Unbind shader
			shader.unbind();
		}
	}

	public void init() {
		framebuffer = new GlFramebuffer(net.minecraft.client.Minecraft.getInstance().getFramebuffer().viewWidth, net.minecraft.client.Minecraft.getInstance().getFramebuffer().viewHeight, true, Minecraft.IS_SYSTEM_MAC);
	}

	public void clear() {
		if (framebuffer != null) framebuffer.clear(Minecraft.IS_SYSTEM_MAC);
	}

	public void onResized(int width, int height) {
		if (framebuffer != null) framebuffer.resize(width, height, Minecraft.IS_SYSTEM_MAC);
	}

}
