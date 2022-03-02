import dev.kkorolyov.pancake.core.registry.ActionStratDeferredConverterFactory;
import dev.kkorolyov.pancake.platform.plugin.DeferredConverterFactory;

module dev.kkorolyov.pancake.core {
	requires org.slf4j;

	requires dev.kkorolyov.pancake.platform;

	exports dev.kkorolyov.pancake.core.action;
	exports dev.kkorolyov.pancake.core.component;
	exports dev.kkorolyov.pancake.core.component.event;
	exports dev.kkorolyov.pancake.core.component.movement;
	exports dev.kkorolyov.pancake.core.system;
	exports dev.kkorolyov.pancake.core.system.cleanup;

	provides DeferredConverterFactory.ActionStrat with ActionStratDeferredConverterFactory;
}
