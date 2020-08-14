package me.deftware.client.framework.utils;

/**
 * @author Deftware
 */
public class Tuple<T, E, K> {
	private T left;
	private E middle;
	private K right;

	public Tuple(final T left, final E middle, final K right) {
		this.left = left;
		this.middle = middle;
		this.right = right;
	}

	public T getLeft() {
		return this.left;
	}

	public E getMiddle() {
		return this.middle;
	}

	public K getRight() {
		return this.right;
	}

	public void setLeft(final T left) {
		this.left = left;
	}

	public void setMiddle(final E middle) {
		this.middle = middle;
	}

	public void setRight(final K right) {
		this.right = right;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) return true;
		if (!(o instanceof Tuple)) return false;
		final Tuple<?, ?, ?> other = (Tuple<?, ?, ?>) o;
		if (!other.canEqual((Object) this)) return false;
		final Object this$left = this.getLeft();
		final Object other$left = other.getLeft();
		if (this$left == null ? other$left != null : !this$left.equals(other$left)) return false;
		final Object this$middle = this.getMiddle();
		final Object other$middle = other.getMiddle();
		if (this$middle == null ? other$middle != null : !this$middle.equals(other$middle)) return false;
		final Object this$right = this.getRight();
		final Object other$right = other.getRight();
		if (this$right == null ? other$right != null : !this$right.equals(other$right)) return false;
		return true;
	}

	protected boolean canEqual(final Object other) {
		return other instanceof Tuple;
	}

	@Override
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final Object $left = this.getLeft();
		result = result * PRIME + ($left == null ? 43 : $left.hashCode());
		final Object $middle = this.getMiddle();
		result = result * PRIME + ($middle == null ? 43 : $middle.hashCode());
		final Object $right = this.getRight();
		result = result * PRIME + ($right == null ? 43 : $right.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Tuple(left=" + this.getLeft() + ", middle=" + this.getMiddle() + ", right=" + this.getRight() + ")";
	}
}
