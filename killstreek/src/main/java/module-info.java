module dev.kkorolyov.killstreek {
	requires kotlin.stdlib;

	requires simple.funcs;
	requires simple.structs;
	requires simple.props;

	requires dev.kkorolyov.pancake.core;
	requires dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.application.javafx;
	requires dev.kkorolyov.pancake.audio.javafx;

	// Make services visible to Providers
	exports dev.kkorolyov.killstreek.system to simple.files;

	provides dev.kkorolyov.pancake.platform.GameSystem with
			dev.kkorolyov.killstreek.system.DamageSystem,
			dev.kkorolyov.killstreek.system.GcSystem;
}
