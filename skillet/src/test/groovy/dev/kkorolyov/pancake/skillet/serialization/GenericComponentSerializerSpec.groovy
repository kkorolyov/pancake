package dev.kkorolyov.pancake.skillet.serialization

import dev.kkorolyov.pancake.platform.serialization.string.MapStringSerializer
import dev.kkorolyov.pancake.platform.serialization.string.entity.ComponentStringSerializer
import dev.kkorolyov.pancake.platform.specbase.BaseSerializerSpec
import dev.kkorolyov.pancake.skillet.model.GenericComponent

import static dev.kkorolyov.simplespecs.SpecUtilities.randString
import static dev.kkorolyov.simplespecs.SpecUtilities.setField

class GenericComponentSerializerSpec extends BaseSerializerSpec {
	String name = randString()
	Map<String, Object> attributes = new HashMap<>()
	String attributesS = "{}"

	def setup() {
		reps << [(new GenericComponent(name, attributes)): "$name$attributesS"]

		serializer = new GenericComponentSerializer()
		setField("mapSerializer", ComponentStringSerializer, serializer, Mock(MapStringSerializer) {
			read(attributesS) >> attributes
			write(attributes) >> attributesS

			match({this.&hasOut}) >> Optional.of(attributes)
		})
	}

	def "complains if no name"() {
		when:
		serializer.read(attributesS)

		then:
		thrown(IllegalArgumentException)
	}
}
