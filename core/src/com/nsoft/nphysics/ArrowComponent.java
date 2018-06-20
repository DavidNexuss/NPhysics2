package com.nsoft.nphysics;

import com.badlogic.gdx.math.Vector2;

public class ArrowComponent extends SimpleArrow implements AxisComponent {

	public ArrowComponent(Vector2 start, Vector2 end) {
		super(start, end);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onAngleChange(Vector2 pivot,float difAngleRad) {
		
		Vector2 newEnd = Util.rotPivot(pivot, getEnd(), difAngleRad);
		Vector2 newStart = Util.rotPivot(pivot, getStart(), difAngleRad);
		
		setStart(newStart.x, newStart.y);
		setEnd(newEnd.x,newEnd.y);
	}
}
