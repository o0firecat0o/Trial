#version 330 core

layout (location = 0) out vec4 color;

in DATA{
	vec2 tc;
} fs_in;

uniform sampler2D tex1;
uniform sampler2D tex2;

void main()
{
	color = texture(tex1, fs_in.tc);
	if(color.r < 1){
		color = vec4(0.8,0.8,0.8,0.6f);
	}
	vec4 colorB = texture(tex2, fs_in.tc);
	if(colorB.a > 0.1){
		color.rgba = colorB.rgba;
	}
	
	if (color.w < 0.1)
			discard;
}
