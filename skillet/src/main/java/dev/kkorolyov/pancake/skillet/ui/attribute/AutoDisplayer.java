package dev.kkorolyov.pancake.skillet.ui.attribute;

import javafx.scene.Node;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map.Entry;

/**
 * Displays properties using the most appropriate displayer.
 */
public class AutoDisplayer extends Displayer<Object> {
	private static final Collection<Displayer> strategies = Arrays.asList(
			new URIDisplayer(),
			new NumberDisplayer(),
			new VectorDisplayer(),
			new StringDisplayer(),
			new MapDisplayer()
	);

	/**
	 * Constructs a new auto displayer.
	 */
	public AutoDisplayer() {
		super(Object.class);
	}

	@Override
	public Node display(Entry<String, Object> entry) {
		return strategies.stream()
				.filter(strategy -> strategy.accepts(entry))
				.findFirst()
				.map(displayer -> displayer.display(entry))
				.orElseThrow(() -> new UnsupportedOperationException("No displayer for: " + entry));
	}
}
