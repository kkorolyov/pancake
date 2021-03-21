import dev.kkorolyov.pancake.core.registry.ActionStratDeferredConverterFactory;
import dev.kkorolyov.pancake.platform.registry.DeferredConverterFactory;

module dev.kkorolyov.pancake.core {
	requires org.slf4j;
	requires org.yaml.snakeyaml;

	requires dev.kkorolyov.pancake.platform;

	exports dev.kkorolyov.pancake.core.action;
	exports dev.kkorolyov.pancake.core.component;
	exports dev.kkorolyov.pancake.core.component.media;
	exports dev.kkorolyov.pancake.core.component.movement;
	exports dev.kkorolyov.pancake.core.event;
	exports dev.kkorolyov.pancake.core.input;
	exports dev.kkorolyov.pancake.core.system;

	provides dev.kkorolyov.pancake.platform.GameSystem with
			dev.kkorolyov.pancake.core.system.ActionSystem,
			dev.kkorolyov.pancake.core.system.InputSystem,
			dev.kkorolyov.pancake.core.system.DampingSystem,
			dev.kkorolyov.pancake.core.system.AccelerationSystem,
			dev.kkorolyov.pancake.core.system.MovementSystem,
			dev.kkorolyov.pancake.core.system.ChainSystem,
			dev.kkorolyov.pancake.core.system.CollisionSystem,
			dev.kkorolyov.pancake.core.system.SpawnSystem,
			dev.kkorolyov.pancake.core.system.AnimationSystem,
			dev.kkorolyov.pancake.core.system.RenderSystem,
			dev.kkorolyov.pancake.core.system.AudioSystem;
	provides DeferredConverterFactory.ActionStrat with ActionStratDeferredConverterFactory;
}
