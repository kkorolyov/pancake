package dev.kkorolyov.pancake.platform.registry.internal;

import dev.kkorolyov.pancake.platform.Resources;
import dev.kkorolyov.pancake.platform.media.audio.Audio;
import dev.kkorolyov.pancake.platform.registry.Registry;
import dev.kkorolyov.pancake.platform.registry.ResourceReaderFactory;
import dev.kkorolyov.simplefuncs.convert.Converter;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * {@link ResourceReaderFactory.AudioResource} provided by the pancake platform.
 */
public final class AudioResourceReaderFactory implements ResourceReaderFactory.AudioResource {
	private static final Pattern AUDIO_PATH_PATTERN = Pattern.compile(".+");

	@Override
	public Converter<String, Optional<? extends Audio>> get(Registry<? super String, ? extends Audio> registry) {
		return (Converter<String, Optional<? extends Audio>>) (Converter) Converter.selective(
				in -> AUDIO_PATH_PATTERN.matcher(in).matches(),
				Resources.AUDIO_FACTORY::get
		);
	}
}
