#version 330

in vec4 vColor;
out vec4 fColor;

uniform float alpha;

void main(void)
{
    fColor = vColor;
    fColor.a = alpha;
}