import dev.kkorolyov.pancake.javafx.JavaFXApplication;
import dev.kkorolyov.pancake.javafx.JavaFXRenderMedium;

module dev.kkorolyov.pancake.application.javafx {
	requires kotlin.stdlib;

	requires javafx.graphics;

	requires simple.funcs;

	requires dev.kkorolyov.pancake.platform;

	provides dev.kkorolyov.pancake.platform.application.Application with JavaFXApplication;
	provides dev.kkorolyov.pancake.platform.media.graphic.RenderMedium with JavaFXRenderMedium;
}
