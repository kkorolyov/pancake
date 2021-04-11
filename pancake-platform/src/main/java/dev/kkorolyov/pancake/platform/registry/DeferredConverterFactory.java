package dev.kkorolyov.pancake.platform.registry;

import dev.kkorolyov.flopple.function.convert.Converter;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.media.audio.Audio;
import dev.kkorolyov.pancake.platform.media.graphic.Renderable;

import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Generates converters converting primitive objects to deferreds.
 * Primitive objects include:
 * <pre>
 * String
 * Number
 * Iterable
 * Map
 * </pre>
 * Clients can obtain a module-path-loaded instance of a converter factory using {@link #get(Class)} with the relevant provider type.
 * @param <T> resource type
 */
@FunctionalInterface
public interface DeferredConverterFactory<T> {
	/**
	 * @return converter converting primitive objects to {@code T} deferreds
	 * @see Converter#selective(Predicate, Converter)
	 */
	Converter<Object, Optional<Deferred<String, T>>> get();

	/**
	 * Generates {@link Vector3} deferred converters.
	 */
	interface VectorStrat extends DeferredConverterFactory<Vector3> {}

	/**
	 * Generates {@link dev.kkorolyov.pancake.platform.action.Action} deferred converters.
	 */
	interface ActionStrat extends DeferredConverterFactory<Action> {}

	/**
	 * Generates {@link Audio} deferred converters.
	 */
	interface AudioStrat extends DeferredConverterFactory<Audio> {}

	/**
	 * Generates {@link Renderable} deferred converters.
	 */
	interface RenderableStrat extends DeferredConverterFactory<Renderable> {}

	/**
	 * @param c deferred converter factory type to reduce
	 * @param <T> deferred converter factory type
	 * @return reduced converter from converters supplied by all providers of type {@code c} on the module path
	 */
	static <T> Converter<Object, Optional<Deferred<String, T>>> get(Class<? extends DeferredConverterFactory<T>> c) {
		return Converter.reducing(
				ServiceLoader.load(c).stream()
						.map(ServiceLoader.Provider::get)
						.map(DeferredConverterFactory::get)
						.collect(Collectors.toSet())
		);
	}
}
