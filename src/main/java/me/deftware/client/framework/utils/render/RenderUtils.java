package me.deftware.client.framework.utils.render;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.platform.GlStateManager;
import me.deftware.client.framework.wrappers.IResourceLocation;
import me.deftware.client.framework.wrappers.entity.*;
import me.deftware.client.framework.wrappers.math.IAxisAlignedBB;
import me.deftware.client.framework.wrappers.math.IVec3d;
import me.deftware.client.framework.wrappers.world.IBlockPos;
import me.deftware.client.framework.wrappers.world.ICamera;
import me.deftware.client.framework.wrappers.world.IChunkPos;
import me.deftware.mixin.imp.IMixinEntityRenderer;
import me.deftware.mixin.imp.IMixinRenderManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.PlayerSkinTexture;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BoundingBox;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.lwjgl.opengl.GL11.*;

/**
 * Common functions used for rendering
 */
@SuppressWarnings("Duplicates")
public class RenderUtils {

    public static int enemy = 0;
    public static int friend = 1;
    public static int other = 2;
    public static int target = 3;
    public static int team = 4;

    public static void loadShader(IResourceLocation location) {
        ((IMixinEntityRenderer) MinecraftClient.getInstance().gameRenderer).loadCustomShader(location);
    }

    public static IMixinRenderManager getRenderManager() {
        return (IMixinRenderManager) MinecraftClient.getInstance().getEntityRenderManager();
    }

