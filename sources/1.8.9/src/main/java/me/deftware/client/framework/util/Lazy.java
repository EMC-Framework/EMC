package me.deftware.client.framework.util;

import java.util.function.Supplier;

public class Lazy<T> {

	private T value;
	private final Supplier<T> supplier;

	public Lazy(Supplier<T> supplier) {
		this.supplier = supplier;
	}

	public T get() {
		if (this.value == null)
			this.value = this.supplier.get();
		return this.value;
	}

}
