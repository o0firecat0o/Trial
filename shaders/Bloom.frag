#version 330 core

layout (location = 0) out vec4 color;

in DATA{
	vec2 tc;
} fs_in;

uniform sampler2D tex;
uniform sampler2D tex2;

void main()
{
	vec4 sceneColor = texture(tex, fs_in.tc);
	vec4 highlightColor = texture(tex2, fs_in.tc);
	color = sceneColor + highlightColor*0.5;

}
