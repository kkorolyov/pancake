import dev.kkorolyov.pancake.core.registry.ActionResourceConverterFactory;
import dev.kkorolyov.pancake.platform.registry.ResourceConverterFactory;

module dev.kkorolyov.pancake.core {
	requires org.slf4j;

	requires dev.kkorolyov.pancake.platform;

	exports dev.kkorolyov.pancake.core.component;
	exports dev.kkorolyov.pancake.core.component.event;
	exports dev.kkorolyov.pancake.core.component.limit;
	exports dev.kkorolyov.pancake.core.component.movement;
	exports dev.kkorolyov.pancake.core.component.tag;
	exports dev.kkorolyov.pancake.core.system;
	exports dev.kkorolyov.pancake.core.system.cleanup;
	exports dev.kkorolyov.pancake.core.system.limit;

	provides ResourceConverterFactory with ActionResourceConverterFactory;
}
