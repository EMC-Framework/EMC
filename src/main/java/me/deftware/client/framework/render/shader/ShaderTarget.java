package me.deftware.client.framework.render.shader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import java.util.function.Predicate;

/**
 * @author Deftware
 */
public enum ShaderTarget {
	PLAYER, ENTITY, DROPPED, STORAGE;
	private Predicate<String> predicate = type -> true;
	private boolean enabled = false;
	private Framebuffer framebuffer;
	private Shader shader;

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

	public void setPredicate(final Predicate<String> predicate) {
		this.predicate = predicate;
	}

	public Predicate<String> getPredicate() {
		return this.predicate;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	public Framebuffer getFramebuffer() {
		return this.framebuffer;
	}

	public void setShader(final Shader shader) {
		this.shader = shader;
	}
}
