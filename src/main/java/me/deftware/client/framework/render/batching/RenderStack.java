package me.deftware.client.framework.render.batching;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuSampler;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import lombok.Getter;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.client.framework.render.gl.GLX;
import me.deftware.client.framework.render.texture.GlTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;
import org.joml.Matrix3x2f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * new RenderStackImpl()
 * optional glLineWidth
 * .begin()
 * optional .glColor(color).
 * call to draw functions
 * .end();
 *
 * @author Deftware
 */
@SuppressWarnings("unchecked")
public abstract class RenderStack<T> implements VertexConstructor {

	public static float scale = 1;
	public static final CopyOnWriteArrayList<Runnable> scaleChangeCallback = new CopyOnWriteArrayList<>();
	private static final ByteBuffer COLOR_MASK_BUF = ByteBuffer.allocateDirect(4);
	private static final IntBuffer SCISSOR_BOX_BUF = ByteBuffer.allocateDirect(16).order(ByteOrder.nativeOrder()).asIntBuffer();
	private static ScreenRectangle activeStencilScissor = null;

	public static float getScale() {
		return scale;
	}

	public static void setScale(float scale) {
		if (scale < 0.5f)
			scale = 0.5f;
		if (scale > 4)
			scale = 4;
		Bootstrap.EMCSettings.putPrimitive("RENDER_SCALE", RenderStack.scale = scale);
		scaleChangeCallback.forEach(Runnable::run);
	}

	@Getter
	protected boolean scaled = true;

	protected float red = 1f, green = 1f, blue = 1f, alpha = 1f, lineWidth = 2f;
	protected Color lastColor = Color.white;

	private record VertexData(float x, float y, float z, float u, float v, float r, float g, float b, float a) {
	}

	private static final class MutableVertex {
		float x, y, z, u, v, r, g, b, a;
	}

	private record ScreenVertex(float x, float y, float u, float v, int argb) {
	}

	private record ScreenQuad(ScreenVertex v0, ScreenVertex v1, ScreenVertex v2, ScreenVertex v3) {
	}

	private final List<MutableVertex> vertices = new ArrayList<>();
	private final List<MutableVertex> vertexPool = new ArrayList<>();
	private int vertexPoolIndex = 0;
	private final Vector3f tmpPos = new Vector3f();
	private MutableVertex currentVertex = null;
	private boolean building = false;
	private int mode = -1;

	private GLX context;

	public T setScaled(boolean scaling) {
		this.scaled = scaling;
		return (T) this;
	}

	public T push() {
		context.push();
		return (T) this;
	}

	public T pop() {
		context.pop();
		return (T) this;
	}

	public T lineWidth(float width) {
		this.lineWidth = width;
		return (T) this;
	}

	public T glColor(int rgb, float alpha) {
		this.red = ((rgb >> 16) & 0xFF) / 255f;
		this.green = ((rgb >> 8) & 0xFF) / 255f;
		this.blue = (rgb & 0xFF) / 255f;
		this.alpha = alpha / 255f;
		return (T) this;
	}

	public T glColor(Color color) {
		return glColor(color, color.getAlpha());
	}

	public T glColor(Color color, float alpha) {
		this.red = color.getRed() / 255.0F;
		this.green = color.getGreen() / 255.0F;
		this.blue = color.getBlue() / 255.0F;
		this.alpha = alpha / 255.0F;
		lastColor = color;
		return (T) this;
	}

	public abstract T begin(GLX context);

	public T begin(GLX context, int mode) {
		this.context = context;
		this.mode = mode;
		this.vertices.clear();
		this.vertexPoolIndex = 0;
		this.currentVertex = null;
		this.building = true;
		setShader();
		return (T) this;
	}

	protected void setBuilder(PrimitiveTopology drawMode, VertexFormat vertexFormat) {
	}

	public void end() {
		flushVertices();
	}

	public boolean isBuilding() {
		return building;
	}

	private static boolean isColorWriteDisabled() {
		if (!GL11.glIsEnabled(GL11.GL_STENCIL_TEST)) {
			return false;
		}
		COLOR_MASK_BUF.clear();
		GL11.glGetBooleanv(GL11.GL_COLOR_WRITEMASK, COLOR_MASK_BUF);
		return COLOR_MASK_BUF.get(0) == 0
				&& COLOR_MASK_BUF.get(1) == 0
				&& COLOR_MASK_BUF.get(2) == 0
				&& COLOR_MASK_BUF.get(3) == 0;
	}

