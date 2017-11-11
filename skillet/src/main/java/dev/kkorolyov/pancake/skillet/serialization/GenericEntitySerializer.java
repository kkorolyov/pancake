package dev.kkorolyov.pancake.skillet.serialization;

import dev.kkorolyov.pancake.platform.serialization.string.StringSerializer;
import dev.kkorolyov.pancake.platform.serialization.string.entity.EntityStringSerializer;
import dev.kkorolyov.pancake.skillet.model.GenericComponent;
import dev.kkorolyov.pancake.skillet.model.GenericEntity;

import java.util.Arrays;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Serializes {@link GenericEntity}.
 */
public class GenericEntitySerializer extends StringSerializer<GenericEntity> {
	private static final String PATTERN = new EntityStringSerializer(null).pattern();
	private static final String SPLIT_PATTERN = ",\\s*(?=\\w+\\s*\\{)";
	private static final Pattern NAME_PATTERN = Pattern.compile("\\w+(?=\\s*\\[)");
	private static final StringSerializer<GenericComponent> componentSerializer = new GenericComponentSerializer();

	/**
	 * Constructs a new generic entity serializer.
	 */
	public GenericEntitySerializer() {
		super(PATTERN);
	}

	@Override
	public GenericEntity read(String out) {
		String name = NAME_PATTERN.matcher(out).results()
				.findFirst()
				.map(MatchResult::group)
				.orElseThrow(() -> new IllegalArgumentException("Does not contain an entity name: " + out));
		Iterable<GenericComponent> components = Arrays.stream(out.split(SPLIT_PATTERN))	// Split beforehand because matches() is greedy
				.flatMap(componentSerializer::matches)
				.collect(Collectors.toList());

		return new GenericEntity(name, components);
	}

	@Override
	public String write(GenericEntity in) {
		return in.getName() + in.getComponents().stream()
				.map(componentSerializer::write)
				.collect(Collectors.joining("," + System.lineSeparator() + "\t", "[" + System.lineSeparator() + "\t", System.lineSeparator() + "]"));
	}
}
