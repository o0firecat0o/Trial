#version 330 core

layout (location = 0) out vec4 color;

in DATA{
	vec2 tc;
} fs_in;

uniform sampler2D tex;

uniform vec3 colorTaint = vec3(1,1,1);
uniform float alphaTaint = 1;

void main()
{
	color = texture(tex, fs_in.tc);
	color.rgb = color.rgb*colorTaint;
	color.a = color.a*alphaTaint;
}