	public static ScreenRectangle getActiveScissor(GuiGraphicsExtractor gfx) {
		boolean stencilEnabled = GL11.glIsEnabled(GL11.GL_STENCIL_TEST);
		if (!stencilEnabled) {
			activeStencilScissor = null;
		}
		ScreenRectangle scissor = gfx != null && gfx.scissorStack != null ? gfx.scissorStack.peek() : null;
		ScreenRectangle glScissor = getGlScissor();
		if (glScissor != null) {
			scissor = scissor != null ? scissor.intersection(glScissor) : glScissor;
			if (scissor == null) {
				return new ScreenRectangle(0, 0, 0, 0);
			}
		}
		if (stencilEnabled && activeStencilScissor != null) {
			scissor = scissor != null ? scissor.intersection(activeStencilScissor) : activeStencilScissor;
			if (scissor == null) {
				return new ScreenRectangle(0, 0, 0, 0);
			}
		}
		return scissor;
	}

	private static ScreenRectangle getGlScissor() {
		if (!GL11.glIsEnabled(GL11.GL_SCISSOR_TEST)) {
			return null;
		}
		SCISSOR_BOX_BUF.clear();
		GL11.glGetIntegerv(GL11.GL_SCISSOR_BOX, SCISSOR_BOX_BUF);
		int sx = SCISSOR_BOX_BUF.get(0);
		int sy = SCISSOR_BOX_BUF.get(1);
		int sw = SCISSOR_BOX_BUF.get(2);
		int sh = SCISSOR_BOX_BUF.get(3);
		if (sw <= 0 || sh <= 0) {
			return new ScreenRectangle(0, 0, 0, 0);
		}
		Window win = Minecraft.getInstance().getWindow();
		double guiScale = Math.max(1.0, win.getGuiScale());
		int fbHeight = win.getHeight();
		int gx = (int) Math.floor(sx / guiScale);
		int gy = (int) Math.floor((fbHeight - (sy + sh)) / guiScale);
		int gx1 = (int) Math.ceil((sx + sw) / guiScale);
		int gy1 = (int) Math.ceil((fbHeight - sy) / guiScale);
		return new ScreenRectangle(gx, gy, Math.max(0, gx1 - gx), Math.max(0, gy1 - gy));
	}

