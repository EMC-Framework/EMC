package me.deftware.mixin.imp;

import net.minecraft.network.chat.Component;

public interface IMixinSignBlockEntity {

    Component[] getTextRows();
}
