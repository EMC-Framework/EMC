package me.deftware.client.framework.render.shader;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;

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
			framebuffer.drawInternal(MinecraftClient.getInstance().window.getFramebufferWidth(), MinecraftClient.getInstance().window.getFramebufferHeight(), false);
			// Unbind shader
			shader.unbind();
		}
	}

	public void init() {
		framebuffer = new Framebuffer(MinecraftClient.getInstance().getFramebuffer().viewportWidth, MinecraftClient.getInstance().getFramebuffer().viewportHeight, true, MinecraftClient.IS_SYSTEM_MAC);
	}

	public void clear() {
		if (framebuffer != null) framebuffer.clear(MinecraftClient.IS_SYSTEM_MAC);
	}

	public void onResized(int width, int height) {
		if (framebuffer != null) framebuffer.resize(width, height, MinecraftClient.IS_SYSTEM_MAC);
	}

}
