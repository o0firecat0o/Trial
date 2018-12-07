#version 330 core

layout (location = 0) in vec4 position;
layout (location = 1) in vec2 tc;

uniform mat4 pr_matrix;
uniform mat4 vw_matrix = mat4(1.0);
uniform mat4 ml_matrix = mat4(1.0);

out vec2 blurTextureCoords[11];

out DATA{
	vec2 tc;
} vs_out;

void main(){
	gl_Position = pr_matrix * vw_matrix * ml_matrix * position;
	vs_out.tc = tc;
	
	float pixelSize = 1.0/1080;
	
	for(int i=-5;i<=5;i++){
		blurTextureCoords[i+5] = tc + vec2(pixelSize*i,0.0);
	}
}
