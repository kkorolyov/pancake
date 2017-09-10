package dev.kkorolyov.pancake.skillet.utility.ui;

import javafx.beans.property.ReadOnlyProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Provides methods for adding features to JavaFX UI elements.
 */
public final class UIDecorator {
	private UIDecorator() {}

	/**
	 * Attaches a tooltip to a node.
	 * @param tooltip attached tooltip
	 * @param node node to attach tooltip to
	 * @return {@code node} after tooltip attachment
	 */
	public static <T extends Node> T tooltip(String tooltip, T node) {
		Tooltip.install(node, new Tooltip(tooltip));
		return node;
	}

	/**
	 * Attaches a context menu to a node.
	 * @param contextMenuSupplier provides the context menu to attach
	 * @param node node to attach context menu to
	 * @return {@code node} after context menu attachment
	 */
	public static <T extends Node, C extends ContextMenu> T contextMenu(Supplier<C> contextMenuSupplier, T node) {
		node.setOnContextMenuRequested(e -> {
			contextMenuSupplier.get().show(node, e.getScreenX(), e.getScreenY());
			e.consume();
		});
		return node;
	}

	/**
	 * Sets the action handler of a button.
	 * @param handler attached action handler
	 * @param button button to set action handler on
	 * @return {@code button} after action handler attachment
	 */
	public static <T extends ButtonBase> T action(EventHandler<ActionEvent> handler, T button) {
		button.setOnAction(handler);
		return button;
	}
	/**
	 * Sets the action handler of a menu item.
	 * @param handler attached action handler
	 * @param menuItem menu item to set action handler on
	 * @return {@code menuItem} after action handler attachment
	 */
	public static <T extends MenuItem> T action(EventHandler<ActionEvent> handler, T menuItem) {
		menuItem.setOnAction(handler);
		return menuItem;
	}



	/**
	 * Sets a change listener on a property.
	 * @param listener attached change listener
	 * @param node node used as change listener target
	 * @param propertyFunction retrieves the property by applying a function on {@code node} on which to attach listener
	 * @return {@code node} after change listener attachment
	 */
	public static <V, T extends Node> T change(ThrowingChangeListener<? super V, T> listener, Function<T, ReadOnlyProperty<V>> propertyFunction, T node) {
		propertyFunction.apply(node).addListener((observable, oldValue, newValue) -> {
			try {
				listener.changed(node, oldValue, newValue);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		return node;
	}

	/**
	 * Sets the collapsible attribute on a titled pane.
	 * @param collapsible collapsible state
	 * @param titledPane titled pane to set attribute of
	 * @return {@code titledPane} after setting {@code collapsible}
	 */
	public static <T extends TitledPane> T collapsible(boolean collapsible, T titledPane) {
		titledPane.setCollapsible(collapsible);
		return titledPane;
	}

	@FunctionalInterface
	public interface ThrowingChangeListener<V, T> {
		void changed(T target, V oldValue, V newValue) throws Exception;
	}
}
