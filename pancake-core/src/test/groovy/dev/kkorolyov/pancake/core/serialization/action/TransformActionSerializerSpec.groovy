package dev.kkorolyov.pancake.core.serialization.action

import dev.kkorolyov.pancake.core.action.TransformAction
import dev.kkorolyov.pancake.platform.action.ActionRegistry
import dev.kkorolyov.pancake.platform.math.Vector
import dev.kkorolyov.pancake.platform.storage.serialization.AbstractContextualSerializerSpec

import spock.lang.Shared

import static dev.kkorolyov.pancake.platform.SpecUtilities.*

class TransformActionSerializerSpec extends AbstractContextualSerializerSpec<TransformAction, String, ActionRegistry> {
	@Shared float x = randFloat(), y = randFloat(), z = randFloat()
	@Shared Vector position = new Vector(x, y, z)
	@Shared float rotation = randFloat()

	def setupSpec() {
		inReps = [
				new TransformAction(position, rotation),
				new TransformAction(position)
		]
		outReps = [
				String.format("TRANSFORM{(%s,%s,%s), %s}", bigDecimal(x), bigDecimal(y), bigDecimal(z), bigDecimal(rotation)),
				String.format("TRANSFORM{(%s,%s,%s)}", bigDecimal(x), bigDecimal(y), bigDecimal(z))
		]
	}
	def setup() {
		context = null

		serializer = new TransformActionSerializer()
	}

	def "accepts matching pattern"() {
		expect:
		serializer.accepts(outRep)

		where:
		outRep << outReps
	}
	def "rejects non-matching pattern"() {
		expect:
		!serializer.accepts(randString())
	}
}
