package dev.kkorolyov.pancake.core.registry;

import dev.kkorolyov.flub.function.convert.Converter;
import dev.kkorolyov.pancake.core.component.Force;
import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.core.component.Velocity;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.io.Structizers;
import dev.kkorolyov.pancake.platform.math.Matrix4;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.registry.Resource;
import dev.kkorolyov.pancake.platform.registry.ResourceConverterFactory;

import java.util.Map;
import java.util.Optional;

/**
 * {@link ResourceConverterFactory} for core actions.
 */
public final class ActionResourceConverterFactory implements ResourceConverterFactory<Action> {
	@Override
	public Converter<Object, Optional<Resource<Action>>> get() {
		return Converter.reducing(
				force(),
				velocity(),
				transform()
		);
	}
	private Converter<Object, Optional<Resource<Action>>> force() {
		return Converter.selective(
				t -> t instanceof Map && ((Map<?, ?>) t).containsKey("force"),
				t -> {
					Vector3 force = fromStruct(Vector3.class, ((Map<?, ?>) t).get("force"));
					return Resource.constant(entity -> entity.get(Force.class).getValue().add(force));
				}
		);
	}
	private Converter<Object, Optional<Resource<Action>>> velocity() {
		return Converter.selective(
				t -> t instanceof Map && ((Map<?, ?>) t).containsKey("velocity"),
				t -> {
					Vector3 velocity = fromStruct(Vector3.class, ((Map<?, ?>) t).get("velocity"));
					return Resource.constant(entity -> entity.get(Velocity.class).getLinear().set(velocity));
				}
		);
	}
	private Converter<Object, Optional<Resource<Action>>> transform() {
		return Converter.selective(
				t -> t instanceof Map && ((Map<?, ?>) t).containsKey("transform"),
				t -> {
					Map<?, ?> transformMap = (Map<?, ?>) ((Map<?, ?>) t).get("transform");
					Vector3 translation = fromStruct(Vector3.class, transformMap.get("translation"));
					Matrix4 rotation = fromStruct(Matrix4.class, transformMap.get("rotation"));
					Vector3 scale = fromStruct(Vector3.class, transformMap.get("scale"));

					return Resource.constant(entity -> {
						Transform transform = entity.get(Transform.class);
						if (translation != null) transform.getTranslation().set(translation);
						if (rotation != null) transform.getRotation().set(rotation);
						if (scale != null) transform.getScale().set(scale);
					});
				}
		);
	}

	private <T> T fromStruct(Class<T> c, Object o) {
		return o == null ? null : Structizers.fromStruct(c, o);
	}

	@Override
	public Class<Action> getType() {
		return Action.class;
	}
}
