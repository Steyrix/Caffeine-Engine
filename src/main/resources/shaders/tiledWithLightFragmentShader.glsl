#version 330
in vec2 fragmentUV;
in float lightIntensity;
out vec4 fColor;
uniform sampler2D textureSample;
void main(void)
{
    vec4 lightColor = vec4(lightIntensity, lightIntensity, lightIntensity, 1.0);
    fColor = texture(textureSample, fragmentUV).rgba * vec4(0.5, 0.5, 0.5, 1.0) * lightColor;

    if(fColor.a <= 0){
        discard;
    }
}