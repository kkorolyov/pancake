package dev.kkorolyov.pancake.skillet;

import dev.kkorolyov.pancake.platform.storage.Entity;
import dev.kkorolyov.pancake.platform.storage.serialization.EntitySerializer;
import dev.kkorolyov.simplefiles.stream.StreamStrategies;

import java.nio.charset.StandardCharsets;

import static dev.kkorolyov.simplefiles.Files.bytes;
import static dev.kkorolyov.simplefiles.Files.in;
import static dev.kkorolyov.simplefiles.Files.out;

/**
 * Handles filesystem resources and I/O.
 */
public class ResourceHandler {
	private final EntitySerializer entitySerializer = new EntitySerializer();

	/**
	 * Loads an entity from a resource.
	 * @param path path to resource to load
	 * @return loaded entity
	 */
	public Entity load(String path) {
		return entitySerializer.read(
				new String(
						bytes(in(path, StreamStrategies.IN_PATH, StreamStrategies.IN_CLASSPATH)),
						StandardCharsets.UTF_8));
	}
	/**
	 * Saves an entity to a resource.
	 * @param entity entity to save
	 * @param path path to resource to save as
	 */
	public void save(Entity entity, String path) {
		bytes(out(path, StreamStrategies.OUT_PATH),
				entitySerializer.write(entity).getBytes(StandardCharsets.UTF_8));
	}
}
