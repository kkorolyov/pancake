package dev.kkorolyov.pancake.platform;

/**
 * Maintains information useful to tooling and debugging.
 */
public interface Debuggable {
	/**
	 * Returns a string uniquely identifying this construct in a debug context.
	 */
	String getDebugName();
}
