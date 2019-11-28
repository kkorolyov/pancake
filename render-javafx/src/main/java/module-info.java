module dev.kkorolyov.pancake.render.javafx {
	requires javafx.graphics;

	requires dev.kkorolyov.pancake.platform;

	provides dev.kkorolyov.pancake.platform.media.graphic.RenderMedium with dev.kkorolyov.pancake.render.javafx.JavaFXRenderMedium;
}
