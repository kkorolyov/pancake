module dev.kkorolyov.pancake.platform {
	requires org.slf4j;

	requires dev.kkorolyov.flopple;

	exports dev.kkorolyov.pancake.platform;
	exports dev.kkorolyov.pancake.platform.action;
	exports dev.kkorolyov.pancake.platform.application;
	exports dev.kkorolyov.pancake.platform.entity;
	exports dev.kkorolyov.pancake.platform.event;
	exports dev.kkorolyov.pancake.platform.math;
	exports dev.kkorolyov.pancake.platform.media;
	exports dev.kkorolyov.pancake.platform.media.audio;
	exports dev.kkorolyov.pancake.platform.media.graphic;
	exports dev.kkorolyov.pancake.platform.media.graphic.shape;
	exports dev.kkorolyov.pancake.platform.registry;
	exports dev.kkorolyov.pancake.platform.utility;

	uses dev.kkorolyov.pancake.platform.GameSystem;
	// application and media factories
	uses dev.kkorolyov.pancake.platform.application.Application;
	uses dev.kkorolyov.pancake.platform.media.audio.AudioFactory;
	uses dev.kkorolyov.pancake.platform.media.graphic.RenderMedium;
	// resource reader factories
	uses dev.kkorolyov.pancake.platform.registry.ResourceReaderFactory.ActionResource;
	uses dev.kkorolyov.pancake.platform.registry.ResourceReaderFactory.AudioResource;
	uses dev.kkorolyov.pancake.platform.registry.ResourceReaderFactory.RenderableResource;

	provides dev.kkorolyov.pancake.platform.registry.ResourceReaderFactory.ActionResource with dev.kkorolyov.pancake.platform.registry.internal.ActionResourceReaderFactory;
	provides dev.kkorolyov.pancake.platform.registry.ResourceReaderFactory.AudioResource with dev.kkorolyov.pancake.platform.registry.internal.AudioResourceReaderFactory;
	provides dev.kkorolyov.pancake.platform.registry.ResourceReaderFactory.RenderableResource with dev.kkorolyov.pancake.platform.registry.internal.RenderableResourceReaderFactory;
}
