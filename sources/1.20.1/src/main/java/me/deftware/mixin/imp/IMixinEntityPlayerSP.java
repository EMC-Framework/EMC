package me.deftware.mixin.imp;

public interface IMixinEntityPlayerSP {

	void setHorseJumpPower(float height);

	void sendMessageWithSender(String message, Class<?> sender);

}
