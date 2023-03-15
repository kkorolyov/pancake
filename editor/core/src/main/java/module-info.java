import dev.kkorolyov.pancake.editor.ComponentWidgetFactory;
import dev.kkorolyov.pancake.editor.core.ActionQueueComponentWidgetFactory;
import dev.kkorolyov.pancake.editor.core.BoundsComponentWidgetFactory;
import dev.kkorolyov.pancake.editor.core.DampingComponentWidgetFactory;
import dev.kkorolyov.pancake.editor.core.ForceComponentWidgetFactory;
import dev.kkorolyov.pancake.editor.core.MassComponentWidgetFactory;
import dev.kkorolyov.pancake.editor.core.PositionComponentWidgetFactory;
import dev.kkorolyov.pancake.editor.core.VelocityLimitComponentWidgetFactory;
import dev.kkorolyov.pancake.editor.core.VelocityComponentWidgetFactory;

module dev.kkorolyov.pancake.editor.core {
	requires kotlin.stdlib;

	requires org.slf4j;

	requires org.lwjgl;
	requires org.lwjgl.opengl;
	requires org.lwjgl.stb;
	requires org.lwjgl.glfw;

	requires imgui.binding;

	requires dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.core;
	requires dev.kkorolyov.pancake.editor;

	provides ComponentWidgetFactory with
			ActionQueueComponentWidgetFactory,
			BoundsComponentWidgetFactory,
			DampingComponentWidgetFactory,
			ForceComponentWidgetFactory,
			MassComponentWidgetFactory,
			PositionComponentWidgetFactory,
			VelocityLimitComponentWidgetFactory,
			VelocityComponentWidgetFactory;
}
