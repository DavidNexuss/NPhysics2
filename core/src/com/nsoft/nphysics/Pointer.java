package com.nsoft.nphysics;

import com.badlogic.gdx.math.Vector2;

public interface Pointer {

	Vector2 getStart();
	Vector2 getEnd();
	
	default void setStart	(Vector2 newStart) 	{getStart().set(newStart);}
	default void setStart	(float x,float y) 	{getStart().set(x,y);}
	default void setEnd		(Vector2 newEnd) 	{getEnd().set(newEnd);}
	default void setEnd		(float x,float y) 	{getEnd().set(x,y);}
	
	default Vector2 getModulus() {return new Vector2(getEnd()).sub(getStart());}
}
