package me.deftware.client.framework.render.shader;

import lombok.Getter;
import me.deftware.client.framework.resource.ModResourceManager;
import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import me.deftware.mixin.mixins.shader.PostEffectProcessorAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.DefaultFramebufferSet;
import net.minecraft.client.util.Handle;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Deftware
 */
public class Shader {

    private PostEffectProcessor shaderEffect;
    private Framebuffer framebuffer;

    private final MinecraftIdentifier identifier;
    private final ModResourceManager resourceManager;

    @Getter
    private final ShaderFramebufferSet framebufferSet = new ShaderFramebufferSet();

    private final Map<String, float[]> uniformsF = new HashMap<>();

    public Shader(MinecraftIdentifier identifier, ModResourceManager resourceManager) {
        this.identifier = identifier;
        this.resourceManager = resourceManager;
    }

    public void init() {
        if (isLoaded()) {
            throw new RuntimeException("Shader already initialized");
        }
        MinecraftClient client = MinecraftClient.getInstance();
        var internalBuffer = new SimpleFramebuffer(
                client.getWindow().getFramebufferWidth(),
                client.getWindow().getFramebufferHeight(),
                true
        );
        internalBuffer.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        internalBuffer.clear();
        framebuffer = new Framebuffer(internalBuffer);
        var frameSet = Set.of(DefaultFramebufferSet.MAIN, ShaderFramebufferSet.FINAL);
        shaderEffect = resourceManager.getShaderLoader().loadPostEffect(identifier, frameSet);
    }

    public void close() {
        framebuffer.close();
    }

    public void resize(int width, int height) {
        framebuffer.resize(width, height);
    }

    public void setUniform(String name, float... values) {
        uniformsF.put(name, values);
    }

    public void applyUniforms() {
        var passes = ((PostEffectProcessorAccessor) shaderEffect).getPasses();
        for (var entry : uniformsF.entrySet()) {
            var name = entry.getKey();
            var values = entry.getValue();
            for (PostEffectPass pass : passes) {
                GlUniform uniform = pass.getProgram().getUniform(name);
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
    }

    public boolean isLoaded() {
        return shaderEffect != null && framebuffer != null;
    }

    public PostEffectProcessor getShaderEffect() {
        return shaderEffect;
    }

    public Framebuffer getFramebuffer() {
        return framebuffer;
    }

    public static class ShaderFramebufferSet implements PostEffectProcessor.FramebufferSet {

        /**
         * Dedicated framebuffer for a shader
         */
        public static final Identifier FINAL = Identifier.of("emc:final");

        private final Map<Identifier, Handle<net.minecraft.client.gl.Framebuffer>> handles = new HashMap<>();

        @Override
        public void set(Identifier id, Handle<net.minecraft.client.gl.Framebuffer> framebuffer) {
            handles.put(id, framebuffer);
        }

        @Override
        public Handle<net.minecraft.client.gl.Framebuffer> get(Identifier id) {
            if (!handles.containsKey(id)) {
                throw new RuntimeException("Requested framebuffer " + id + " does not exist in set");
            }
            return handles.get(id);
        }

    }

}
