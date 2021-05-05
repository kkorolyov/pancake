package dev.kkorolyov.pancake.platform.registry.internal;

import dev.kkorolyov.flopple.function.convert.Converter;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.math.Vectors;
import dev.kkorolyov.pancake.platform.plugin.DeferredConverterFactory;
import dev.kkorolyov.pancake.platform.registry.Deferred;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * {@link DeferredConverterFactory.VectorStrat} provided by the pancake platform.
 */
public final class VectorStratDeferredConverterFactory implements DeferredConverterFactory.VectorStrat {
	@Override
	public Converter<Object, Optional<Deferred<String, Vector3>>> get() {
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

	private static Vector3 toVector(double[] components) {
		return Vectors.create(
				components.length > 0 ? components[0] : 0,
				components.length > 1 ? components[1] : 0,
				components.length > 2 ? components[2] : 0
		);
	}
}
