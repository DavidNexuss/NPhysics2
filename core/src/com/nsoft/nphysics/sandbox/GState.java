package com.nsoft.nphysics.sandbox;

public enum GState {
	
	START("Bienvenido a NPhysics2"),
	CREATE_POINT("Crea un punto",()->{Point.lastPoint.setVisible(false);},()->{Point.lastPoint.setVisible(true);}),
	CREATE_SEGMENT("Crea un segmento"),
	CREATE_AXIS("Crea un eje",()->{AxisSupport.temp.setVisible(false);},()->{AxisSupport.temp.setVisible(true);}),
	CREATE_DOUBLE_AXIS("Crea un eje doble",()->{DoubleAxisComponent.tmp.setVisible(false);},()->{DoubleAxisComponent.tmp.setVisible(true);}),
	CREATE_PRISMATIC("Crea una junta prismatica",()->{PrismaticComponent.temp.setVisible(false);},()->{PrismaticComponent.temp.setVisible(true);}),
	CREATE_ROPE("Crea una cuerda"),
	CREATE_SUPPORT("Crea un soporte"),
	CREATE_POLYGON("Crea un objeto"),
	CREATE_FAST_POLYGON("Crea un objeto"),
	CREATE_SEGMENTS("Crea segmentos"),
	CREATE_FORCE("Crea un vector de fuerza"),
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