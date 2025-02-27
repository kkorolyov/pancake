package dev.kkorolyov.pancake.platform.animation;

import dev.kkorolyov.pancake.platform.utility.ArgVerify;

import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

/**
 * A collection of keyframe {@link Frame}s at specific offsets, returning interpolated frames at any offsets between {@code 0} and {@link #size()}.
 */
public final class Timeline<T extends Frame<T>> implements Iterable<Map.Entry<Integer, T>> {
	private final NavigableMap<Integer, T> keyframes = new TreeMap<>();

	/**
	 * Returns whether this timeline has a keyframe at {@code offset}.
	 */
	public boolean contains(int offset) {
		return keyframes.containsKey(offset);
	}
	/**
	 * Sets the keyframe {@code frame} at {@code offset}, replacing any previously set keyframe at that point.
	 * {@code offset} must be {@code >= 0}.
	 */
	public void put(int offset, T frame) {
		ArgVerify.greaterThanEqual("offset", 0, offset);

		keyframes.put(offset, frame);
	}
	/**
	 * Removes the keyframe at {@code offset} and returns it, if any.
	 * Throws {@link IllegalArgumentException} if {@code offset} is less than {@code 0} or greater than {@link #size()}.
	 */
	public T remove(int offset) {
		ArgVerify.betweenInclusive("offset", 0, size(), offset);

		return keyframes.remove(offset);
	}

	/**
	 * Returns the frame at {@code offset}.
	 * Offsets between keyframes return frames interpolated from the 2 adjacent keyframes.
	 * Throws {@link IllegalArgumentException} if {@code offset} is less than {@code 0} or greater than {@link #size()}.
	 */
	public T get(int offset) {
		ArgVerify.betweenInclusive("offset", 0, size(), offset);

		var a = keyframes.floorEntry(offset);
		var b = keyframes.ceilingEntry(offset);
		if (a == null) a = b;
		else if (b == null) b = a;

		int range = b.getKey() - a.getKey();
		double cursor = offset - a.getKey();

		return offset == a.getKey()
				? a.getValue()
				: offset == b.getKey()
				? b.getValue()
				: b.getValue().lerp(a.getValue(), cursor / range);
	}

	/**
	 * Returns the maximum supported offset - equivalent to the last keyframe.
	 * Returns {@code -1} if there are no keyframes.
	 */
	public int size() {
		return keyframes.isEmpty() ? -1 : keyframes.lastKey();
	}

	@Override
	public Iterator<Map.Entry<Integer, T>> iterator() {
		return keyframes.entrySet().iterator();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Timeline<?> other)) return false;
		return Objects.equals(keyframes, other.keyframes);
	}
	@Override
	public int hashCode() {
		return Objects.hashCode(keyframes);
	}
}
