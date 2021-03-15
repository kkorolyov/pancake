package dev.kkorolyov.pancake.platform.registry.internal

import dev.kkorolyov.flopple.function.convert.Converter
import dev.kkorolyov.pancake.platform.media.graphic.CompositeRenderable
import dev.kkorolyov.pancake.platform.media.graphic.Image
import dev.kkorolyov.pancake.platform.media.graphic.RenderMedium
import dev.kkorolyov.pancake.platform.media.graphic.Renderable
import dev.kkorolyov.pancake.platform.registry.Deferred
import dev.kkorolyov.pancake.platform.registry.Registry

import spock.lang.Specification

class RenderableStratDeferredConverterFactorySpec extends Specification {
	RenderMedium renderMedium = Mock()

	Renderable ref = Mock()
	Registry<String, Renderable> registry = new Registry<>().with {
		put("ref", ref)
		it
	}
	RenderableStratDeferredConverterFactory factory = new RenderableStratDeferredConverterFactory(renderMedium, { factory.get() })
	Converter<Object, Optional<Deferred<String, Renderable>>> converter = factory.get()

	def "reads reference"() {
		expect:
		converter.convert("ref").orElse(null).resolve(registry.&get) == ref
	}

	def "reads image"() {
		Image img = Mock()

		1 * renderMedium.getImage("img") >> img

		expect:
		converter.convert([uri: "img"]).orElse(null).resolve() == img
	}

	def "reads composite"() {
		expect:
		converter.convert(["ref", "ref"] as Object).orElse(null).resolve(registry.&get) == new CompositeRenderable([ref, ref])
	}
}
