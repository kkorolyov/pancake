package dev.kkorolyov.pancake.skillet.utility.decorator;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Decorates {@link Node}s.
 */
public class NodeDecorator<T extends Node, D extends NodeDecorator<T, D>> extends UIDecorator<T> {
	NodeDecorator(T object) {
		super(object);
	}

	/**
	 * Sets an ID.
	 * @param id ID to set
	 * @return {@code this}
	 */
	public D id(String id) {
		object.setId(id);
		return (D) this;
	}
	/**
	 * Adds a styling class.
	 * @param styleClass class to add
	 * @return {@code this}
	 */
	public D styleClass(String styleClass) {
		object.getStyleClass().add(styleClass);
		return (D) this;
	}

	/**
	 * Attaches a tooltip.
	 * @param tooltip attached tooltip
	 * @return {@code this}
	 */
	public D tooltip(String tooltip) {
		Tooltip.install(object, new Tooltip(tooltip));
		return (D) this;
	}

	/**
	 * Sets a mouse clicked procedure.
	 * @param procedure attached click procedure
	 * @param clicks number of clicks required to invoke {@code procedure}
	 * @return {@code this}
	 */
	public D click(Runnable procedure, int clicks) {
		object.setOnMouseClicked(e -> {
			if (e.getClickCount() == clicks) procedure.run();
		});
		return (D) this;
	}

	/**
	 * Sets a key press procedure.
	 * @param procedure attached press procedure
	 * @param keys set of keys from which any individual key can invoke {@code procedure}
	 * @return {@code this}
	 */
	public D press(Runnable procedure, KeyCode... keys) {
		object.setOnKeyPressed(e -> {
			if (Arrays.stream(keys).anyMatch(keyCode -> e.getCode() == keyCode)) procedure.run();
		});
		return (D) this;
	}
	/**
	 * Sets a key press procedure.
	 * @param procedure attached press procedure
	 * @param keyCombination key combination required to invoke {@code procedure}
	 * @return {@code this}
	 */
	public D press(Runnable procedure, KeyCombination keyCombination) {
		object.setOnKeyPressed(e -> {
			if (keyCombination.match(e)) procedure.run();
		});
		return (D) this;
	}

	/**
	 * Sets a change listener on a property.
	 * @param propertyFunction retrieves the property from the decorated node
	 * @param listener attached change listener
	 * @return {@code this}
	 */
	public <V> D change(Function<T, ObservableValue<V>> propertyFunction, ThrowingChangeListener<? super V, T> listener) {
		propertyFunction.apply(object).addListener((observable, oldValue, newValue) -> {
			try {
				listener.changed(object, oldValue, newValue);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		return (D) this;
	}

	/**
	 * Attaches a context menu.
	 * @param contextMenuSupplier provides the context menu to attach
	 * @return {@code this}
	 */
	public <C extends ContextMenu> D contextMenu(Supplier<C> contextMenuSupplier, T node) {
		node.setOnContextMenuRequested(e -> {
			contextMenuSupplier.get().show(node, e.getScreenX(), e.getScreenY());
			e.consume();
		});
		return (D) this;
	}
}
