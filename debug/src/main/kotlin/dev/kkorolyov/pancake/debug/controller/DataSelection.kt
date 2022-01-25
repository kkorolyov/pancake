package dev.kkorolyov.pancake.debug.controller

import dev.kkorolyov.pancake.debug.data.EntityData
import dev.kkorolyov.pancake.debug.data.GameSystemData
import tornadofx.Controller

/**
 * Maintains selection state of various dynamic models.
 */
class DataSelection : Controller() {
	val gameSystemData: GameSystemData = GameSystemData()
	val entityData: EntityData = EntityData()
}
