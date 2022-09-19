#version 330
in vec2 fragmentUV;
in vec4 pos;
layout(origin_upper_left) in vec4 gl_FragCoord;
out vec4 fColor;
uniform vec2 lightSourcePos;
uniform sampler2D textureSample;
void main(void)
{
    float lightIntensity = 1.0/distance(pos.xy, lightSourcePos);
    vec4 lightCol = vec4(lightIntensity, lightIntensity, lightIntensity, 1.0);
    fColor = texture(textureSample, fragmentUV).rgba * lightCol;

    if(fColor.a <= 0){
        discard;
    }
}
