package dev.kkorolyov.pancake.skillet;

import dev.kkorolyov.pancake.platform.serialization.string.StringSerializer;
import dev.kkorolyov.pancake.skillet.model.GenericEntity;
import dev.kkorolyov.pancake.skillet.serialization.GenericEntitySerializer;
import dev.kkorolyov.simplefiles.stream.StreamStrategies;

import java.nio.charset.StandardCharsets;

import static dev.kkorolyov.simplefiles.Files.bytes;
import static dev.kkorolyov.simplefiles.Files.in;
import static dev.kkorolyov.simplefiles.Files.out;

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
	public void save(GenericEntity entity, String path) {
		bytes(out(path, StreamStrategies.OUT_PATH),
				entitySerializer.write(entity).getBytes(StandardCharsets.UTF_8));
	}
}
