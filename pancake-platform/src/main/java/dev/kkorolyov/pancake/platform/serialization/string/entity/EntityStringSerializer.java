package dev.kkorolyov.pancake.platform.serialization.string.entity;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.EntityPool;
import dev.kkorolyov.pancake.platform.serialization.AutoSerializer;
import dev.kkorolyov.pancake.platform.serialization.Serializer;
import dev.kkorolyov.pancake.platform.serialization.string.StringSerializer;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Serializes entities to strings.
 * A serialized entity matches the pattern
 * <pre>
 * NAME[
 * 	component1{...},
 * 	component2{...},
 * 	...
 * ]
 * </pre>
 */
public class EntityStringSerializer extends StringSerializer<Entity> {
	private static final String COMPONENT_PATTERN = ComponentStringSerializer.BASE_PATTERN;

	private final EntityPool context;
	private final Serializer<Component, String> componentSerializer = new AutoSerializer(ComponentStringSerializer.class);

	/**
	 * Constructs a new entity string serializer.
	 * @param context associated entity pool
	 */
	public EntityStringSerializer(EntityPool context) {
		super("\\[\\s*" + COMPONENT_PATTERN + "(,\\s*" + COMPONENT_PATTERN + ")*\\s*]");
		this.context = context;
	}

	@Override
	public Entity read(String out) {
		return context.create()
				.add(Arrays.stream(out.split(",\\s*(?=" + COMPONENT_PATTERN + ")"))
						.map(componentSerializer::read)
						.collect(Collectors.toList()));
	}
	@Override
	public String write(Entity in) {
		return in.stream()
				.map(componentSerializer::write)
				.collect(Collectors.joining("," + System.lineSeparator() + "\t", "{" + System.lineSeparator() + "\t", System.lineSeparator() + "}"));
	}

	@Override
	public Stream<Entity> matches(String out) {
		return Arrays.stream(out.split(",\\s*(?=" + pattern() + ")"))
				.flatMap(super::matches);
	}
}
