#version 330 core

layout (location = 0) out vec4 color;

in DATA{
	vec2 tc;
} fs_in;

uniform sampler2D tex;
uniform sampler2D tex2;

void main()
{
	color =  texture(tex, fs_in.tc) * 0.5 + texture(tex2, fs_in.tc) * 1;

	if(color.a<0.1){
		discard;
	}
}
