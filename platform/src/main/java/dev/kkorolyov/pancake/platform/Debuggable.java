package dev.kkorolyov.pancake.platform;

/**
 * Maintains information useful to tooling and debugging.
 */
public interface Debuggable {
	/**
	 * Returns a string uniquely identifying this construct.
	 * Defaults to the class simple name, or class full name if no simple name.
	 */
	default String getDebugName() {
		String simpleName = getClass().getSimpleName();
		return simpleName.isEmpty() ? getClass().getName() : simpleName;
	}
}
