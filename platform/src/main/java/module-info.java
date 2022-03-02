import dev.kkorolyov.pancake.platform.plugin.DeferredConverterFactory;
import dev.kkorolyov.pancake.platform.registry.internal.ActionStratDeferredConverterFactory;
import dev.kkorolyov.pancake.platform.registry.internal.VectorStratDeferredConverterFactory;

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
	exports dev.kkorolyov.pancake.platform.math;
	exports dev.kkorolyov.pancake.platform.registry;
	exports dev.kkorolyov.pancake.platform.utility;
	exports dev.kkorolyov.pancake.platform.plugin;

	// resource reader factories
	uses DeferredConverterFactory.VectorStrat;
	uses DeferredConverterFactory.ActionStrat;

	provides DeferredConverterFactory.VectorStrat with VectorStratDeferredConverterFactory;
	provides DeferredConverterFactory.ActionStrat with ActionStratDeferredConverterFactory;
}
