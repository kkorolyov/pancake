module dev.kkorolyov.pancake.javafx.application {
	requires kotlin.stdlib;

	requires javafx.graphics;

	requires dev.kkorolyov.pancake.platform;

	provides dev.kkorolyov.pancake.platform.application.Application with dev.kkorolyov.pancake.javafx.JavaFxApplication;
	provides dev.kkorolyov.pancake.platform.media.graphic.RenderMedium with dev.kkorolyov.pancake.javafx.JavaFxRenderMedium;
}
