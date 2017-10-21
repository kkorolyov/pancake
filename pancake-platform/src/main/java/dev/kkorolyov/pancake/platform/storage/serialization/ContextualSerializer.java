package dev.kkorolyov.pancake.platform.storage.serialization;

/**
 * A {@link Serializer} which may use some context to assist in serialization.
 * @param <C> context type
 * @see Serializer
 */
public interface ContextualSerializer<I, O, C> extends Serializer<I, O> {
	/**
	 * Deserializes an instance representation with the help of a context.
	 * @param out serialized representation
	 * @param context deserialization context
	 * @return deserialized instance
	 */
	I read(O out, C context);
	/**
	 * Serializes an instance with the help of a context.
	 * @param in instance to serialize
	 * @param context serialization context
	 * @return serialized instance representation
	 */
	O write(I in, C context);
}
