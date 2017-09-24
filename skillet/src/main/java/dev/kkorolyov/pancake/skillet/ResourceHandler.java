package dev.kkorolyov.pancake.skillet;

import dev.kkorolyov.pancake.platform.storage.Entity;
import dev.kkorolyov.pancake.platform.storage.serialization.EntitySerializer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Handles filesystem resources and I/O.
 */
public class ResourceHandler {
	private static final Charset charset = Charset.forName("UTF-8");

	private final EntitySerializer entitySerializer = new EntitySerializer();

	/**
	 * Loads an entity from a resource.
	 * @param path path to resource to load
	 * @return loaded entity
	 */
	public Entity load(Path path) {
		try {
			return entitySerializer.read(
					new String(Files.readAllBytes(path), charset));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	/**
	 * Saves an entity to a resource.
	 * @param entity entity to save
	 * @param path path to resource to save as
	 */
	public void save(Entity entity, Path path) {
		try {
			Files.write(path,	entitySerializer.write(entity).getBytes(charset));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
