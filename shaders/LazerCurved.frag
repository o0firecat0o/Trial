#version 330 core

layout (location = 0) out vec4 color;

in DATA{
	vec2 tc;
} fs_in;

//uniform sampler2D tex;

uniform float timer = 0;


float amplitude = 0.5f;

//width: actual width of the lazer (1/width)

void AlphaPlus(float width, float frequencyMultiply,float phaserOffset, float intensity, vec3 LazerColor){
	float convertedYcoordinate = fs_in.tc.y*2-1;
	float convertToSinCurve = amplitude * (0.7-abs(fs_in.tc.x-0.5)) * sin((fs_in.tc.x-timer)*frequencyMultiply + phaserOffset);

	float distanceFromCurve = abs(convertedYcoordinate-sin(convertToSinCurve))*(width);

	if((1-distanceFromCurve)>0){
		color.a += (1-distanceFromCurve)*intensity;
		//Should this be add or =?
		color.rgb += (1-distanceFromCurve)*LazerColor*intensity;
	}
}

void main()
{
	color = vec4(0,0,0,0);

	vec3 colorB = vec3(1.0, 0.988, 0.212);
	vec3 colorA = vec3(1.0, 0.016, 0.063);

	AlphaPlus(1f, 0f, 0, 0.7, colorB);
	AlphaPlus(5f, 12f, 0, 0.9, colorA);
	AlphaPlus(5f, 12f, 3.1415926535, 0.9,colorA);
}
