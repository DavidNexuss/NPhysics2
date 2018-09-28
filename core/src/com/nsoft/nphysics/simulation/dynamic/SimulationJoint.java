package com.nsoft.nphysics.simulation.dynamic;

import java.text.DecimalFormat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.drawables.SimpleArrow;
import com.nsoft.nphysics.sandbox.ui.ArrowLabel;
import com.nsoft.nphysics.sandbox.ui.FontManager;

public class SimulationJoint extends Actor{

	ArrowLabel xlabel;
	ArrowLabel ylabel;
	ArrowLabel alabel;
	
	SimpleArrow xreaction;
	SimpleArrow yreaction;
	SimpleArrow areaction;
	
	private Joint j;
	
	boolean useMidPoint = false;
	
	boolean drawComponents = true;
	boolean drawMod = false;
	
	static DecimalFormat format = new DecimalFormat("#.##");
	
	boolean show = true;
	
	PolygonObject a,b;
	
	public SimulationJoint(Joint j) {
		
		xreaction = new SimpleArrow(j.getAnchorA(), new Vector2());
		yreaction = new SimpleArrow(j.getAnchorA(), new Vector2());
		areaction = new SimpleArrow(j.getAnchorA(), new Vector2());
		
		xreaction.setColor(Color.GREEN);
		yreaction.setColor(Color.GREEN);
		areaction.setColor(Color.ORANGE);
		
		xlabel = new ArrowLabel(NPhysics.currentStage.getUiGroup());
		ylabel = new ArrowLabel(NPhysics.currentStage.getUiGroup());
		alabel = new ArrowLabel(NPhysics.currentStage.getUiGroup());
		
		NPhysics.currentStage.addActor(xreaction);
		NPhysics.currentStage.addActor(yreaction);
		NPhysics.currentStage.addActor(areaction);
		
		xlabel.setStyle(FontManager.subtitle, Color.GREEN);
		ylabel.setStyle(FontManager.subtitle, Color.GREEN);
		alabel.setStyle(FontManager.subtitle,Color.ORANGE);
		
		xlabel.setVisible(false);
		ylabel.setVisible(false);
		alabel.setVisible(false);
		
		this.j = j;
	}
	
	public Vector2 getPosition() {
		
		return j.getAnchorA().scl(Util.UNIT);
	}
	
	public Vector2 getPositionB() {
		
		return j.getAnchorB().scl(Util.UNIT);
	}
	
	final Vector2 reaction = new Vector2();
	final Vector2 dreaction = new Vector2();
	final Vector2 jointPos = new Vector2();
	
	public void draw(Batch batch,float parentAlpha) {
		
		xlabel.setVisible(false);
		ylabel.setVisible(false);
		alabel.setVisible(false);
		
		xreaction.setVisible(false);
		yreaction.setVisible(false);
		areaction.setVisible(false);
		
		if(SimulationStage.active) {
		
			reaction.set(j.getReactionForce(1f / SimulationStage.getPhysicsDelta()).scl(-1f));
			dreaction.set(new Vector2(reaction).scl(Util.UNIT / SimulationStage.ForceMultiplier));
			
			if(useMidPoint) {
				
				jointPos.set(new Vector2(getPosition().add(getPositionB())).scl(0.5f));
			}else {
				
				jointPos.set(getPosition());
			}
			
			
			if(drawComponents) {
				
				xreaction.setStart(jointPos);
				xreaction.setEnd(new Vector2(dreaction).scl(1, 0).add(jointPos));
				
				yreaction.setStart(jointPos);
				yreaction.setEnd(new Vector2(dreaction).scl(0, 1).add(jointPos));
				
				xreaction.updateVertexArray();
				yreaction.updateVertexArray();
				
				xlabel.setText(format.format(reaction.x) + " xN");
				ylabel.setText(format.format(reaction.y) + " yN");
			}
			
			if(drawMod) {
				
				areaction.setStart(jointPos);
				areaction.setEnd(new Vector2(dreaction).add(jointPos));
				
				areaction.updateVertexArray();
				
				alabel.setText(format.format(reaction.len()) + "N");
			}
			
		}
		
		if(drawComponents) {
			
			xlabel.setVisible(true);
			ylabel.setVisible(true);
			xreaction.setVisible(true);
			yreaction.setVisible(true);
			
			xlabel.getColor().a = show ? 1 : PolygonObject.hidealpha;
			ylabel.getColor().a = show ? 1 : PolygonObject.hidealpha;
			
			xreaction.setColor(show ? Color.GREEN : Color.GREEN.cpy().mul(1, 1, 1, PolygonObject.hidealpha));
			yreaction.setColor(show ? Color.GREEN : Color.GREEN.cpy().mul(1, 1, 1, PolygonObject.hidealpha));
			
			xlabel.setPosition(new Vector2(jointPos).add(50, -50));
			ylabel.setPosition(new Vector2(jointPos).add(-120,50));
		}
		
		if (drawMod) {
			
			alabel.setVisible(true);
			areaction.setVisible(true);
			
			alabel.getColor().a = show ? 1 : PolygonObject.hidealpha;
			
			areaction.setColor(show ? Color.ORANGE : Color.ORANGE.cpy().mul(1, 1, 1, PolygonObject.hidealpha));
			alabel.setPosition(new Vector2(jointPos).add(50,50));
		}
	}
}
