package dev.kkorolyov.pancake.event;

/**
 * A command applicable on an entity.
 */
public interface Command {
	void execute(Entity entity);
	void revert(Entity entity);
}
