#version 330 core

layout (location = 0) in vec4 position;
layout (location = 1) in vec2 tc;

layout (location = 2) in mat4 modelViewMatrix;
layout (location = 6) in vec4 col;

uniform mat4 pr_matrix;
uniform mat4 vw_matrix = mat4(1.0);

out DATA{
	vec2 tc;
	vec4 col;
} vs_out;

void main(){
	gl_Position = pr_matrix * vw_matrix * modelViewMatrix * position;
	vs_out.tc = tc;
	vs_out.col = col;
}
