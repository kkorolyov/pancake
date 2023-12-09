package dev.kkorolyov.pancake.core.io;

import dev.kkorolyov.flub.data.Graph;
import dev.kkorolyov.pancake.core.component.ActionQueue;
import dev.kkorolyov.pancake.core.component.Bounds;
import dev.kkorolyov.pancake.core.component.Damping;
import dev.kkorolyov.pancake.core.component.Force;
import dev.kkorolyov.pancake.core.component.Mass;
import dev.kkorolyov.pancake.core.component.Orientation;
import dev.kkorolyov.pancake.core.component.Path;
import dev.kkorolyov.pancake.core.component.Position;
import dev.kkorolyov.pancake.core.component.Velocity;
import dev.kkorolyov.pancake.core.component.limit.VelocityLimit;
import dev.kkorolyov.pancake.core.component.tag.Collidable;
import dev.kkorolyov.pancake.core.component.tag.Correctable;
import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.io.Structizer;
import dev.kkorolyov.pancake.platform.io.Structizers;
import dev.kkorolyov.pancake.platform.math.Vector3;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * {@link Structizer} for {@code core} {@link Component}s.
 */
public class ComponentStructizer implements Structizer {
	@SuppressWarnings("Convert2MethodRef")
	@Override
	public Optional<Object> toStruct(Object o) {
		return Optional.of(o)
				.map(Structizer.first(
						// TODO
						Structizer.select(ActionQueue.class, t -> List.of()),
						// TODO
						Structizer.select(Bounds.class, t -> List.of()),
						Structizer.select(Path.class, t -> Map.of(
								"strength", t.getStrength(),
								"proximity", t.getProximity(),
								"snapStrategy", t.getSnapStrategy().name(),
								"steps", StreamSupport.stream(t.spliterator(), false).map(Structizers::toStruct).toList()
						)),
						Structizer.select(Collidable.class, t -> t.getPriority()),
						Structizer.select(Correctable.class, t -> t.getPriority()),
						Structizer.select(Mass.class, t -> t.getValue()),
						Structizer.select(VelocityLimit.class, t -> t.getValue()),
						Structizer.select(Damping.class, t -> Structizers.toStruct(t.getValue())),
						Structizer.select(Force.class, t -> Structizers.toStruct(t.getValue())),
						Structizer.select(Orientation.class, t -> Structizers.toStruct(t.getValue())),
						Structizer.select(Position.class, t -> Structizers.toStruct(t.getValue())),
						Structizer.select(Velocity.class, t -> Structizers.toStruct(t.getValue()))
				));
	}

	@Override
	public <T> Optional<T> fromStruct(Class<T> c, Object o) {
		return Stream.of(
						Optional.of(o)
								.map(t -> t instanceof Collection<?> ? (Collection<?>) t : null)
								.map(Structizer.first(
										// TODO
										Structizer.select(c, ActionQueue.class, t -> new ActionQueue()),
										// TODO
										Structizer.select(c, Bounds.class, t -> new Bounds(new Graph<>()))
								)),
						Optional.of(o)
								.map(t -> t instanceof Map<?, ?> && ((Map<?, ?>) t).keySet().stream().allMatch(key -> key instanceof String) ? (Map<String, Object>) t : null)
								.map(Structizer.first(
										Structizer.select(c, Path.class, t -> {
											var result = new Path(((Number) t.get("strength")).doubleValue(), ((Number) t.get("proximity")).doubleValue(), Path.SnapStrategy.valueOf((String) t.get("snapStrategy")));
											var steps = t.get("steps");
											if (steps != null) {
												for (var step : ((Iterable<Iterable<Number>>) steps)) result.add(Structizers.fromStruct(Vector3.class, step));
											}

											return result;
										})
								)),
						Optional.of(o)
								.map(t -> t instanceof Integer ? (Integer) t : null)
								.map(Structizer.first(
										Structizer.select(c, Collidable.class, Collidable::new),
										Structizer.select(c, Correctable.class, Correctable::new)
								)),
						Optional.of(o)
								.map(t -> t instanceof Number ? (Number) t : null)
								.map(Structizer.first(
										Structizer.select(c, Mass.class, t -> new Mass(t.doubleValue())),
										Structizer.select(c, VelocityLimit.class, t -> new VelocityLimit(t.doubleValue()))
								)),
						Optional.of(o)
								.map(Structizer.first(
										Structizer.select(c, Damping.class, t -> new Damping(Structizers.fromStruct(Vector3.class, t))),
										Structizer.select(c, Force.class, t -> new Force(Structizers.fromStruct(Vector3.class, t))),
										Structizer.select(c, Orientation.class, t -> new Orientation(Structizers.fromStruct(Vector3.class, t))),
										Structizer.select(c, Position.class, t -> new Position(Structizers.fromStruct(Vector3.class, t))),
										Structizer.select(c, Velocity.class, t -> new Velocity(Structizers.fromStruct(Vector3.class, t)))
								))
				)
				.filter(Optional::isPresent)
				.findFirst()
				.flatMap(t -> (Optional<T>) t);
	}
}