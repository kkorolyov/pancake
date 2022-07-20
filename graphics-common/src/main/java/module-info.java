module dev.kkorolyov.pancake.graphics.common {
	requires kotlin.stdlib.jdk8;

	requires dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.core;

	exports dev.kkorolyov.pancake.graphics.common;
	exports dev.kkorolyov.pancake.graphics.common.component;
	exports dev.kkorolyov.pancake.graphics.common.system;
}
