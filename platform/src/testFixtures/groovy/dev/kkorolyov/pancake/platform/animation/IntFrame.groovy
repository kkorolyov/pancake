package dev.kkorolyov.pancake.platform.animation
/**
 * {@link Frame} wrapper for {@link Integer}.
 */
final class IntFrame implements Frame<IntFrame> {
	final int value

	IntFrame(int value) {
		this.value = value
	}

	@Override
	IntFrame lerp(IntFrame other, double mix) {
		return new IntFrame((other.value + (value - other.value) * mix) as int)
	}
	@Override
	IntFrame sum(IntFrame other) {
		return new IntFrame(value + other.value)
	}
	@Override
	IntFrame diff(IntFrame other) {
		return new IntFrame(value - other.value)
	}

	boolean equals(o) {
		if (this.is(o)) return true
		if (o == null || getClass() != o.class) return false

		IntFrame other = (IntFrame) o

		if (value != other.value) return false

		return true
	}
	int hashCode() {
		return value
	}
}
