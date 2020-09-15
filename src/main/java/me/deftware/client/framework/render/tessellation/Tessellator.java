package me.deftware.client.framework.render.tessellation;

/**
 * @author Deftware
 */
public class Tessellator {

	private static Tessellator tessellatorInstance;

	public static Tessellator getInstance() {
		if (tessellatorInstance == null || tessellatorInstance.getMinecraftTessellator() != net.minecraft.client.renderer.Tessellator.getInstance()) {
			tessellatorInstance = new Tessellator();
		}
		return tessellatorInstance;
	}

	private final net.minecraft.client.renderer.Tessellator tessellator;
	private final VertexBuilder builder;

	private Tessellator() {
		this.tessellator = net.minecraft.client.renderer.Tessellator.getInstance();
		this.builder = new VertexBuilder(this.tessellator.getWorldRenderer());
	}

	public net.minecraft.client.renderer.Tessellator getMinecraftTessellator() {
		return tessellator;
	}

	public VertexBuilder getBuilder() {
		return builder;
	}

	public void draw() {
		tessellator.draw();
	}

}
