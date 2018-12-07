#version 330 core

layout (location = 0) out vec4 color;

in DATA{
	vec2 tc;
} fs_in;

uniform sampler2D tex;
uniform sampler2D tex2;

vec4 screen = vec4(1280,720,1,1); //may have to change this later

vec2 norm = vec2(0.02,0.02); //degree of distortion
float trans = screen.x / screen.y;
vec2 pixelDistance = vec2(1.0 / screen.x, 1.0 / screen.y);
vec4 screenEdge = vec4(pixelDistance.x * 0.5, pixelDistance.y * 0.5, (pixelDistance.x * -0.5) + screen.z, (pixelDistance.y * -0.5) + screen.w);

vec4 getDisplacedFragment(in vec2 displace, in sampler2D t) {
	displace = clamp(displace, screenEdge.xy, screenEdge.zw);
	return texture(t, displace);
}

void main()
{
	vec3 col;

	vec4 dis = texture(tex2, fs_in.tc);
	dis += getDisplacedFragment(fs_in.tc + vec2(pixelDistance.x, 0.0), tex2);
	dis += getDisplacedFragment(fs_in.tc - vec2(pixelDistance.x, 0.0), tex2);
	dis += getDisplacedFragment(fs_in.tc + vec2(0.0, pixelDistance.y), tex2);
	dis += getDisplacedFragment(fs_in.tc - vec2(0.0, pixelDistance.y), tex2);
	dis *= 0.2;

	if (dis.a > 0.0 && dis.b > 0.0) {
		float scale = dis.b * norm.x + norm.y;

		vec2 displacement = ((dis.rg * 2.0) - 1.0) * scale;
		displacement.x /= trans;
		displacement += fs_in.tc;

		col = getDisplacedFragment(displacement, tex).rgb;
	} else {
		col = texture(tex, fs_in.tc).rgb;
	}

	color = vec4(col,1.0);
}