	private void flushVertices() {
		building = false;
		currentVertex = null;
		if (vertices.isEmpty() || context == null) {
			vertices.clear();
			return;
		}
		GuiGraphicsExtractor gfx = context.getContext();
		if (gfx == null) {
			vertices.clear();
			return;
		}
		// Stencil mask passes disable color writes via glColorMask(false, false, false, false),
		// but GuiElementRenderState defers vertex submission until after glColorMask is restored.
		// Build the 2D quads first so we can capture the mask bounds as a ScreenRectangle scissor
		// for subsequent clipped child draws while GL_STENCIL_TEST remains enabled.
		boolean stencilEnabled = !context.isIn3D() && GL11.glIsEnabled(GL11.GL_STENCIL_TEST);
		if (!stencilEnabled) {
			activeStencilScissor = null;
		}
		boolean stencilMaskPass = stencilEnabled && isColorWriteDisabled();
		boolean textured = getFormat() == DefaultVertexFormat.POSITION_TEX_COLOR;
		AbstractTexture boundTexture = textured ? GlTexture.getBoundAbstractTexture() : null;
		if (textured && (boundTexture == null || boundTexture.getTextureView() == null)) {
			vertices.clear();
			return;
		}

		List<ScreenQuad> quads = new ArrayList<>(Math.max(1, vertices.size() / 4));
		if (context.isIn3D()) {
			buildQuads3D(gfx, quads);
		} else {
			buildQuads2D(quads);
		}
		vertices.clear();
		if (quads.isEmpty()) {
			return;
		}

		float minX = Float.POSITIVE_INFINITY, minY = Float.POSITIVE_INFINITY;
		float maxX = Float.NEGATIVE_INFINITY, maxY = Float.NEGATIVE_INFINITY;
		for (ScreenQuad q : quads) {
			minX = Math.min(minX, Math.min(Math.min(q.v0.x, q.v1.x), Math.min(q.v2.x, q.v3.x)));
			minY = Math.min(minY, Math.min(Math.min(q.v0.y, q.v1.y), Math.min(q.v2.y, q.v3.y)));
			maxX = Math.max(maxX, Math.max(Math.max(q.v0.x, q.v1.x), Math.max(q.v2.x, q.v3.x)));
			maxY = Math.max(maxY, Math.max(Math.max(q.v0.y, q.v1.y), Math.max(q.v2.y, q.v3.y)));
		}

		int ix0 = (int) Math.floor(minX);
		int iy0 = (int) Math.floor(minY);
		int ix1 = (int) Math.ceil(maxX);
		int iy1 = (int) Math.ceil(maxY);
		ScreenRectangle bounds = new ScreenRectangle(ix0, iy0, Math.max(1, ix1 - ix0), Math.max(1, iy1 - iy0));
		if (stencilMaskPass) {
			activeStencilScissor = bounds;
			return;
		}

		ScreenRectangle scissor = gfx.scissorStack != null ? gfx.scissorStack.peek() : null;
		if (!context.isIn3D()) {
			ScreenRectangle glScissor = getGlScissor();
			if (glScissor != null) {
				scissor = scissor != null ? scissor.intersection(glScissor) : glScissor;
				if (scissor == null) {
					return;
				}
			}
			if (stencilEnabled && activeStencilScissor != null) {
				scissor = scissor != null ? scissor.intersection(activeStencilScissor) : activeStencilScissor;
				if (scissor == null) {
					return;
				}
			}
		}
		if (scissor != null) {
			bounds = scissor.intersection(bounds);
			if (bounds == null) {
				return;
			}
		}

		RenderPipeline pipeline = textured ? RenderPipelines.GUI_TEXTURED : RenderPipelines.GUI;
		TextureSetup textureSetup;
		if (textured) {
			GpuSampler sampler = boundTexture.getSampler();
			if (sampler == null) {
				sampler = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
			}
			textureSetup = TextureSetup.singleTexture(boundTexture.getTextureView(), sampler);
		} else {
			textureSetup = TextureSetup.noTexture();
		}

		gfx.guiRenderState.addGuiElement(new BatchElementRenderState(pipeline, textureSetup, scissor, bounds, quads, textured));
	}

	private void buildQuads2D(List<ScreenQuad> quads) {
		float scaleFix = inCustomMatrix ? (1.0f / (float) Math.max(1.0, Minecraft.getInstance().getWindow().getGuiScale())) : 1.0f;
		int n = vertices.size();
		switch (mode) {
			case GL11.GL_LINES -> {
				for (int i = 0; i + 1 < n; i += 2) {
					addLineQuad2D(quads, vertices.get(i), vertices.get(i + 1), scaleFix);
				}
			}
			case GL11.GL_LINE_STRIP -> {
				for (int i = 0; i + 1 < n; i++) {
					addLineQuad2D(quads, vertices.get(i), vertices.get(i + 1), scaleFix);
				}
			}
			case GL11.GL_TRIANGLES -> {
				for (int i = 0; i + 2 < n; i += 3) {
					ScreenVertex v0 = toScreen2D(vertices.get(i), scaleFix);
					ScreenVertex v1 = toScreen2D(vertices.get(i + 1), scaleFix);
					ScreenVertex v2 = toScreen2D(vertices.get(i + 2), scaleFix);
					addNormalizedQuad(quads, v0, v1, v2, v2);
				}
			}
			case GL11.GL_TRIANGLE_STRIP -> {
				for (int i = 0; i + 2 < n; i++) {
					ScreenVertex v0 = toScreen2D(vertices.get(i), scaleFix);
					ScreenVertex v1 = toScreen2D(vertices.get(i + 1), scaleFix);
					ScreenVertex v2 = toScreen2D(vertices.get(i + 2), scaleFix);
					addNormalizedQuad(quads, v0, v1, v2, v2);
				}
			}
			case GL11.GL_TRIANGLE_FAN -> {
				if (n >= 3) {
					ScreenVertex c = toScreen2D(vertices.get(0), scaleFix);
					for (int i = 1; i + 1 < n; i++) {
						addNormalizedQuad(quads, c, toScreen2D(vertices.get(i), scaleFix), toScreen2D(vertices.get(i + 1), scaleFix), toScreen2D(vertices.get(i + 1), scaleFix));
					}
				}
			}
			default -> {
				for (int i = 0; i + 3 < n; i += 4) {
					addNormalizedQuad(
							quads,
							toScreen2D(vertices.get(i), scaleFix),
							toScreen2D(vertices.get(i + 1), scaleFix),
							toScreen2D(vertices.get(i + 2), scaleFix),
							toScreen2D(vertices.get(i + 3), scaleFix)
					);
				}
			}
		}
	}

