package dev.kkorolyov.pancake.platform.serialization.string.entity;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.EntityPool;
import dev.kkorolyov.pancake.platform.serialization.AutoSerializer;
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
public class EntityStringSerializer extends StringSerializer<Entity> {
	private static final Pattern COMPONENT_PATTERN = Pattern.compile("\\w+\\s?\\{[\\s\\S]+}");

	private final EntityPool context;
	private final Serializer<Component, String> componentSerializer = new AutoSerializer(ComponentStringSerializer.class);

	/**
	 * Constructs a new entity string serializer.
	 * @param context associated entity pool
	 */
	public EntityStringSerializer(EntityPool context) {
		super("\\w+\\s?\\[[\\s\\S]+(,\\s*[\\s\\S]+)*\\s*]");
		this.context = context;
	}

	@Override
	public Entity read(String out) {
		return context.create(COMPONENT_PATTERN.matcher(out).results()
				.map(MatchResult::group)
				.map(componentSerializer::read)
				.collect(Collectors.toList()));
	}
	@Override
	public String write(Entity in) {
		return in.getId() + in.streamComponents()
				.map(componentSerializer::write)
				.collect(Collectors.joining("," + System.lineSeparator() + "\t", "{" + System.lineSeparator() + "\t", System.lineSeparator() + "}"));
	}
}
