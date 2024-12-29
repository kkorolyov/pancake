import dev.kkorolyov.pancake.platform.io.Serializer;
import dev.kkorolyov.pancake.platform.action.io.ActionSerializer;
import dev.kkorolyov.pancake.platform.animation.io.ChoreographySerializer;
import dev.kkorolyov.pancake.platform.animation.io.TimelineSerializer;
import dev.kkorolyov.pancake.platform.io.internal.MathStructizer;
import dev.kkorolyov.pancake.platform.math.io.MatrixSerializer;
import dev.kkorolyov.pancake.platform.math.io.VectorSerializer;
import dev.kkorolyov.pancake.platform.io.Structizer;
import dev.kkorolyov.pancake.platform.io.internal.MathStructizer;
import dev.kkorolyov.pancake.platform.registry.ResourceConverterFactory;
import dev.kkorolyov.pancake.platform.registry.internal.ActionResourceConverterFactory;
import dev.kkorolyov.pancake.platform.registry.internal.EntityTemplateResourceConverterFactory;

module dev.kkorolyov.pancake.platform {
	// logging
	requires org.slf4j;
	// FIXME here so kotlin projects using this can use different format log configs
	// FIXME kotlin compiler cannot find this on module path
	requires com.fasterxml.jackson.dataformat.yaml;

	// resource parsing
	requires org.yaml.snakeyaml;
	// component conversions
	requires io.github.classgraph;
	// expose for resource mapping API
	requires transitive dev.kkorolyov.flub;

	exports dev.kkorolyov.pancake.platform;
	exports dev.kkorolyov.pancake.platform.action;
	exports dev.kkorolyov.pancake.platform.animation;
	exports dev.kkorolyov.pancake.platform.entity;
	exports dev.kkorolyov.pancake.platform.entity.io;
	exports dev.kkorolyov.pancake.platform.io;
	exports dev.kkorolyov.pancake.platform.math;
	exports dev.kkorolyov.pancake.platform.registry;
	exports dev.kkorolyov.pancake.platform.utility;

	uses Serializer;
	uses Structizer;
	uses ResourceConverterFactory;

	provides Serializer with
			VectorSerializer,
			MatrixSerializer,
			ChoreographySerializer,
			TimelineSerializer,
			ActionSerializer;
	provides Structizer with
			MathStructizer;
	provides ResourceConverterFactory with
			ActionResourceConverterFactory,
			EntityTemplateResourceConverterFactory;
}
