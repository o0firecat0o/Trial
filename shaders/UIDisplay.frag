#version 330 core

layout (location = 0) out vec4 color;

in DATA{
	vec2 tc;
	vec2 tc2;
} fs_in;

uniform sampler2D tex;  //Splunkey
uniform sampler2D tex2; //ICE

void main()
{
	vec4 IceColor = texture(tex2, fs_in.tc);
	
	
	vec2 ManTC = vec2(fs_in.tc2.x, fs_in.tc2.y);
	vec4 ManColor = texture(tex, ManTC );
	
		
	color = ManColor;
	color.a = IceColor.a;
	
}
