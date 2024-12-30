import dev.kkorolyov.pancake.core.animation.io.TransformFrameSerializer;
import dev.kkorolyov.pancake.core.component.io.AnimatorSerializer;
import dev.kkorolyov.pancake.core.component.io.BoundsSerializer;
import dev.kkorolyov.pancake.core.component.io.DampingSerializer;
import dev.kkorolyov.pancake.core.component.io.ForceSerializer;
import dev.kkorolyov.pancake.core.component.io.MassSerializer;
import dev.kkorolyov.pancake.core.component.io.PathSerializer;
import dev.kkorolyov.pancake.core.component.io.TransformSerializer;
import dev.kkorolyov.pancake.core.component.io.VelocitySerializer;
import dev.kkorolyov.pancake.core.component.limit.io.TransformLimitSerializer;
import dev.kkorolyov.pancake.core.component.limit.io.VelocityLimitSerializer;
import dev.kkorolyov.pancake.core.component.tag.io.CollidableSerializer;
import dev.kkorolyov.pancake.core.component.tag.io.CorrectableSerializer;
import dev.kkorolyov.pancake.core.io.AnimationStructizer;
import dev.kkorolyov.pancake.core.io.ComponentStructizer;
import dev.kkorolyov.pancake.core.registry.ActionResourceConverterFactory;
import dev.kkorolyov.pancake.platform.io.Serializer;
import dev.kkorolyov.pancake.platform.io.Structizer;
import dev.kkorolyov.pancake.platform.registry.ResourceConverterFactory;

module dev.kkorolyov.pancake.core {
	requires org.slf4j;

	requires dev.kkorolyov.pancake.platform;

	exports dev.kkorolyov.pancake.core.animation;
	exports dev.kkorolyov.pancake.core.component;
	exports dev.kkorolyov.pancake.core.component.event;
	exports dev.kkorolyov.pancake.core.component.limit;
	exports dev.kkorolyov.pancake.core.component.tag;
	exports dev.kkorolyov.pancake.core.system;
	exports dev.kkorolyov.pancake.core.system.cleanup;

	provides Serializer with
			AnimatorSerializer,
			BoundsSerializer,
			DampingSerializer,
			ForceSerializer,
			MassSerializer,
			PathSerializer,
			TransformSerializer,
			VelocitySerializer,
			CollidableSerializer,
			CorrectableSerializer,
			TransformLimitSerializer,
			VelocityLimitSerializer,
			TransformFrameSerializer;
	provides Structizer with
			AnimationStructizer,
			ComponentStructizer;
	provides ResourceConverterFactory with ActionResourceConverterFactory;
}
