package dev.kkorolyov.pancake.skillet.utility.ui;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Region;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

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

	private UIDecorator(O object) {
		this.object = object;
	}

	/** @return decorated UI object */
	public O get() {
		return object;
	}

	/**
	 * Wraps content in a scroll pane.
	 * @param content wrapped node
	 * @return wrapping scroll pane
	 */
	public static ScrollPane scroll(Node content) {
		ScrollPane scrollPane = new ScrollPane(content);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);
		scrollPane.setMinSize(0, 0);

		return scrollPane;
	}

	@FunctionalInterface
	public interface ThrowingChangeListener<V, T> {
		void changed(T target, V oldValue, V newValue) throws Exception;
	}

	/**
	 * Decorates {@link Node}s.
	 */
	public static class NodeDecorator<T extends Node, D extends NodeDecorator<T, D>> extends UIDecorator<T> {
		private NodeDecorator(T object) {
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
		 * @param listener attached change listener
		 * @param propertyFunction retrieves the property from the decorated node
		 * @return {@code this}
		 */
		public <V> D change(ThrowingChangeListener<? super V, T> listener, Function<T, ObservableValue<V>> propertyFunction) {
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

	public static class RegionDecorator<T extends Region, D extends RegionDecorator<T, D>> extends NodeDecorator<T, D> {
		private RegionDecorator(T object) {
			super(object);
		}

		/**
		 * Sets a minimum size.
		 * @param minWidth minimum width, or {@code null}
		 * @param minHeight minimum height, or {@code null}
		 * @return {@code this}
		 */
		public D minSize(Double minWidth, Double minHeight) {
			if (minWidth != null) object.setMinWidth(minWidth);
			if (minHeight != null) object.setMinHeight(minHeight);

			return (D) this;
		}
		/**
		 * Sets a maximum size.
		 * @param maxWidth maximum width, or {@code null}
		 * @param maxHeight maximum height, or {@code null}
		 * @return {@code this}
		 */
		public D maxSize(Double maxWidth, Double maxHeight) {
			if (maxWidth != null) object.setMaxWidth(maxWidth);
			if (maxHeight != null) object.setMaxHeight(maxHeight);

			return (D) this;
		}
	}

	/**
	 * Decorates {@link ButtonBase} implementors.
	 */
	public static class ButtonDecorator<T extends ButtonBase, D extends ButtonDecorator<T, D>> extends RegionDecorator<T, D> {
		private ButtonDecorator(T object) {
			super(object);
		}

		/**
		 * Sets an action procedure.
		 * @param procedure attached action procedure
		 * @return {@code this}
		 */
		public D action(Runnable procedure) {
			object.setOnAction(e -> procedure.run());
			return (D) this;
		}
	}

	public static class MenuItemDecorator<T extends MenuItem> extends UIDecorator<T> {
		private MenuItemDecorator(T object) {
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

	public static class TabDecorator<T extends Tab> extends UIDecorator<T> {
		private TabDecorator(T object) {
			super(object);
		}

		/**
		 * Sets a change listener on a property.
		 * @param listener attached change listener
		 * @param propertyFunction retrieves the property from the decorated tab
		 * @return {@code this}
		 */
		public <V> TabDecorator<T> change(ThrowingChangeListener<? super V, T> listener, Function<T, ObservableValue<V>> propertyFunction) {
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
}
