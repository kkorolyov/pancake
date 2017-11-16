package dev.kkorolyov.pancake.platform.serialization.string.entity;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.serialization.string.MapStringSerializer;
import dev.kkorolyov.pancake.platform.serialization.string.StringSerializer;

import java.util.Map;

/**
 * Serializes components to strings.
 * A serialized component matches the pattern {@code PREFIX{key: val,...}}, i.e. a unique identifier followed by a map.
 * @param <I> component instance type
 */
public abstract class ComponentStringSerializer<I extends Component> extends StringSerializer<I> {
	private static final StringSerializer<Map<String, Object>> mapSerializer = new MapStringSerializer();

	/**
	 * Constructs a new component string serializer.
	 * @param prefix accepted component prefix
	 */
	protected ComponentStringSerializer(String prefix) {
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
