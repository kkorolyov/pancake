package dev.kkorolyov.pancake.skillet;

import dev.kkorolyov.pancake.platform.serialization.string.StringSerializer;
import dev.kkorolyov.pancake.skillet.model.GenericEntity;
import dev.kkorolyov.pancake.skillet.serialization.GenericEntitySerializer;

import static dev.kkorolyov.pancake.platform.Resources.string;

/**
 * Handles filesystem resources and I/O.
 */
public class ResourceHandler {
	private final StringSerializer<GenericEntity> entitySerializer = new GenericEntitySerializer();

	/**
	 * Loads an entity from a resource.
	 * @param path path to resource to load
	 * @return loaded entity
	 */
	public GenericEntity load(String path) {
		return entitySerializer.read(string(path));
	}
	/**
	 * Saves an entity to a resource.
	 * @param entity entity to save
	 * @param path path to resource to save as
	 */
	public void save(GenericEntity entity, String path) {
		string(path, entitySerializer.write(entity));
	}
}
