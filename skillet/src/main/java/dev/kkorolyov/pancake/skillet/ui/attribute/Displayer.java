package dev.kkorolyov.pancake.skillet.ui.attribute;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import java.util.Map.Entry;

import static dev.kkorolyov.pancake.skillet.decorator.UIDecorator.decorate;

/**
 * Displays a map entry.
 */
public abstract class Displayer<T> {
	private final Class<?> acceptedType;

	/**
	 * Constructs a new displayer.
	 * @param acceptedType accepted values' runtime type
	 */
	protected Displayer(Class<? super T> acceptedType) {
		this.acceptedType = acceptedType;
	}

	/**
	 * Generates a displayable representation of an entry.
	 * @param entry map entry to display
	 * @return representative display of property
	 */
	public abstract Node display(Entry<String, T> entry);

	protected Node simpleDisplay(String name, Node value) {
		return decorate(new BorderPane())
				.left(decorate(new Label(name + ": "))
						.styleClass("attribute-name")
						.tooltip(getTooltipText())
						.get())
				.right(value)
				.get();
	}

	protected String getTooltipText() {
		return acceptedType.getSimpleName() + " attribute";
	}

	/** @return {@code true} if able to display entry */
	public boolean accepts(Entry<String, Object> entry) {
		return acceptedType.isAssignableFrom(entry.getValue().getClass());
	}
}
