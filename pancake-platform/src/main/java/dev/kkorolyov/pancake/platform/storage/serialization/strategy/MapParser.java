package dev.kkorolyov.pancake.platform.storage.serialization.strategy;

import dev.kkorolyov.pancake.platform.storage.serialization.ValueParser;
import dev.kkorolyov.pancake.platform.storage.serialization.ValueParsers;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Parses maps.
 */
public class MapParser implements ValueParser {
	@Override
	public Object parse(String s) {
		Map<String, Object> map = new LinkedHashMap<>();

		String[] pairs = s.substring(1, s.length() - 1).split(",\\s?");
		for (String pair : pairs) {
			String[] splitPair = pair.split("\\s*=\\s*", 2);
			String name = splitPair[0], valueS = splitPair[1];

			map.put(name, ValueParsers.getStrategy(valueS).parse(valueS));
		}
		return map;
	}

	@Override
	public boolean accepts(String s) {
		return s.matches("\\{.*}");
	}
}
