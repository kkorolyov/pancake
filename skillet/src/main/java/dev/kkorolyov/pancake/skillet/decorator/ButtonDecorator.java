package dev.kkorolyov.pancake.skillet.decorator;

import javafx.scene.control.ButtonBase;

/**
 * Decorates {@link ButtonBase} implementors.
 */
public class ButtonDecorator<T extends ButtonBase, D extends ButtonDecorator<T, D>> extends LabeledDecorator<T, D> {
	protected ButtonDecorator(T object) {
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
