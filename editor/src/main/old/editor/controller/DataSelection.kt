package dev.kkorolyov.pancake.editor.controller

import dev.kkorolyov.pancake.editor.data.EntityData
import dev.kkorolyov.pancake.editor.data.GameSystemData
import tornadofx.Controller

/**
 * Maintains selection state of various dynamic models.
 */
class DataSelection : Controller() {
	val gameSystemData: GameSystemData = GameSystemData()
	val entityData: EntityData = EntityData()
}
