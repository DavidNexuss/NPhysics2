package com.nsoft.nphysics.sandbox;

import com.badlogic.gdx.math.Vector2;
import com.nsoft.nphysics.sandbox.interfaces.AxisComponent;

public class ArrowComponent extends SimpleArrow implements AxisComponent { //TODO: INCOMPLETE

	public ArrowComponent(Vector2 start, Vector2 end) {
		super(start, end);
		
	}

	@Override
	public void onAngleChange(Vector2 pivot,float difAngleRad) {
		
		Vector2 newEnd = Util.rotPivot(pivot, getEnd(), difAngleRad);
		Vector2 newStart = Util.rotPivot(pivot, getStart(), difAngleRad);
		
		setStart(newStart.x, newStart.y);
		setEnd(newEnd.x,newEnd.y);
	}
}
