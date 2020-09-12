package me.deftware.client.framework.render.shader;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;

import java.util.function.Predicate;

/**
 * @author Deftware
 */
public enum ShaderTarget {

	PLAYER, ENTITY, DROPPED, STORAGE;

	private @Setter @Getter Predicate<String> predicate = type -> true;
	private @Getter @Setter boolean enabled = false;
	private @Getter Framebuffer framebuffer;
	private @Setter Shader shader;

	public void renderBuffer() {
		if (framebuffer != null && shader != null && enabled) {
			// Bind shader
			shader.bind();
			shader.setupUniforms();
			// Draw buffer
			framebuffer.framebufferRenderExt(Minecraft.getInstance().getFramebuffer().framebufferWidth, Minecraft.getInstance().getFramebuffer().framebufferHeight, false);
			// Unbind shader
			shader.unbind();
		}
	}

	public void init() {
		framebuffer = new Framebuffer(Minecraft.getInstance().getFramebuffer().framebufferWidth, Minecraft.getInstance().getFramebuffer().framebufferHeight, true);
	}

	public void clear() {
		if (framebuffer != null) framebuffer.framebufferClear();
	}

	public void onResized(int width, int height) {
		if (framebuffer != null) framebuffer.createBindFramebuffer(width, height);
	}

}
