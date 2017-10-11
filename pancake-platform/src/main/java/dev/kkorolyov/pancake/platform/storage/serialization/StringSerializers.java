package dev.kkorolyov.pancake.platform.storage.serialization;

import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

/**
 * Deserializes strings to the most appropriate instance.
 */
public final class StringSerializers {
	private static final Iterable<StringSerializer> serializers = ServiceLoader.load(StringSerializer.class);

	/**
	 * @param s string to deserialize
	 * @return deserialized instance
	 * @throws UnsupportedOperationException if no serializer accepting {@code s}
	 */
	public static Object read(String s) {
		return getStrategy(s).read(s);
	}
	private static StringSerializer<?> getStrategy(String s) {
		return StreamSupport.stream(serializers.spliterator(), false)
				.filter(serializer -> serializer.accepts(s))
				.findFirst()
				.orElseThrow(() -> new UnsupportedOperationException("No serializer accepts: " + s));
	}

	private StringSerializers() {}
}
