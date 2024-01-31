#version 330

in vec2 fragmentUV;
out vec4 fColor;

uniform sampler2D textureSample;
uniform float transparency;

void main(void)
{
    fColor = texture(textureSample, fragmentUV).rgba;
    fColor.a = transparency;

    if (fColor.a <= 0) {
        discard;
    }
}
