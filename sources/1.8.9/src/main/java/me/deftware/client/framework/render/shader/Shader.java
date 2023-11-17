package me.deftware.client.framework.render.shader;

import me.deftware.client.framework.resource.ModResourceManager;
import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import me.deftware.mixin.imp.Uniformable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderUniform;


import java.util.List;

/**
 * @author Deftware
 */
public class Shader {

    private ShaderGroup shaderEffect;
    private Framebuffer framebuffer;

    private final MinecraftIdentifier identifier;
    private final ModResourceManager resourceManager;

    public Shader(MinecraftIdentifier identifier, ModResourceManager resourceManager) {
        this.identifier = identifier;
        this.resourceManager = resourceManager;
    }

    public void init() {
        Minecraft client = Minecraft.getMinecraft();
        if (shaderEffect == null) {
            try {
                shaderEffect = new ShaderGroup(client.getTextureManager(), resourceManager, client.getFramebuffer(), identifier);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            shaderEffect.createBindFramebuffers(client.displayWidth, client.displayHeight);
            framebuffer = new Framebuffer(shaderEffect.getFramebufferRaw("final"));
        }
    }

    public void close() {
        shaderEffect.deleteShaderGroup();
    }

    public void resize(int width, int height) {
        shaderEffect.createBindFramebuffers(width, height);
    }

    public void setUniform(String name, float... values) {
        List<net.minecraft.client.shader.Shader> passes = getUniformable().getPostShaders();
        for (net.minecraft.client.shader.Shader pass : passes) {
            ShaderUniform uniform = pass.getShaderManager().getShaderUniform(name);
            if (uniform != null) {
                if (values.length == 4) {
                    uniform.set(values[0], values[1], values[2], values[3]);
                } else if (values.length == 3) {
                    uniform.set(values[0], values[1], values[2]);
                } else if (values.length == 2) {
                    uniform.set(values[0], values[1]);
                } else if (values.length == 1) {
                    uniform.set(values[0]);
                }
            }
        }
    }

    public boolean isLoaded() {
        return shaderEffect != null && framebuffer != null;
    }

    public void render(float partialTicks) {
        shaderEffect.loadShaderGroup(partialTicks);
    }

    public ShaderGroup getShaderEffect() {
        return shaderEffect;
    }

    public Uniformable getUniformable() {
        return (Uniformable) shaderEffect;
    }

    public Framebuffer getFramebuffer() {
        return framebuffer;
    }

}
