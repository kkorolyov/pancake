package dev.kkorolyov.pancake.input;

import dev.kkorolyov.pancake.entity.Action;

/*
 * TODO Implement this in a good way. 
 */
public abstract class InteractiveAction {
	public abstract Action onPress();
	public abstract Action onRelease();
	public abstract Action onHold(int minTime);
}
