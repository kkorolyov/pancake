import dev.kkorolyov.pancake.core.registry.ActionStratDeferredConverterFactory;
import dev.kkorolyov.pancake.core.system.AccelerationSystem;
import dev.kkorolyov.pancake.core.system.ActionSystem;
import dev.kkorolyov.pancake.core.system.AnimationSystem;
import dev.kkorolyov.pancake.core.system.AudioSystem;
import dev.kkorolyov.pancake.core.system.CappingSystem;
import dev.kkorolyov.pancake.core.system.ChainSystem;
import dev.kkorolyov.pancake.core.system.CollisionSystem;
import dev.kkorolyov.pancake.core.system.DampingSystem;
import dev.kkorolyov.pancake.core.system.InputSystem;
import dev.kkorolyov.pancake.core.system.MovementSystem;
import dev.kkorolyov.pancake.core.system.RenderSystem;
import dev.kkorolyov.pancake.core.system.SpawnSystem;
import dev.kkorolyov.pancake.platform.plugin.GameSystem;
import dev.kkorolyov.pancake.platform.plugin.DeferredConverterFactory;

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

	provides GameSystem with
			ActionSystem,
			InputSystem,
			DampingSystem,
			AccelerationSystem,
			CappingSystem,
			MovementSystem,
			ChainSystem,
			CollisionSystem,
			SpawnSystem,
			AnimationSystem,
			RenderSystem,
			AudioSystem;
	provides DeferredConverterFactory.ActionStrat with ActionStratDeferredConverterFactory;
}
