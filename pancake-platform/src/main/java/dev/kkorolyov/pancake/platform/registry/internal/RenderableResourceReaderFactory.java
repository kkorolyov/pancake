package dev.kkorolyov.pancake.platform.registry.internal;

import dev.kkorolyov.pancake.platform.Resources;
import dev.kkorolyov.pancake.platform.media.graphic.CompositeRenderable;
import dev.kkorolyov.pancake.platform.media.graphic.Image;
import dev.kkorolyov.pancake.platform.media.graphic.Renderable;
import dev.kkorolyov.pancake.platform.registry.Registry;
import dev.kkorolyov.pancake.platform.registry.ResourceReaderFactory;
import dev.kkorolyov.simplefuncs.convert.Converter;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

/**
 * {@link ResourceReaderFactory.RenderableResource} provided by the pancake platform.
 */
public final class RenderableResourceReaderFactory implements ResourceReaderFactory.RenderableResource {
	private static final Pattern IMAGE_PATTERN = Pattern.compile("[\\w.\\-/]+");

	private static final Pattern COMPOSITE_SPLIT_PATTERN = Pattern.compile(",\\s?");
	private static final Pattern COMPOSITE_PATTERN = Pattern.compile("\\[.+(" + COMPOSITE_SPLIT_PATTERN + ".+)*]");

	private static Converter<String, Optional<Image>> image() {
		return Converter.selective(
				in -> IMAGE_PATTERN.matcher(in).matches(),
				Resources.RENDER_MEDIUM::getImage
		);
	}
	private static Converter<String, Optional<CompositeRenderable<Renderable>>> composite(Registry<? super String, ? extends Renderable> registry) {
		Converter<String, ? extends Optional<? extends Renderable>> autoConverter = generateAutoConverter(registry);

		return Converter.selective(
				in -> COMPOSITE_PATTERN.matcher(in).matches(),
				in -> new CompositeRenderable<>(
						Arrays.stream(COMPOSITE_SPLIT_PATTERN.split(in.substring(1, in.length() - 1)))
								.map(autoConverter::convert)
								.flatMap(Optional::stream)
								.collect(toList())
				)
		);
	}

	private static Converter<String, ? extends Optional<? extends Renderable>> generateAutoConverter(Registry<? super String, ? extends Renderable> registry) {
		return ResourceReaderFactory.reduce(RenderableResource.class, registry);
	}

	@Override
	public Converter<String, Optional<? extends Renderable>> get(Registry<? super String, ? extends Renderable> registry) {
		return Converter.reducing(
				image(),
				composite(registry)
		);
	}
}
