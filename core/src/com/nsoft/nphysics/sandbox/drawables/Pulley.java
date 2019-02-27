package com.nsoft.nphysics.sandbox.drawables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.Say;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.interfaces.Position;

public class Pulley implements Say{

	
	public static final Color pulley = new Color(0.3f, 0.3f, 0.8f, 1);
	public static final Color selected = new Color(0.3f,0.8f,0.3f,1);
	
	private static final float Off = 3;
	private static final float growth = 2;
	private Position GroundA,GroundB;
	private Position AnchorA,AnchorB;
	
	private boolean end;
	
	private Polygon hitboxGroundA,hitboxJoint,hitboxGroundB;
	
	public boolean selectedFlag = false;
	
	public float alphaCounter;
	public Pulley() {
		
		hitboxGroundA = new Polygon();
		hitboxGroundB = new Polygon();
		hitboxJoint = new Polygon();
	}

	public Position getGroundA() { return GroundA; }

	public void setGroundA(Position groundA) {
		GroundA = groundA;
		testEnd();
	}

	public Position getGroundB() { return GroundB; }

	public void setGroundB(Position groundB) {
		GroundB = groundB;
		testEnd();
	}

	public Position getAnchorA() { return AnchorA; }

	public void setAnchorA(Position anchorA) {
		AnchorA = anchorA;
		testEnd();
	}

	public Position getAnchorB() { return AnchorB; }

	public void setAnchorB(Position anchorB) {
		AnchorB = anchorB;
		testEnd();
	}
	
	private void testEnd(){
		
		if(isComplete()) {end = true;end();}
	}
	
	public boolean isComplete() {
		
		return AnchorA != null && AnchorB != null && GroundA != null && GroundB != null;
	}
	public void update() {
		
		if(end) end();
	}
	private void end() {
		
		float[][] box1 = createBox(AnchorA, GroundA);
		float[][] box2 = createBox(AnchorB, AnchorA);
		float[][] box3 = createBox(GroundB, AnchorB);
		
		Util.inject(box1, (buff)->{hitboxGroundA.setVertices(buff);});
		Util.inject(box2, (buff)->{hitboxJoint.setVertices(buff);});
		Util.inject(box3, (buff)->{hitboxGroundB.setVertices(buff);});
	}
	
	public boolean isInside(float x,float y) {

		return end && (hitboxGroundA.contains(x, y) || hitboxJoint.contains(x, y) || hitboxGroundB.contains(x,y));
	}
	private float[][] createBox(Position End,Position Start){
		
		Vector2 v1 = new Vector2(End.getVector()).sub(Start.getVector());
		float v1len = v1.len();
		
		float[][] hitBox1 = new float[][] {{0,-Off},
									   	   {0,+Off},
									   	   {v1len,+Off},
									   	   {v1len,-Off}};
		float[][] hitBox1_proj = new float[4][2];
		
		Util.proj(hitBox1, hitBox1_proj, Start.getX(), Start.getY(), v1.angleRad());
		return hitBox1_proj;
	}
	public void render() {
		
		if(end) {
			
			NPhysics.currentStage.shapeline.setColor(selectedFlag ? selected : pulley);
			Gdx.gl.glLineWidth(3 + growth*alphaCounter);
			NPhysics.currentStage.shapeline.begin(ShapeType.Line);
			
			NPhysics.currentStage.shapeline.line(GroundA.getVector(), AnchorA.getVector());
			NPhysics.currentStage.shapeline.line(AnchorA.getVector(), AnchorB.getVector());
			NPhysics.currentStage.shapeline.line(AnchorB.getVector(), GroundB.getVector());
			
			NPhysics.currentStage.shapeline.end();
			
		}
	}
}
