import dev.kkorolyov.pancake.graphics.io.ComponentStructizer;
import dev.kkorolyov.pancake.platform.io.Structizer;

module dev.kkorolyov.pancake.graphics {
	requires kotlin.stdlib;

	requires dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.core;

	exports dev.kkorolyov.pancake.graphics;
	exports dev.kkorolyov.pancake.graphics.util;
	exports dev.kkorolyov.pancake.graphics.resource;
	exports dev.kkorolyov.pancake.graphics.component;
	exports dev.kkorolyov.pancake.graphics.system;

	provides Structizer with
			ComponentStructizer;
}
