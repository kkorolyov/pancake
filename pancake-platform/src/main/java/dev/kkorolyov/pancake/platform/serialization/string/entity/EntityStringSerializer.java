package dev.kkorolyov.pancake.platform.serialization.string.entity;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.EntityRegistry;
import dev.kkorolyov.pancake.platform.serialization.AutoSerializer;
import dev.kkorolyov.pancake.platform.serialization.Serializer;
import dev.kkorolyov.pancake.platform.serialization.string.StringSerializer;

import java.util.Arrays;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
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
	private static final Pattern NAME_PATTERN = Pattern.compile("\\w+(?=\\s*\\[)");
	private static final Pattern COMPONENT_PATTERN = Pattern.compile("\\w+\\s?\\{[\\s\\S]+}");

	private final EntityRegistry context;
	private final Serializer<Component, String> componentSerializer = new AutoSerializer(ComponentStringSerializer.class);

	/**
	 * Constructs a new entity string serializer.
	 * @param context associated entity registry
	 */
	public EntityStringSerializer(EntityRegistry context) {
		super("\\w+\\s?\\[[\\s\\S]+(,\\s*[\\s\\S]+)*\\s*]");
		this.context = context;
	}

	@Override
	public Entity read(String out) {
		return new Entity(
				NAME_PATTERN.matcher(out).results().findFirst()
						.map(MatchResult::group)
						.orElseThrow(() -> new IllegalArgumentException("Does not contain an entity name: " + out)),
				Arrays.stream(NAME_PATTERN.matcher(out).replaceAll("").split(",\\s*(?=\\w+\\{)"))
						.map(componentSerializer::read)
						.collect(Collectors.toList()));
	}
	@Override
	public String write(Entity in) {
		return in.getId() + in.streamComponents()
				.map(componentSerializer::write)
				.collect(Collectors.joining("," + System.lineSeparator() + "\t", "{" + System.lineSeparator() + "\t", System.lineSeparator() + "}"));
	}

	@Override
	public Stream<Entity> matches(String out) {
		return Arrays.stream(out.split(",\\s*(?=\\w+\\s*\\[)"))
				.flatMap(super::matches);
	}
}
