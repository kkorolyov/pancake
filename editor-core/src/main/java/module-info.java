import dev.kkorolyov.pancake.editor.ComponentDataFactory;
import dev.kkorolyov.pancake.editor.core.ActionQueueComponentDataFactory;
import dev.kkorolyov.pancake.editor.core.BoundsComponentDataFactory;
import dev.kkorolyov.pancake.editor.core.DampingComponentDataFactory;
import dev.kkorolyov.pancake.editor.core.ForceComponentDataFactory;
import dev.kkorolyov.pancake.editor.core.MassComponentDataFactory;
import dev.kkorolyov.pancake.editor.core.TransformComponentDataFactory;
import dev.kkorolyov.pancake.editor.core.VelocityCapComponentDataFactory;
import dev.kkorolyov.pancake.editor.core.VelocityComponentDataFactory;

module dev.kkorolyov.pancake.editor.core {
	requires kotlin.stdlib;

	requires tornadofx;
	requires javafx.base;
	requires javafx.graphics;

	requires dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.core;
	requires dev.kkorolyov.pancake.editor;

	provides ComponentDataFactory with
			ActionQueueComponentDataFactory,
			BoundsComponentDataFactory,
			DampingComponentDataFactory,
			ForceComponentDataFactory,
			MassComponentDataFactory,
			TransformComponentDataFactory,
			VelocityCapComponentDataFactory,
			VelocityComponentDataFactory;
}
