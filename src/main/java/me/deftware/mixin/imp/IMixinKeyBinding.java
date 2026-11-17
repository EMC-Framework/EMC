package me.deftware.mixin.imp;

import com.mojang.blaze3d.platform.InputConstants;

public interface IMixinKeyBinding {

    void emcSetPressed(boolean state);

    InputConstants.Key getInput();

}
