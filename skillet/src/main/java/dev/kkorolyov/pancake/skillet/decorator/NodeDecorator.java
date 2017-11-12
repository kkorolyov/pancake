package dev.kkorolyov.pancake.skillet.decorator;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.ScrollEvent;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Decorates {@link Node}s.
 */
public class NodeDecorator<T extends Node, D extends NodeDecorator<T, D>> extends UIDecorator<T> {
	protected NodeDecorator(T object) {
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
	 * Adds styling classes.
	 * @param classes styling classes to add
	 * @return {@code this}
	 */
	public D styleClass(String... classes) {
		object.getStyleClass().addAll(classes);
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
		return press(procedure, Arrays.stream(keys)
				.map(key -> new KeyCodeCombination(key))
				.toArray(KeyCombination[]::new));
	}
	/**
	 * Sets a key press procedure.
	 * @param procedure attached press procedure
	 * @param keyCombinations set of key combinations from which any individual combination can invoke {@code procedure}
	 * @return {@code this}
	 */
	public D press(Runnable procedure, KeyCombination... keyCombinations) {
		object.setOnKeyPressed(e -> {
			if (Arrays.stream(keyCombinations).anyMatch(keyCombination -> keyCombination.match(e))) procedure.run();
		});
		return (D) this;
	}

	/**
	 * Sets multiple key press procedures.
	 * @param keyProcedures map of key combinations to the procedures they invoke
	 * @return {@code this}
	 */
	public D press(Map<KeyCombination, Runnable> keyProcedures) {
		object.setOnKeyPressed(e -> {
			keyProcedures.entrySet().stream()
					.filter(entry -> entry.getKey().match(e))
					.findFirst()
					.ifPresent(entry -> entry.getValue().run());
		});
		return (D) this;
	}

	/**
	 * Sets a scroll event procedure
	 * @param eventHandler scroll event handler
	 * @return {@code this}
	 */
	public D scroll(EventHandler<? super ScrollEvent> eventHandler) {
		object.setOnScroll(eventHandler);
		return (D) this;
	}

	/**
	 * Sets a property value.
	 * @param propertyFunction retrieves the property from the decorated node
	 * @param value new property value
	 * @return {@code this}
	 */
	public <V> D property(Function<T, ObjectProperty<V>> propertyFunction, V value) {
			propertyFunction.apply(object).setValue(value);
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
	public <C extends ContextMenu> D contextMenu(Supplier<C> contextMenuSupplier) {
		object.setOnContextMenuRequested(e -> {
			contextMenuSupplier.get().show(object, e.getScreenX(), e.getScreenY());
			e.consume();
		});
		return (D) this;
	}

	/**
	 * Applies a binding on a property.
	 * @param propertyFunction retrieves the property from the decorated node
	 * @param bound property to bind
	 * @return {@code this}
	 */
	public <V> D bind(Function<T, Property<V>> propertyFunction, ObservableValue<V> bound) {
		propertyFunction.apply(object).bind(bound);
		return (D) this;
	}
}
