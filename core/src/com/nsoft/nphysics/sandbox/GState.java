package com.nsoft.nphysics.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.nsoft.nphysics.NPhysics;

public enum GState {
	
	START("Bienvenido a NPhysics2"),
	CREATE_POINT("Crea un punto",()->{Point.lastPoint.setVisible(false);},()->{Point.lastPoint.setVisible(true);}),
	CREATE_SEGMENT("Crea un segmento"),
	CREATE_AXIS("Crea un eje",()->{AxisSupport.temp.setVisible(false);},()->{AxisSupport.temp.setVisible(true); 	AxisSupport.Axis = new Texture(Gdx.files.internal("misc/axis.png"));}),
	CREATE_SUPPORT("Crea un soporte"),
	CREATE_POLYGON("Crea un objeto"),
	CREATE_SEGMENTS("Crea segmentos"),
	HOOK_FORCE_ARROW("Dibuja el vector de fuerza"),
	HOOK_FORCE_ARROW2("Dibuja el vector de fuerza"), 
;	
	public String description;
	public Runnable cleanTask;
	public Runnable setUpTask;
	
	public boolean hasCleanTask() {return cleanTask != null;}
	public boolean hasSetUpTask() {return setUpTask != null;}
	
	private GState(String description) {
		
		this(description, null);
	}
	
	GState(String description,Runnable cleanTask) {
		
		this(description, cleanTask,null);
	}
	GState(String description,Runnable cleanTask,Runnable setUpTask){
		
		this.description = description;
		this.cleanTask = cleanTask;
		this.setUpTask = setUpTask;
	}
	
}