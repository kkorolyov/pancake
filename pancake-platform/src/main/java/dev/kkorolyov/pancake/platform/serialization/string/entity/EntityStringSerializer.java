package dev.kkorolyov.pancake.platform.serialization.string.entity;


import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.EntityPool;
import dev.kkorolyov.pancake.platform.event.EventBroadcaster;
import dev.kkorolyov.pancake.platform.serialization.AutoSerializer;
import dev.kkorolyov.pancake.platform.serialization.ContextualSerializer;
import dev.kkorolyov.pancake.platform.serialization.Serializer;
import dev.kkorolyov.pancake.platform.serialization.string.StringSerializer;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
public class EntityStringSerializer extends StringSerializer<Entity> implements ContextualSerializer<Entity, String, EntityPool> {
	private static final Pattern COMPONENT_PATTERN = Pattern.compile("\\w+\\s?\\{[\\s\\S]+}");
	private static final Serializer<Component, String> componentSerializer = new AutoSerializer(ComponentStringSerializer.class);

	/**
	 * Constructs a new entity string serializer.
	 */
	public EntityStringSerializer() {
		super("\\w+\\s?\\[[\\s\\S]+(,\\s*[\\s\\S]+)*\\s*]");
	}

	@Override
	public Entity read(String out) {
		return read(out, new EntityPool(new EventBroadcaster()));
	}
	@Override
	public String write(Entity in) {
		return write(in, new EntityPool(new EventBroadcaster()));
	}

	@Override
	public Entity read(String out, EntityPool context) {
		return context.create(COMPONENT_PATTERN.matcher(out).results()
				.map(MatchResult::group)
				.map(componentSerializer::read)
				.collect(Collectors.toList()));
	}
	@Override
	public String write(Entity in, EntityPool context) {
		return in.getId() + in.streamComponents()
				.map(componentSerializer::write)
				.collect(Collectors.joining("," + System.lineSeparator() + "\t", "{" + System.lineSeparator() + "\t", System.lineSeparator() + "}"));
	}
}
