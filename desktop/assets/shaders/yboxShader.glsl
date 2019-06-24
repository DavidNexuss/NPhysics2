uniform vec2 resolution;
void main(void)
{
	
	float fall = abs((gl_FragCoord.y - resolution.y/2.0)/100);
    gl_FragColor = vec4(0.2 - fall,0.2 - fall,0.2 - fall,1.0);
}
