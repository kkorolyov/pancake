package dev.kkorolyov.pancake.skillet.serialization

import dev.kkorolyov.pancake.platform.specbase.BaseSerializerSpec
import dev.kkorolyov.pancake.skillet.model.GenericComponent
import dev.kkorolyov.pancake.skillet.model.GenericEntity

import static dev.kkorolyov.simplespecs.SpecUtilities.randString

class GenericEntitySerializerSpec extends BaseSerializerSpec {
	String name = randString()
	Iterable<GenericComponent> components = []
	String componentsS = "[]"

	def setup() {
		reps << [(new GenericEntity(name, components)): "$name$componentsS"]

		serializer = new GenericEntitySerializer()
	}

	def "complains if no name"() {
		when:
		serializer.read(componentsS)

		then:
		thrown(IllegalArgumentException)
	}
}