	private void buildQuads3D(GuiGraphicsExtractor gfx, List<ScreenQuad> quads) {
		Matrix4f viewProj = context.getViewProjection3d();
		if (viewProj == null) {
			return;
		}
		float guiW = gfx.guiWidth();
		float guiH = gfx.guiHeight();
		int n = vertices.size();
		switch (mode) {
			case GL11.GL_LINES -> {
				for (int i = 0; i + 1 < n; i += 2) {
					addClippedLine3D(quads, viewProj, guiW, guiH, vertices.get(i), vertices.get(i + 1));
				}
			}
			case GL11.GL_LINE_STRIP -> {
				for (int i = 0; i + 1 < n; i++) {
					addClippedLine3D(quads, viewProj, guiW, guiH, vertices.get(i), vertices.get(i + 1));
				}
			}
			case GL11.GL_TRIANGLES -> {
				for (int i = 0; i + 2 < n; i += 3) {
					addClippedTriangle3D(quads, viewProj, guiW, guiH, vertices.get(i), vertices.get(i + 1), vertices.get(i + 2));
				}
			}
			case GL11.GL_TRIANGLE_STRIP -> {
				for (int i = 0; i + 2 < n; i++) {
					addClippedTriangle3D(quads, viewProj, guiW, guiH, vertices.get(i), vertices.get(i + 1), vertices.get(i + 2));
				}
			}
			case GL11.GL_TRIANGLE_FAN -> {
				if (n >= 3) {
					MutableVertex c = vertices.get(0);
					for (int i = 1; i + 1 < n; i++) {
						addClippedTriangle3D(quads, viewProj, guiW, guiH, c, vertices.get(i), vertices.get(i + 1));
					}
				}
			}
			default -> {
				for (int i = 0; i + 3 < n; i += 4) {
					addClippedTriangle3D(quads, viewProj, guiW, guiH, vertices.get(i), vertices.get(i + 1), vertices.get(i + 2));
					addClippedTriangle3D(quads, viewProj, guiW, guiH, vertices.get(i), vertices.get(i + 2), vertices.get(i + 3));
				}
			}
		}
	}

	private ScreenVertex toScreen2D(MutableVertex v, float scaleFix) {
		float sx = v.x;
		float sy = v.y;
		if (context != null && context.getContext() != null) {
			Vector2f pos = context.getContext().pose().transformPosition(sx, sy, new Vector2f());
			sx = pos.x;
			sy = pos.y;
		}
		return new ScreenVertex(sx * scaleFix, sy * scaleFix, v.u, v.v, packColor(v));
	}

	private void addLineQuad2D(List<ScreenQuad> quads, MutableVertex a, MutableVertex b, float scaleFix) {
		addLineQuadScreen(quads, toScreen2D(a, scaleFix), toScreen2D(b, scaleFix));
	}

	private void addLineQuadScreen(List<ScreenQuad> quads, ScreenVertex p0, ScreenVertex p1) {
		float dx = p1.x - p0.x;
		float dy = p1.y - p0.y;
		float len = (float) Math.hypot(dx, dy);
		if (len < 1.0e-4f) {
			return;
		}
		float halfW = Math.max(0.5f, lineWidth * 0.5f);
		float nx = (-dy / len) * halfW;
		float ny = (dx / len) * halfW;
		addNormalizedQuad(
				quads,
				new ScreenVertex(p0.x - nx, p0.y - ny, p0.u, p0.v, p0.argb),
				new ScreenVertex(p0.x + nx, p0.y + ny, p0.u, p0.v, p0.argb),
				new ScreenVertex(p1.x + nx, p1.y + ny, p1.u, p1.v, p1.argb),
				new ScreenVertex(p1.x - nx, p1.y - ny, p1.u, p1.v, p1.argb)
		);
	}

