module dev.kkorolyov.killstreek {
	requires kotlin.stdlib;

	requires javafx.graphics;
	requires javafx.media;

	requires simple.structs;

	requires dev.kkorolyov.pancake.core;
	requires dev.kkorolyov.pancake.platform;

	exports dev.kkorolyov.killstreek to javafx.graphics;
	exports dev.kkorolyov.killstreek.system to simple.files;

	provides dev.kkorolyov.pancake.platform.GameSystem with
			dev.kkorolyov.killstreek.system.DamageSystem;
}
