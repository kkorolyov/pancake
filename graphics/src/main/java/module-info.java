import dev.kkorolyov.pancake.graphics.component.io.LensSerializer;
import dev.kkorolyov.pancake.graphics.component.io.ModelSerializer;
import dev.kkorolyov.pancake.graphics.resource.io.BufferSerializerInt;
import dev.kkorolyov.pancake.graphics.resource.io.BufferSerializerVertex;
import dev.kkorolyov.pancake.graphics.resource.io.MeshSerializer;
import dev.kkorolyov.pancake.graphics.resource.io.PathSourceSerializer;
import dev.kkorolyov.pancake.graphics.resource.io.PixelBufferSerializer;
import dev.kkorolyov.pancake.graphics.resource.io.ProgramSerializer;
import dev.kkorolyov.pancake.graphics.resource.io.RawSourceSerializer;
import dev.kkorolyov.pancake.graphics.resource.io.ShaderSerializer;
import dev.kkorolyov.pancake.graphics.resource.io.TextureSerializer;
import dev.kkorolyov.pancake.graphics.resource.io.VertexSerializer;
import dev.kkorolyov.pancake.platform.io.Serializer;

module dev.kkorolyov.pancake.graphics {
	requires kotlin.stdlib;

	requires transitive org.lwjgl;
	requires transitive org.lwjgl.stb;

	requires dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.core;

	exports dev.kkorolyov.pancake.graphics;
	exports dev.kkorolyov.pancake.graphics.util;
	exports dev.kkorolyov.pancake.graphics.resource;
	exports dev.kkorolyov.pancake.graphics.component;
	exports dev.kkorolyov.pancake.graphics.system;

	uses dev.kkorolyov.pancake.graphics.RenderBackend;

	provides Serializer with
			LensSerializer,
			ModelSerializer,
			BufferSerializerInt,
			BufferSerializerVertex,
			MeshSerializer,
			ProgramSerializer,
			ShaderSerializer,
			PathSourceSerializer,
			RawSourceSerializer,
			TextureSerializer,
			PixelBufferSerializer,
			VertexSerializer;
}
