#version 330 core

layout (location = 0) out vec4 color;

in DATA{
	vec2 tc;
	vec2 tc2;
} fs_in;

uniform sampler2D tex1;
uniform sampler2D tex2;
uniform sampler2D tex3;

uniform int MapSize = 50;

bool returnOccupy(sampler2D texx, vec2 coordinate){
	if(texture(texx, coordinate).r < 0.1){
		return false;
	}else{
		return true;
	}
}

int textureConnect(sampler2D texx, vec2 Selfcoorindate){
	int returnINT = 0;
	float offset = 1f/MapSize;
	
	if(returnOccupy(texx, Selfcoorindate + vec2(0,offset))){
		returnINT += 1;
	}
	if(returnOccupy(texx, Selfcoorindate + vec2(offset,0))){
		returnINT += 2;
	}
	if(returnOccupy(texx, Selfcoorindate + vec2(0,-offset))){
		returnINT += 4;
	}
	if(returnOccupy(texx, Selfcoorindate + vec2(-offset,0))){
		returnINT += 8;
	}
	
	return returnINT;
}

vec2 intToVec2(int INT){
	int y = int(floor(float(INT)/8f));
	int x = INT - y *8;
	return vec2(x,y);
}

vec4 finalColor(sampler2D texx, vec2 Selfcoorindate, vec4 Tempcolor){	
	vec2 realCoordinate = mod(fs_in.tc2 * MapSize, 1);
	float offset = 1f/MapSize;

	bool U = returnOccupy(texx, Selfcoorindate + vec2(0,offset));
	bool B = returnOccupy(texx, Selfcoorindate + vec2(0,-offset));
	bool L = returnOccupy(texx, Selfcoorindate + vec2(-offset,0));
	bool R = returnOccupy(texx, Selfcoorindate + vec2(offset,0));
	bool UR = returnOccupy(texx, Selfcoorindate + vec2(offset,offset));
	bool UL = returnOccupy(texx, Selfcoorindate + vec2(-offset,offset));
	bool BR = returnOccupy(texx, Selfcoorindate + vec2(offset,-offset));
	bool BL = returnOccupy(texx, Selfcoorindate + vec2(-offset,-offset)); 
	
	vec4 colorA = vec4(0,0,0,0);
	
	if(U && R && UR){
		colorA = texture(tex1, (realCoordinate + intToVec2(16))/8);
		if(colorA.a > 0.1){
		Tempcolor = colorA;
		return Tempcolor;
		}
	}
	
	if(B && R && BR){
		colorA = texture(tex1, (realCoordinate + intToVec2(17))/8);
		if(colorA.a > 0.1){
		Tempcolor = colorA;
		return Tempcolor;
		}
	}
	
	if(B && L && BL){
		colorA = texture(tex1, (realCoordinate + intToVec2(18))/8);
		if(colorA.a > 0.1){
		Tempcolor = colorA;
		return Tempcolor;
		}
	}
	
	if(U && L && UL){
		colorA = texture(tex1, (realCoordinate + intToVec2(19))/8);
		if(colorA.a > 0.1){
		Tempcolor = colorA;
		return Tempcolor;
		}
	}
	
	return Tempcolor;
}	

void main()
{		
	if(!returnOccupy(tex2,fs_in.tc)){
		discard;
	}
	
	vec2 realCoordinate = mod(fs_in.tc2 * MapSize, 1);
	vec4 borderColor = texture(tex1, (realCoordinate + intToVec2(textureConnect(tex2, fs_in.tc)))/ 8);
	vec4 insideColor = texture(tex3, fs_in.tc * MapSize);	

	color = borderColor;
	
	color = finalColor(tex2, fs_in.tc, color);
	
	if(color.a < 0.4){
		discard;
	}
	
	//if not the border
	if(color.r != 1){
		color = insideColor;
	}
}

