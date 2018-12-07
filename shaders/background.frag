#version 330 core

layout (location = 0) out vec4 color;

in DATA{
	vec2 tc;
} fs_in;

uniform sampler2D tex1;
uniform sampler2D tex2;
uniform sampler2D tex3;

uniform int MapSize = 50;



void main()
{

	
	vec4 borderColor = texture(tex1, fs_in.tc * MapSize);
	float grassData = texture(tex2, fs_in.tc).r;
	vec4 insideColor = texture(tex3, fs_in.tc * MapSize);	

	color = borderColor;
	
	if(grassData<0.1){
		discard;
	}
	
	if(color.a < 0.4){
		discard;
	}
	
	//if not the border
	if(color.r != 1){
		color = insideColor;
	}
	
	
	
	//color.a = getColorA(extraCoordinate_X, extraCoordinate_Y, tex2);
	
}

