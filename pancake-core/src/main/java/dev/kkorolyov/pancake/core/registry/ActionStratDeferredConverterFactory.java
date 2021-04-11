package dev.kkorolyov.pancake.core.registry;

import dev.kkorolyov.flopple.function.convert.Converter;
import dev.kkorolyov.pancake.core.action.ForceAction;
import dev.kkorolyov.pancake.core.action.TransformAction;
import dev.kkorolyov.pancake.core.action.VelocityAction;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.registry.Deferred;
import dev.kkorolyov.pancake.platform.registry.DeferredConverterFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import static dev.kkorolyov.flopple.function.Memoizer.memoize;

/**
 * {@link DeferredConverterFactory.ActionStrat} for core actions.
 */
public class ActionStratDeferredConverterFactory implements DeferredConverterFactory.ActionStrat {
	private static final Collection<String> FORCE_KEYS = Set.of("force");
	private static final Collection<String> VELOCITY_KEYS = Set.of("velocity");
	private static final Collection<String> TRANSFORM_KEYS = Set.of("position", "rotation");

	private final Supplier<Converter<Object, Optional<Deferred<String, Vector3>>>> vectorReader = memoize(() -> DeferredConverterFactory.get(VectorStrat.class));

	private Converter<Object, Optional<Deferred<String, Action>>> force() {
		return Converter.selective(
				in -> in instanceof Map && FORCE_KEYS.containsAll(((Map<?, ?>) in).keySet()),
				in -> Deferred.direct(new ForceAction(toVector(((Map<?, ?>) in).get("force"))))
		);
	}

	private Converter<Object, Optional<Deferred<String, Action>>> velocity() {
		return Converter.selective(
				in -> in instanceof Map && VELOCITY_KEYS.containsAll(((Map<?, ?>) in).keySet()),
				in -> Deferred.direct(new VelocityAction(toVector(((Map<?, ?>) in).get("velocity"))))
		);
	}

	private Converter<Object, Optional<Deferred<String, Action>>> transform() {
		return Converter.selective(
				in -> in instanceof Map && TRANSFORM_KEYS.containsAll(((Map<?, ?>) in).keySet()),
				in -> Deferred.direct(new TransformAction(toVector(((Map<?, ?>) in).get("position")), toVector(((Map<?, ?>) in).get("rotation"))))
		);
	}

	private Vector3 toVector(Object in) {
		return in == null ? null : vectorReader.get().convert(in)
				.map(Deferred::resolve)
				.orElseThrow(() -> new IllegalArgumentException("No resource reader matches: " + in));
	}

	@Override
	public Converter<Object, Optional<Deferred<String, Action>>> get() {
		return Converter.reducing(
				force(),
				velocity(),
				transform()
		);
	}
}
