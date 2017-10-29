package dev.kkorolyov.pancake.platform.serialization.string.entity;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.serialization.string.MapStringSerializer;
import dev.kkorolyov.pancake.platform.serialization.string.StringSerializer;

import java.util.Map;

/**
 * Serializes components to strings.
 * A serialized component matches the pattern {@code PREFIX{key: val,...}}, i.e. a unique identifier followed by a map.
 * @param <T> component instance type
 */
public abstract class ComponentStringSerializer<T extends Component> extends StringSerializer<T> {
	private static final StringSerializer<Map<String, Object>> mapSerializer = new MapStringSerializer();

	/**
	 * Constructs a new component string serializer.
	 * @param prefix accepted component prefix
	 */
	public ComponentStringSerializer(String prefix) {
		super(prefix + mapSerializer.pattern());
	}

	protected Map<String, Object> readMap(String out) {
		return mapSerializer.match(out)
				.orElseThrow(() -> new IllegalArgumentException("Does not contain a map-like construct: " + out));
	}
	protected String writeMap(Map<String, Object> in) {
		return mapSerializer.write(in);
	}
}
