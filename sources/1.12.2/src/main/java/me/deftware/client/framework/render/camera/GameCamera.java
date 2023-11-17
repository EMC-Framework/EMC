package me.deftware.client.framework.render.camera;

import me.deftware.client.framework.math.Vector3;

/**
 * @author Deftware
 */
public interface GameCamera {
	
	Vector3<Double> getCameraPosition();

	float _getRotationPitch();

	float _getRotationYaw();

	double _getRenderPosX();

	double _getRenderPosY();

	double _getRenderPosZ();

}
