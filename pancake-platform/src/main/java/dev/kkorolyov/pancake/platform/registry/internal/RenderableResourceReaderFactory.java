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
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dev.kkorolyov.simplefuncs.function.Memoizer.memoize;
import static java.util.stream.Collectors.toList;

/**
 * {@link ResourceReaderFactory.RenderableResource} provided by the pancake platform.
 */
public final class RenderableResourceReaderFactory implements ResourceReaderFactory.RenderableResource {
	private static final Pattern REFERENCE_PATTERN = Pattern.compile("\\w+");

	private static final Pattern IMAGE_PATTERN = Pattern.compile("(?i)IMG(?-i)\\(([\\w.\\-]+)\\)");

	private static final Pattern COMPOSITE_PATTERN = Pattern.compile("\\[.+(,\\s?.+)*]");
	private static final Pattern COMPOSITE_SPLIT_PATTERN = Pattern.compile(",\\s?");

	private final Function<? super Registry<? super String, ? extends Renderable>, ? extends Converter<? super String, ? extends Optional<? extends Renderable>>> autoConverter;

	public RenderableResourceReaderFactory() {
		this(memoize(registry -> ResourceReaderFactory.get(RenderableResource.class, registry)));
	}
	private RenderableResourceReaderFactory(Function<? super Registry<? super String, ? extends Renderable>, ? extends Converter<? super String, ? extends Optional<? extends Renderable>>> autoConverter) {
		this.autoConverter = autoConverter;
	}

	private static Converter<String, Optional<Renderable>> reference(Registry<? super String, ? extends Renderable> registry) {
		return Converter.selective(
				in -> REFERENCE_PATTERN.matcher(in).matches(),
				registry::get
		);
	}
	private static Converter<String, Optional<Image>> image() {
		return Converter.selective(
				in -> IMAGE_PATTERN.matcher(in).matches(),
				in -> {
					Matcher matcher = IMAGE_PATTERN.matcher(in);
					matcher.matches();

					return Resources.RENDER_MEDIUM.getImage(matcher.group(1));
				}
		);
	}
	private Converter<String, Optional<CompositeRenderable<Renderable>>> composite(Registry<? super String, ? extends Renderable> registry) {
		return Converter.selective(
				in -> COMPOSITE_PATTERN.matcher(in).matches(),
				in -> new CompositeRenderable<>(
						Arrays.stream(COMPOSITE_SPLIT_PATTERN.split(in.substring(1, in.length() - 1)))
								.map(autoConverter.apply(registry)::convert)
								.flatMap(Optional::stream)
								.collect(toList())
				)
		);
	}

	@Override
	public Converter<String, Optional<? extends Renderable>> get(Registry<? super String, ? extends Renderable> registry) {
		return Converter.reducing(
				reference(registry),
				image(),
				composite(registry)
		);
	}
}
