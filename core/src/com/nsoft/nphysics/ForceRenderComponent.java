package com.nsoft.nphysics;

import com.badlogic.gdx.math.Vector2;

public class ForceRenderComponent extends ArrowActor{

	ArrowComponent xcomponent;
	ArrowComponent ycomponent;
	
	public ForceRenderComponent(Vector2 start,Vector2 end) {
		
		super(start, end);
		
	}
}
