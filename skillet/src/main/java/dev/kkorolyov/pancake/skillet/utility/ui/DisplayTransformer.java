package dev.kkorolyov.pancake.skillet.utility.ui;

import dev.kkorolyov.pancake.skillet.ui.Panel;

import javafx.scene.layout.Pane;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Convenience methods for data transformation.
 */
public final class DisplayTransformer {
	private DisplayTransformer() {}

	public static <T extends Panel, R extends Pane> R asPane(Collection<T> displayables, Supplier<R> paneSupplier) {
		return displayables.stream()
				.collect(paneCollector(paneSupplier));
	}

	public static <T extends Panel, R extends Pane> Collector<T, R, R> paneCollector(Supplier<R> paneSupplier) {
		return Collector.of(
				paneSupplier,
				(pane, data) -> pane.getChildren().add(data.getRoot()),
				(pane1, pane2) -> {
					pane1.getChildren().addAll(pane2.getChildren());
					return pane1;
				}
		);
	}
}
