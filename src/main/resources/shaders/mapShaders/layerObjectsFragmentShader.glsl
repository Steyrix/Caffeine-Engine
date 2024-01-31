#version 330

in vec2 fragmentUV;
out vec4 fColor;

uniform sampler2D textureSample;
uniform float transparency;

void main(void)
{
    fColor = vec4(1.0, 1.0, 1.0, transparency) * texture(textureSample, fragmentUV);

    if (fColor.a <= 0) {
        discard;
    }
}
