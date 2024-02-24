package dev.kkorolyov.pancake.platform.entity;

import dev.kkorolyov.pancake.platform.Debuggable;

/**
 * A container of entity data.
 */
public interface Component extends Debuggable {
	/**
	 * Returns the simple name of this object's class.
	 * If it has no simple name - e.g. anonymous class - returns the class full name.
	 */
	@Override
	default String getDebugName() {
		String simpleName = getClass().getSimpleName();
		return simpleName.isEmpty() ? getClass().getName() : simpleName;
	}
}
