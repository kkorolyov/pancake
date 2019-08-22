package dev.kkorolyov.pancake.platform.serialization;

/**
 * Serializes an instance to some other representation.
 * @param <I> raw instance type
 * @param <O> serialized instance type
 */
public interface Serializer<I, O> {
	/** @return serializer converting between same-type input and output without any additional processing */
	static <T> Serializer<T, T> identity() {
		return new Serializer<>() {
			@Override
			public T read(T out) {
				return out;
			}
			@Override
			public T write(T in) {
				return in;
			}

			@Override
			public boolean accepts(T out) {
				return true;
			}
		};
	}

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
