#version 460 core

layout(binding=0) uniform sampler2D sampler;

in vec2 texCoord;
in vec3 color;

out vec4 result;

void main() {
	result = vec4(color, 1.0) * texture(sampler, texCoord);
}
