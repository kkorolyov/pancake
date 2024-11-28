package dev.kkorolyov.pancake.platform.io;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A write-only view of a {@link ByteBuffer} that serializes object references on the fly.
 * On object write, serializes the data using the nearest {@link Class}-matched {@link Serializer} provider on the classpath, and stores an incrementing ID reference to the serialized instance.
 * Subsequent writes of this instance will instead write that single ID to the backing buffer.
 * To maintain this order-reliant encounter guarantee, it is critical that a provider {@link Serializer#write(Object, WriteContext)} data in the same order that it will {@link Serializer#read(ReadContext)}.
 */
public final class WriteContext implements AutoCloseable {
	private final ByteBuffer buffer;
	private final Consumer<? super ByteBuffer> flush;

	private final Map<Object, Integer> objects = new IdentityHashMap<>();

	/**
	 * Constructs a new write context using {@code buffer} to write to {@code channel}.
	 */
	public WriteContext(ByteBuffer buffer, WritableByteChannel channel) {
		this(
				buffer.rewind().limit(buffer.capacity()),
				t -> {
					t.flip();
					try {
						channel.write(t);
					} catch (IOException e) {
						throw new UncheckedIOException(e);
					}
					t.compact();
				}
		);
	}

	/**
	 * Constructs a new write context writing to {@code buffer}.
	 * Does not gracefully handle exhausted buffers.
	 */
	public WriteContext(ByteBuffer buffer) {
		this(buffer, t -> {});
	}
	/**
	 * Constructs a new write context writing to {@code buffer}.
	 * The {@code flush} callback is invoked with the provided buffer once it is exhausted but a write operation requires more space, and is expected to flush the buffer to make space for new data.
	 */
	public WriteContext(ByteBuffer buffer, Consumer<? super ByteBuffer> flush) {
		this.buffer = buffer;
		this.flush = flush;
		// add padding for the 0 (initial reference) ID
		objects.put(null, 0);
	}

	/**
	 * Writes {@code value} to the current position in the backing buffer and increments the position.
	 */
	public void putByte(byte value) {
		ensureRemaining(1);
		buffer.put(value);
	}
	/**
	 * Writes {@code value} to the current position in the backing buffer and increments the position.
	 */
	public void putInt(int value) {
		ensureRemaining(4);
		buffer.putInt(value);
	}
	/**
	 * Writes {@code value} to the current position in the backing buffer and increments the position.
	 */
	public void putLong(long value) {
		ensureRemaining(8);
		buffer.putLong(value);
	}
	/**
	 * Writes {@code value} to the current position in the backing buffer and increments the position.
	 */
	public void putDouble(double value) {
		ensureRemaining(8);
		buffer.putDouble(value);
	}
	/**
	 * Writes {@code value} to the current position in the backing buffer and increments the position.
	 */
	public void putBoolean(boolean value) {
		putByte((byte) (value ? 1 : 0));
	}
	/**
	 * Writes {@code value} to the current position in the backing buffer and increments the position.
	 */
	public void putString(String value) {
		var bytes = value.getBytes(StandardCharsets.UTF_8);
		putInt(bytes.length);
		ensureRemaining(bytes.length);
		buffer.put(bytes);
	}

	/**
	 * Writes {@code value} using the best matching serializer to the current position in the backing buffer and increments the position.
	 * If this has previously been invoked with {@code value}, writes a reference ID, instead.
	 */
	public <T> void putObject(T value) {
		if (value == null) {
			// -1 sentinel for null value
			putInt(-1);
		} else {
			var id = objects.get(value);
			if (id != null) {
				putInt(id);
			} else {
				putInt(0);
				SerializerLoader.get((Class<T>) value.getClass()).write(value, this);
				objects.put(value, objects.size());
			}
		}
	}

	private void ensureRemaining(int count) {
		if (buffer.remaining() < count) {
			flush.accept(buffer);
		}
	}

	/**
	 * Invokes this context's {@code flush} callback with its buffer.
	 */
	@Override
	public void close() {
		flush.accept(buffer);
	}
}
