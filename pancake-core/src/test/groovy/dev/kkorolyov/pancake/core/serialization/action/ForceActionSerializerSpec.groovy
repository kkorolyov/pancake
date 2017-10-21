package dev.kkorolyov.pancake.core.serialization.action

import dev.kkorolyov.pancake.core.action.ForceAction
import dev.kkorolyov.pancake.platform.action.ActionRegistry
import dev.kkorolyov.pancake.platform.math.Vector
import dev.kkorolyov.pancake.platform.storage.serialization.AbstractContextualSerializerSpec

import spock.lang.Shared

import static dev.kkorolyov.pancake.platform.SpecUtilities.*

class ForceActionSerializerSpec extends AbstractContextualSerializerSpec<ForceAction, String, ActionRegistry> {
	@Shared float x = randFloat(), y = randFloat(), z = randFloat()
	@Shared Vector force = new Vector(x, y, z)

	def setupSpec() {
		inReps << new ForceAction(force)
		outReps << String.format("FORCE{(%s,%s,%s)}", bigDecimal(x), bigDecimal(y), bigDecimal(z))
	}
	def setup() {
		context = null

		serializer = new ForceActionSerializer()
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
