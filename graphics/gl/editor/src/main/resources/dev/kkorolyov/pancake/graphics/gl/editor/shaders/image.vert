#version 460 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 inTexCoord;

out vec2 texCoord;

layout (location = 0) uniform mat4 transform;

void main() {
	// drawing to a texture, so vertical flip
	gl_Position = transform * vec4(position.x, position.y * -1, position.z, 1);

	// pass-through
	texCoord = inTexCoord;
}
