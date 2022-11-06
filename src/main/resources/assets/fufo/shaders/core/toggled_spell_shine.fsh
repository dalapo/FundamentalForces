#version 150

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float GameTime;
uniform float ShineCount;
uniform float Speed;

in vec4 vertexColor;
in vec2 texCoord0;
in vec2 texCoord2;

out vec4 fragColor;

void main() {
    vec3 col = texture(Sampler0, texCoord0).xyz * sin(texCoord0.y*ShineCount+(GameTime*Speed));
    fragColor = vec4(col, 1) * ColorModulator;
}
