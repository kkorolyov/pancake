package dev.kkorolyov.pancake.platform.registry.internal

import dev.kkorolyov.flopple.function.convert.Converter
import dev.kkorolyov.pancake.platform.media.audio.Audio
import dev.kkorolyov.pancake.platform.registry.Deferred
import dev.kkorolyov.pancake.platform.service.AudioFactory

import spock.lang.Shared
import spock.lang.Specification

class AudioStratDeferredConverterFactorySpec extends Specification {
	@Shared
	String[] uris = ["file://local/path", "https://remote/path", "rando"]

	AudioFactory audioFactory = Mock()

	AudioStratDeferredConverterFactory factory = new AudioStratDeferredConverterFactory(audioFactory)
	Converter<Object, Optional<Deferred<String, Audio>>> converter = factory.get()

	def "reads path"() {
		1 * audioFactory.get(uri) >> audio

		expect:
		converter.convert(uri).orElse(null).resolve() == audio

		where:
		uri << uris
		audio << uris.collect { Mock(Audio) }
	}
}
