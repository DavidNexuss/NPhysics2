#version 120
uniform float time;
uniform vec2 resolution;


void main(void)
{
    vec2 pos = gl_FragCoord.xy;
	vec2 Gpos = vec2(300.0,300.0);
	
	vec2 dist = pos - Gpos;
	
	float len = sqrt(dist.x*dist.x + dist.y*dist.y);
	float mlen = mod(len,30.0);
	
	if(mlen < 1.0 || mlen > 29.0){
	
		gl_FragColor = vec4(0.2,0.2,0.2,1.0);
	}else if(mod(atan(dist.y / dist.x)*57.2958,5.0) < 1.0){

		gl_FragColor = vec4(0.2,0.2,0.2,1.0);
	}
	else{

		gl_FragColor = vec4(1.0,1.0,1.0,1.0);
	}
}
