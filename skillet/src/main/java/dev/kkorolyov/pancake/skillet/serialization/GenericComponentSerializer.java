package dev.kkorolyov.pancake.skillet.serialization;

import dev.kkorolyov.pancake.platform.serialization.string.entity.ComponentStringSerializer;
import dev.kkorolyov.pancake.skillet.model.GenericComponent;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * Serializes {@link GenericComponent}.
 */
public class GenericComponentSerializer extends ComponentStringSerializer<GenericComponent> {
	private static final Pattern namePattern = Pattern.compile("\\w+(?=\\{)");

	/**
	 * Constructs a new generic component string serializer.
	 */
	public GenericComponentSerializer() {
		super("\\w+");
	}

	@Override
	public GenericComponent read(String out) {
		String name = namePattern.matcher(out).results()
				.findFirst()
				.map(MatchResult::group)
				.orElseThrow(() -> new IllegalArgumentException("Does not contain a component name: " + out));

		return new GenericComponent(name, readMap(out));
	}
	@Override
	public String write(GenericComponent in) {
		return in.getName() + writeMap(in.getAttributes());
	}
}
