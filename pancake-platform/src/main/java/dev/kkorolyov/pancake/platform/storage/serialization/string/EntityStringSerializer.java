package dev.kkorolyov.pancake.platform.storage.serialization.string;

import dev.kkorolyov.pancake.platform.storage.Entity;
import dev.kkorolyov.pancake.platform.storage.serialization.StringSerializer;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Serializes entities to strings.
 */
public class EntityStringSerializer extends StringSerializer<Entity> {
	private final ComponentStringSerializer componentStringSerializer = new ComponentStringSerializer();

	public EntityStringSerializer() {
		super("(.+=.+(\\R|$))+");
	}

	/**
	 * Reads an entity from a string.
	 * Each line in the string is expected to be in the format:
	 * <pre>
	 * {componentName}={attributeName}: {attributeValue}...
	 * </pre>
	 * i.e. a component name mapped to an arbitrary list of attributes.
	 * Valid attribute value types include:
	 * <pre>
	 * Number - 123, -456, 1.23
	 * String - "someText"
	 * Map - {key=value, key=value, key=value...}
	 * </pre>
	 */
	@Override
	public Entity read(String s) {
		return Arrays.stream(s.split("\\R"))
				.map(componentStringSerializer::read)
				.collect(
						Entity::new,
						Entity::addComponent,
						(entity1, entity2) -> entity1.addAll(entity2.getComponents()));
	}
	/**
	 * Writes an entity to a string in the format expected by {@link #read(String)}.
	 */
	@Override
	public String write(Entity entity) {
		return entity.getComponents().stream()
				.map(componentStringSerializer::write)
				.collect(Collectors.joining(System.lineSeparator()));
	}
}
