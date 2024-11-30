package dev.kkorolyov.pancake.platform.registry.internal;

import dev.kkorolyov.flub.function.convert.Converter;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.io.Serializer;
import dev.kkorolyov.pancake.platform.registry.Resource;
import dev.kkorolyov.pancake.platform.registry.ResourceConverterFactory;
import dev.kkorolyov.pancake.platform.registry.ResourceConverters;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

/**
 * {@link ResourceConverterFactory} for basic actions.
 * @deprecated to be replaced with a {@link Serializer}
 */
@Deprecated
public final class ActionResourceConverterFactory implements ResourceConverterFactory<Action> {
	private final Supplier<? extends Converter<Object, Resource<Action>>> autoConverter;

	/**
	 * Public constructor for {@code ServiceLoader}.
	 */
	public ActionResourceConverterFactory() {
		this(() -> ResourceConverters.get(Action.class));
	}
	ActionResourceConverterFactory(Supplier<? extends Converter<Object, Resource<Action>>> autoConverter) {
		this.autoConverter = autoConverter;
	}

	@Override
	public Converter<Object, Optional<Resource<Action>>> get() {
		return Converter.reducing(
				reference(),
				many()
		);
	}
	private static Converter<Object, Optional<Resource<Action>>> reference() {
		return Converter.selective(
				t -> t instanceof String,
				t -> registry -> registry.get(t.toString())
		);
	}
	private Converter<Object, Optional<Resource<Action>>> many() {
		return Converter.selective(
				t -> t instanceof Iterable<?>,
				t -> registry -> new ManyAction(
						StreamSupport.stream(((Iterable<?>) t).spliterator(), false)
								.map(sub -> autoConverter.get().convert(sub))
								.map(sub -> sub.get(registry))
								.toList()
				)
		);
	}

	@Override
	public Class<Action> getType() {
		return Action.class;
	}

	private record ManyAction(List<Action> delegates) implements Action {
		@Override
		public void apply(Entity entity) {
			for (Action delegate : delegates) delegate.apply(entity);
		}
	}
}
