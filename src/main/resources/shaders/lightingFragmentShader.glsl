#version 330
in vec2 fragmentUV;
in vec4 pos;
out vec4 fColor;

uniform vec2 screenSize;
uniform vec2 lightSourceSize;
uniform vec2 lightSourceCoords;
uniform sampler2D textureSample;
void main(void)
{
    vec2 screenCenter = vec2(screenSize.x / 2, screenSize.y / 2);

    float lightSourceX =
    (lightSourceCoords.x - screenCenter.x + lightSourceSize.x) / screenSize.x;

    float lightSourceY =
    (lightSourceCoords.y - screenCenter.y - lightSourceSize.y) / screenSize.y;

    vec2 lightSourcePos = vec2(lightSourceX, lightSourceY);

    float lightIntensity = 1.0/distance(lightSourcePos, pos.xy);
    if(lightIntensity >= 100) {
        lightIntensity = 0;
    }
    if(lightIntensity >= 3) {
        lightIntensity = 3;
    }
    vec4 lightCol = vec4(lightIntensity, lightIntensity, lightIntensity, 1.0);
    fColor = texture(textureSample, fragmentUV).rgba * lightCol;

    if(fColor.a <= 0){
        discard;
    }
}
