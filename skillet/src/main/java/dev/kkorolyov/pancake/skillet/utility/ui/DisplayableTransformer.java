package dev.kkorolyov.pancake.skillet.utility.ui;

import dev.kkorolyov.pancake.skillet.display.Displayable;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Convenience methods for data transformation.
 */
public final class DisplayableTransformer {
	private DisplayableTransformer() {}

	public static <T extends Displayable, R extends Pane> R asPane(Collection<T> displayables, Supplier<R> paneSupplier) {
		return asPane(displayables, paneSupplier, null);
	}
	public static <T extends Displayable, R extends Pane> R asPane(Collection<T> displayables, Supplier<R> paneSupplier, TriConsumer<T, Node, R> triConsumer) {
		return displayables.stream()
				.collect(paneCollector(paneSupplier, triConsumer));
	}

	public static <T extends Displayable, R extends Pane> Collector<T, R, R> paneCollector(Supplier<R> paneSupplier) {
		return paneCollector(paneSupplier, null);
	}
	public static <T extends Displayable, R extends Pane> Collector<T, R, R> paneCollector(Supplier<R> paneSupplier, TriConsumer<T, Node, R> triConsumer) {
		return Collector.of(
				paneSupplier,
				(pane, data) -> {
					Node node = data.toNode();
					pane.getChildren().add(node);

					if (triConsumer != null) triConsumer.apply(data, node, pane);
				},
				(pane1, pane2) -> {
					pane1.getChildren().addAll(pane2.getChildren());
					return pane1;
				}
		);
	}

	public interface TriConsumer<T1, T2, T3> {
		void apply(T1 t1, T2 t2, T3 t3);
	}
}
