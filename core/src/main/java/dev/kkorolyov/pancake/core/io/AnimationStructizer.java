package dev.kkorolyov.pancake.core.io;

import dev.kkorolyov.pancake.core.animation.TransformFrame;
import dev.kkorolyov.pancake.platform.io.Structizer;
import dev.kkorolyov.pancake.platform.io.Structizers;
import dev.kkorolyov.pancake.platform.math.Vector3;

import java.util.Map;
import java.util.Optional;

/**
 * {@link Structizer} for animation primitives.
 * @deprecated prefer {@link TransformFrameSerializer}
 */
public final class AnimationStructizer implements Structizer {
	@Override
	public Optional<Object> toStruct(Object o) {
		return Optional.of(o)
				.map(Structizer.first(
						Structizer.select(
								TransformFrame.class, t -> Map.of(
										"translation", Structizers.toStruct(t.getTranslation()),
										"rotation", Structizers.toStruct(t.getRotation()),
										"scale", Structizers.toStruct(t.getScale())
								)
						)
				));
	}

	@Override
	public <T> Optional<T> fromStruct(Class<T> c, Object o) {
		return Optional.of(o)
				.filter(t -> t instanceof Map<?, ?> && ((Map<?, ?>) t).keySet().stream().allMatch(key -> key instanceof String))
				.map(t -> (Map<String, Object>) t)
				.map(Structizer.first(
						Structizer.select(
								c, TransformFrame.class, t -> {
									var result = new TransformFrame();
									var translation = t.get("translation");
									var rotation = t.get("rotation");
									var scale = t.get("scale");

									if (translation != null) result.getTranslation().set(Structizers.fromStruct(Vector3.class, translation));
									if (rotation != null) result.getRotation().set(Structizers.fromStruct(Vector3.class, rotation));
									if (scale != null) result.getScale().set(Structizers.fromStruct(Vector3.class, scale));

									return result;
								}
						)
				));
	}
}
