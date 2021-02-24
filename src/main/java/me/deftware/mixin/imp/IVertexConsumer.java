package me.deftware.mixin.imp;

import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;

public interface IVertexConsumer {

    void quadAlpha(MatrixStack.Entry matrixEntry, BakedQuad quad, float[] brightnesses, float red, float green, float blue, float alpha, int[] lights, int overlay, boolean useQuadColorData);

}
