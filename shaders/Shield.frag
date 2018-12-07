#version 330 core

layout (location = 0) out vec4 color;

in DATA{
	vec2 tc;
} fs_in;

uniform sampler2D tex;	//The shield image
uniform sampler2D tex2;	//The main render
uniform sampler2D tex3;	//The distort shader
uniform sampler2D tex4;	//The shield edge

uniform vec2 ScreenSize;
uniform float timer;	//Timer between 0,1 indicating distoriton;
uniform float distortionValue;	//how much the center should distort
uniform float edgeThickness;	//how thick the white should be near the inner edge
uniform vec3 shieldColor;

void main()
{
	color = texture(tex, fs_in.tc);

	float d = distance(fs_in.tc, vec2(0.5f,0.5f));

	vec2 ScreenCoordinate = vec2(gl_FragCoord.x/ScreenSize.x, gl_FragCoord.y/ScreenSize.y);

	vec2 distortion = texture(tex3, vec2(fs_in.tc.x,fs_in.tc.y+timer)).rg* 2.0 - 1.0;


	//outside of shield
	if(color.a<0.1f){
		//really ouside of everything
		if(texture(tex4,fs_in.tc*0.8+0.1).r < 0.8f){
			discard;
		}
		//ouside of shield, but inside the fire effect of shield
		else{
			color = vec4(shieldColor,1);
		}
	}else{
		color.rgb = texture(tex2,ScreenCoordinate + distortion*distortionValue).rgb + pow(d,edgeThickness);

	}

	//outside boundary
	if(texture(tex, fs_in.tc).r>0.7){
		color = vec4(shieldColor,1);
	}
}
