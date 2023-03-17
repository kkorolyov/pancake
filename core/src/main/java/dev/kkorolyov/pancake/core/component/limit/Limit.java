package dev.kkorolyov.pancake.core.component.limit;

import dev.kkorolyov.pancake.platform.entity.Component;

/**
 * Imposes some limit on components of type {@link T}.
 */
public interface Limit<T extends Component> extends Component {
	/**
	 * Optionally modifies {@code component} to adhere to this component's configured limits.
	 */
	void limit(T component);
}
