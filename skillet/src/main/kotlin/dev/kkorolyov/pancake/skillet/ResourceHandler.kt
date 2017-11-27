package dev.kkorolyov.pancake.skillet

import dev.kkorolyov.pancake.platform.Resources.string
import dev.kkorolyov.pancake.skillet.model.Workspace
import dev.kkorolyov.pancake.skillet.serialization.WorkspaceSerializer
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import javafx.stage.Stage
import java.io.File

/**
 * Handles workspace I/O.
 */
object ResourceHandler {
	lateinit var stage: Stage

	/**
	 * Loads a workspace from a resource.
	 * @param path path to resource to load
	 * @return loaded workspace
	 */
	fun load(path: String): Workspace = WorkspaceSerializer.read(string(path))
	/**
	 * Saves a workspace to a resource.
	 * @param workspace workspace to save
	 * @param path path to resource to save as
	 */
	fun save(workspace: Workspace, path: String) = string(path, WorkspaceSerializer.write(workspace))

	/**
	 * Opens a file chooser to load a file.
	 * @param title file chooser title
	 * @param extensionFilters optional extension filters
	 * @return chosen file
	 */
	fun chooseLoad(title: String = "", initialDirectory:File = File("").absoluteFile, vararg extensionFilters: ExtensionFilter): File? =
			buildFileChooser(title, initialDirectory = initialDirectory, extensionFilters = *extensionFilters).showOpenDialog(stage)
	/**
	 * Opens a file chooser to load multiple files.
	 * @param title file chooser title
	 * @param extensionFilters optional extension filters
	 * @return chosen files
	 */
	fun chooseLoadMultiple(title: String = "", vararg extensionFilters: ExtensionFilter): List<File>? =
			buildFileChooser(title, extensionFilters = *extensionFilters).showOpenMultipleDialog(stage)
	/**
	 * Opens a file chooser to save a file.
	 * @param title file chooser title
	 * @param initialFileName initial chooser filename
	 * @param extensionFilters optional extension filters
	 * @return chosen file
	 */
	fun chooseSave(title: String = "", initialFileName: String?, vararg extensionFilters: ExtensionFilter): File? =
			buildFileChooser(title, initialFileName, extensionFilters = *extensionFilters).showSaveDialog(stage)

	private fun buildFileChooser(
			title: String = "",
			initialFileName: String? = null,
			initialDirectory: File = File("").absoluteFile,
			vararg extensionFilters: ExtensionFilter): FileChooser = FileChooser().apply {
		this.title = title
		this.initialFileName = initialFileName
		this.initialDirectory = if (initialDirectory.exists()) initialDirectory else File("").absoluteFile
		this.extensionFilters.addAll(extensionFilters)
	}
}
