package dev.kkorolyov.pancake

import dev.kkorolyov.pancake.math.Vector
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton

import java.lang.reflect.Field

/**
 * Provides utility methods for Specs.
 */
class SpecUtilities {
	private static final Random rand = new Random()

	/**
	 * Reflectively gets a static field's value.
	 * @param field field name
	 * @param c static field owner
	 * @return class {@code c's} value for {@code field}
	 */
	static Object getField(String field, Class<?> c) {
		Field f = c.getDeclaredField(field)
		f.setAccessible(true)

		return f.get(null)
	}
	/**
	 * Reflectively gets a field's value.
	 * @param field field name
	 * @param object field owner
	 * @return {@code object's} value for {@code field}
	 */
	static Object getField(String field, Object object) {
		Field f = object.class.getDeclaredField(field)
		f.setAccessible(true)

		return f.get(object)
	}

	/**
	 * Reflectively sets a field's value
	 * @param field field name
	 * @param object field owner
	 * @param value new field value
	 */
	static void setField(String field, Object object, Object value) {
		setField(field, object.class, object, value)
	}
	/**
	 * Reflectively sets a field's value
	 * @param field field name
	 * @param c declaring class
	 * @param object field owner
	 * @param value new field value
	 */
	static void setField(String field, Class<?> c, Object object, Object value) {
		Field f = c.getDeclaredField(field)
		f.setAccessible(true)

		f.set(object, value)
	}

	/** @return random float between {@code 0} and {@code MAX_INT} */
	static float randFloat() {
		return rand.nextInt(Integer.MAX_VALUE) * rand.nextFloat()
	}
	/** @return random long */
	static long randLong() {
		return rand.nextLong()
	}

	/** @return random KeyCode */
	static KeyCode randKeyCode() {
		return KeyCode.values()[rand.nextInt(KeyCode.values().length)]
	}
	/** @return random MouseButton */
	static MouseButton randMouseButton() {
		return MouseButton.values()[rand.nextInt(MouseButton.values().length)]
	}

	/** @return vector with randomized component values */
	static Vector randVector() {
		return new Vector(randFloat(), randFloat(), randFloat())
	}
}
