package dev.kkorolyov.pancake.platform.registry.internal

import dev.kkorolyov.flopple.function.convert.Converter
import dev.kkorolyov.pancake.platform.media.audio.Audio
import dev.kkorolyov.pancake.platform.media.audio.AudioFactory
import dev.kkorolyov.pancake.platform.registry.Registry

import spock.lang.Shared
import spock.lang.Specification

class AudioStratDeferredConverterFactorySpec extends Specification {
	@Shared
	String[] uris = ["file://local/path", "https://remote/path", "rando"]

	AudioStratDeferredConverterFactory factory = new AudioStratDeferredConverterFactory()

	AudioFactory audioFactory = Mock()

	Registry<String, Audio> registry = new Registry<>()
	Converter<String, Optional<Audio>> converter = factory.get(registry)

	def "reads path"() {
		1 * audioFactory.get(uri) >> audio

		expect:
		converter.convert(uri).orElse(null) == audio

		where:
		uri << uris
		audio << uris.collect { Mock(Audio) }
	}
}
