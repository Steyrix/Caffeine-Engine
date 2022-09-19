#version 330
in vec2 fragmentUV;
in vec2 lightSourcePos;
in vec2 worldPos;
out vec4 fColor;
uniform sampler2D textureSample;
void main(void)
{
    fColor = texture(textureSample, fragmentUV).rgba * vec4(0.5, 0.5, 0.5, 1.0);
    fColor = fColor * 1.0/distance(lightSourcePos, worldPos);

    if(fColor.a <= 0){
        discard;
    }
}