	private void addClippedLine3D(List<ScreenQuad> quads, Matrix4f viewProj, float guiW, float guiH, MutableVertex a, MutableVertex b) {
		Vector4f ca = toClip(viewProj, a);
		Vector4f cb = toClip(viewProj, b);
		int c0 = packColor(a);
		int c1 = packColor(b);
		final float nearW = 0.05f;
		boolean aIn = ca.w >= nearW;
		boolean bIn = cb.w >= nearW;
		if (!aIn && !bIn) {
			return;
		}
		float u0 = a.u, v0 = a.v;
		float u1 = b.u, v1 = b.v;
		if (!aIn) {
			float t = (nearW - ca.w) / (cb.w - ca.w);
			ca.lerp(cb, t);
			u0 = lerp(u0, u1, t);
			v0 = lerp(v0, v1, t);
			c0 = lerpColor(c0, c1, t);
		} else if (!bIn) {
			float t = (nearW - cb.w) / (ca.w - cb.w);
			cb.lerp(ca, t);
			u1 = lerp(u1, u0, t);
			v1 = lerp(v1, v0, t);
			c1 = lerpColor(c1, c0, t);
		}
		// Clip against padded NDC bounds [-1.5w, 1.5w] so segments near the camera
		// never project to extreme screen coordinates and corrupt bounds/quads.
		final float limit = 1.5f;
		float dx = cb.x - ca.x;
		float dy = cb.y - ca.y;
		float dw = cb.w - ca.w;
		float[] p = {
				-(dx + limit * dw),
				dx - limit * dw,
				-(dy + limit * dw),
				dy - limit * dw
		};
		float[] q = {
				ca.x + limit * ca.w,
				limit * ca.w - ca.x,
				ca.y + limit * ca.w,
				limit * ca.w - ca.y
		};
		float t0 = 0.0f;
		float t1 = 1.0f;
		for (int i = 0; i < 4; i++) {
			if (Math.abs(p[i]) < 1.0e-6f) {
				if (q[i] < 0.0f) {
					return;
				}
			} else {
				float r = q[i] / p[i];
				if (p[i] < 0.0f) {
					if (r > t1) return;
					if (r > t0) t0 = r;
				} else {
					if (r < t0) return;
					if (r < t1) t1 = r;
				}
			}
		}
		if (t0 > t1) {
			return;
		}
		Vector4f origA = new Vector4f(ca);
		Vector4f origB = new Vector4f(cb);
		float origU0 = u0, origV0 = v0;
		float origU1 = u1, origV1 = v1;
		int origC0 = c0, origC1 = c1;
		if (t0 > 0.0f) {
			ca.set(origA).lerp(origB, t0);
			u0 = lerp(origU0, origU1, t0);
			v0 = lerp(origV0, origV1, t0);
			c0 = lerpColor(origC0, origC1, t0);
		}
		if (t1 < 1.0f) {
			cb.set(origA).lerp(origB, t1);
			u1 = lerp(origU0, origU1, t1);
			v1 = lerp(origV0, origV1, t1);
			c1 = lerpColor(origC0, origC1, t1);
		}
		ScreenVertex sa = clipToScreen(ca, guiW, guiH, u0, v0, c0);
		ScreenVertex sb = clipToScreen(cb, guiW, guiH, u1, v1, c1);
		addLineQuadScreen(quads, sa, sb);
	}

