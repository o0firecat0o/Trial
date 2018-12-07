#version 330 core

layout (location = 0) out vec4 color;

in DATA{
	vec2 tc;
} fs_in;

uniform sampler2D tex1;
uniform sampler2D tex2;

uniform vec2 lightobb;

uniform int MapSize = 50;

float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

float getColorA(float extraCoordinate_X, float extraCoordinate_Y, sampler2D texx){
	float random = rand(fs_in.tc);
		
	float colorINIT = 1;
	
	float offset = 1f/(float(MapSize));
	//TODO: fix this 0.02
	float Data_R = texture(texx, vec2(fs_in.tc.x-offset,fs_in.tc.y)).r;
	float Data_L = texture(texx, vec2(fs_in.tc.x+offset,fs_in.tc.y)).r;
	
	float Data_U = texture(texx, vec2(fs_in.tc.x,fs_in.tc.y+offset)).r;
	float Data_B = texture(texx, vec2(fs_in.tc.x,fs_in.tc.y-offset)).r;
	
	float Data_UR = texture(texx, vec2(fs_in.tc.x-offset,fs_in.tc.y+offset)).r;
	float Data_UL = texture(texx, vec2(fs_in.tc.x+offset,fs_in.tc.y+offset)).r;
	float Data_BR = texture(texx, vec2(fs_in.tc.x-offset,fs_in.tc.y-offset)).r; 
	float Data_BL = texture(texx, vec2(fs_in.tc.x+offset,fs_in.tc.y-offset)).r; 
	
	if(Data_R < 1 && extraCoordinate_X < 0.5){
		colorINIT -= (1-extraCoordinate_X*2)+random*0.2;
	}
	if(Data_L < 1 && extraCoordinate_X > 0.5){
		colorINIT -= (extraCoordinate_X-0.5)*2+random*0.2;
	}
	if(Data_B < 1 && extraCoordinate_Y < 0.5){
		colorINIT -= (1-extraCoordinate_Y*2)+random*0.2;
	}	
	if(Data_U < 1 && extraCoordinate_Y > 0.5){
		colorINIT -= (extraCoordinate_Y-0.5)*2+random*0.2;
	}
	if(Data_UR < 1 && extraCoordinate_X < 0.5 && extraCoordinate_Y > 0.5){
		float A = (1-extraCoordinate_X*2);
		float B = (extraCoordinate_Y-0.5)*2;
		float C = A * B;
		colorINIT -= C + random*0.2;
	}
	if(Data_UL < 1 && extraCoordinate_X > 0.5 && extraCoordinate_Y > 0.5){
		float A = (extraCoordinate_X-0.5)*2;
		float B = (extraCoordinate_Y-0.5)*2;
		float C = A * B;
		colorINIT -= C + random*0.2;
	}
	if(Data_BR < 1 && extraCoordinate_X < 0.5 && extraCoordinate_Y < 0.5){
		float A = (1-extraCoordinate_X*2);
		float B = (1-extraCoordinate_Y*2);
		float C = A * B;
		colorINIT -= C + random*0.2;
	}
	if(Data_BL < 1 && extraCoordinate_X > 0.5 && extraCoordinate_Y < 0.5){
		float A = (extraCoordinate_X-0.5)*2;
		float B = (1-extraCoordinate_Y*2);
		float C = A * B;
		colorINIT -= C + random*0.2;
	}
	
	return colorINIT;
}

void main()
{
	if(texture(tex2, fs_in.tc).a == 0){
		discard;
	}
	
	float actualCoordinate_X = fs_in.tc.x*MapSize;
	float extraCoordinate_X = mod(actualCoordinate_X, 1);
	float actualCoordinate_Y = fs_in.tc.y*MapSize;
	float extraCoordinate_Y = mod(actualCoordinate_Y, 1);
	
	color = texture(tex1, fs_in.tc) * getColorA(extraCoordinate_X,extraCoordinate_Y,tex2);	
	
	float distancefromBlob = distance(lightobb, vec2(actualCoordinate_X, actualCoordinate_Y));
	distancefromBlob = 0.3f/(distancefromBlob);
	color.r += distancefromBlob * 0.1;
	color.g += distancefromBlob * 0.1;
	
	if(color.a < 0.5){
		discard;
	}
}
