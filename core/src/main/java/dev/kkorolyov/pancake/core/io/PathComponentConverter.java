package dev.kkorolyov.pancake.core.io;

import dev.kkorolyov.pancake.core.component.Path;
import dev.kkorolyov.pancake.platform.entity.ComponentConverter;
import dev.kkorolyov.pancake.platform.io.ObjectConverters;

import java.util.Map;
import java.util.stream.StreamSupport;

public final class PathComponentConverter implements ComponentConverter<Path> {
	@Override
	public Path read(Object data) {
		var map = (Map<String, Object>) data;

		var result = new Path(((Number) map.get("strength")).doubleValue(), ((Number) map.get("buffer")).doubleValue());
		var steps = map.get("steps");
		if (steps != null) {
			var vectorConverter = ObjectConverters.vector3();
			for (var step : ((Iterable<Iterable<Number>>) steps)) {
				result.add(vectorConverter.convert(step));
			}
		}

		return result;
	}
	@Override
	public Object write(Path path) {
		return Map.of(
				"strength", path.getStrength(),
				"buffer", path.getBuffer(),
				"steps", StreamSupport.stream(path.spliterator(), false).toList()
		);
	}

	@Override
	public Class<Path> getType() {
		return Path.class;
	}
}
