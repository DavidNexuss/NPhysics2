/***********************************************
*Shader de renderització de la quadrícula
*@author David
************************************************/

uniform float u_texture; //Necessari per Libgdx
uniform float grid; //Tamany de la quadrícula
uniform float xoffset; //Punt d'origen en x
uniform float yoffset; //Punt d'origen en y
uniform float X; //Coordenada X del cursor
uniform float Y; //Coordenada Y del cursor
uniform float PX; //Coordenada X del cursor (amb el snap aplicat)
uniform float PY; //Coordenada Y del cursor (amb el snap aplicat)
uniform mat4 u_projTrans; //Necessari per Libgdx

void main()
{
 
	//Es calcula si aquest pixel haurá de formar part de la mira de senyalització
	int ix = int(gl_FragCoord.x) - int(PX);
	int iy = int(gl_FragCoord.y) - int(PY);
		
	if((ix > -1 && ix < 2)|| (iy > -1 && iy < 2)){
		gl_FragColor = vec4(1,0,0,1.0);
		
	}else {
	   
		//En cas negatiu es calcula si el pixel haurá formar part de la quadrícula 
		float x = mod(gl_FragCoord.x - xoffset,grid);
		float y = mod(gl_FragCoord.y - yoffset,grid);
    
		if((x > -1.0 && x < 1.0) || (y > -1.0 && y < 1.0)){
			//En cas afirmatiu es calcula el degradat a aplicar per proximitat al cursor
			//Es calcula la distancia punt-cursor (no cal fer l'arrel per que aquest nomes es un valor per comparar)
			float d = (X - gl_FragCoord.x)*(X - gl_FragCoord.x) + (Y - gl_FragCoord.y)*(Y - gl_FragCoord.y);
			gl_FragColor = vec4(0.4 + 1000.0/d,0.4,0.4,1.0);	
		}else{
			//En cas negatiu es deixa sense color el pixel
			//Aquest serà pintat per un altre shader
			gl_FragColor = vec4(0.0,0.0,0.0,0.0);
		}
	}
    
    // Output to screen
}