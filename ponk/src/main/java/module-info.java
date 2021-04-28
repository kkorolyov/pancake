import dev.kkorolyov.pancake.platform.GameSystem;

module dev.kkorolyov.ponk {
	requires kotlin.stdlib;

	// logging
	requires org.slf4j;
	requires org.apache.logging.log4j;
	requires com.fasterxml.jackson.dataformat.yaml;

	requires dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.core;

	provides GameSystem with dev.kkorolyov.ponk.system.ScoreSystem;
}
