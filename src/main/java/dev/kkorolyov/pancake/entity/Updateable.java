package dev.kkorolyov.pancake.entity;

/**
 * Provides for regular updates.
 */
public interface Updateable {
	/**
	 * Updates this object.
	 * @param dt seconds elapsed since last update
	 */
	void update(float dt);
}
