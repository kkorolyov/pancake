package dev.kkorolyov.pancake.editor.view

import dev.kkorolyov.pancake.editor.colRatios
import dev.kkorolyov.pancake.editor.rowRatios
import tornadofx.View
import tornadofx.gridpane
import tornadofx.gridpaneConstraints

class EditorScene : View("Editor") {
	private val loopDetails: LoopDetails by inject()
	private val systemsTable: SystemsTable by inject()
	private val entitiesTable: EntitiesTable by inject()
	private val systemDetails: SystemDetails by inject()
	private val entityDetails: EntityDetails by inject()

	override val root = gridpane {
		add(loopDetails.root.gridpaneConstraints {
			columnRowIndex(0, 0)
			columnSpan = 2
		})
		add(systemsTable.root.gridpaneConstraints {
			columnRowIndex(0, 1)
		})
		add(entitiesTable.root.gridpaneConstraints {
			columnRowIndex(1, 1)
		})
		add(systemDetails.root.gridpaneConstraints {
			columnRowIndex(0, 2)
		})
		add(entityDetails.root.gridpaneConstraints {
			columnRowIndex(1, 2)
		})

		colRatios(1, 1)
	}
}
