module dev.kkorolyov.pancake.core {
	requires javafx.graphics;
	requires javafx.media;

	requires simple.funcs;
	requires simple.logs;
	requires simple.structs;

	requires dev.kkorolyov.pancake.platform;

	exports dev.kkorolyov.pancake.core.action;
	exports dev.kkorolyov.pancake.core.component;
	exports dev.kkorolyov.pancake.core.component.media;
	exports dev.kkorolyov.pancake.core.component.movement;
	exports dev.kkorolyov.pancake.core.event;
	exports dev.kkorolyov.pancake.core.system;

	exports dev.kkorolyov.pancake.core.serialization.string.action to simple.files;

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
	provides dev.kkorolyov.pancake.platform.serialization.string.action.ActionStringSerializer with
			dev.kkorolyov.pancake.core.serialization.string.action.ForceActionSerializer,
			dev.kkorolyov.pancake.core.serialization.string.action.TransformActionSerializer;
}
