package me.deftware.mixin.imp;

public interface IMixinEntityLivingBase {

	int getActiveItemStackUseCount();

	void _setStepHeight(float height);

	float getAirStrafingSpeed();

	void setAirStrafingSpeed(float value);

}
