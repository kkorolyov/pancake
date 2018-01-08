package dev.kkorolyov.pancake.platform

import dev.kkorolyov.pancake.platform.math.Vector
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton

import java.lang.reflect.Field
import java.lang.reflect.Modifier

/**
 * Provides utility methods for Specs.
 */
class SpecUtilities {
	private static final Random rand = new Random()

	/**
	 * Reflectively gets a field's value.
	 * @see #getField(java.lang.String, java.lang.Class, java.lang.Object)
	 */
	static Object getField(String field, Object object) {
		return getField(field, object.class, object)
	}
	/**
	 * Reflectively gets a static field's value.
	 * @see #getField(java.lang.String, java.lang.Class, java.lang.Object)
	 */
	static Object getField(String field, Class<?> c) {
		return getField(field, c, null)
	}
	/**
	 * Reflectively gets a field's value.
	 * @param field field name
	 * @param c declaring class
	 * @param object field owner
	 * @return {@code object's} value for {@code field} defined by class {@code c}
	 */
	static Object getField(String field, Class<?> c, Object object) {
		Field f = c.getDeclaredField(field)
		f.setAccessible(true)

		return f.get(object)
	}

	/**
	 * Reflectively sets a field's value
	 * @see #setField(java.lang.String, java.lang.Class, java.lang.Object, java.lang.Object)
	 */
	static void setField(String field, Object object, Object value) {
		setField(field, object.class, object, value)
	}
	/**
	 * Reflectively sets a static field's value.
	 * @see #setField(java.lang.String, java.lang.Class, java.lang.Object, java.lang.Object)
	 */
	static void setField(String field, Class<?> c, Object value) {
		setField(field, c, null, value)
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

		unfinalize(f).set(object, value)
	}
	private static Field unfinalize(Field f) {
		Field modifiers = Field.class.getDeclaredField("modifiers")
		modifiers.setAccessible(true)
		modifiers.setInt(f, f.modifiers & ~Modifier.FINAL)

		return f;
	}

	/**
	 * Wraps a float in a {@code BigDecimal}.
	 * @param value float value to wrap
	 * @return {@code value} as a {@code BigDecimal}
	 */
	static BigDecimal bigDecimal(float value) {
		return BigDecimal.valueOf(value)
	}

	/** @return random int */
	static int randInt() {
		return rand.nextInt()
	}
	/** @return random long */
	static long randLong() {
		return rand.nextLong()
	}
	/** @return random float between {@code 0} and {@code MAX_INT} */
	static float randFloat() {
		return rand.nextInt(Integer.MAX_VALUE) * rand.nextFloat()
	}
	/**
	 * @return random String using UUID
	 * @param delimiter optional delimiter between UUID segments
	 */
	static String randString(String delimiter = "") {
		return UUID.randomUUID().toString().replaceAll("-", delimiter)
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
