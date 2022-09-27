#version 330
in vec2 fragmentUV;
in vec4 pos;
out vec4 fColor;

uniform vec2 screenSize;
uniform vec2 lightSourceSize;
uniform vec2 lightSourceCoords;
uniform float lightIntensityCap;
uniform sampler2D textureSample;
void main(void)
{
    vec2 screenCenter = vec2(screenSize.x / 2, screenSize.y / 2);

    float horizontalDiff = -lightSourceSize.x / 2;
    float verticalDiff = lightSourceSize.y;

    float lightSourceX =
    (lightSourceCoords.x - horizontalDiff) / screenSize.x * 2 - 1;

    float lightSourceY =
    (-lightSourceCoords.y - verticalDiff) / screenSize.y * 2 + 1;

    vec2 lightSourcePos = vec2(lightSourceX, lightSourceY);

    float lightIntensity = 1.0/distance(lightSourcePos, pos.xy);

    if(lightIntensity >= lightIntensityCap) {
        lightIntensity = lightIntensityCap;
    }

    vec4 lightCol = vec4(lightIntensity, lightIntensity, lightIntensity, 1.0);
    fColor = texture(textureSample, fragmentUV).rgba * lightCol;

    if(fColor.a <= 0){
        discard;
    }
}
