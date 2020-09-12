package me.deftware.client.framework.util.types;

/**
 * @author Deftware
 */
public class Pair<L, R> {
	private L left;
	private R right;

	public Pair(final L left, final R right) {
		this.left = left;
		this.right = right;
	}

	public L getLeft() {
		return this.left;
	}

	public R getRight() {
		return this.right;
	}

	public void setLeft(final L left) {
		this.left = left;
	}

	public void setRight(final R right) {
		this.right = right;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) return true;
		if (!(o instanceof Pair)) return false;
		final Pair<?, ?> other = (Pair<?, ?>) o;
		if (!other.canEqual((Object) this)) return false;
		final Object this$left = this.getLeft();
		final Object other$left = other.getLeft();
		if (this$left == null ? other$left != null : !this$left.equals(other$left)) return false;
		final Object this$right = this.getRight();
		final Object other$right = other.getRight();
		if (this$right == null ? other$right != null : !this$right.equals(other$right)) return false;
		return true;
	}

	protected boolean canEqual(final Object other) {
		return other instanceof Pair;
	}

	@Override
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final Object $left = this.getLeft();
		result = result * PRIME + ($left == null ? 43 : $left.hashCode());
		final Object $right = this.getRight();
		result = result * PRIME + ($right == null ? 43 : $right.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Pair(left=" + this.getLeft() + ", right=" + this.getRight() + ")";
	}
}
