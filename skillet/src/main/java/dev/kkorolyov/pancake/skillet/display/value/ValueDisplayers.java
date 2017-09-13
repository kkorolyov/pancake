package dev.kkorolyov.pancake.skillet.display.value;

import dev.kkorolyov.pancake.muffin.data.type.Attribute;
import dev.kkorolyov.pancake.skillet.display.value.strategy.MapDisplayer;
import dev.kkorolyov.pancake.skillet.display.value.strategy.NumberDisplayer;
import dev.kkorolyov.pancake.skillet.display.value.strategy.StringDisplayer;

import java.util.Arrays;
import java.util.Collection;

/**
 * Provides strategies for displaying attributes based on value type.
 */
public final class ValueDisplayers {
	private static final Collection<ValueDisplayer> strategies = Arrays.asList(
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
