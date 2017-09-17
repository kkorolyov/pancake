package dev.kkorolyov.pancake.skillet.utility.decorator;

import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.Region;

/**
 * Provides methods for adding features to JavaFX UI elements.
 */
public abstract class UIDecorator<O> {
	O object;

	/** @return decorator around {@code node} */
	public static <T extends Node, D extends NodeDecorator<T, D>> NodeDecorator<T, D> decorate(T node) {
		return new NodeDecorator<>(node);
	}
	/** @return decorator around {@code region} */
	public static <T extends Region, D extends RegionDecorator<T, D>> RegionDecorator<T, D> decorate(T region) {
		return new RegionDecorator<>(region);
	}
	/** @return decorator around {@code scrollPane} */
	public static <T extends ScrollPane> ScrollPaneDecorator<T> decorate(T scrollPane) {
		return new ScrollPaneDecorator<>(scrollPane);
	}
	/** @return decorator around {@code button} */
	public static <T extends ButtonBase, D extends ButtonDecorator<T, D>> ButtonDecorator<T, D> decorate(T button) {
		return new ButtonDecorator<>(button);
	}
	/** @return decorator around {@code menuItem} */
	public static <T extends MenuItem> MenuItemDecorator<T> decorate(T menuItem) {
		return new MenuItemDecorator<>(menuItem);
	}
	/** @return decorator around {@code tab} */
	public static <T extends Tab> TabDecorator<T> decorate(T tab) {
		return new TabDecorator<>(tab);
	}

	UIDecorator(O object) {
		this.object = object;
	}

	/** @return decorated UI object */
	public O get() {
		return object;
	}

	@FunctionalInterface
	public interface ThrowingChangeListener<V, T> {
		void changed(T target, V oldValue, V newValue) throws Exception;
	}
}
