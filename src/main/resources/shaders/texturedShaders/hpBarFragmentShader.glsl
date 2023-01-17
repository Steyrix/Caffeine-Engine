#version 330

out vec4 fColor;

in vec2 fragmentUV;

uniform float filled;
uniform sampler2D textureSample;

void main(void) {
    float offset = fragmentUV.x > filled ? 0.5 : 0.0;
    vec2 texCoord = vec2(fragmentUV.x, fragmentUV.y / 2 + offset);
    fColor = texture(textureSample, texCoord);
}