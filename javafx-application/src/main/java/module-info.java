module dev.kkorolyov.pancake.application.javafx {
	requires kotlin.stdlib;

	requires javafx.graphics;

	requires simple.funcs;

	requires dev.kkorolyov.pancake.platform;

	// Make services visible to JavaFX,  Providers
	exports dev.kkorolyov.pancake.javafx to javafx.graphics, simple.files;

	provides dev.kkorolyov.pancake.platform.application.Application with dev.kkorolyov.pancake.javafx.JavaFxApplication;
	provides dev.kkorolyov.pancake.platform.media.graphic.RenderMedium with dev.kkorolyov.pancake.javafx.JavaFxRenderMedium;
}
