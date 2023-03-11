package dev.kkorolyov.pancake.core.registry;

import dev.kkorolyov.flub.function.convert.Converter;
import dev.kkorolyov.pancake.core.action.ForceAction;
import dev.kkorolyov.pancake.core.action.PositionAction;
import dev.kkorolyov.pancake.core.action.VelocityAction;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.registry.ObjectConverters;
import dev.kkorolyov.pancake.platform.registry.Resource;
import dev.kkorolyov.pancake.platform.registry.ResourceConverterFactory;

import java.util.Map;
import java.util.Optional;

/**
 * {@link ResourceConverterFactory} for core actions.
 */
public final class ActionResourceConverterFactory implements ResourceConverterFactory<Action> {
	private final Converter<Iterable<?>, Vector3> vector3Converter = ObjectConverters.vector3();

	@Override
	public Converter<Object, Optional<Resource<Action>>> get() {
		return Converter.reducing(
				force(),
				velocity(),
				position()
		);
	}
	private Converter<Object, Optional<Resource<Action>>> force() {
		return Converter.selective(
				t -> t instanceof Map<?, ?> && ((Map<?, ?>) t).containsKey("force"),
				t -> Resource.constant(new ForceAction(asVector(((Map<?, ?>) t).get("force"))))
		);
	}
	private Converter<Object, Optional<Resource<Action>>> velocity() {
		return Converter.selective(
				t -> t instanceof Map<?, ?> && ((Map<?, ?>) t).containsKey("velocity"),
				t -> Resource.constant(new VelocityAction(asVector(((Map<?, ?>) t).get("velocity"))))
		);
	}
	private Converter<Object, Optional<Resource<Action>>> position() {
		return Converter.selective(
				t -> t instanceof Map<?, ?> && ((Map<?, ?>) t).containsKey("position"),
				t -> Resource.constant(new PositionAction(asVector(((Map<?, ?>) t).get("position"))))
		);
	}

	private Vector3 asVector(Object o) {
		return o == null ? null : vector3Converter.convert((Iterable<?>) o);
	}

	@Override
	public Class<Action> getType() {
		return Action.class;
	}
}
