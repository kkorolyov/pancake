package dev.kkorolyov.pancake.skillet.decorator;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 * Decorates {@link ContextMenu}s.
 */
public class ContextMenuDecorator<T extends ContextMenu> extends UIDecorator<T> {
	protected ContextMenuDecorator(T object) {
		super(object);
	}

	/**
	 * Adds a menu item.
	 * @param name menu item name
	 * @param procedure menu item action
	 * @return {@code this}
	 */
	public ContextMenuDecorator<T> item(String name, Runnable procedure) {
		object.getItems().add(
				decorate(new MenuItem(name))
						.action(procedure)
						.get());
		return this;
	}
}
