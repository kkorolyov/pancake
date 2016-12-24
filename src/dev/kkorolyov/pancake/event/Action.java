package dev.kkorolyov.pancake.event;

/**
 * An action performed by an entity within a single step.
 */
public abstract class Action {
	//private Entity entity;
	
	public abstract void execute();
	public abstract void revert();
}
