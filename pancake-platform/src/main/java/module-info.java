module dev.kkorolyov.pancake.platform {
	requires javafx.media;

	requires simple.files;
	requires simple.funcs;
	requires simple.logs;
	requires simple.props;
	requires simple.structs;

	exports dev.kkorolyov.pancake.platform;
	exports dev.kkorolyov.pancake.platform.action;
	exports dev.kkorolyov.pancake.platform.entity;
	exports dev.kkorolyov.pancake.platform.event;
	exports dev.kkorolyov.pancake.platform.event.management;
	exports dev.kkorolyov.pancake.platform.math;
	exports dev.kkorolyov.pancake.platform.media;
	exports dev.kkorolyov.pancake.platform.media.audio;
	exports dev.kkorolyov.pancake.platform.media.graphic;
	exports dev.kkorolyov.pancake.platform.media.graphic.shape;
	exports dev.kkorolyov.pancake.platform.serialization;
	exports dev.kkorolyov.pancake.platform.serialization.string;
	exports dev.kkorolyov.pancake.platform.utility;

	// System
	uses dev.kkorolyov.pancake.platform.GameSystem;
	// Media factories
	uses dev.kkorolyov.pancake.platform.media.audio.AudioFactory;
	// Resource reader factories
	uses dev.kkorolyov.pancake.platform.registry.properties.ResourceReaderFactory.ActionResource;
	uses dev.kkorolyov.pancake.platform.registry.properties.ResourceReaderFactory.AudioResource;
	uses dev.kkorolyov.pancake.platform.registry.properties.ResourceReaderFactory.RenderableResource;

	provides dev.kkorolyov.pancake.platform.registry.properties.ResourceReaderFactory.ActionResource with dev.kkorolyov.pancake.platform.registry.properties.ActionResourceReaderFactory;
	provides dev.kkorolyov.pancake.platform.registry.properties.ResourceReaderFactory.AudioResource with dev.kkorolyov.pancake.platform.registry.properties.AudioResourceReaderFactory;
	provides dev.kkorolyov.pancake.platform.registry.properties.ResourceReaderFactory.RenderableResource with dev.kkorolyov.pancake.platform.registry.properties.RenderableResourceReaderFactory;
}
