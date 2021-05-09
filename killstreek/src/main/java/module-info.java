import dev.kkorolyov.pancake.platform.plugin.GameSystem;

module dev.kkorolyov.killstreek {
	requires kotlin.stdlib;

	requires org.slf4j;
	requires org.apache.logging.log4j;
	requires com.fasterxml.jackson.dataformat.yaml;

	requires dev.kkorolyov.pancake.core;
	requires dev.kkorolyov.pancake.platform;

	provides GameSystem with
			dev.kkorolyov.killstreek.system.DamageSystem,
			dev.kkorolyov.killstreek.system.GcSystem;
}
