package dev.kkorolyov.pancake.core.serialization.string.action

import dev.kkorolyov.pancake.core.action.TransformAction
import dev.kkorolyov.pancake.platform.math.Vector
import dev.kkorolyov.pancake.platform.specbase.BaseSerializerSpec

import static dev.kkorolyov.pancake.platform.SpecUtilities.bigDecimal
import static dev.kkorolyov.simplespecs.SpecUtilities.randDouble

class TransformActionSerializerSpec extends BaseSerializerSpec<TransformAction, String> {
	double x = randDouble(), y = randDouble(), z = randDouble()
	double xR = randDouble(), yR = randDouble(), zR = randDouble()
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
