package dev.kkorolyov.pancake.platform.entity

import dev.kkorolyov.pancake.platform.registry.Registry
import dev.kkorolyov.pancake.platform.registry.Resource

import spock.lang.Specification

class EntityConverterSpec extends Specification {
	def "reads with referenced component"() {
		String key = "compo"

		Registry<Component> registry = new Registry<>()
		registry.put(key, Resource.constant(new Component() {}))

		EntityConverter converter = new EntityConverter(registry)

		Map<String, Object> data = [
				wutev: key
		]

		expect:
		converter.read(data) == [
				(registry.get(key).class): registry.get(key)
		]
	}

	def "throws on null referenced component"() {
		EntityConverter converter = new EntityConverter(new Registry<Component>())

		Map<String, Object> data = [
				wutev: "nothing"
		]

		when:
		converter.read(data)

		then:
		thrown(IllegalArgumentException)
	}
}
