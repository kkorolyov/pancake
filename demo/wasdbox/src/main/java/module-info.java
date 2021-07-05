module dev.kkorolyov.pancake.demo.wasdbox {
	requires kotlin.stdlib;

	requires javafx.graphics;
	requires javafx.media;
	requires javafx.controls;

	requires org.slf4j;
	requires org.apache.logging.log4j;
	requires org.yaml.snakeyaml;

	requires dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.core;
	requires dev.kkorolyov.pancake.graphics.jfx;
	requires dev.kkorolyov.pancake.audio.jfx;
	requires dev.kkorolyov.pancake.input.jfx;
}
