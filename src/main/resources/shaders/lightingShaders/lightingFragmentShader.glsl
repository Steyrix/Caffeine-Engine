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
    float horizontalDiff = -lightSourceSize.x / 2;
    float verticalDiff = lightSourceSize.y;

    float lightSourceX =
    (lightSourceCoords.x - horizontalDiff) / screenSize.x * 2 - 1;

    float lightSourceY =
    (-lightSourceCoords.y - verticalDiff) / screenSize.y * 2 + 1;

    vec2 lightSourcePos = vec2(lightSourceX, lightSourceY);

    vec3 ambient = vec3(0.8, 0.8, 0.8);

    float dist = distance(lightSourcePos, pos.xy);
    float lightIntensity = 1.0/dist;
    float targetRadius = 0.3;
    float diffuse = 0;

    if(dist < targetRadius) {
        diffuse = 1.0 - abs(dist / targetRadius);
    }

    if (lightIntensity >= lightIntensityCap) {
        lightIntensity = lightIntensityCap;
    }

    if (lightIntensity <= 1) {
        lightIntensity = 1;
    }

    vec4 fragColor = texture(textureSample, fragmentUV).rgba;
    vec3 lightCol = vec3(lightIntensity, lightIntensity, lightIntensity);
    fColor = vec4(min(fragColor.rgb * ((lightCol * diffuse) + ambient), fragColor.rgb), 1.0);

    if (fColor.a <= 0){
        discard;
    }
}
