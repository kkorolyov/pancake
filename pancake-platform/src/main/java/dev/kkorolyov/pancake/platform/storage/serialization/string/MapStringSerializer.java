package dev.kkorolyov.pancake.platform.storage.serialization.string;

import dev.kkorolyov.pancake.platform.storage.serialization.StringSerializer;
import dev.kkorolyov.pancake.platform.storage.serialization.StringSerializers;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Parses maps.
 */
public class MapStringSerializer extends StringSerializer<Map> {
	public MapStringSerializer() {
		super("\\{.+}");
	}

	@Override
	public Map read(String s) {
		Map<String, Object> map = new LinkedHashMap<>();

		String[] pairs = s.substring(1, s.length() - 1).split(",\\s?");
		for (String pair : pairs) {
			String[] splitPair = pair.split("\\s*=\\s*", 2);
			String name = splitPair[0], valueS = splitPair[1];

			map.put(name, StringSerializers.read(valueS));
		}
		return map;
	}
	@Override
	public String write(Map map) {
		return map.toString();
	}
}
