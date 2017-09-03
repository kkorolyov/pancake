package dev.kkorolyov.pancake.skillet.uibuilder;

import dev.kkorolyov.pancake.skillet.display.Displayable;

import javafx.scene.layout.Pane;
import java.util.Collection;
import java.util.stream.Collector;

/**
 * Convenience methods for data transformation.
 */
public final class DisplayableTransformer {
	private DisplayableTransformer() {}

	public static <T extends Displayable, R extends Pane> R asPane(Collection<T> displayables, Class<R> paneType) {
		return displayables.stream()
				.collect(paneCollector(paneType));
	}
	private static <T> T newInstance(Class<T> c) {
		try {
			return c.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T extends Displayable, R extends Pane> Collector<T, R, R> paneCollector(Class<R> paneType) {
		return Collector.of(
				() -> newInstance(paneType),
				(pane, displayable) -> pane.getChildren().add(displayable.toNode()),
				(pane1, pane2) -> {
					pane1.getChildren().addAll(pane2.getChildren());
					return pane1;
				}
		);
	}
}
