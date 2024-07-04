import dev.kkorolyov.pancake.core.editor.ActionQueueComponentWidgetFactory;
import dev.kkorolyov.pancake.core.editor.AnimationQueueComponentWidgetFactory;
import dev.kkorolyov.pancake.core.editor.BoundsComponentWidgetFactory;
import dev.kkorolyov.pancake.core.editor.DampingComponentWidgetFactory;
import dev.kkorolyov.pancake.core.editor.ForceComponentWidgetFactory;
import dev.kkorolyov.pancake.core.editor.GoComponentWidgetFactory;
import dev.kkorolyov.pancake.core.editor.MassComponentWidgetFactory;
import dev.kkorolyov.pancake.core.editor.PathComponentWidgetFactory;
import dev.kkorolyov.pancake.core.editor.TransformComponentWidgetFactory;
import dev.kkorolyov.pancake.core.editor.VelocityComponentWidgetFactory;
import dev.kkorolyov.pancake.core.editor.VelocityLimitComponentWidgetFactory;
import dev.kkorolyov.pancake.editor.factory.WidgetFactory;

module dev.kkorolyov.pancake.core.editor {
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

	requires io.github.classgraph;

	provides WidgetFactory with
			// components
			ActionQueueComponentWidgetFactory,
			AnimationQueueComponentWidgetFactory,
			BoundsComponentWidgetFactory,
			DampingComponentWidgetFactory,
			ForceComponentWidgetFactory,
			GoComponentWidgetFactory,
			MassComponentWidgetFactory,
			PathComponentWidgetFactory,
			TransformComponentWidgetFactory,
			VelocityComponentWidgetFactory,
			VelocityLimitComponentWidgetFactory;
}
