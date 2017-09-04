package dev.kkorolyov.pancake.skillet.utility.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import java.util.function.Supplier;

/**
 * Provides convenience methods for building JavaFX UI elements.
 */
public final class UIBuilder {
	private UIBuilder() {}

	public static Label buildLabel(String title) {
		return buildLabel(title, null);
	}
	public static Label buildLabel(String title, String tooltip) {
		Label label = new Label(title);
		if (tooltip != null) label.setTooltip(new Tooltip(tooltip));

		return label;
	}

	public static Button buildButton(String title, EventHandler<ActionEvent> handler) {
		return buildButton(title, null, handler);
	}
	public static Button buildButton(String title, String tooltip, EventHandler<ActionEvent> handler) {
		Button button = new Button(title);
		if (tooltip != null) button.setTooltip(new Tooltip(tooltip));
		button.setOnAction(handler);

		return button;
	}

	public static TextField buildTextField(String value, ThrowingChangeListener<? super String, TextField> listener) {
		TextField textField = new TextField(value);
		textField.textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				listener.changedThrows(textField, oldValue, newValue);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		return textField;
	}

	public static TitledPane buildTitledPane(String title, Node content) {
		return buildTitledPane(title, null, content);
	}
	public static TitledPane buildTitledPane(String title, String tooltip, Node content) {
		return buildTitledPane(title, tooltip, content, true);
	}
	public static TitledPane buildTitledPane(String title, String tooltip, Node content, boolean collapsible) {
		TitledPane titledPane = new TitledPane(title, content);
		if (tooltip != null) titledPane.setTooltip(new Tooltip(tooltip));
		titledPane.setCollapsible(collapsible);

		return titledPane;
	}

	public static MenuItem buildMenuItem(String title, EventHandler<ActionEvent> handler) {
		MenuItem menuItem = new MenuItem(title);
		menuItem.setOnAction(handler);

		return menuItem;
	}

	public static <T extends Node, C extends ContextMenu> T attachContextMenu(T node, Supplier<C> contextMenuSupplier) {
		node.setOnContextMenuRequested(e -> {
			contextMenuSupplier.get().show(node, e.getScreenX(), e.getScreenY());
			e.consume();
		});
		return node;
	}

	public interface ThrowingChangeListener<V, T> {
		void changedThrows(T target, V oldValue, V newValue) throws Exception;
	}
}
