package dev.kkorolyov.pancake.skillet.utility.decorator;

import javafx.scene.control.MenuItem;

/**
 * Decorates {@link MenuItem}s.
 */
public class MenuItemDecorator<T extends MenuItem> extends UIDecorator<T> {
	MenuItemDecorator(T object) {
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
