package com.nsoft.nphysics;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class AlphaActor extends Group {

	
	public float getAlpha(){
		
		return getColor().a;
	}
	
	public void setAlpha(float a) {
		
		getColor().a = a;
	}
}
