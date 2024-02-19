package dev.kkorolyov.pancake.core.io;

import dev.kkorolyov.flub.data.Graph;
import dev.kkorolyov.pancake.core.component.ActionQueue;
import dev.kkorolyov.pancake.core.component.Bounds;
import dev.kkorolyov.pancake.core.component.Damping;
import dev.kkorolyov.pancake.core.component.Force;
import dev.kkorolyov.pancake.core.component.Mass;
import dev.kkorolyov.pancake.core.component.Path;
import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.core.component.Velocity;
import dev.kkorolyov.pancake.core.component.limit.VelocityLimit;
import dev.kkorolyov.pancake.core.component.tag.Collidable;
import dev.kkorolyov.pancake.core.component.tag.Correctable;
import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.io.Structizer;
import dev.kkorolyov.pancake.platform.io.Structizers;
import dev.kkorolyov.pancake.platform.math.Matrix4;
import dev.kkorolyov.pancake.platform.math.Vector3;

import java.util.Arrays;
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
						Structizer.select(VelocityLimit.class, t -> Arrays.asList(t.getLinear(), t.getAngular())),
						Structizer.select(Damping.class, t -> Structizers.toStruct(t.getLinear())),
						Structizer.select(Force.class, t -> Arrays.asList(Structizers.toStruct(t.getValue()), Structizers.toStruct(t.getOffset()))),
						Structizer.select(Transform.class, t -> Map.of(
								"translation", Structizers.toStruct(t.getTranslation()),
								"rotation", Structizers.toStruct(t.getRotation()),
								"scale", Structizers.toStruct(t.getScale())
								// TODO support parent - once component memoization is in
						)),
						Structizer.select(Velocity.class, t -> Arrays.asList(Structizers.toStruct(t.getLinear()), Structizers.toStruct(t.getAngular())))
				));
	}

	@Override
	public <T> Optional<T> fromStruct(Class<T> c, Object o) {
		return Stream.of(
						Optional.of(o)
								.map(t -> t instanceof Collection<?> ? (Collection<?>) t : null)
								.map(Structizer.first(
										Structizer.select(c, Velocity.class, t -> {
											var result = new Velocity();

											var it = t.iterator();
											if (it.hasNext()) result.getLinear().set(Structizers.fromStruct(Vector3.class, it.next()));
											if (it.hasNext()) result.getAngular().set(Structizers.fromStruct(Vector3.class, it.next()));

											return result;
										}),
										Structizer.select(c, Force.class, t -> {
											var result = new Force();

											var it = t.iterator();
											if (it.hasNext()) result.getValue().set(Structizers.fromStruct(Vector3.class, it.next()));
											if (it.hasNext()) result.getOffset().set(Structizers.fromStruct(Vector3.class, it.next()));

											return result;
										}),
										Structizer.select(c, VelocityLimit.class, t -> {
											var it = t.iterator();
											var linear = it.hasNext() ? ((Number) it.next()).doubleValue() : 0.0;
											var angular = it.hasNext() ? ((Number) it.next()).doubleValue() : 0.0;

											return new VelocityLimit(linear, angular);
										}),
										Structizer.select(c, Damping.class, t -> {
											var it = t.iterator();
											var linear = it.hasNext() ? Structizers.fromStruct(Vector3.class, it.next()) : Vector3.of();
											var angular = it.hasNext() ? Structizers.fromStruct(Vector3.class, it.next()) : Vector3.of();

											return new Damping(linear, angular);
										}),
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
										}),
										Structizer.select(c, Transform.class, t -> {
											var result = new Transform();
											var translation = t.get("translation");
											var rotation = t.get("rotation");
											var scale = t.get("scale");

											if (translation != null) result.getTranslation().set(Structizers.fromStruct(Vector3.class, translation));
											if (rotation != null) result.getRotation().multiply(Structizers.fromStruct(Matrix4.class, rotation));
											if (scale != null) result.getScale().set(Structizers.fromStruct(Vector3.class, scale));

											return result;
											// TODO support parent - once component memoization is in
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
										Structizer.select(c, Mass.class, t -> new Mass(t.doubleValue()))
								))
				)
				.filter(Optional::isPresent)
				.findFirst()
				.flatMap(t -> (Optional<T>) t);
	}
}
