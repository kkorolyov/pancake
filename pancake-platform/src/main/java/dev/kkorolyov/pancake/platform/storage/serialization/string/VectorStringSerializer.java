package dev.kkorolyov.pancake.platform.storage.serialization.string;

import dev.kkorolyov.pancake.platform.math.Vector;
import dev.kkorolyov.pancake.platform.storage.serialization.StringSerializer;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Serializes {@link Vector}.
 */
public class VectorStringSerializer extends StringSerializer<Vector> {
	private static final NumberStringSerializer numberSerializer = new NumberStringSerializer();

	/**
	 * Constructs a vector serializer.
	 */
	public VectorStringSerializer() {
		super(String.format("\\(%s(,\\s?%s)+\\)",
				numberSerializer.pattern(), numberSerializer.pattern()));
	}

	@Override
	public Vector read(String out) {
		List<Float> components = numberSerializer.matches(out)
				.map(BigDecimal::floatValue)
				.collect(Collectors.toList());

		return new Vector(
				components.get(0),
				components.get(1),
				components.size() > 2 ? components.get(2) : 0);
	}
	@Override
	public String write(Vector in) {
		return Stream.of(in.getX(), in.getY(), in.getZ())
				.map(String::valueOf)
				.collect(Collectors.joining(",", "(", ")"));
	}
}
