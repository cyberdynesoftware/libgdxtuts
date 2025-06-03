#version 330 core
out vec4 FragColor;

in vec2 TexCoord;

uniform sampler2D ourTexture;
uniform sampler2D otherTexture;

uniform float mix_param;

void main()
{
    FragColor = mix(texture(ourTexture, TexCoord), texture(otherTexture, TexCoord), mix_param);
}
