package dev.kkorolyov.pancake.skillet.uibuilder;

import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

/**
 * Convenience methods for building JavaFX TextFields and TextAreas.
 */
public final class TextBuilder {
	private TextBuilder() {}

	public static TextField buildTextField(String value, ThrowingChangeListener<? super String> listener) {
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

	public interface ThrowingChangeListener<T> {
		void changedThrows(TextInputControl target, T oldValue, T newValue) throws Exception;
	}
}