	private void addClippedTriangle3D(List<ScreenQuad> quads, Matrix4f viewProj, float guiW, float guiH, MutableVertex a, MutableVertex b, MutableVertex c) {
		record ClipVert(Vector4f clip, float u, float v, int argb) {}
		final float nearW = 0.05f;
		ClipVert[] poly = {
				new ClipVert(toClip(viewProj, a), a.u, a.v, packColor(a)),
				new ClipVert(toClip(viewProj, b), b.u, b.v, packColor(b)),
				new ClipVert(toClip(viewProj, c), c.u, c.v, packColor(c))
		};
		List<ClipVert> out = new ArrayList<>(4);
		for (int i = 0; i < poly.length; i++) {
			ClipVert curr = poly[i];
			ClipVert prev = poly[(i + poly.length - 1) % poly.length];
			boolean currIn = curr.clip.w >= nearW;
			boolean prevIn = prev.clip.w >= nearW;
			if (currIn != prevIn) {
				float t = (nearW - prev.clip.w) / (curr.clip.w - prev.clip.w);
				Vector4f inter = new Vector4f(prev.clip).lerp(curr.clip, t);
				out.add(new ClipVert(inter, lerp(prev.u, curr.u, t), lerp(prev.v, curr.v, t), lerpColor(prev.argb, curr.argb, t)));
			}
			if (currIn) {
				out.add(curr);
			}
		}
		if (out.size() < 3) {
			return;
		}
		ScreenVertex s0 = clipToScreen(out.get(0).clip, guiW, guiH, out.get(0).u, out.get(0).v, out.get(0).argb);
		ScreenVertex s1 = clipToScreen(out.get(1).clip, guiW, guiH, out.get(1).u, out.get(1).v, out.get(1).argb);
		ScreenVertex s2 = clipToScreen(out.get(2).clip, guiW, guiH, out.get(2).u, out.get(2).v, out.get(2).argb);
		ScreenVertex s3 = out.size() >= 4 ? clipToScreen(out.get(3).clip, guiW, guiH, out.get(3).u, out.get(3).v, out.get(3).argb) : s2;
		addNormalizedQuad(quads, s0, s1, s2, s3);
	}

	private static Vector4f toClip(Matrix4f viewProj, MutableVertex v) {
		return viewProj.transform(new Vector4f(v.x, v.y, v.z, 1.0f));
	}

	private static ScreenVertex clipToScreen(Vector4f clip, float guiW, float guiH, float u, float v, int argb) {
		float invW = 1.0f / clip.w;
		float ndcX = clip.x * invW;
		float ndcY = clip.y * invW;
		float sx = (ndcX * 0.5f + 0.5f) * guiW;
		float sy = (0.5f - ndcY * 0.5f) * guiH;
		return new ScreenVertex(sx, sy, u, v, argb);
	}

	private static void addNormalizedQuad(List<ScreenQuad> quads, ScreenVertex v0, ScreenVertex v1, ScreenVertex v2, ScreenVertex v3) {
		float cross = (v1.x - v0.x) * (v2.y - v0.y) - (v1.y - v0.y) * (v2.x - v0.x);
		if (cross > 0.0f) {
			quads.add(new ScreenQuad(v0, v3, v2, v1));
		} else {
			quads.add(new ScreenQuad(v0, v1, v2, v3));
		}
	}

