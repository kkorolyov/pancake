package dev.kkorolyov.pancake.debug

import dev.kkorolyov.pancake.platform.math.Vector3

fun prettyPrint(vector: Vector3) = String.format("(%.3f, %.3f, %.3f)", vector.x, vector.y, vector.z)
