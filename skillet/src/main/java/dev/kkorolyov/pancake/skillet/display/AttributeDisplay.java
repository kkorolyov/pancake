package dev.kkorolyov.pancake.skillet.display;

import dev.kkorolyov.pancake.skillet.data.Attribute;
import dev.kkorolyov.pancake.skillet.data.Attribute.AttributeChangeEvent;
import dev.kkorolyov.pancake.skillet.data.DataChangeListener;
import dev.kkorolyov.pancake.skillet.data.DataObservable.DataChangeEvent;
import dev.kkorolyov.pancake.skillet.utility.Data;
import dev.kkorolyov.pancake.skillet.utility.ui.UIDecorator;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static dev.kkorolyov.pancake.skillet.utility.ui.DisplayTransformer.paneCollector;
import static dev.kkorolyov.pancake.skillet.utility.ui.UIDecorator.change;

/**
 * Displays an {@link Attribute}.
 */
public class AttributeDisplay implements Display, DataChangeListener<Attribute> {
	private Node root;
	private ValueDisplayer strategy;

	private static Node simpleDisplay(String name, String tooltip, Node value) {
		Label label = tooltip(tooltip,
				new Label(name + ": "));

		BorderPane pane = new BorderPane();
		pane.setLeft(label);
		pane.setRight(value);
		BorderPane.setAlignment(label, Pos.CENTER);
		BorderPane.setAlignment(value, Pos.CENTER);

		return pane;
	}

	private static <N extends Node> N tooltip(String tooltip, N value) {
		return UIDecorator.tooltip(tooltip + " attribute", value);
	}

	/**
	 * Constructs a new attribute display.
	 * @param attribute displayed attribute
	 */
	public AttributeDisplay(Attribute attribute) {
		changed(attribute, AttributeChangeEvent.VALUE);

		attribute.register(this);
	}

	@Override
	public Node getRoot() {
		return root;
	}

	@Override
	public void changed(Attribute target, DataChangeEvent event) {
		if (AttributeChangeEvent.VALUE == event) {
			strategy = ValueDisplayer.getStrategy(target);

			root = strategy.display(target);
			root.setId(target.getName());
		}
	}

	private static abstract class ValueDisplayer {
		private static final Collection<ValueDisplayer> strategies = Arrays.asList(
				new NumberDisplayer(),
				new StringDisplayer(),
				new MapDisplayer()
		);

		private static ValueDisplayer getStrategy(Attribute attribute) {
			Class<?> c = attribute.getValue().getClass();

			return strategies.stream()
					.filter(strategy -> strategy.accepts(c))
					.findFirst()
					.orElseThrow(() -> new UnsupportedOperationException("No display strategy for: " + c));
		}

		abstract Node display(Attribute attribute);

		abstract boolean accepts(Class<?> c);

		private static class NumberDisplayer extends ValueDisplayer {
			@Override
			public Node display(Attribute attribute) {
				return simpleDisplay(attribute.getName(), "Number", change((target, oldValue, newValue) -> {
							if (!Data.isNumberOrEmpty(newValue)) target.setText(oldValue);

							if (!target.getText().equals(oldValue)) {
								attribute.setValue(Data.isNumber(target.getText())
										? NumberFormat.getInstance().parse(target.getText())
										: 0);
							}
						},
						TextInputControl::textProperty,
						new TextField(attribute.getValue().toString())));
			}

			@Override
			boolean accepts(Class<?> c) {
				return Number.class.isAssignableFrom(c);
			}
		}

		private static class StringDisplayer extends ValueDisplayer {
			@Override
			public Node display(Attribute attribute) {
				return simpleDisplay(attribute.getName(), "Text", change((target, oldValue, newValue) -> {
							if (!newValue.equals(oldValue)) attribute.setValue(newValue);
						},
						TextInputControl::textProperty,
						new TextField(attribute.getValue().toString())));
			}

			@Override
			boolean accepts(Class<?> c) {
				return String.class.isAssignableFrom(c);
			}
		}

		private static class MapDisplayer extends ValueDisplayer {
			@Override
			public Node display(Attribute attribute) {
				Map<String, Object> map = attribute.getValue(Map.class);

				return tooltip("Map",
						new TitledPane(attribute.getName(), map.entrySet().stream()
								.map(entry -> new AttributeDisplay(
										new Attribute(entry.getKey(), entry.getValue())
												.register((inner, event) -> entry.setValue(inner.getValue()))
								)).collect(paneCollector(VBox::new))));
			}

			@Override
			boolean accepts(Class<?> c) {
				return Map.class.isAssignableFrom(c);
			}
		}
	}
}