	private int packColor(MutableVertex v) {
		float mr = clamp01(v.r * context.getColorR());
		float mg = clamp01(v.g * context.getColorG());
		float mb = clamp01(v.b * context.getColorB());
		float ma = clamp01(v.a * context.getColorA());
		int a = Math.round(ma * 255.0f);
		int r = Math.round(mr * 255.0f);
		int g = Math.round(mg * 255.0f);
		int b = Math.round(mb * 255.0f);
		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	private static float clamp01(float v) {
		return v < 0.0f ? 0.0f : Math.min(1.0f, v);
	}

	private static float lerp(float a, float b, float t) {
		return a + (b - a) * t;
	}

	private static int lerpColor(int c0, int c1, float t) {
		int a = Math.round(lerp((c0 >>> 24) & 0xFF, (c1 >>> 24) & 0xFF, t));
		int r = Math.round(lerp((c0 >>> 16) & 0xFF, (c1 >>> 16) & 0xFF, t));
		int g = Math.round(lerp((c0 >>> 8) & 0xFF, (c1 >>> 8) & 0xFF, t));
		int b = Math.round(lerp(c0 & 0xFF, c1 & 0xFF, t));
		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	private record BatchElementRenderState(
			RenderPipeline pipeline,
			TextureSetup textureSetup,
			ScreenRectangle scissorArea,
			ScreenRectangle bounds,
			List<ScreenQuad> quads,
			boolean textured
	) implements GuiElementRenderState {
		@Override
		public void buildVertices(VertexConsumer consumer) {
			for (ScreenQuad q : quads) {
				emitVertex(consumer, q.v0);
				emitVertex(consumer, q.v1);
				emitVertex(consumer, q.v2);
				emitVertex(consumer, q.v3);
			}
		}

		private void emitVertex(VertexConsumer consumer, ScreenVertex v) {
			if (textured) {
				consumer.addVertex(v.x, v.y, 0.0f).setUv(v.u, v.v).setColor(v.argb);
			} else {
				consumer.addVertex(v.x, v.y, 0.0f).setColor(v.argb);
			}
		}
	}

	protected void drawBuffer() {
		/*var shader = RenderSystem.getShader();
		if (shader != null && shader.lineWidth != null) {
			shader.lineWidth.set(RenderSystem.getShaderLineWidth());
		}
		var result = builder.endNullable();
		if (result != null) {
			BufferRenderer.drawWithGlobalProgram(result);
		}
		builder = null;*/
	}

	public static void noBlend() {
		//RenderSystem.disableBlend();
	}

	public static void blend() {
		/*RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderSystem.enableBlend();*/
	}

	public static void setupGl() {
		blend();
		// RenderSystem.disableTexture();
		//RenderSystem.disableDepthTest();
		//RenderSystem.depthMask(false);
	}

	public static void restoreGl() {
		noBlend();
		//RenderSystem.depthMask(true);
		// RenderSystem.enableTexture();
		//RenderSystem.enableDepthTest();
	}

	public static PrimitiveTopology translate(int mode) {
		return switch (mode) {
			case GL11.GL_QUADS -> PrimitiveTopology.QUADS;
			case GL11.GL_LINE_STRIP -> PrimitiveTopology.DEBUG_LINE_STRIP;
			case GL11.GL_LINES -> PrimitiveTopology.DEBUG_LINES;
			case GL11.GL_TRIANGLE_FAN -> PrimitiveTopology.TRIANGLE_FAN;
			case GL11.GL_TRIANGLE_STRIP -> PrimitiveTopology.TRIANGLE_STRIP;
			case GL11.GL_TRIANGLES -> PrimitiveTopology.TRIANGLES;
			default -> PrimitiveTopology.QUADS;
		};
	}

	protected Matrix4f getModel() {
		return context.getModel();
	}

	@Override
	public void next() {
		currentVertex = null;
	}

	@Override
	public VertexConstructor color(float r, float g, float b, float alpha) {
		if (currentVertex != null) {
			currentVertex.r = r;
			currentVertex.g = g;
			currentVertex.b = b;
			currentVertex.a = alpha;
		}
		return this;
	}

	@Override
	public VertexConstructor texture(float u, float v) {
		if (currentVertex != null) {
			currentVertex.u = u;
			currentVertex.v = v;
		}
		return this;
	}

	@Override
	public VertexConstructor vertex(double x, double y, double z) {
		Vector3f pos = getModel().transformPosition((float) x, (float) y, (float) z, tmpPos);
		MutableVertex v;
		if (vertexPoolIndex < vertexPool.size()) {
			v = vertexPool.get(vertexPoolIndex++);
		} else {
			v = new MutableVertex();
			vertexPool.add(v);
			vertexPoolIndex++;
		}
		v.x = pos.x;
		v.y = pos.y;
		v.z = pos.z;
		v.r = red;
		v.g = green;
		v.b = blue;
		v.a = alpha;
		vertices.add(v);
		currentVertex = v;
		return this;
	}

	@Override
	public VertexConstructor normal(float x, float y, float z) {
		return this;
	}

	protected VertexFormat getFormat() {
		return DefaultVertexFormat.POSITION_COLOR;
	}

	protected void setShader() {
		//RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
	}

	@Getter
	private static boolean inCustomMatrix = false;

	/**
	 * Creates a 1 to 1 pixel matrix
	 */
	public static void reloadCustomMatrix() {
		if (inCustomMatrix)
			throw new IllegalStateException("Already in custom matrix!");
		inCustomMatrix = true;
	}

	public static void reloadMinecraftMatrix() {
		if (!inCustomMatrix)
			throw new IllegalStateException("Already in Minecraft matrix!");
		inCustomMatrix = false;
	}

	protected static void setMatrix(float width, float height) {
	}

}
