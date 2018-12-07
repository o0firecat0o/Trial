#version 330 core

layout (location = 0) out vec4 color;

in DATA{
	vec2 tc;
} fs_in;

uniform float thickness;
uniform float precentage;
uniform vec3 Color;
uniform float innerDiameter;

void main()
{
	color = vec4(Color,1);
	// d is the range from 0 to 1 from the center
	float d = distance(fs_in.tc,vec2(0.5,0.5))*1.414f;

	if(d>innerDiameter&&d<innerDiameter+thickness){

	}else{
		discard;
	}

	float x = fs_in.tc.x-0.5f;
	float y = fs_in.tc.y-0.5f;
	// finding the degree
	float degree = atan(y/x);
	// changing the angle from clockwise to anti clockwise
	degree = -degree;
	// correcting the degree, the degree now should be -4.71 to 1.57;
	if(fs_in.tc.x<=0.5f){
		degree = -1.57+(degree-1.57);
	}
	// changing the input precentage to suit the degree;
	float correctedprecentage = -4.71+6.28*precentage;

	if(degree>correctedprecentage){
		discard;
	}
}
