package dev.kkorolyov.pancake.platform.plugin;

import dev.kkorolyov.flopple.function.convert.Converter;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.media.audio.Audio;
import dev.kkorolyov.pancake.platform.media.graphic.Renderable;
import dev.kkorolyov.pancake.platform.registry.Deferred;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Generates converters converting primitive objects to deferreds.
 * Primitive objects include:
 * <pre>
 * String
 * Number
 * Iterable
 * Map
 * </pre>
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
}
