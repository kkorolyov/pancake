import dev.kkorolyov.pancake.javafx.JavaFxApplication;
import dev.kkorolyov.pancake.javafx.JavaFxRenderMedium;

module dev.kkorolyov.pancake.application.javafx {
	requires kotlin.stdlib;

	requires javafx.graphics;

	requires simple.funcs;

	requires dev.kkorolyov.pancake.platform;

	provides dev.kkorolyov.pancake.platform.application.Application with JavaFxApplication;
	provides dev.kkorolyov.pancake.platform.media.graphic.RenderMedium with JavaFxRenderMedium;
}
