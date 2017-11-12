package dev.kkorolyov.pancake.skillet.decorator;

import javafx.scene.layout.Region;

/**
 * Decorates {@link Region}s.
 */
public class RegionDecorator<T extends Region, D extends RegionDecorator<T, D>> extends NodeDecorator<T, D> {
	protected RegionDecorator(T object) {
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

	/**
	 * Sets a preferred size.
	 * @param prefWidth preferred width, or {@code null}
	 * @param prefHeight preferred height, or {@code null}
	 * @return {@code this}
	 */
	public D prefSize(Double prefWidth, Double prefHeight) {
		if (prefWidth != null) object.setPrefWidth(prefWidth);
		if (prefHeight != null) object.setPrefHeight(prefHeight);

		return (D) this;
	}
}
