#version 330 core

layout (location = 0) out vec4 color;

in DATA{
	vec2 tc;
} fs_in;

uniform sampler2D tex;
uniform sampler2D tex2;

uniform int x_size;
uniform int y_size;

vec2 intToVec2(int INT){
	int y = int(floor(float(INT)/16f));
	int x = INT - y *16;
	return vec2(x,y);
}


void main()
{
	vec2 realCoordinate = mod(vec2(fs_in.tc.x * x_size,fs_in.tc.y * y_size), 1);
	
	//return the character, for example 1=17,2=18,default=96;
	int character = int(texture(tex2,fs_in.tc).r*255);
		
	color = texture(tex, (realCoordinate + intToVec2(character))/ 16);
	if (color.w < 1)
		discard;
}
