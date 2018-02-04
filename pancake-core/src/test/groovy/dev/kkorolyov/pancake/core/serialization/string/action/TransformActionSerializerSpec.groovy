package dev.kkorolyov.pancake.core.serialization.string.action

import dev.kkorolyov.pancake.core.action.TransformAction
import dev.kkorolyov.pancake.platform.math.Vector
import dev.kkorolyov.pancake.platform.specbase.BaseSerializerSpec

import static dev.kkorolyov.pancake.platform.SpecUtilities.bigDecimal
import static dev.kkorolyov.pancake.platform.SpecUtilities.randFloat

class TransformActionSerializerSpec extends BaseSerializerSpec<TransformAction, String> {
	float x = randFloat(), y = randFloat(), z = randFloat()
	float xR = randFloat(), yR = randFloat(), zR = randFloat()
	Vector position = new Vector(x, y, z)
	Vector rotation = new Vector(xR, yR, zR)

	def setup() {
		reps += [
				(new TransformAction(position, rotation)): "TRANSFORM{(${bigDecimal(x)},${bigDecimal(y)},${bigDecimal(z)}), (${bigDecimal(xR)},${bigDecimal(yR)},${bigDecimal(zR)})}",
				(new TransformAction(position)): "TRANSFORM{(${bigDecimal(x)},${bigDecimal(y)},${bigDecimal(z)})}"
		]
		serializer = new TransformActionSerializer()
	}
}
