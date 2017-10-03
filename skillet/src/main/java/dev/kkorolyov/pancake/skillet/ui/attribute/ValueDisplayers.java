package dev.kkorolyov.pancake.skillet.ui.attribute;

import dev.kkorolyov.pancake.platform.storage.Attribute;
import dev.kkorolyov.pancake.skillet.ui.attribute.strategy.MapDisplayer;
import dev.kkorolyov.pancake.skillet.ui.attribute.strategy.NumberDisplayer;
import dev.kkorolyov.pancake.skillet.ui.attribute.strategy.StringDisplayer;
import dev.kkorolyov.pancake.skillet.ui.attribute.strategy.URIDisplayer;

import java.util.Arrays;
import java.util.Collection;

/**
 * Provides strategies for displaying attributes based on value type.
 */
public final class ValueDisplayers {
	private static final Collection<ValueDisplayer> strategies = Arrays.asList(
			new URIDisplayer(),
			new NumberDisplayer(),
			new StringDisplayer(),
			new MapDisplayer()
	);

	/**
	 * @param attribute attribute to display
	 * @return most appropriate displayer for {@code attribute}
	 * @throws UnsupportedOperationException if no displayer for {@code attribute}
	 */
	public static ValueDisplayer getStrategy(Attribute attribute) {
		Class<?> c = attribute.getValue().getClass();

		return strategies.stream()
				.filter(strategy -> strategy.accepts(c))
				.findFirst()
				.orElseThrow(() -> new UnsupportedOperationException("No display strategy for: " + c));
	}

	private ValueDisplayers() {}
}