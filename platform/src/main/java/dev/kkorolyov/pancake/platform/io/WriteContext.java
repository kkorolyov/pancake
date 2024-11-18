package dev.kkorolyov.pancake.platform.io;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Writes content to a {@link ByteBuffer}.
 * Each call to a {@code get} method writes the value to the current position in the backing buffer and increments the position.
 */
public final class WriteContext {
	private final ByteBuffer buffer;

	private final Map<Object, Integer> objects = new IdentityHashMap<>();
	// ID 0 refers to the initial reference to the object
	private int idCounter = 1;

	/**
	 * Constructs a new write context writing to {@code buffer}.
	 */
	public WriteContext(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	/**
	 * Writes {@code value} to the current position in the backing buffer and increments the position.
	 */
	public void putByte(byte value) {
		buffer.put(value);
	}
	/**
	 * Writes {@code value} to the current position in the backing buffer and increments the position.
	 */
	public void putInt(int value) {
		buffer.putInt(value);
	}
	/**
	 * Writes {@code value} to the current position in the backing buffer and increments the position.
	 */
	public void putDouble(double value) {
		buffer.putDouble(value);
	}
	/**
	 * Writes {@code value} to the current position in the backing buffer and increments the position.
	 */
	public void putBoolean(boolean value) {
		buffer.put((byte) (value ? 1 : 0));
	}
	/**
	 * Writes {@code value} to the current position in the backing buffer and increments the position.
	 */
	public void putString(String value) {
		var bytes = value.getBytes(StandardCharsets.UTF_8);
		putInt(bytes.length);
		buffer.put(bytes);
	}

	/**
	 * Writes {@code value} using the best matching serializer to the current position in the backing buffer and increments the position.
	 * If this has previously been invoked with {@code value}, writes a reference ID, instead.
	 */
	public <T> void putObject(T value) {
		var id = objects.get(value);
		if (id != null) {
			putInt(id);
		} else {
			putInt(0);
			Serializers.get((Class<T>) value.getClass()).write(value, this);
			objects.put(value, idCounter++);
		}
	}
}
