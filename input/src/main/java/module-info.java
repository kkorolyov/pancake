import dev.kkorolyov.pancake.input.component.io.InputMapperSerializer;
import dev.kkorolyov.pancake.input.control.io.KeyControlSerializer;
import dev.kkorolyov.pancake.input.control.io.MouseButtonControlSerializer;
import dev.kkorolyov.pancake.platform.io.Serializer;

module dev.kkorolyov.pancake.input {
	requires kotlin.stdlib;

	requires dev.kkorolyov.pancake.platform;

	exports dev.kkorolyov.pancake.input.component;
	exports dev.kkorolyov.pancake.input.control;
	exports dev.kkorolyov.pancake.input.event;

	provides Serializer with
			InputMapperSerializer,
			KeyControlSerializer,
			MouseButtonControlSerializer;
}
