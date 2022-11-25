#version 150

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float GameTime;

uniform float Speed;
uniform float Width;
uniform float Ramping;
uniform float Dodge;

in vec4 vertexColor;
in vec2 texCoord0;
in vec2 texCoord2;

out vec4 fragColor;

float tri(float v) {
    return abs(fract(v)*2.-1.);
}

void main() {
    vec4 tex= texture(Sampler0, texCoord0);
    vec4 spec= tex;
    spec+= vec4(vec3(Dodge), 0.);

    float w= tri(texCoord0.y/Width+GameTime*Speed);
    w= pow(w, Ramping);

    fragColor = mix(tex, spec, w) * vertexColor * ColorModulator;
}