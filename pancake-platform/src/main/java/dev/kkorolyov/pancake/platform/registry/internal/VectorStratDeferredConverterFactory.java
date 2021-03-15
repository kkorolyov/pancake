package dev.kkorolyov.pancake.platform.registry.internal;

import dev.kkorolyov.flopple.function.convert.Converter;
import dev.kkorolyov.pancake.platform.math.Vector;
import dev.kkorolyov.pancake.platform.registry.Deferred;
import dev.kkorolyov.pancake.platform.registry.DeferredConverterFactory;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * {@link DeferredConverterFactory.VectorStrat} provided by the pancake platform.
 */
public final class VectorStratDeferredConverterFactory implements DeferredConverterFactory.VectorStrat {
	@Override
	public Converter<Object, Optional<Deferred<String, Vector>>> get() {
		return Converter.selective(
				in -> in instanceof Iterable,
				in -> Deferred.direct(toVector(
						StreamSupport.stream(((Iterable<?>) in).spliterator(), false)
								.map(String::valueOf)
								.map(BigDecimal::new)
								.mapToDouble(BigDecimal::doubleValue)
								.toArray()
				))
		);
	}

	private static Vector toVector(double[] components) {
		return new Vector(
				components.length > 0 ? components[0] : 0,
				components.length > 1 ? components[1] : 0,
				components.length > 2 ? components[2] : 0
		);
	}
}
