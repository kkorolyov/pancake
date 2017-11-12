package dev.kkorolyov.pancake.skillet.decorator;

import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

/**
 * Decorates and alters JavaFX UI elements.
 */
public abstract class UIDecorator<O> {
	protected O object;

	/** @return decorator around {@code node} */
	public static <T extends Node, D extends NodeDecorator<T, D>> NodeDecorator<T, D> decorate(T node) {
		return new NodeDecorator<>(node);
	}
	/** @return decorator around {@code region} */
	public static <T extends Region, D extends RegionDecorator<T, D>> RegionDecorator<T, D> decorate(T region) {
		return new RegionDecorator<>(region);
	}
	/** @return decorator around {@code labeled} */
	public static <T extends Labeled, D extends LabeledDecorator<T, D>> LabeledDecorator<T, D> decorate(T labeled) {
		return new LabeledDecorator<>(labeled);
	}
	/** @return decorator around {@code button} */
	public static <T extends ButtonBase, D extends ButtonDecorator<T, D>> ButtonDecorator<T, D> decorate(T button) {
		return new ButtonDecorator<>(button);
	}

	/** @return decorator around {@code borderPane} */
	public static <T extends BorderPane, D extends BorderPaneDecorator<T, D>> BorderPaneDecorator<T, D> decorate(T borderPane) {
		return new BorderPaneDecorator<>(borderPane);
	}

	/** @return decorator around {@code scrollPane} */
	public static <T extends ScrollPane> ScrollPaneDecorator<T> decorate(T scrollPane) {
		return new ScrollPaneDecorator<>(scrollPane);
	}

	/** @return decorator around {@code titledPane} */
	public static <T extends TitledPane> TitledPaneDecorator<T> decorate(T titledPane) {
		return new TitledPaneDecorator<>(titledPane);
	}

	/** @return decorator around {@code spinner} */
	public static <T extends Spinner> SpinnerDecorator<T> decorate(T spinner) {
		return new SpinnerDecorator<>(spinner);
	}

	/** @return decorator around {@code contextMenu} */
	public static <T extends ContextMenu> ContextMenuDecorator<T> decorate(T contextMenu) {
		return new ContextMenuDecorator<>(contextMenu);
	}
	/** @return decorator around {@code menuItem} */
	public static <T extends MenuItem> MenuItemDecorator<T> decorate(T menuItem) {
		return new MenuItemDecorator<>(menuItem);
	}

	/** @return decorator around {@code tab} */
	public static <T extends Tab> TabDecorator<T> decorate(T tab) {
		return new TabDecorator<>(tab);
	}

	/**
	 * Constructs a new decorator.
	 * @param object decorated object
	 */
	protected UIDecorator(O object) {
		this.object = object;
	}

	/** @return decorated UI element */
	public O get() {
		return object;
	}

	/**
	 * Convenience interface for change listeners which throw checked exceptions.
	 */
	@FunctionalInterface
	public interface ThrowingChangeListener<V, T> {
		void changed(T target, V oldValue, V newValue) throws Exception;
	}
}
