#version 330

in vec4 vColor;
layout(location = 0) out vec4 fColor;

void main(void)
{
    fColor = vColor;
    fColor.a = 1.0;
}