    public static void disableShader() {
        MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().gameRenderer.disableShader());
    }

    public static void glColor(Color color) {
        GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F,
                color.getAlpha() / 255.0F);
    }

    public static int floor_double(double value) {
        int i = (int) value;
        return value < i ? i - 1 : i;
    }

    public static void enableGL3D(float lineWidth) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        GlStateManager.activeTexture(33985);
        GlStateManager.disableTexture();
        GlStateManager.activeTexture(33984);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(lineWidth);
    }

    public static void drawFilledCircle(int xx, int yy, float radius, int col) {
        float f = (col >> 24 & 0xFF) / 255.0F;
        float f2 = (col >> 16 & 0xFF) / 255.0F;
        float f3 = (col >> 8 & 0xFF) / 255.0F;
        float f4 = (col & 0xFF) / 255.0F;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
        GL11.glBegin(6);

        for (int i = 0; i < 50; i++) {
            float x = (float) (radius * Math.sin(i * 0.12566370614359174D));
            float y = (float) (radius * Math.cos(i * 0.12566370614359174D));
            GL11.glColor4f(f2, f3, f4, f);
            GL11.glVertex2f(xx + x, yy + y);
        }

        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    public static void drawSelectionBoundingBox(IAxisAlignedBB BoundingBox) {
        RenderUtils.drawSelectionBoundingBox(BoundingBox.getAABB());
    }

    public static void renderChunks(List<IChunkPos> positions, Color color) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder worldRenderer = tessellator.getBufferBuilder();

        final float red = color.getRed();
        final float green = color.getGreen();
        final float blue = color.getBlue();
        final float alpha = color.getAlpha();

        double renderY = Math.floor(IEntityPlayer.getBoundingBox().getAABB().minY) + 0.05 - ICamera.getPosY();

        GL11.glPushMatrix();
        RenderUtils.fixDarkLight();
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(1.0f);
        GL11.glColor4f(red, green, blue, alpha);

        GL11.glTranslated(-ICamera.getPosX(), -ICamera.getPosY(), -ICamera.getPosZ());

        for (IChunkPos chunkPos : positions) {
            if (chunkPos != null) {
                IVec3d chunkVector = chunkPos.getBlockPos().getVector();

                worldRenderer.begin(2, VertexFormats.POSITION);
                worldRenderer.vertex(chunkVector.getFluentX(), renderY, chunkVector.getFluentZ()).next();
                worldRenderer.vertex(chunkVector.getFluentX() + 16, renderY, chunkVector.getFluentZ()).next();
                worldRenderer.vertex(chunkVector.getFluentX() + 16, renderY, chunkVector.getFluentZ() + 16).next();
                worldRenderer.vertex(chunkVector.getFluentX(), renderY, chunkVector.getFluentZ() + 16).next();
                tessellator.draw();
            }
        }

        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawSelectionBoundingBox(BoundingBox boundingBox) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBufferBuilder();
        vertexbuffer.begin(3, VertexFormats.POSITION);
        vertexbuffer.vertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ).next();
        vertexbuffer.vertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).next();
        vertexbuffer.vertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).next();
        vertexbuffer.vertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).next();
        vertexbuffer.vertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ).next();
        tessellator.draw();
        vertexbuffer.begin(3, VertexFormats.POSITION);
        vertexbuffer.vertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).next();
        vertexbuffer.vertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).next();
        vertexbuffer.vertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).next();
        vertexbuffer.vertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).next();
        vertexbuffer.vertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).next();
        tessellator.draw();
        vertexbuffer.begin(1, VertexFormats.POSITION);
        vertexbuffer.vertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ).next();
        vertexbuffer.vertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).next();
        vertexbuffer.vertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).next();
        vertexbuffer.vertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).next();
        vertexbuffer.vertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).next();
        vertexbuffer.vertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).next();
        vertexbuffer.vertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).next();
        vertexbuffer.vertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).next();
        tessellator.draw();
    }

    public static void drawCircle(int xx, int yy, int radius, int col) {
        float f = (col >> 24 & 0xFF) / 255.0F;
        float f2 = (col >> 16 & 0xFF) / 255.0F;
        float f3 = (col >> 8 & 0xFF) / 255.0F;
        float f4 = (col & 0xFF) / 255.0F;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glBegin(2);
        for (int i = 0; i < 70; i++) {
            float x = (float) (radius * Math.cos(i * 0.08975979010256552D));
            float y = (float) (radius * Math.sin(i * 0.08975979010256552D));
            GL11.glColor4f(f2, f3, f4, f);
            GL11.glVertex2f(xx + x, yy + y);
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }

    public static void drawRect(float g, float h, float i, float j, Color c) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        RenderUtils.glColor(c);
        GL11.glBegin(7);
        GL11.glVertex2d(i, h);
        GL11.glVertex2d(g, h);
        GL11.glVertex2d(g, j);
        GL11.glVertex2d(i, j);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public static void drawRect(float g, float h, float i, float j, int col1) {
        float f = (col1 >> 24 & 0xFF) / 255.0F;
        float f2 = (col1 >> 16 & 0xFF) / 255.0F;
        float f3 = (col1 >> 8 & 0xFF) / 255.0F;
        float f4 = (col1 & 0xFF) / 255.0F;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(7);
        GL11.glVertex2d(i, h);
        GL11.glVertex2d(g, h);
        GL11.glVertex2d(g, j);
        GL11.glVertex2d(i, j);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, int col1, int col2) {
        RenderUtils.drawRect(x, y, x2, y2, col2);
        float f = (col1 >> 24 & 0xFF) / 255.0F;
        float f2 = (col1 >> 16 & 0xFF) / 255.0F;
        float f3 = (col1 >> 8 & 0xFF) / 255.0F;
        float f4 = (col1 & 0xFF) / 255.0F;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, Color c, Color c2) {
        RenderUtils.drawRect(x, y, x2, y2, c2);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        RenderUtils.glColor(c);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public static void box(double x, double y, double z, double x2, double y2, double z2, float red, float green,
                           float blue, float alpha) {
        x -= (RenderUtils.getRenderManager()).getRenderPosX();
        y -= (RenderUtils.getRenderManager()).getRenderPosY();
        z -= (RenderUtils.getRenderManager()).getRenderPosZ();
        x2 -= (RenderUtils.getRenderManager()).getRenderPosX();
        y2 -= (RenderUtils.getRenderManager()).getRenderPosY();
        z2 -= (RenderUtils.getRenderManager()).getRenderPosZ();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL_BLEND);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        RenderUtils.drawColorBox(new BoundingBox(x, y, z, x2, y2, z2), red, green, blue, alpha);
        GL11.glColor4d(0.0D, 0.0D, 0.0D, 0.5D);
        RenderUtils.drawSelectionBoundingBox(new BoundingBox(x, y, z, x2, y2, z2));
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL_BLEND);
        GlStateManager.clearCurrentColor();
    }

    public static void frame(double x, double y, double z, double x2, double y2, double z2, Color color) {
        x -= (RenderUtils.getRenderManager()).getRenderPosX();
        y -= (RenderUtils.getRenderManager()).getRenderPosY();
        z -= (RenderUtils.getRenderManager()).getRenderPosZ();
        x2 -= (RenderUtils.getRenderManager()).getRenderPosX();
        y2 -= (RenderUtils.getRenderManager()).getRenderPosY();
        z2 -= (RenderUtils.getRenderManager()).getRenderPosZ();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL_BLEND);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        RenderUtils.glColor(color);
        RenderUtils.drawSelectionBoundingBox(new BoundingBox(x, y, z, x2, y2, z2));
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL_BLEND);
    }

    public static void ESPBox(IBlockPos IBlockPos, double red, double green, double blue, double alpha, float lineWidth, boolean boundryBox) {
        RenderUtils.fixDarkLight();
        GlStateManager.clearCurrentColor();
        double x = IBlockPos.getX() - (RenderUtils.getRenderManager()).getRenderPosX();
        double y = IBlockPos.getY() - (RenderUtils.getRenderManager()).getRenderPosY();
        double z = IBlockPos.getZ() - (RenderUtils.getRenderManager()).getRenderPosZ();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL_BLEND);
        GL11.glLineWidth(lineWidth);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glColor4d(red, green, blue, alpha);
        RenderUtils.drawColorBox(new BoundingBox(x, y, z, x + 1.0, y + 1.0, z + 1.0), 0F, 0F, 0F, 0F);
        if(boundryBox){
            GL11.glColor4d(0.0D, 0.0D, 0.0D, 0.5D);
            RenderUtils.drawSelectionBoundingBox(new BoundingBox(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
        }
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL_BLEND);
        GlStateManager.clearCurrentColor();
    }

    public static void ESPBox(IAxisAlignedBB IBlockPos, double red, double green, double blue, double alpha, float lineWidth, boolean boundryBox) {
        RenderUtils.fixDarkLight();
        GlStateManager.clearCurrentColor();
        IBlockPos = IBlockPos.offSet(-(RenderUtils.getRenderManager()).getRenderPosX(), -(RenderUtils.getRenderManager()).getRenderPosY(), -(RenderUtils.getRenderManager()).getRenderPosZ());
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL_BLEND);
        GL11.glLineWidth(lineWidth);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glColor4d(red, green, blue, alpha);
        RenderUtils.drawColorBox(IBlockPos.getAABB(), 0.0F, 0.0F, 0.0F, 0.0F);
        if(boundryBox){
            GL11.glColor4d(0.0D, 0.0D, 0.0D, 0.5D);
            RenderUtils.drawSelectionBoundingBox(IBlockPos.getAABB());
        }
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL_BLEND);
        GlStateManager.clearCurrentColor();
    }

    public static void emptyESPBox(IBlockPos IBlockPos, double red, double green, double blue, double alpha, float lineWidth) {
        RenderUtils.fixDarkLight();
        GlStateManager.clearCurrentColor();
        double x = IBlockPos.getX() - (RenderUtils.getRenderManager()).getRenderPosX();
        double y = IBlockPos.getY() - (RenderUtils.getRenderManager()).getRenderPosY();
        double z = IBlockPos.getZ() - (RenderUtils.getRenderManager()).getRenderPosZ();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL_BLEND);
        GL11.glLineWidth(lineWidth);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glColor4d(red, green, blue, alpha);
        RenderUtils.drawSelectionBoundingBox(new BoundingBox(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL_BLEND);
        GlStateManager.clearCurrentColor();
    }

    public static void emptyESPBox(IAxisAlignedBB IBlockPos, double red, double green, double blue, double alpha, float lineWidth) {
        RenderUtils.fixDarkLight();
        GlStateManager.clearCurrentColor();
        IBlockPos = IBlockPos.offSet(-(RenderUtils.getRenderManager()).getRenderPosX(), -(RenderUtils.getRenderManager()).getRenderPosY(), -(RenderUtils.getRenderManager()).getRenderPosZ());
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL_BLEND);
        GL11.glLineWidth(lineWidth);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glColor4d(red, green, blue, alpha);
        RenderUtils.drawSelectionBoundingBox(IBlockPos.getAABB());
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL_BLEND);
        GlStateManager.clearCurrentColor();
    }

    public static void fixDarkLight() {
        GlStateManager.enableLighting();
        GuiLighting.disable();
    }

    private static final HashMap<String, Pair<Boolean, Identifier>> loadedSkins = new HashMap<>();

    public static void bindSkinTexture(String name, String uuid) {
        GameProfile profile = new GameProfile(UUID.fromString(uuid), name);
        if(loadedSkins.containsKey(name)) {
            if (loadedSkins.get(name).getLeft()) {
                MinecraftClient.getInstance().getTextureManager().bindTexture(loadedSkins.get(name).getRight());
            } else {
                MinecraftClient.getInstance().getTextureManager().bindTexture(DefaultSkinHelper.getTexture(profile.getId()));
            }
        } else {
            loadedSkins.put(name, new Pair<>(false, null));
            try {
                MinecraftClient.getInstance().getSkinProvider().loadSkin(profile, (type, identifier, minecraftProfileTexture) -> {
                    if (type == MinecraftProfileTexture.Type.SKIN) {
                        loadedSkins.put(name, new Pair<>(true, identifier));
                    }
                }, true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void drawAltFace(String name, String uuid, int x, int y, int w, int h, boolean selected) {
        try {
            bindSkinTexture(name, uuid);
            glEnable(GL_BLEND);
            glColor4f(0.9F, 0.9F, 0.9F, 1.0F);
            DrawableHelper.blit(x, y, 24, 24, w, h, 192, 192);
            DrawableHelper.blit(x, y, 120, 24, w, h, 192, 192);
            glDisable(GL_BLEND);
        } catch (Exception ignored) { }
    }

    public static void nukerBox(IBlockPos IBlockPos, float damage) {
        RenderUtils.fixDarkLight();
        GlStateManager.clearCurrentColor();
        double x = IBlockPos.getX() - (RenderUtils.getRenderManager()).getRenderPosX();
        double y = IBlockPos.getY() - (RenderUtils.getRenderManager()).getRenderPosY();
        double z = IBlockPos.getZ() - (RenderUtils.getRenderManager()).getRenderPosZ();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(1.0F);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(damage, 1.0F - damage, 0.0F, 0.15F);
        RenderUtils.drawColorBox(
                new BoundingBox(x + 0.5D - damage / 2.0F, y + 0.5D - damage / 2.0F, z + 0.5D - damage / 2.0F,
                        x + 0.5D + damage / 2.0F, y + 0.5D + damage / 2.0F, z + 0.5D + damage / 2.0F),
                damage, 1.0F - damage, 0.0F, 0.15F);
        GL11.glColor4d(0.0D, 0.0D, 0.0D, 0.5D);
        RenderUtils.drawSelectionBoundingBox(
                new BoundingBox(x + 0.5D - damage / 2.0F, y + 0.5D - damage / 2.0F, z + 0.5D - damage / 2.0F,
                        x + 0.5D + damage / 2.0F, y + 0.5D + damage / 2.0F, z + 0.5D + damage / 2.0F));
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }

    public static void searchBox(IBlockPos IBlockPos) {
        RenderUtils.fixDarkLight();
        GlStateManager.clearCurrentColor();
        double x = IBlockPos.getX() - (RenderUtils.getRenderManager()).getRenderPosX();
        double y = IBlockPos.getY() - (RenderUtils.getRenderManager()).getRenderPosY();
        double z = IBlockPos.getZ() - (RenderUtils.getRenderManager()).getRenderPosZ();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(1.0F);
        float sinus = 1.0F - MathHelper
                .abs(MathHelper.sin((System.currentTimeMillis() % 1000) % 10000L / 10000.0F * 3.1415927F * 4.0F) * 1.0F);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(1.0F - sinus, sinus, 0.0F, 0.15F);
        RenderUtils.drawColorBox(new BoundingBox(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D), 1.0F - sinus, sinus, 0.0F, 0.15F);
        GL11.glColor4d(0.0D, 0.0D, 0.0D, 0.5D);
        RenderUtils.drawSelectionBoundingBox(new BoundingBox(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }

    public static void drawColorBox(IAxisAlignedBB BoundingBox, float red, float green, float blue, float alpha) {
        RenderUtils.drawColorBox(BoundingBox.getAABB(), red, green, blue, alpha);
    }

    public static void drawOutlinedBox(IAxisAlignedBB bbb) {
        BoundingBox bb = bbb.getAABB();

        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);

        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);

        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);

        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);

        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);

        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);

        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);

        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);

        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);

        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);

        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);

        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
    }

    public static void drawNode(IAxisAlignedBB bbb) {
        BoundingBox bb = bbb.getAABB();

        double midX = (bb.minX + bb.maxX) / 2;
        double midY = (bb.minY + bb.maxY) / 2;
        double midZ = (bb.minZ + bb.maxZ) / 2;

        GL11.glVertex3d(midX, midY, bb.maxZ);
        GL11.glVertex3d(bb.minX, midY, midZ);

        GL11.glVertex3d(bb.minX, midY, midZ);
        GL11.glVertex3d(midX, midY, bb.minZ);

        GL11.glVertex3d(midX, midY, bb.minZ);
        GL11.glVertex3d(bb.maxX, midY, midZ);

        GL11.glVertex3d(bb.maxX, midY, midZ);
        GL11.glVertex3d(midX, midY, bb.maxZ);

        GL11.glVertex3d(midX, bb.maxY, midZ);
        GL11.glVertex3d(bb.maxX, midY, midZ);

        GL11.glVertex3d(midX, bb.maxY, midZ);
        GL11.glVertex3d(bb.minX, midY, midZ);

        GL11.glVertex3d(midX, bb.maxY, midZ);
        GL11.glVertex3d(midX, midY, bb.minZ);

        GL11.glVertex3d(midX, bb.maxY, midZ);
        GL11.glVertex3d(midX, midY, bb.maxZ);

        GL11.glVertex3d(midX, bb.minY, midZ);
        GL11.glVertex3d(bb.maxX, midY, midZ);

        GL11.glVertex3d(midX, bb.minY, midZ);
        GL11.glVertex3d(bb.minX, midY, midZ);

        GL11.glVertex3d(midX, bb.minY, midZ);
        GL11.glVertex3d(midX, midY, bb.minZ);

        GL11.glVertex3d(midX, bb.minY, midZ);
        GL11.glVertex3d(midX, midY, bb.maxZ);
    }

    public static void drawColorBox(BoundingBox BoundingBox, float red, float green, float blue, float alpha) {
        Tessellator ts = Tessellator.getInstance();
        BufferBuilder vb = ts.getBufferBuilder();
        vb.begin(7, VertexFormats.POSITION_UV);
        vb.vertex(BoundingBox.minX, BoundingBox.minY, BoundingBox.minZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.minX, BoundingBox.maxY, BoundingBox.minZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.maxX, BoundingBox.minY, BoundingBox.minZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.maxX, BoundingBox.maxY, BoundingBox.minZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.maxX, BoundingBox.minY, BoundingBox.maxZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.maxX, BoundingBox.maxY, BoundingBox.maxZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.minX, BoundingBox.minY, BoundingBox.maxZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.minX, BoundingBox.maxY, BoundingBox.maxZ).color(red, green, blue, alpha).next();
        ts.draw();
        vb.begin(7, VertexFormats.POSITION_UV);
        vb.vertex(BoundingBox.maxX, BoundingBox.maxY, BoundingBox.minZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.maxX, BoundingBox.minY, BoundingBox.minZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.minX, BoundingBox.maxY, BoundingBox.minZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.minX, BoundingBox.minY, BoundingBox.minZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.minX, BoundingBox.maxY, BoundingBox.maxZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.minX, BoundingBox.minY, BoundingBox.maxZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.maxX, BoundingBox.maxY, BoundingBox.maxZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.maxX, BoundingBox.minY, BoundingBox.maxZ).color(red, green, blue, alpha).next();
        ts.draw();
        vb.begin(7, VertexFormats.POSITION_UV);
        vb.vertex(BoundingBox.minX, BoundingBox.maxY, BoundingBox.minZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.maxX, BoundingBox.maxY, BoundingBox.minZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.maxX, BoundingBox.maxY, BoundingBox.maxZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.minX, BoundingBox.maxY, BoundingBox.maxZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.minX, BoundingBox.maxY, BoundingBox.minZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.minX, BoundingBox.maxY, BoundingBox.maxZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.maxX, BoundingBox.maxY, BoundingBox.maxZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.maxX, BoundingBox.maxY, BoundingBox.minZ).color(red, green, blue, alpha).next();
        ts.draw();
        vb.begin(7, VertexFormats.POSITION_UV);
        vb.vertex(BoundingBox.minX, BoundingBox.minY, BoundingBox.minZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.maxX, BoundingBox.minY, BoundingBox.minZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.maxX, BoundingBox.minY, BoundingBox.maxZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.minX, BoundingBox.minY, BoundingBox.maxZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.minX, BoundingBox.minY, BoundingBox.minZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.minX, BoundingBox.minY, BoundingBox.maxZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.maxX, BoundingBox.minY, BoundingBox.maxZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.maxX, BoundingBox.minY, BoundingBox.minZ).color(red, green, blue, alpha).next();
        ts.draw();
        vb.begin(7, VertexFormats.POSITION_UV);
        vb.vertex(BoundingBox.minX, BoundingBox.minY, BoundingBox.minZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.minX, BoundingBox.maxY, BoundingBox.minZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.minX, BoundingBox.minY, BoundingBox.maxZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.minX, BoundingBox.maxY, BoundingBox.maxZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.maxX, BoundingBox.minY, BoundingBox.maxZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.maxX, BoundingBox.maxY, BoundingBox.maxZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.maxX, BoundingBox.minY, BoundingBox.minZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.maxX, BoundingBox.maxY, BoundingBox.minZ).color(red, green, blue, alpha).next();
        ts.draw();
        vb.begin(7, VertexFormats.POSITION_UV);
        vb.vertex(BoundingBox.minX, BoundingBox.maxY, BoundingBox.maxZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.minX, BoundingBox.minY, BoundingBox.maxZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.minX, BoundingBox.maxY, BoundingBox.minZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.minX, BoundingBox.minY, BoundingBox.minZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.maxX, BoundingBox.maxY, BoundingBox.minZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.maxX, BoundingBox.minY, BoundingBox.minZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.maxX, BoundingBox.maxY, BoundingBox.maxZ).color(red, green, blue, alpha).next();
        vb.vertex(BoundingBox.maxX, BoundingBox.minY, BoundingBox.maxZ).color(red, green, blue, alpha).next();
        ts.draw();
    }

    public static void tracerLine(IItemEntity entity, int mode) {
        RenderUtils.tracerLine(entity.getItem(), mode);
    }

    public static void tracerLine(IPlayer entity, int mode) {
        RenderUtils.tracerLine(entity.getPlayer(), mode);
    }

    public static void tracerLine(IMob entity, int mode) {
        RenderUtils.tracerLine(entity.getMob(), mode);
    }

    public static void tracerLine(IEntity entity, int mode) {
        RenderUtils.tracerLine(entity.getEntity(), mode);
    }

    public static void tracerLine(IDummyEntity entity, Color color) {
        RenderUtils.tracerLine(entity.getEntity(), color);
    }

    public static void tracerLine(Entity entity, int mode) {
        RenderUtils.fixDarkLight();
        GlStateManager.clearCurrentColor();
        double x = entity.x - (RenderUtils.getRenderManager()).getRenderPosX();
        double y = entity.y + entity.getHeight() / 2.0F - (RenderUtils.getRenderManager()).getRenderPosY();
        double z = entity.z - (RenderUtils.getRenderManager()).getRenderPosZ();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        if (mode == 0) {
            GL11.glColor4d(1.0F - MinecraftClient.getInstance().player.distanceTo(entity) / 40.0F,
                    MinecraftClient.getInstance().player.distanceTo(entity) / 40.0F, 0.0D, 0.5D);
        } else if (mode == 1) {
            GL11.glColor4d(0.0D, 0.0D, 1.0D, 0.5D);
        } else if (mode == 2) {
            GL11.glColor4d(1.0D, 1.0D, 0.0D, 0.5D);
        } else if (mode == 3) {
            GL11.glColor4d(1.0D, 0.0D, 0.0D, 0.5D);
        } else if (mode == 4) {
            GL11.glColor4d(0.0D, 1.0D, 0.0D, 0.5D);
        }
        Vec3d eyes = new Vec3d(0.0D, 0.0D, 1.0D)
                .rotateX(-(float) Math.toRadians(MinecraftClient.getInstance().player.pitch))
                .rotateY(-(float) Math.toRadians(MinecraftClient.getInstance().player.yaw));

        GL11.glBegin(1);

        GL11.glVertex3d(eyes.x, eyes.y, eyes.z);
        GL11.glVertex3d(x, y, z);


        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }

    public static void tracerLine(Entity entity, Color color) {
        RenderUtils.fixDarkLight();
        GlStateManager.clearCurrentColor();

        double x = entity.x - (RenderUtils.getRenderManager()).getRenderPosX();
        double y = entity.y + entity.getHeight() / 2.0F - (RenderUtils.getRenderManager()).getRenderPosY();
        double z = entity.z - (RenderUtils.getRenderManager()).getRenderPosZ();

        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);

        RenderUtils.glColor(color);

        Vec3d eyes = new Vec3d(0.0D, 0.0D, 1.0D)
                .rotateX(-(float) Math.toRadians(MinecraftClient.getInstance().player.pitch))
                .rotateY(-(float) Math.toRadians(MinecraftClient.getInstance().player.yaw));

        GL11.glBegin(1);

        GL11.glVertex3d(eyes.x, eyes.y, eyes.z);
        GL11.glVertex3d(x, y, z);

        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }

    public static void tracerLine(Entity entity, Color color, float alpha) {
        RenderUtils.fixDarkLight();
        GlStateManager.clearCurrentColor();
        double x = entity.x - (RenderUtils.getRenderManager()).getRenderPosX() + 0.5f;
        double y = entity.y + entity.getHeight() / 2.0F - (RenderUtils.getRenderManager()).getRenderPosY() - 0.5f;
        double z = entity.z - (RenderUtils.getRenderManager()).getRenderPosZ() + 0.5f;

        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);

        GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);

        Vec3d eyes = new Vec3d(0.0D, 0.0D, 1.0D)
                .rotateX(-(float) Math.toRadians(MinecraftClient.getInstance().player.pitch))
                .rotateY(-(float) Math.toRadians(MinecraftClient.getInstance().player.yaw));

        GL11.glBegin(1);

        GL11.glVertex3d(eyes.x, eyes.y, eyes.z);
        GL11.glVertex3d(x, y, z);

        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);

        GlStateManager.clearCurrentColor();
    }

    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    public static void enableGL3D() {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4353);
        GL11.glDisable(2896);
    }

    public static void disableGL3D() {
        GL11.glEnable(2896);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
    }

    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void glColor(int hex) {
        float alpha = (hex >> 24 & 0xFF) / 255.0F;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static int reAlpha(int color, float alpha) {
        Color c = new Color(color);
        float r = ((float) 1 / 255) * c.getRed();
        float g = ((float) 1 / 255) * c.getGreen();
        float b = ((float) 1 / 255) * c.getBlue();
        return new Color(r, g, b, alpha).getRGB();
    }

    public static void drawBorderedRectReliant(float x, float y, float x1, float y1, float lineWidth, int inside,
                                               int border) {
        RenderUtils.enableGL2D();
        RenderUtils.drawRect(x, y, x1, y1, inside);
        RenderUtils.glColor(border);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(3);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        RenderUtils.disableGL2D();
    }

    public static void tracerLine(int x, int y, int z, Color color) {
        x = (int) (x + (0.5D - (RenderUtils.getRenderManager()).getRenderPosX()));
        y = (int) (y + (0.5D - (RenderUtils.getRenderManager()).getRenderPosY()));
        z = (int) (z + (0.5D - (RenderUtils.getRenderManager()).getRenderPosZ()));
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        RenderUtils.glColor(color);
        GL11.glBegin(1);

        GL11.glVertex3d(0.0D, MinecraftClient.getInstance().player.getEyeHeight(MinecraftClient.getInstance().player.getPose()), 0.0D);
        GL11.glVertex3d(x, y, z);

        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }
}
