#version 330

in vec2 fragmentUV;
out vec4 fColor;

uniform sampler2D textureSample;
uniform float transparency;

void main() {
    fColor = texture(textureSample, fragmentUV).rgba;
    fColor.a = transparency;
}
