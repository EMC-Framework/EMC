package me.deftware.mixin.imp;

import net.minecraft.client.gl.PostEffectPass;

import java.util.List;

public interface Uniformable {

    void registerUniformf(String name, float[] values);

    List<PostEffectPass> getPostShaders();

}
