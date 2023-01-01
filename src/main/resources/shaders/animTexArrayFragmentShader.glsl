#version 400
in vec2 fragmentUV;
out vec4 fColor;
uniform sampler2DArray textureArray;
void main(void)
{
    fColor = texture(textureArray, vec3(fragmentUV, 0)).rgba;
    if(fColor.a <= 0){
        discard;
    }
}
