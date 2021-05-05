import dev.kkorolyov.pancake.platform.plugin.Application;
import dev.kkorolyov.pancake.platform.plugin.RenderMedium;

module dev.kkorolyov.pancake.plugin.apprender.jfx {
	requires kotlin.stdlib;

	requires javafx.graphics;

	requires dev.kkorolyov.pancake.platform;

	provides Application with dev.kkorolyov.pancake.javafx.JavaFxApplication;
	provides RenderMedium with dev.kkorolyov.pancake.javafx.JavaFxRenderMedium;
}
