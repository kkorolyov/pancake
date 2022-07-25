package dev.kkorolyov.pancake.platform.registry.internal;

import dev.kkorolyov.flub.function.convert.Converter;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.CollectiveAction;
import dev.kkorolyov.pancake.platform.registry.Resource;
import dev.kkorolyov.pancake.platform.registry.ResourceConverterFactory;
import dev.kkorolyov.pancake.platform.registry.ResourceConverters;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

/**
 * {@link ResourceConverterFactory} for platform actions.
 */
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
				collective()
		);
	}
	private static Converter<Object, Optional<Resource<Action>>> reference() {
		return Converter.selective(
				t -> t instanceof String,
				t -> registry -> registry.get(t.toString())
		);
	}
	private Converter<Object, Optional<Resource<Action>>> collective() {
		return Converter.selective(
				t -> t instanceof Iterable<?>,
				t -> registry -> new CollectiveAction(
						StreamSupport.stream(((Iterable<?>) t).spliterator(), false)
								.map(sub -> autoConverter.get().convert(sub))
								.map(sub -> sub.get(registry))
								::iterator
				)
		);
	}

	@Override
	public Class<Action> getType() {
		return Action.class;
	}
}
