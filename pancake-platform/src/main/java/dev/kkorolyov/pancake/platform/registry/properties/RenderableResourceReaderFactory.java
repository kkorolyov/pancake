package dev.kkorolyov.pancake.platform.registry.properties;

import dev.kkorolyov.pancake.platform.media.graphic.Renderable;
import dev.kkorolyov.pancake.platform.registry.Registry;
import dev.kkorolyov.simplefuncs.convert.Converter;

import java.util.Optional;

/**
 * {@link dev.kkorolyov.pancake.platform.registry.properties.ResourceReaderFactory.RenderableResource} provided by the pancake platform.
 */
public final class RenderableResourceReaderFactory implements ResourceReaderFactory.RenderableResource {
	// TODO

	@Override
	public Converter<String, ? extends Optional<? extends Renderable>> get(Registry<? super String, ? extends Renderable> registry) {
		return Converter.reducing(
				in -> Optional.empty()
		);
	}
}
