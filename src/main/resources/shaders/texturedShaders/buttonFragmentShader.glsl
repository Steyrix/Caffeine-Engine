#version 330

out vec4 fColor;

in vec2 fragmentUV;

uniform bool isPressed;
uniform sampler2D textureSample;

void main(void) {
    float offset = isPressed ? 0.5 : 0.0;
    vec2 texCoord = vec2(fragmentUV.x / 2 + offset, fragmentUV.y);
    fColor = texture(textureSample, texCoord);

    if (fColor.a <= 0) {
        discard;
    }
}