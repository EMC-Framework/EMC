package me.deftware.client.framework.helper;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;

/**
 * @author Deftware
 */
public class GlStateHelper {

	public static void disableAlpha() {
		GlStateManager.disableAlphaTest();
	}

	public static void enableAlpha() {
		GlStateManager.enableAlphaTest();
	}

	public static void enablePolygonOffset() {
		GlStateManager.enablePolygonOffset();
	}

	public static void enableDepth() {
		GlStateManager.enableDepthTest();
	}

	public static void disableDepth() {
		GlStateManager.disableDepthTest();
	}

	public static void disableLighting() {
		GlStateManager.disableLighting();
	}

	public static void enableLighting() {
		GlStateManager.enableLighting();
	}

	public static void enableBlend() {
		GlStateManager.enableBlend();
	}

	public static void disableBlend() {
		GlStateManager.disableBlend();
	}

	public static void disableTexture2D() {
		GlStateManager.disableTexture2D();
	}

	public static void tryBlendFuncSeparate(int srcFactor, int dstFactor, int srcFactorAlpha, int dstFactorAlpha) {
		GlStateManager.blendFuncSeparate(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
	}

	public static void enableTexture2D() {
		GlStateManager.enableTexture2D();
	}

	public static void disableStandardItemLighting() {
		RenderHelper.disableStandardItemLighting();
	}

	public static void enableStandardItemLighting() {
		RenderHelper.enableStandardItemLighting();
	}

	public static void enableGUIStandardItemLighting() {
		RenderHelper.enableGUIStandardItemLighting();
	}

	public static void disablePolygonOffset() {
		GlStateManager.disablePolygonOffset();
	}

	public static void doPolygonOffset(float f, float g) {
		GlStateManager.polygonOffset(f, g);
	}

}
