package dev.kkorolyov.pancake.debug.controller

import dev.kkorolyov.pancake.debug.data.EntityData
import dev.kkorolyov.pancake.debug.data.GameSystemData
import tornadofx.Controller

class DataDetails : Controller() {
	val gameSystemData: GameSystemData = GameSystemData()
	val entityData: EntityData = EntityData()
}
