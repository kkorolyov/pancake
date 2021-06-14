package dev.kkorolyov.pancake.demo.wasdbox

import dev.kkorolyov.pancake.platform.Config
import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.stage.Stage

/**
 * JavaFX application running demo and displaying [scene].
 */
class Demo(private val scene: Scene, private val onClose: () -> Unit) : Application() {
	init {
		start(Stage())
	}

	override fun start(primaryStage: Stage) {
		primaryStage.title = "Pancake Demo - WASDBox"

		primaryStage.scene = scene

		primaryStage.width = Config.get().getProperty("width").toDouble()
		primaryStage.height = Config.get().getProperty("height").toDouble()

		primaryStage.onCloseRequest = EventHandler { onClose() }

		primaryStage.show()
	}
}
