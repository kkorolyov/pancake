package dev.kkorolyov.pancake.platform.registry.internal;

import dev.kkorolyov.flopple.function.convert.Converter;
import dev.kkorolyov.pancake.platform.Resources;
import dev.kkorolyov.pancake.platform.media.graphic.CompositeRenderable;
import dev.kkorolyov.pancake.platform.media.graphic.RenderMedium;
import dev.kkorolyov.pancake.platform.media.graphic.Renderable;
import dev.kkorolyov.pancake.platform.registry.Deferred;
import dev.kkorolyov.pancake.platform.registry.DeferredConverterFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

import static dev.kkorolyov.flopple.function.Memoizer.memoize;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toList;

/**
 * {@link DeferredConverterFactory.RenderableStrat} provided by the pancake platform.
 */
public final class RenderableStratDeferredConverterFactory implements DeferredConverterFactory.RenderableStrat {
	private static final Collection<String> IMAGE_KEYS = Set.of("uri");

	private final RenderMedium renderMedium;
	private final Supplier<Converter<Object, Optional<Deferred<String, Renderable>>>> autoReader;

	public RenderableStratDeferredConverterFactory() {
		this(Resources.RENDER_MEDIUM, memoize(() -> DeferredConverterFactory.get(RenderableStrat.class)));
	}
	RenderableStratDeferredConverterFactory(RenderMedium renderMedium, Supplier<Converter<Object, Optional<Deferred<String, Renderable>>>> autoReader) {
		this.renderMedium = renderMedium;
		this.autoReader = autoReader;
	}

	private static Converter<Object, Optional<Deferred<String, Renderable>>> reference() {
		return Converter.selective(
				in -> in instanceof String,
				in -> Deferred.derived(singleton(in.toString()), resolver -> resolver.apply(in.toString()))
		);
	}
	private Converter<Object, Optional<Deferred<String, Renderable>>> image() {
		return Converter.selective(
				in -> in instanceof Map && ((Map) in).keySet().stream().allMatch(key -> IMAGE_KEYS.contains(key.toString())),
				in -> Deferred.direct(renderMedium.getImage(((Map<?, ?>) in).get("uri").toString()))
		);
	}
	private Converter<Object, Optional<Deferred<String, Renderable>>> composite() {
		return Converter.selective(
				in -> in instanceof Iterable,
				in -> {
					Collection<Deferred<String, Renderable>> subResources = StreamSupport.stream(((Iterable<?>) in).spliterator(), false)
							.map(autoReader.get()::convert)
							.map(opt -> opt.orElseThrow(() -> new IllegalArgumentException("No resource reader matches: " + opt)))
							.collect(toList());

					return Deferred.derived(
							subResources.stream()
									.map(Deferred::getDependencies)
									.flatMap(Collection::stream)
									::iterator,
							resolver -> new CompositeRenderable<>(
									subResources.stream()
											.map(subResource -> subResource.resolve(resolver))
											::iterator
							)
					);
				}
		);
	}

	@Override
	public Converter<Object, Optional<Deferred<String, Renderable>>> get() {
		return Converter.reducing(
				reference(),
				image(),
				composite()
		);
	}
}
