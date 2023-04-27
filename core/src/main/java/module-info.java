import dev.kkorolyov.pancake.core.io.ActionQueueComponentConverter;
import dev.kkorolyov.pancake.core.io.BoundsComponentConverter;
import dev.kkorolyov.pancake.core.io.tag.CollidableComponentConverter;
import dev.kkorolyov.pancake.core.io.tag.CorrectableComponentConverter;
import dev.kkorolyov.pancake.core.io.DampingComponentConverter;
import dev.kkorolyov.pancake.core.io.ForceComponentConverter;
import dev.kkorolyov.pancake.core.io.MassComponentConverter;
import dev.kkorolyov.pancake.core.io.OrientationComponentConverter;
import dev.kkorolyov.pancake.core.io.PathComponentConverter;
import dev.kkorolyov.pancake.core.io.PositionComponentConverter;
import dev.kkorolyov.pancake.core.io.VelocityComponentConverter;
import dev.kkorolyov.pancake.core.io.limit.VelocityLimitComponentConverter;
import dev.kkorolyov.pancake.core.registry.ActionResourceConverterFactory;
import dev.kkorolyov.pancake.platform.entity.ComponentConverter;
import dev.kkorolyov.pancake.platform.registry.ResourceConverterFactory;

module dev.kkorolyov.pancake.core {
	requires org.slf4j;

	requires dev.kkorolyov.pancake.platform;

	exports dev.kkorolyov.pancake.core.component;
	exports dev.kkorolyov.pancake.core.component.event;
	exports dev.kkorolyov.pancake.core.component.limit;
	exports dev.kkorolyov.pancake.core.component.tag;
	exports dev.kkorolyov.pancake.core.system;
	exports dev.kkorolyov.pancake.core.system.cleanup;

	provides ComponentConverter with
			ActionQueueComponentConverter,
			BoundsComponentConverter,
			CollidableComponentConverter,
			CorrectableComponentConverter,
			DampingComponentConverter,
			ForceComponentConverter,
			MassComponentConverter,
			OrientationComponentConverter,
			PathComponentConverter,
			PositionComponentConverter,
			VelocityComponentConverter,
			VelocityLimitComponentConverter;

	provides ResourceConverterFactory with ActionResourceConverterFactory;
}
