package me.deftware.mixin.imp;

import me.deftware.client.framework.wrappers.entity.IEntity;

import java.util.HashMap;

public interface IMixinWorldClient {

	HashMap<Integer, IEntity> getIEntities();

}
