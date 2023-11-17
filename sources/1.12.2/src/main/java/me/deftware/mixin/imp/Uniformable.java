package me.deftware.mixin.imp;

import net.minecraft.client.shader.Shader;

import java.util.List;

public interface Uniformable {

    void registerUniformf(String name, float[] values);

    List<Shader> getPostShaders();

}
