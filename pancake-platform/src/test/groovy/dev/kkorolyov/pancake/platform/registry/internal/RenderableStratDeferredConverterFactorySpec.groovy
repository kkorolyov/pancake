package dev.kkorolyov.pancake.platform.registry.internal

import dev.kkorolyov.flopple.function.convert.Converter
import dev.kkorolyov.pancake.platform.media.graphic.CompositeRenderable
import dev.kkorolyov.pancake.platform.media.graphic.Image
import dev.kkorolyov.pancake.platform.media.graphic.RenderMedium
import dev.kkorolyov.pancake.platform.media.graphic.Renderable
import dev.kkorolyov.pancake.platform.registry.Registry

import spock.lang.Shared
import spock.lang.Specification

class RenderableStratDeferredConverterFactorySpec extends Specification {
	@Shared
	String[] references = ["ref", "ref4", "newRef"]

	@Shared
	RenderableStratDeferredConverterFactory factory = new RenderableStratDeferredConverterFactory()

	RenderMedium renderMedium = Mock()

	Registry<String, Renderable> registry = new Registry<>()
	Converter<String, Optional<Renderable>> converter = factory.get(registry)

	def "reads reference"() {
		registry.put(name, renderable)

		expect:
		converter.convert(name).orElse(null) == renderable

		where:
		name << references
		renderable << references.collect { Mock(Renderable) }
	}

	def "reads image"() {
		1 * renderMedium.getImage(name) >> image

		expect:
		converter.convert("IMG($name)" as String).orElse(null) == image

		where:
		name << references
		image << references.collect { Mock(Image) }
	}

	def "reads composite"() {
		Map<String, Image> nameToRenderable = references.collectEntries { [(it): Mock(Renderable)] }
		nameToRenderable.each(registry.&put)

		expect:
		converter.convert(references as String).orElse(null) == new CompositeRenderable(nameToRenderable.values())
	}
}
