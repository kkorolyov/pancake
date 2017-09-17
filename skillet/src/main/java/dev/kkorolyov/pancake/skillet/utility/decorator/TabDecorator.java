package dev.kkorolyov.pancake.skillet.utility.decorator;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import java.util.function.Function;

/**
 * Decorates {@link Tab}s.
 */
public class TabDecorator<T extends Tab> extends UIDecorator<T> {
	TabDecorator(T object) {
		super(object);
	}

	/**
	 * Sets a graphic.
	 * @param graphic tab graphic
	 * @return {@code this}
	 */
	public TabDecorator<T> graphic(Node graphic) {
		object.setGraphic(graphic);
		return this;
	}
	/**
	 * Sets content.
	 * @param content tab content
	 * @return {@code this}
	 */
	public TabDecorator<T> content(Node content) {
		object.setContent(content);
		return this;
	}


	public TabDecorator<T> close(Runnable procedure) {
		object.setOnClosed(e -> procedure.run());
		return this;
	}

	/**
	 * Sets a change listener on a property.
	 * @param propertyFunction retrieves the property from the decorated tab
	 * @param listener attached change listener
	 * @return {@code this}
	 */
	public <V> TabDecorator<T> change(Function<T, ObservableValue<V>> propertyFunction, ThrowingChangeListener<? super V, T> listener) {
		propertyFunction.apply(object).addListener((observable, oldValue, newValue) -> {
			try {
				listener.changed(object, oldValue, newValue);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		return this;
	}
}
