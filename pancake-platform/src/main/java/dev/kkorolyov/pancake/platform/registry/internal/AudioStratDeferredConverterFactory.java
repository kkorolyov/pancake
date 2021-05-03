package dev.kkorolyov.pancake.platform.registry.internal;

import dev.kkorolyov.flopple.function.convert.Converter;
import dev.kkorolyov.pancake.platform.media.audio.Audio;
import dev.kkorolyov.pancake.platform.registry.Deferred;
import dev.kkorolyov.pancake.platform.registry.DeferredConverterFactory;
import dev.kkorolyov.pancake.platform.service.AudioFactory;
import dev.kkorolyov.pancake.platform.service.Services;

import java.util.Optional;

/**
 * {@link DeferredConverterFactory.AudioStrat} provided by the pancake platform.
 */
public final class AudioStratDeferredConverterFactory implements DeferredConverterFactory.AudioStrat {
	private final AudioFactory audioFactory;

	public AudioStratDeferredConverterFactory() {
		this(Services.audioFactory());
	}
	AudioStratDeferredConverterFactory(AudioFactory audioFactory) {
		this.audioFactory = audioFactory;
	}

	@Override
	public Converter<Object, Optional<Deferred<String, Audio>>> get() {
		return Converter.selective(
				in -> in instanceof String,
				in -> Deferred.direct(audioFactory.get(in.toString()))
		);
	}
}
