package dev.kkorolyov.pancake.skillet.decorator;

import dev.kkorolyov.pancake.skillet.UIDecorator;

import javafx.scene.control.MenuItem;

/**
 * Decorates {@link MenuItem}s.
 */
public class MenuItemDecorator<T extends MenuItem> extends UIDecorator<T> {
	/**
	 * Constructs a new MenuItem decorator.
	 * @param object decorated object
	 */
	public MenuItemDecorator(T object) {
		super(object);
	}

	/**
	 * Sets an action procedure.
	 * @param procedure attached action procedure
	 * @return {@code this}
	 */
	public MenuItemDecorator<T> action(Runnable procedure) {
		object.setOnAction(e -> procedure.run());
		return this;
	}
}
