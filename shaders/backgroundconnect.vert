#version 330 core

layout (location = 0) in vec4 position;
layout (location = 1) in vec2 tc;

uniform mat4 pr_matrix;
uniform mat4 vw_matrix = mat4(1.0);
uniform mat4 ml_matrix = mat4(1.0);

out DATA{
	vec2 tc;
	vec2 tc2;
} vs_out;



void main(){
	gl_Position = pr_matrix * vw_matrix * ml_matrix * position;
	vs_out.tc = vec2(tc.x, 1-tc.y);
	vs_out.tc2 = tc;
}

