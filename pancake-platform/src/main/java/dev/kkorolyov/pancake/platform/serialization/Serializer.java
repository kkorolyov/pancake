package dev.kkorolyov.pancake.platform.serialization;

/**
 * Serializes an instance to some other representation.
 * @param <I> raw instance type
 * @param <O> serialized instance type
 */
public interface Serializer<I, O> {
	/**
	 * Deserializes an instance representation.
	 * @param out serialized representation
	 * @return deserialized instance
	 */
	I read(O out);
	/**
	 * Serializes an instance.
	 * @param in instance to serialize
	 * @return serialized instance representation
	 */
	O write(I in);

	/**
	 * @param out serialized representation
	 * @return {@code true} if this serializer can deserialize {@code out}
	 */
	boolean accepts(O out);
}
