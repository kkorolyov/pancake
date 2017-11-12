package dev.kkorolyov.pancake.skillet;

import dev.kkorolyov.pancake.platform.serialization.string.StringSerializer;
import dev.kkorolyov.pancake.skillet.model.Workspace;
import dev.kkorolyov.pancake.skillet.serialization.WorkspaceSerializer;

import static dev.kkorolyov.pancake.platform.Resources.string;

/**
 * Handles filesystem resources and I/O.
 */
public class ResourceHandler {
	private final StringSerializer<Workspace> workspaceSerializer = new WorkspaceSerializer();

	/**
	 * Loads a workspace from a resource.
	 * @param path path to resource to load
	 * @return loaded workspace
	 */
	public Workspace load(String path) {
		return workspaceSerializer.read(string(path));
	}
	/**
	 * Saves a workspace to a resource.
	 * @param workspace workspace to save
	 * @param path path to resource to save as
	 */
	public void save(Workspace workspace, String path) {
		string(path, workspaceSerializer.write(workspace));
	}
}
