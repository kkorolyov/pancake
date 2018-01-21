package dev.kkorolyov.pancake.platform.serialization.string;

import dev.kkorolyov.pancake.platform.serialization.AutoSerializer;
import dev.kkorolyov.pancake.platform.serialization.Serializer;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Parses maps.
 */
public class MapStringSerializer extends StringSerializer<Map<String, Object>> {
	private static final Serializer<Object, String> autoSerializer = new AutoSerializer(StringSerializer.class);

	public MapStringSerializer() {
		super("\\{\\s*\\w+:[\\s\\S]+(,\\s*\\w+:[\\s\\S]+)*}");
	}

	@Override
	public Map<String, Object> read(String s) {
		return Arrays.stream(s.substring(1, s.length() - 1).split(",\\s*(?=\\w+:)"))
				.map(String::trim)
				.map(property -> property.split(":\\s?", 2))
				.collect(Collectors.toMap(split -> split[0], split -> autoSerializer.read(split[1]), (o1, o2) -> o1, LinkedHashMap::new));
	}
	@Override
	public String write(Map<String, Object> map) {
		return map.entrySet().stream()
				.map(entry -> entry.getKey() + ": " + autoSerializer.write(entry.getValue()))
				.collect(Collectors.joining("," + System.lineSeparator() + "\t", "{" + System.lineSeparator() + "\t", System.lineSeparator() + "}"));
	}
}
