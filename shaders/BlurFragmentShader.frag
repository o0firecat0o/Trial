#version 330 core

layout (location = 0) out vec4 color;

in vec2 blurTextureCoords[11];

in DATA{
	vec2 tc;
} fs_in;

uniform sampler2D tex;

void main()
{
	//color = texture(tex, fs_in.tc);

	color = vec4(0.0);
	color += texture(tex, blurTextureCoords[0]) * 0.0093;
	color += texture(tex, blurTextureCoords[1]) * 0.028002;
	color += texture(tex, blurTextureCoords[2]) * 0.065984;
	color += texture(tex, blurTextureCoords[3]) * 0.121703;
	color += texture(tex, blurTextureCoords[4]) * 0.175713;
	color += texture(tex, blurTextureCoords[5]) * 0.198596;
	color += texture(tex, blurTextureCoords[6]) * 0.175713;
	color += texture(tex, blurTextureCoords[7]) * 0.121703;
	color += texture(tex, blurTextureCoords[8]) * 0.065984;
	color += texture(tex, blurTextureCoords[9]) * 0.028002;
	color += texture(tex, blurTextureCoords[10]) * 0.0093;
}
