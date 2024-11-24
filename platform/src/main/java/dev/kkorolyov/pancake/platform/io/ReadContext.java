package dev.kkorolyov.pancake.platform.io;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * A read-only view of a {@link ByteBuffer} that hydrates object references on the fly.
 * On object read, deserializes the data using the nearest {@link Class}-matched {@link Serializer} provider on the classpath, and stores an incrementing ID reference to the deserialized instance.
 * Subsequent reads of this ID from the backing buffer will return that same instance.
 * To maintain this order-reliant encounter guarantee, it is critical that a provider {@link Serializer#write(Object, WriteContext)} data in the same order that it will {@link Serializer#read(ReadContext)}.
 */
public final class ReadContext {
	private final ByteBuffer buffer;

	private final List<Object> objects = new ArrayList<>();

	/**
	 * Constructs a new read context reading from {@code buffer}.
	 */
	public ReadContext(ByteBuffer buffer) {
		this.buffer = buffer;
		// add padding for the 0 (initial reference) ID
		objects.add(null);
	}

	/**
	 * Returns the byte at the current position in the backing buffer and increments the position.
	 */
	public byte getByte() {
		return buffer.get();
	}
	/**
	 * Returns the int at the current position in the backing buffer and increments the position.
	 */
	public int getInt() {
		return buffer.getInt();
	}
	/**
	 * Returns the long at the current position in the backing buffer and increments the position.
	 */
	public long getLong() {
		return buffer.getLong();
	}
	/**
	 * Returns the double at the current position in the backing buffer and increments the position.
	 */
	public double getDouble() {
		return buffer.getDouble();
	}
	/**
	 * Returns the boolean at the current position in the backing buffer and increments the position.
	 */
	public boolean getBoolean() {
		return buffer.get() == 1;
	}
	/**
	 * Returns the string at the current position in the backing buffer and increments the position.
	 */
	public String getString() {
		var bytes = new byte[buffer.getInt()];
		buffer.get(bytes);
		return new String(bytes, StandardCharsets.UTF_8);
	}

	/**
	 * Reads the object of type {@code c} at the current position in the backing buffer using the best matching serializer and increments the position.
	 * If the current position is a reference ID to a previously-seen object, returns that same object.
	 */
	public <T> T getObject(Class<T> c) {
		var id = getInt();
		if (id != 0) {
			// -1 sentinel for null value
			return id == -1 ? null : (T) objects.get(id);
		} else {
			var object = SerializerLoader.get(c).read(this);
			objects.add(object);
			return object;
		}
	}
}
