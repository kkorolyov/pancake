package dev.kkorolyov.pancake.skillet.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Provides various convenience methods for manipulating abstract data.
 */
public final class Data {
	private Data() {}

	/** @return {@code true} if {@code s} is a valid number */
	public static boolean isNumber(String s) {
		return s.matches("[+-]?(\\d+\\.\\d+|\\d+)");
	}

	/**
	 * Clones an object by serializing and deserializing it.
	 * @param o object to clone
	 * @return clone of {@code o}
	 */
	public static <T extends Serializable> T serialClone(T o) {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		try (ObjectOutputStream oo = new ObjectOutputStream(bo)) {
			oo.writeObject(o);
			oo.close();

			try (ObjectInputStream oi = new ObjectInputStream(new ByteArrayInputStream(bo.toByteArray()))) {
				return (T) oi.readObject();
			}
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
