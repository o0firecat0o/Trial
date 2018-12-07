#version 330 core

layout (location = 0) out vec4 color;

in DATA{
	vec2 tc;
} fs_in;

uniform sampler2D tex;	//laser_m
uniform sampler2D tex2;	//laser_m_o
uniform sampler2D tex3;	//laser_s
uniform sampler2D tex4;	//laser_s_o
uniform sampler2D tex5;	//laser_e
uniform sampler2D tex6;	//laser_e_o
uniform sampler2D tex7; //rock_n

//float width = 0.2f;
//float amplitude = 0.5f;

uniform float timer = 0;
uniform float alpha = 1;
uniform vec3 LazerColor = vec3(1,1,1);
uniform float relativeScale = 0.1;

void main()
{
	float noise = texture(tex7,vec2(fs_in.tc.x-timer,fs_in.tc.y)).r;

	color = texture(tex,vec2(fs_in.tc.x+noise,fs_in.tc.y));
	color += texture(tex2,fs_in.tc);
	color *= vec4(LazerColor,1);

	if(fs_in.tc.x>(1-relativeScale)){
		color = texture(tex5,vec2((fs_in.tc.x-1+relativeScale)*(1/relativeScale),fs_in.tc.y));
		color += texture(tex6,vec2((fs_in.tc.x-1+relativeScale)*(1/relativeScale),fs_in.tc.y));
		color *= vec4(LazerColor,1);
	}


	if(fs_in.tc.x<relativeScale){
		color = texture(tex3,vec2(fs_in.tc.x*(1/relativeScale),fs_in.tc.y));

		color += texture(tex4,vec2(fs_in.tc.x*(1/relativeScale),fs_in.tc.y));
		color *= vec4(LazerColor,1);
	}

	color.a *= alpha;

//	float convertedYcoordinate = fs_in.tc.y*2-1;
//	float convertToSinCruve = amplitude* sin(fs_in.tc.x*12);
//
//	if(abs(convertedYcoordinate-sin(convertToSinCruve))<width){
//		discard;
//	}
}
