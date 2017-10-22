package dev.kkorolyov.pancake.core.serialization.action

import dev.kkorolyov.pancake.core.action.ForceAction
import dev.kkorolyov.pancake.platform.action.ActionRegistry
import dev.kkorolyov.pancake.platform.math.Vector
import dev.kkorolyov.pancake.platform.specbase.BaseContextualSerializerSpec

import static dev.kkorolyov.pancake.platform.SpecUtilities.bigDecimal
import static dev.kkorolyov.pancake.platform.SpecUtilities.randFloat

class ForceActionSerializerSpec extends BaseContextualSerializerSpec<ForceAction, String, ActionRegistry> {
	float x = randFloat(), y = randFloat(), z = randFloat()
	Vector force = new Vector(x, y, z)

	def setup() {
		reps << [(new ForceAction(force)): "FORCE{(${bigDecimal(x)},${bigDecimal(y)},${bigDecimal(z)})}"]
		context = null

		serializer = new ForceActionSerializer()
	}
}
