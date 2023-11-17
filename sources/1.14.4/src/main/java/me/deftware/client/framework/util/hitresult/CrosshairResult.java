package me.deftware.client.framework.util.hitresult;

import me.deftware.client.framework.math.Vector3;
import net.minecraft.util.hit.HitResult;

/**
 * @author Deftware
 */
public class CrosshairResult {
	
	protected HitResult hitResult;

	public CrosshairResult(HitResult hitResult) {
		this.hitResult = hitResult;
	}

	public Vector3<Double> getVector() {
		return (Vector3<Double>) hitResult.getPos();
	}

	public HitResult getMinecraftHitResult() {
		return hitResult;
	}

	public CrosshairResult setReference(HitResult result) {
		this.hitResult = result;
		return this;
	}

}
