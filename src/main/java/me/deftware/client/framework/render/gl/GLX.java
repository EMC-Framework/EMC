package me.deftware.client.framework.render.gl;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.StringJoiner;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.joml.Matrix3x2fStack;

/**
 * Manages matrix transformations
 *
 * @author Deftware
 */
public class GLX {

    private GLXProvider provider = new GLXMatrixProvider();

    private static final GLX instance = new GLX();

    private GuiGraphicsExtractor context;
    private Matrix4f viewProjection3d;
    private final Matrix4fStack pose3d = new Matrix4fStack(16);
    private float colorR = 1f, colorG = 1f, colorB = 1f, colorA = 1f;

    /*
        Internal functions
     */

    public GuiGraphicsExtractor getContext() {
        return context;
    }

    public static GLX of(GuiGraphicsExtractor context) {
        instance.context = context;
        instance.viewProjection3d = null;
        instance.colorR = 1f;
        instance.colorG = 1f;
        instance.colorB = 1f;
        instance.colorA = 1f;
        return instance;
    }

    public static GLX of3D(GuiGraphicsExtractor context, Matrix4f viewProjection3d) {
        instance.context = context;
        instance.viewProjection3d = viewProjection3d != null ? new Matrix4f(viewProjection3d) : null;
        instance.pose3d.clear();
        instance.pose3d.identity();
        instance.colorR = 1f;
        instance.colorG = 1f;
        instance.colorB = 1f;
        instance.colorA = 1f;
        return instance;
    }

    public boolean isIn3D() {
        return viewProjection3d != null;
    }

    public Matrix4f getViewProjection3d() {
        return viewProjection3d;
    }

    public float getColorR() {
        return colorR;
    }

    public float getColorG() {
        return colorG;
    }

    public float getColorB() {
        return colorB;
    }

    public float getColorA() {
        return colorA;
    }

    public Matrix4f getModel() {
        if (viewProjection3d != null) {
            return pose3d;
        }
        if (context == null) {
            return new Matrix4f();
        }
        Matrix3x2fStack m = context.pose();
        return new Matrix4f(
                m.m00(), m.m01(), 0f, 0f,
                m.m10(), m.m11(), 0f, 0f,
                0f, 0f, 1f, 0f,
                m.m20(), m.m21(), 0f, 1f
        );
    }

    public void modelViewStack(Consumer<Matrix4fStack> action) {
        var matrixStack = RenderSystem.getModelViewStack();
        matrixStack.pushMatrix();
        matrixStack.mul(getModel());
        action.accept(matrixStack);
        matrixStack.popMatrix();
    }

    /*
        Public
     */

    public void isolate(Runnable action) {
        push();
        action.run();
        pop();
    }

    public void push() {
        provider.push();
    }

    public void pop() {
        provider.pop();
    }

    public void color(float red, float green, float blue, float alpha) {
        provider.color(red, green, blue, alpha);
    }

    public void color(float red, float green, float blue) {
        color(red, green, blue, 1f);
    }

    public void scale(float x, float y, float z) {
        provider.scale(x, y, z);
    }

    public void scale(double x, double y, double z) {
        scale((float) x, (float) y, (float) z);
    }

    public void translate(double x, double y, double z) {
        translate((float) x, (float) y, (float) z);
    }

    public void translate(float x, float y, float z) {
        provider.translate(x, y, z);
    }

    public void rotate(float angle, float x, float y, float z) {
        provider.rotate(angle, x, y, z);
    }

    public void rotate(double angle, double x, double y, double z) {
        rotate((float) angle, (float) x, (float) y, (float) z);
    }

    public String toString() {
        return new StringJoiner(",")
                .add("renderer=" + provider.getClass().getName())
                .toString();
    }

    public class GLXMatrixProvider implements GLXProvider {

        @Override
        public void translate(float x, float y, float z) {
            if (isIn3D()) {
                pose3d.translate(x, y, z);
            } else if (context != null) {
                context.pose().translate(x, y);
            }
        }

        @Override
        public void scale(float x, float y, float z) {
            if (isIn3D()) {
                pose3d.scale(x, y, z);
            } else if (context != null) {
                context.pose().scale(x, y);
            }
        }

        @Override
        public void color(float red, float green, float blue, float alpha) {
            colorR = red;
            colorG = green;
            colorB = blue;
            colorA = alpha;
        }

        @Override
        public void rotate(float angle, float x, float y, float z) {
            if (isIn3D()) {
                if (x != 0)
                    pose3d.rotate(Axis.XP.rotationDegrees(x > 0 ? angle : -angle));
                if (y != 0)
                    pose3d.rotate(Axis.YP.rotationDegrees(y > 0 ? angle : -angle));
                if (z != 0)
                    pose3d.rotate(Axis.ZP.rotationDegrees(z > 0 ? angle : -angle));
            } else if (context != null && z != 0) {
                context.pose().rotate((float) Math.toRadians(z > 0 ? angle : -angle));
            }
        }

        @Override
        public void push() {
            if (isIn3D()) {
                pose3d.pushMatrix();
            } else if (context != null) {
                context.pose().pushMatrix();
            }
        }

        @Override
        public void pop() {
            if (isIn3D()) {
                pose3d.popMatrix();
            } else if (context != null) {
                context.pose().popMatrix();
            }
        }

        @Override
        public String id() {
            return "DrawContext";
        }

    }

    public interface GLXProvider {

        default void translate(float x, float y, float z) {
            GL11.glTranslatef(x, y, z);
        }

        default void rotate(float angle, float x, float y, float z) {
            GL11.glRotatef(angle, x, y, z);
        }

        default void scale(float x, float y, float z) {
            GL11.glScalef(x, y, z);
        }

        default void color(float red, float green, float blue, float alpha) {
            GL11.glColor4f(red, green, blue, alpha);
        }

        default void color(Color color) {
            color(
                    color.getRed() / 255f,
                    color.getGreen() / 255f,
                    color.getBlue() / 255f,
                    color.getAlpha() / 255f
            );
        }

        default void push() {
            GL11.glPushMatrix();
        }

        default void pop() {
            GL11.glPopMatrix();
        }

        String id();

    }

}
