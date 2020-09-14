package me.deftware.client.framework.event.events;

import me.deftware.client.framework.entity.block.TileEntity;
import me.deftware.client.framework.event.Event;

public class EventTileBlockRemoved extends Event {
	private final TileEntity blockEntity;

	public EventTileBlockRemoved(final TileEntity blockEntity) {
		this.blockEntity = blockEntity;
	}

	public TileEntity getBlockEntity() {
		return this.blockEntity;
	}
}
