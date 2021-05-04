import dev.kkorolyov.pancake.platform.registry.DeferredConverterFactory;
import dev.kkorolyov.pancake.platform.registry.internal.ActionStratDeferredConverterFactory;
import dev.kkorolyov.pancake.platform.registry.internal.AudioStratDeferredConverterFactory;
import dev.kkorolyov.pancake.platform.registry.internal.RenderableStratDeferredConverterFactory;
import dev.kkorolyov.pancake.platform.registry.internal.VectorStratDeferredConverterFactory;
import dev.kkorolyov.pancake.platform.service.Application;
import dev.kkorolyov.pancake.platform.service.AudioFactory;
import dev.kkorolyov.pancake.platform.service.RenderMedium;

module dev.kkorolyov.pancake.platform {
	// logging
	requires org.slf4j;

	// resource parsing
	requires org.yaml.snakeyaml;
	// expose for resource mapping API
	requires transitive dev.kkorolyov.flopple;

	exports dev.kkorolyov.pancake.platform;
	exports dev.kkorolyov.pancake.platform.action;
	exports dev.kkorolyov.pancake.platform.entity;
	exports dev.kkorolyov.pancake.platform.event;
	exports dev.kkorolyov.pancake.platform.math;
	exports dev.kkorolyov.pancake.platform.media;
	exports dev.kkorolyov.pancake.platform.media.audio;
	exports dev.kkorolyov.pancake.platform.media.graphic;
	exports dev.kkorolyov.pancake.platform.media.graphic.shape;
	exports dev.kkorolyov.pancake.platform.registry;
	exports dev.kkorolyov.pancake.platform.utility;
	exports dev.kkorolyov.pancake.platform.service;

	uses dev.kkorolyov.pancake.platform.GameSystem;
	// application and media factories
	uses Application;
	uses AudioFactory;
	uses RenderMedium;
	// resource reader factories
	uses DeferredConverterFactory.VectorStrat;
	uses DeferredConverterFactory.ActionStrat;
	uses DeferredConverterFactory.AudioStrat;
	uses DeferredConverterFactory.RenderableStrat;

	provides DeferredConverterFactory.VectorStrat with VectorStratDeferredConverterFactory;
	provides DeferredConverterFactory.ActionStrat with ActionStratDeferredConverterFactory;
	provides DeferredConverterFactory.AudioStrat with AudioStratDeferredConverterFactory;
	provides DeferredConverterFactory.RenderableStrat with RenderableStratDeferredConverterFactory;
}
