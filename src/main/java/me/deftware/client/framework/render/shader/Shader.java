package me.deftware.client.framework.render.shader;

import me.deftware.client.framework.main.bootstrap.Bootstrap;
import org.lwjgl.opengl.GL20;

/**
 * @author Deftware
 */
public class Shader {
	private final IShaderProvider provider;
	private boolean bound = false;
	private int program;
	private int vs;
	private int fs;

	public Shader(IShaderProvider provider) {
		this.provider = provider;
		try {
			program = GL20.glCreateProgram();
			// Vertex shader
			vs = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
			provider.compileVertexShader(vs);
			if (GL20.glGetShaderi(vs, GL20.GL_COMPILE_STATUS) != 1) {
				throw new RuntimeException(GL20.glGetShaderInfoLog(vs));
			}
			// Fragment shader
			fs = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
			provider.compileFragmentShader(fs);
			if (GL20.glGetShaderi(fs, GL20.GL_COMPILE_STATUS) != 1) {
				throw new RuntimeException(GL20.glGetShaderInfoLog(fs));
			}
			// Attach
			GL20.glAttachShader(program, vs);
			GL20.glAttachShader(program, fs);
			// Link to program
			GL20.glLinkProgram(program);
			if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) != 1) {
				throw new RuntimeException(GL20.glGetShaderInfoLog(program));
			}
			GL20.glValidateProgram(program);
			if (GL20.glGetProgrami(program, GL20.GL_VALIDATE_STATUS) != 1) {
				throw new RuntimeException(GL20.glGetShaderInfoLog(program));
			}
			Bootstrap.logger.info("Loaded shader {}", provider.getName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setupUniforms() {
	}

	public int getUniform(String name) {
		return GL20.glGetUniformLocation(program, name);
	}

	public void bind() {
		GL20.glUseProgram(program);
		bound = true;
	}

	public void unbind() {
		GL20.glUseProgram(0);
		bound = false;
	}

	public IShaderProvider getProvider() {
		return this.provider;
	}

	public boolean isBound() {
		return this.bound;
	}

	public int getProgram() {
		return this.program;
	}

	public int getVs() {
		return this.vs;
	}

	public int getFs() {
		return this.fs;
	}
}
