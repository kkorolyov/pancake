import dev.kkorolyov.pancake.platform.entity.ComponentConverter;
import dev.kkorolyov.pancake.platform.registry.ResourceConverterFactory;
import dev.kkorolyov.pancake.platform.registry.internal.ActionResourceConverterFactory;

module dev.kkorolyov.pancake.platform {
	// logging
	requires org.slf4j;
	// FIXME here so kotlin projects using this can use different format log configs
	// FIXME kotlin compiler cannot find this on module path
	requires com.fasterxml.jackson.dataformat.yaml;

	// resource parsing
	requires org.yaml.snakeyaml;
	// expose for resource mapping API
	requires transitive dev.kkorolyov.flub;

	exports dev.kkorolyov.pancake.platform;
	exports dev.kkorolyov.pancake.platform.action;
	exports dev.kkorolyov.pancake.platform.entity;
	exports dev.kkorolyov.pancake.platform.io;
	exports dev.kkorolyov.pancake.platform.math;
	exports dev.kkorolyov.pancake.platform.registry;
	exports dev.kkorolyov.pancake.platform.utility;

	uses ComponentConverter;
	uses ResourceConverterFactory;

	provides ResourceConverterFactory with ActionResourceConverterFactory;
}
