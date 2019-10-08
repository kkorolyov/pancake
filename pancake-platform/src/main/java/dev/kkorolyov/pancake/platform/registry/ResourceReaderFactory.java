package dev.kkorolyov.pancake.platform.registry;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.media.audio.Audio;
import dev.kkorolyov.pancake.platform.media.graphic.Renderable;
import dev.kkorolyov.simplefiles.Providers;
import dev.kkorolyov.simplefuncs.convert.Converter;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Generates resource readers attached to a {@link Registry} reading resources from strings.
 * @param <T> resource type
 */
public interface ResourceReaderFactory<T> {
	/**
	 * @param registry registry to attach generated reader to
	 * @return resource reader attached to {@code registry} reading {@code registry}-type resources from strings
	 */
	Converter<String, Optional<? extends T>> get(Registry<? super String, ? extends T> registry);

	/**
	 * Generates {@link Action} resource readers.
	 */
	interface ActionResource extends ResourceReaderFactory<Action> {}

	/**
	 * Generates {@link Audio} resource readers.
	 */
	interface AudioResource extends ResourceReaderFactory<Audio> {}

	/**
	 * Generates {@link Renderable} resource readers.
	 */
	interface RenderableResource extends ResourceReaderFactory<Renderable> {}

	/**
	 * @param c resource reader factory type to reduce
	 * @param registry registry to apply
	 * @param <T> resource reader factory type
	 * @return reduced converter from converters supplied by all providers of type {@code c} on the module path
	 */
	static <T> Converter<String, Optional<? extends T>> reduce(Class<? extends ResourceReaderFactory<T>> c, Registry<? super String, ? extends T> registry) {
		return Converter.reducing(
				Providers.fromDescriptor(c).stream()
						.map(factory -> factory.get(registry))
						.collect(Collectors.toSet())
		);
	}
}
