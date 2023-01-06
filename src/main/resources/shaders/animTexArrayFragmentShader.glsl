#version 400
in vec2 fragmentUV;
out vec4 fColor;
uniform sampler2DArray textureArray;
uniform int textureArrayLayer;
void main(void)
{
    fColor = texture(textureArray, vec3(fragmentUV, textureArrayLayer)).rgba;
    if(fColor.a <= 0){
        discard;
    }
}
