package dev.kkorolyov.pancake.entity;

import dev.kkorolyov.pancake.input.InputPoller;

/**
 * An entity controller responding to manual keyboard and mouse input.
 */
public class InteractiveEntityController implements EntityController {
	private InputPoller input;
	
	public InteractiveEntityController(InputPoller input) {
		this.input = input;
	}
	
	@Override
	public void update(Entity entity) {
		for (Action action : input.poll())
			action.execute(entity);
	}

}
