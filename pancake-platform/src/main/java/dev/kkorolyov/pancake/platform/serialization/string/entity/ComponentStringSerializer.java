package dev.kkorolyov.pancake.platform.serialization.string.entity;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.serialization.string.MapStringSerializer;
import dev.kkorolyov.pancake.platform.serialization.string.StringSerializer;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Serializes components to strings.
 * A serialized component matches the pattern {@code PREFIX{key: val,...}}, i.e. a unique identifier followed by a map.
 * @param <I> component instance type
 */
public abstract class ComponentStringSerializer<I extends Component> extends StringSerializer<I> {
	/** Base pattern matching all component string serializers */
	public static final String BASE_PATTERN;
	private static final Pattern NAME_PATTERN = Pattern.compile("\\w+");
	private static final StringSerializer<Map<String, Object>> mapSerializer = new MapStringSerializer();

	static {
		BASE_PATTERN = NAME_PATTERN + mapSerializer.pattern();
	}

	/**
	 * Constructs a new component string serializer.
	 * @param prefix accepted component prefix
	 * @throws IllegalArgumentException if {@code prefix} does not match the required pattern
	 */
	protected ComponentStringSerializer(String prefix) {
		super(prefix + mapSerializer.pattern());

		if (!NAME_PATTERN.matcher(prefix).matches()) throw new IllegalArgumentException("Provided prefix [" + prefix + "] does not match required pattern: " + NAME_PATTERN);
	}

	protected Map<String, Object> readMap(String out) {
		return mapSerializer.match(out)
				.orElseThrow(() -> new IllegalArgumentException("Does not contain a map-like construct: " + out));
	}
	protected String writeMap(Map<String, Object> in) {
		return mapSerializer.write(in);
	}

	@Override
	public Stream<I> matches(String out) {
		return Arrays.stream(out.split(",\\s*(?=" + pattern() + ")"))
				.flatMap(super::matches);
	}
}
