package dev.kkorolyov.pancake.platform.io;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A read-only view of a {@link ByteBuffer} that hydrates object references on the fly.
 * On object read, deserializes the data using the nearest {@link Class}-matched {@link Serializer} provider on the classpath, and stores an incrementing ID reference to the deserialized instance.
 * Subsequent reads of this ID from the backing buffer will return that same instance.
 * To maintain this order-reliant encounter guarantee, it is critical that a provider {@link Serializer#write(Object, WriteContext)} data in the same order that it will {@link Serializer#read(ReadContext)}.
 */
public final class ReadContext {
	private final ByteBuffer buffer;
	private final Consumer<? super ByteBuffer> next;

	private final List<Object> objects = new ArrayList<>();

	/**
	 * Constructs a new read context using {@code buffer} to read from {@code channel}.
	 */
	public ReadContext(ByteBuffer buffer, ReadableByteChannel channel) {
		this(
				buffer.rewind().limit(0),
				t -> {
					t.compact();
					try {
						if (channel.read(t) < 0) {
							throw new IllegalStateException("channel has reached end of stream");
						}
					} catch (IOException e) {
						throw new UncheckedIOException(e);
					}
					t.flip();
				}
		);
	}

	/**
	 * Constructs a new read context reading from {@code buffer}.
	 * Does not gracefully handle exhausted buffers.
	 */
	public ReadContext(ByteBuffer buffer) {
		this(buffer, t -> {});
	}
	/**
	 * Constructs a new read context reading from {@code buffer}.
	 * The {@code next} callback is invoked with the provided buffer once it is exhausted but a read operation requires more data, and is expected to update the buffer with new data.
	 */
	public ReadContext(ByteBuffer buffer, Consumer<? super ByteBuffer> next) {
		this.buffer = buffer;
		this.next = next;
		// add padding for the 0 (initial reference) ID
		objects.add(null);
	}

	/**
	 * Invokes `dstGenerator` with the bytes length at the current position, fills the result with the bytes, returns it, and increments the position.
	 */
	public ByteBuffer get(Function<? super Integer, ? extends ByteBuffer> dstGenerator) {
		var length = getInt();
		ensureRemaining(length);

		var dst = dstGenerator.apply(length);
		dst.put(0, buffer, buffer.position(), length);

		buffer.position(buffer.position() + length);

		return dst;
	}
	/**
	 * Returns the byte at the current position in the backing buffer and increments the position.
	 */
	public byte getByte() {
		ensureRemaining(1);
		return buffer.get();
	}
	/**
	 * Returns the int at the current position in the backing buffer and increments the position.
	 */
	public int getInt() {
		ensureRemaining(4);
		return buffer.getInt();
	}
	/**
	 * Returns the long at the current position in the backing buffer and increments the position.
	 */
	public long getLong() {
		ensureRemaining(8);
		return buffer.getLong();
	}
	/**
	 * Returns the double at the current position in the backing buffer and increments the position.
	 */
	public double getDouble() {
		ensureRemaining(8);
		return buffer.getDouble();
	}
	/**
	 * Returns the boolean at the current position in the backing buffer and increments the position.
	 */
	public boolean getBoolean() {
		return getByte() == 1;
	}
	/**
	 * Returns the string at the current position in the backing buffer and increments the position.
	 */
	public String getString() {
		var size = getInt();
		if (size < 0) {
			return null;
		} else {
			var bytes = new byte[size];
			ensureRemaining(bytes.length);
			buffer.get(bytes);
			return new String(bytes, StandardCharsets.UTF_8);
		}
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

	private void ensureRemaining(int count) {
		if (buffer.remaining() < count) {
			next.accept(buffer);
		}
	}
}
