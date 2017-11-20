package dev.kkorolyov.pancake.skillet

import dev.kkorolyov.pancake.platform.Resources.string
import dev.kkorolyov.pancake.skillet.model.Workspace
import dev.kkorolyov.pancake.skillet.serialization.WorkspaceSerializer

/**
 * Handles workspace I/O.
 */
class ResourceHandler {
	private val workspaceSerializer = WorkspaceSerializer()

	/**
	 * Loads a workspace from a resource.
	 * @param path path to resource to load
	 * @return loaded workspace
	 */
	fun load(path: String): Workspace = workspaceSerializer.read(string(path))
	/**
	 * Saves a workspace to a resource.
	 * @param workspace workspace to save
	 * @param path path to resource to save as
	 */
	fun save(workspace: Workspace, path: String) = string(path, workspaceSerializer.write(workspace))
}
