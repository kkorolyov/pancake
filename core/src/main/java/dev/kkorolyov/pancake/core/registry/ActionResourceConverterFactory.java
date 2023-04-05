package dev.kkorolyov.pancake.core.registry;

import dev.kkorolyov.flub.function.convert.Converter;
import dev.kkorolyov.pancake.core.component.Orientation;
import dev.kkorolyov.pancake.core.component.Position;
import dev.kkorolyov.pancake.core.component.movement.Force;
import dev.kkorolyov.pancake.core.component.movement.Velocity;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.io.ObjectConverters;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.registry.Resource;
import dev.kkorolyov.pancake.platform.registry.ResourceConverterFactory;

import java.util.Map;
import java.util.Optional;

/**
 * {@link ResourceConverterFactory} for core actions.
 */
public final class ActionResourceConverterFactory implements ResourceConverterFactory<Action> {
	private final Converter<Iterable<Number>, Vector3> vector3Converter = ObjectConverters.vector3();

	@Override
	public Converter<Object, Optional<Resource<Action>>> get() {
		return Converter.reducing(
				force(),
				velocity(),
				position(),
				orientation()
		);
	}
	private Converter<Object, Optional<Resource<Action>>> force() {
		return Converter.selective(
				t -> t instanceof Map && ((Map<?, ?>) t).containsKey("force"),
				t -> {
					Vector3 force = asVector(((Map<?, ?>) t).get("force"));
					return Resource.constant(entity -> entity.get(Force.class).getValue().add(force));
				}
		);
	}
	private Converter<Object, Optional<Resource<Action>>> velocity() {
		return Converter.selective(
				t -> t instanceof Map && ((Map<?, ?>) t).containsKey("velocity"),
				t -> {
					Vector3 velocity = asVector(((Map<?, ?>) t).get("velocity"));
					return Resource.constant(entity -> entity.get(Velocity.class).getValue().set(velocity));
				}
		);
	}
	private Converter<Object, Optional<Resource<Action>>> position() {
		return Converter.selective(
				t -> t instanceof Map && ((Map<?, ?>) t).containsKey("position"),
				t -> {
					Vector3 position = asVector(((Map<?, ?>) t).get("position"));
					return Resource.constant(entity -> entity.get(Position.class).getValue().set(position));
				}
		);
	}
	private Converter<Object, Optional<Resource<Action>>> orientation() {
		return Converter.selective(
				t -> t instanceof Map && ((Map<?, ?>) t).containsKey("orientation"),
				t -> {
					Vector3 orientation = asVector(((Map<?, ?>) t).get("orientation"));
					return Resource.constant(entity -> entity.get(Orientation.class).getValue().set(orientation));
				}
		);
	}

	private Vector3 asVector(Object o) {
		return o == null ? null : vector3Converter.convert((Iterable<Number>) o);
	}

	@Override
	public Class<Action> getType() {
		return Action.class;
	}
}
