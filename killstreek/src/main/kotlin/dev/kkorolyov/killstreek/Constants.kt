package dev.kkorolyov.killstreek

import dev.kkorolyov.pancake.platform.math.Vector3
import dev.kkorolyov.pancake.platform.math.Vectors

const val OBJECT_MASS = .1
const val PLAYER_MASS = 10.0

val MAX_SPEED: Vector3 = Vectors.create(50.0, 50.0, 50.0)
val OBJECT_DAMPING: Vector3 = Vectors.create(.9, .9, .9)
val PLAYER_DAMPING: Vector3 = Vectors.create(.5, .5, .5)

val BOX: Vector3 = Vectors.create(1.0, 1.0, 0.0)
val RADIUS = BOX.x / 2.0
