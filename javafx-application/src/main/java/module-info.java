import dev.kkorolyov.pancake.platform.service.Application;
import dev.kkorolyov.pancake.platform.service.RenderMedium;

module dev.kkorolyov.pancake.javafx.application {
	requires kotlin.stdlib;

	requires javafx.graphics;

	requires dev.kkorolyov.pancake.platform;

	provides Application with dev.kkorolyov.pancake.javafx.JavaFxApplication;
	provides RenderMedium with dev.kkorolyov.pancake.javafx.JavaFxRenderMedium;
}
