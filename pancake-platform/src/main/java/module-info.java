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
	exports dev.kkorolyov.pancake.platform.serialization.string.action;
	exports dev.kkorolyov.pancake.platform.utility;

	// System
	uses dev.kkorolyov.pancake.platform.GameSystem;
	// Media factories
	uses dev.kkorolyov.pancake.platform.media.audio.AudioFactory;
	// Serializers
	uses dev.kkorolyov.pancake.platform.serialization.string.StringSerializer;
	uses dev.kkorolyov.pancake.platform.serialization.string.action.ActionStringSerializer;

	provides dev.kkorolyov.pancake.platform.serialization.string.StringSerializer with
			dev.kkorolyov.pancake.platform.serialization.string.VectorStringSerializer,
			dev.kkorolyov.pancake.platform.serialization.string.NumberStringSerializer,
			dev.kkorolyov.pancake.platform.serialization.string.StringStringSerializer,
			dev.kkorolyov.pancake.platform.serialization.string.URIStringSerializer,
			dev.kkorolyov.pancake.platform.serialization.string.MapStringSerializer;
	provides dev.kkorolyov.pancake.platform.serialization.string.action.ActionStringSerializer with
			dev.kkorolyov.pancake.platform.serialization.string.action.MultiStageActionStringSerializer,
			dev.kkorolyov.pancake.platform.serialization.string.action.CollectiveActionStringSerializer,
			dev.kkorolyov.pancake.platform.serialization.string.action.ActionReferenceStringSerializer;
}
