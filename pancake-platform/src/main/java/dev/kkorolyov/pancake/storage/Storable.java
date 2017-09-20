package dev.kkorolyov.pancake.storage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * An easily-packaged, portable object.
 */
public abstract class Storable<T extends Storable<T>> implements Serializable {
	private final Collection<StorableListener<T>> listeners = new CopyOnWriteArrayList<>();

	/**
	 * Distributes a change event to all registered listeners.
	 * @param event specific change event
	 * @return number of listeners which received event
	 */
	protected int changed(StorableChangeEvent event) {
		for (StorableListener<T> listener : listeners) listener.changed((T) this, event);

		return listeners.size();
	}

	/**
	 * @param listener new listener
	 * @return {@code this}
	 */
	public T register(StorableListener<T> listener) {
		listeners.add(listener);
		return (T) this;
	}
	/**
	 * @param listener listener to remove
	 * @return {@code true} if {@code listener} was registered and is now removed
	 */
	public boolean unregister(StorableListener<T> listener) {
		if (!listeners.contains(listener)) return false;

		listeners.remove(listener);
		return true;
	}

	/**
	 * Clones this object by serializing and deserializing it.
	 * @return clone of {@code this}
	 */
	public T serialClone() {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		try (ObjectOutputStream oo = new ObjectOutputStream(bo)) {
			oo.writeObject(this);
			oo.close();

			try (ObjectInputStream oi = new ObjectInputStream(new ByteArrayInputStream(bo.toByteArray()))) {
				return (T) oi.readObject();
			}
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Denotes a type of change.
	 */
	public interface StorableChangeEvent {}
}
