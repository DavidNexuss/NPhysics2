package com.nsoft.nphysics.sandbox.drawables;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.Say;
import com.nsoft.nphysics.sandbox.Point;
import com.nsoft.nphysics.sandbox.RopeComponent;
import com.nsoft.nphysics.sandbox.Sandbox;
import com.nsoft.nphysics.sandbox.SelectHandle;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.interfaces.ClickIn;
import com.nsoft.nphysics.sandbox.interfaces.Parent;
import com.nsoft.nphysics.sandbox.interfaces.Position;
import com.nsoft.nphysics.sandbox.interfaces.RawJoint;

public class Spring implements Say{

	public Color normal = new Color(0.2f, 0.4f, 0.2f, 1f);
	public static final Color springselected = new Color(0.9f, 0.8f, 0.2f, 1f);
	public static final int InputOFF = 5;
	public float XOffset = 5;
	public float YScale = 8;
	
	private Position A,B;
	private ArrayList<Vector2> drawPoints = new ArrayList<>();
	public boolean selectedFlag;
	
	private boolean end = false;
	
	private float lenght;
	
	private final Vector2 PA = new Vector2();
	private final Vector2 PB = new Vector2();
	private final Vector2 PC = new Vector2();
	private final Vector2 PD = new Vector2();
	
	private float definedLenght;
	
	public Spring() {
		this(-1);
	}
	
	public Spring(float lenght) {
		this.lenght = lenght;
	}
	public void addAnchorA(Position A) {
		this.A = A;
		testEnd();
	}
	
	public void addAnchorB(Position B) {
		this.B = B;
		testEnd();
	}
	
	public Position getAnchorA() {return A;}
	public Position getAnchorB() {return B;}
	private void testEnd() {if(!end && A != null && B != null) { end(); end = true;}}
	
	public boolean isEnd() {return end;}
	private void end() {
	
		updateSpring();
	}
	
	public void updateSpring() {
		
		drawPoints.clear();
		Vector2 dif = B.getVector().sub(A.getVector());
		float len = dif.len();
		
		float XO = A.getX();
		float YO = A.getY();

		if(lenght > 0) {
			
			XOffset = 5*len/lenght;
			YScale = (float) Math.min(20, 10*Math.sqrt(lenght/len));
		}else {
			
			definedLenght = len / Util.METERS_UNIT();
		}
		int it = (int) (len / XOffset) + 1;
		int y = (int) -YScale;
		
		for (int i = 0; i < it; i++) {
			
			drawPoints.add(new Vector2(XO + i*XOffset, y + YO));
			y *= -1;
		}
		
		
		rotate();
		float angle = MathUtils.atan2(B.getX() - A.getX(), B.getY() - A.getY());
		angle = -(angle - 90*MathUtils.degRad);
		PA.set(XO, YO+InputOFF);
		PB.set(XO, YO-InputOFF);
		PC.set(XO + len, YO-InputOFF);
		PD.set(XO + len, YO+InputOFF);
		
		PA.set(Util.rotPivot(A.getVector(), PA, angle));
		PB.set(Util.rotPivot(A.getVector(), PB, angle));
		PC.set(Util.rotPivot(A.getVector(), PC, angle));
		PD.set(Util.rotPivot(A.getVector(), PD, angle));
		
		collisionBox = new Polygon();
		
		float[] buff = new float[8];
		
		buff[0] = PA.x;
		buff[1] = PA.y;
		buff[2] = PB.x;
		buff[3] = PB.y;
		buff[4] = PC.x;
		buff[5] = PC.y;
		buff[6] = PD.x;
		buff[7] = PD.y;
		
		collisionBox.setVertices(buff);
	}
	
	private Polygon collisionBox;
	private void rotate() {
		
		float angle = MathUtils.atan2(B.getX() - A.getX(), B.getY() - A.getY());
		angle = -(angle - 90*MathUtils.degRad);
		for (int i = 0; i < drawPoints.size(); i++) {
			
			drawPoints.get(i).set(Util.rotPivot(A.getVector(), drawPoints.get(i), angle));
		}
	}

	public void render() {
		
		if(end) {
			
			if(selectedFlag)NPhysics.currentStage.shapeline.setColor(springselected);
			else NPhysics.currentStage.shapeline.setColor(normal);
			for (int i = 0; i < drawPoints.size(); i++) {
				
				if(i+1 < drawPoints.size()) {
					NPhysics.currentStage.shapeline.begin(ShapeType.Line);
					NPhysics.currentStage.shapeline.line(drawPoints.get(i), drawPoints.get(i+1));
					NPhysics.currentStage.shapeline.end();
				}
			}
			
			NPhysics.currentStage.shapeline.begin(ShapeType.Line);
			NPhysics.currentStage.shapeline.end();
		}
		
	}
	
	public boolean isInside(float x,float y) {
		
		return end ? collisionBox.contains(x, y) : false;
	}
	
	public float getDefinedLenght() { return definedLenght; }
}
