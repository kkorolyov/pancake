package dev.kkorolyov.pancake.skillet

import dev.kkorolyov.pancake.skillet.model.Workspace
import dev.kkorolyov.pancake.skillet.ui.component.ComponentList
import dev.kkorolyov.pancake.skillet.ui.entity.EntityList
import dev.kkorolyov.pancake.skillet.ui.entity.EntityTabPane
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.BorderPane
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File

fun main(vararg args: String) {
	Application.launch(Skillet::class.java, *args)
}

/**
 * Skillet application entry point.
 */
class Skillet : Application() {
	companion object {
		private const val WORKSPACE_FILE_EXTENSION = ".mfn"
	}
	private val workspace: Workspace = Workspace()

	private val entityList: EntityList = EntityList(workspace)
	private val componentList: ComponentList = ComponentList(workspace.componentFactory)
	private val entityTabPane: EntityTabPane = EntityTabPane(workspace,
			entitySelected = { componentList.refreshComponents(it) })

	private lateinit var stage: Stage

	override fun start(primaryStage: Stage?) {
		stage = primaryStage ?: throw NullPointerException()

		workspace.componentFactory.add(
				ResourceHandler.load("defaults$WORKSPACE_FILE_EXTENSION").components,
				false)

		stage.title = "Skillet - Pancake Design Workspace"
		stage.icons.add(Image("pancake-icon.png"))

		val root = BorderPane().apply {
			top = MenuBar(
					Menu("_Entity").apply {
						item("_New", { newEntity() })
						item("_Save", { saveWorkspace() })
						item("_Open", { loadWorkspace() })
					},
					Menu("_Components").apply {
						item("_Load") { loadComponents() }
					})
			left = entityList.root
			right = componentList.root
			center = entityTabPane.root
		}
		stage.scene = Scene(root, 480.0, 480.0)
		stage.scene.stylesheets.add("style.css")
		stage.scene.addEventHandler(KeyEvent.KEY_PRESSED) { e ->
			if (e.isControlDown) {
				when (e.code) {
					KeyCode.N -> newEntity()
					KeyCode.S -> saveWorkspace()
					KeyCode.O -> loadWorkspace()
					KeyCode.L -> loadComponents()
					else -> {}
				}
			}
		}
		stage.show()
	}

	private fun newEntity() {
		workspace.newEntity()
	}
	private fun saveWorkspace() {
		val chooser = buildFileChooser()
		chooser.title = "Save Workspace"
		chooser.initialFileName = "workspace$WORKSPACE_FILE_EXTENSION"

		val result = chooser.showSaveDialog(stage)
		if (result != null) ResourceHandler.save(workspace, result.toString())
	}
	private fun loadWorkspace() {
		val chooser = buildFileChooser()
		chooser.title = "Load Workspace"

		val result = chooser.showOpenDialog(stage)
		if (result != null) workspace.addWorkspace(ResourceHandler.load(result.toString()))
	}
	private fun loadComponents() {
		val chooser = buildFileChooser()
		chooser.title = "Load Components"

		val results = chooser.showOpenMultipleDialog(stage)
		if (results != null) {
			val addedComponents = results
					.map { file -> ResourceHandler.load(file.toString()) }
					.flatMap(Workspace::components)

			workspace.componentFactory.add(addedComponents, false)

			val addedComponentsInfo = Alert(
					Alert.AlertType.INFORMATION,
					addedComponents.joinToString(System.lineSeparator()) { "${it.name} - ${it.attributes.size} attributes" })
			addedComponentsInfo.title = "Added components"
			addedComponentsInfo.headerText = addedComponentsInfo.title

			addedComponentsInfo.show()
		}
	}

	private fun buildFileChooser(): FileChooser {
		val chooser = FileChooser()
		chooser.extensionFilters.add(FileChooser.ExtensionFilter("Skillet Workspaces", "*$WORKSPACE_FILE_EXTENSION"))
		chooser.initialDirectory = File("").absoluteFile

		return chooser
	}
}
