package me.deftware.client.framework.render.gl;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.StringJoiner;

/**
 * Manages matrix transformations
 *
 * @author Deftware
 */
public class GLX {

    private GLXProvider provider = new GLXMatrixProvider();

    private static final GLX instance = new GLX();

    private MatrixStack matrices;

    /*
        Internal functions
     */

    public MatrixStack getMatrices() {
        return matrices;
    }

    public static GLX of(MatrixStack matrices) {
        instance.matrices = matrices;
        return instance;
    }

    public Matrix4f getModel() {
        return matrices.peek().getPositionMatrix();
    }

    public void modelViewStack(Runnable action) {
        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();
        matrixStack.multiplyPositionMatrix(getModel());
        RenderSystem.applyModelViewMatrix();
        action.run();
        matrixStack.pop();
        RenderSystem.applyModelViewMatrix();
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
            matrices.translate(x, y, z);
        }

        @Override
        public void scale(float x, float y, float z) {
            matrices.scale(x, y, z);
        }

        @Override
        public void color(float red, float green, float blue, float alpha) {
            RenderSystem.setShaderColor(red, green, blue, alpha);
        }

        @Override
        public void rotate(float angle, float x, float y, float z) {
            if (x > 0)
                matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(angle));
            if (y > 0)
                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(angle));
            if (z > 0)
                matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(angle));
        }

        @Override
        public void push() {
            matrices.push();
        }

        @Override
        public void pop() {
            matrices.pop();
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
